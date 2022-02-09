package com.fisei.athanasiaapp.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fisei.athanasiaapp.R;
import com.fisei.athanasiaapp.objects.AthanasiaGlobal;
import com.fisei.athanasiaapp.objects.Product;
import com.fisei.athanasiaapp.objects.ShopCartItem;
import com.fisei.athanasiaapp.services.ImageService;

public class ProductArrayAdapter extends ArrayAdapter<Product> {

    /*Define los atributos de las vistas de los productos.
        Un ImageView para la imagen del producto y TextViews para campos
        relevantes a mostrar como el nombre, el género y el precio.
        La cantidad no se muestra directamente sino que en detalles de producto.
      */
    private static class ViewHolder{
        ImageView productImageView;
        TextView productNameView;
        TextView productUnitPriceView;
        TextView productQtyView;
        Button productAddToCartButton;
    }
    //Renderizar la imagen.
    private final Map<String, Bitmap> bitmaps = new HashMap<>();
    //Constructor
    public ProductArrayAdapter(Context context, List<Product> productsList){
        super(context, -1, productsList);
    }
    public View getView(int position, View convertView, ViewGroup parent){
        Product product = getItem(position);
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item_product, parent, false);
            viewHolder.productImageView = (ImageView) convertView.findViewById(R.id.productImageView);
            viewHolder.productNameView = (TextView) convertView.findViewById(R.id.productNameTextView);
            viewHolder.productUnitPriceView = (TextView) convertView.findViewById(R.id.productUnitPriceTextView);
            viewHolder.productAddToCartButton = (Button) convertView.findViewById(R.id.productAddToCartBtn);
            viewHolder.productQtyView = (TextView) convertView.findViewById(R.id.productQtyTextView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(bitmaps.containsKey(product.imageURL)){
            viewHolder.productImageView.setImageBitmap(bitmaps.get(product.imageURL));
        } else {
            new LoadImageTask(viewHolder.productImageView).execute(product.imageURL);
        }
        viewHolder.productNameView.setText(product.name);
        viewHolder.productUnitPriceView.setText(String.format("%s", product.unitPrice + " $"));
        viewHolder.productQtyView.setText(String.format("%s", product.quantity));
        UnableButton(viewHolder.productAddToCartButton, RememberIfButtonWasSelected(product.id));
        viewHolder.productAddToCartButton.setOnClickListener(view -> {
            UnableButton(viewHolder.productAddToCartButton, false);
            AddToShoppingCart(product);
        });
        return convertView;
    }
    private class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
        private final ImageView imageView;
        public LoadImageTask(ImageView imageView){
            this.imageView = imageView;
        }
        @Override
        protected Bitmap doInBackground(String... params){
            Bitmap bitmap = ImageService.GetImageByURL(params[0]);
            bitmaps.put(params[0], bitmap);
            return bitmap;
        }
        @Override
        protected void onPostExecute(Bitmap bitmap){
            imageView.setImageBitmap(bitmap);
        }
    }
    private void UnableButton(Button btn, boolean b){
            btn.setEnabled(b);
    }
    private Boolean RememberIfButtonWasSelected(int id){
        for (ShopCartItem item: AthanasiaGlobal.SHOPPING_CART) {
            if (item.Id == id) {
                return false;
            }
        }
        return true;
    }
    private void AddToShoppingCart(Product p){
        AthanasiaGlobal.SHOPPING_CART.add(new ShopCartItem(p.id, p.name, p.imageURL, 1, p.unitPrice, p.quantity));
        Toast.makeText(getContext(), "Item " + p.name + " added to cart", Toast.LENGTH_SHORT).show();
    }
}