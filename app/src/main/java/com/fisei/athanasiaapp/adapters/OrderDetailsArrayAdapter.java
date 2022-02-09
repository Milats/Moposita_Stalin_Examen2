package com.fisei.athanasiaapp.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fisei.athanasiaapp.objects.OrderDetail;
import com.fisei.athanasiaapp.R;
import com.fisei.athanasiaapp.services.ImageService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDetailsArrayAdapter extends ArrayAdapter<OrderDetail> {
    private static class ViewHolder{
        TextView orderDetailNameTextView;
        TextView orderDetailQuantityTextView;
        TextView orderDetailUnitPriceTextView;
        ImageView orderDetailImageView;
    }
    private final Map<String, Bitmap> bitmaps = new HashMap<>();
    public OrderDetailsArrayAdapter(Context context, List<OrderDetail> orderDetailList){
        super(context, -1, orderDetailList);
    }
    public View getView(int position, View convertView, ViewGroup parent){
        OrderDetail orderDetail = getItem(position);
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item_order_details, parent, false);
            viewHolder.orderDetailNameTextView = (TextView) convertView.findViewById(R.id.textViewOrderDetailName);
            viewHolder.orderDetailQuantityTextView = (TextView) convertView.findViewById(R.id.textViewOrderDetailQuantity);
            viewHolder.orderDetailUnitPriceTextView = (TextView) convertView.findViewById(R.id.textViewOrderDetailUnitPrice);
            viewHolder.orderDetailImageView = (ImageView) convertView.findViewById(R.id.imageViewOrderDetail);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(bitmaps.containsKey(orderDetail.ImageURL)){
            viewHolder.orderDetailImageView.setImageBitmap(bitmaps.get(orderDetail.ImageURL));
        } else {
            new LoadImageTask(viewHolder.orderDetailImageView).execute(orderDetail.ImageURL);
        }
        viewHolder.orderDetailNameTextView.setText(orderDetail.Name);
        viewHolder.orderDetailQuantityTextView.setText(String.format("%s", orderDetail.Quantity));
        viewHolder.orderDetailUnitPriceTextView.setText(String.format("%s", orderDetail.UnitPrice) + " $");
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
}