package com.fisei.athanasiaapp.ui;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.fisei.athanasiaapp.R;
import com.fisei.athanasiaapp.adapters.ProductArrayAdapter;
import com.fisei.athanasiaapp.objects.Product;
import com.fisei.athanasiaapp.services.ProductService;

import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ProductsFragment extends Fragment {

    private List<Product> productList = new ArrayList<>();
    private ProductArrayAdapter productArrayAdapter;

    private ListView listView;

    public ProductsFragment() {
    }
    public static ProductsFragment newInstance(String param1, String param2) {
        ProductsFragment fragment = new ProductsFragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_products, container, false);
        listView = (ListView) view.findViewById(R.id.listViewProductFragment);
        productArrayAdapter = new ProductArrayAdapter(getContext(), QuitProductsWith0Qty(productList));
        GetProductsTask getProductsTask = new GetProductsTask();
        getProductsTask.execute();

        return view;
    }

    class GetProductsTask extends AsyncTask<URL, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(URL... params) {
            productList.clear();
            productList = ProductService.GetAllProducts();
            productList = QuitProductsWith0Qty(productList);
            return null;
        }
        @Override
        protected void onPostExecute(JSONObject jsonObject){
            productArrayAdapter.clear();
            productArrayAdapter.addAll(QuitProductsWith0Qty(productList));
            productArrayAdapter.notifyDataSetChanged();
            listView.setAdapter(productArrayAdapter);
        }
    }
    private List<Product> QuitProductsWith0Qty(List<Product> list){
        for (int x = 0; x < list.size(); x++) {
                if (list.get(x).quantity == 0) {
                    list.remove(x);
                    x = 0;
                }
        }
        return list;
    }
}