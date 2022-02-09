package com.fisei.athanasiaapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.fisei.athanasiaapp.OrderDetailsActivity;
import com.fisei.athanasiaapp.R;
import com.fisei.athanasiaapp.objects.Order;
import com.fisei.athanasiaapp.utilities.Utils;

import java.util.List;

public class OrderArrayAdapter extends ArrayAdapter<Order> {
    private static class ViewHolder{
        TextView orderDateView;
        TextView orderIDView;
        TextView orderTotalView;
        Button orderInfoBtn;
    }

    public OrderArrayAdapter(Context context, List<Order> orderList) {
        super(context, -1, orderList);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        Order order = getItem(position);
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item_myorders, parent, false);
            viewHolder.orderDateView = (TextView) convertView.findViewById(R.id.textViewOrderDate);
            viewHolder.orderIDView = (TextView) convertView.findViewById(R.id.textViewOrderId);
            viewHolder.orderTotalView = (TextView) convertView.findViewById(R.id.textViewOrderTotal);
            viewHolder.orderInfoBtn = (Button) convertView.findViewById(R.id.btnOrderInfo);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.orderDateView.setText(Utils.ConvertDate(order.Date));
        viewHolder.orderIDView.setText(String.format("%s", order.ID));
        viewHolder.orderTotalView.setText(String.format("%s", order.Total + " $"));
        viewHolder.orderInfoBtn.setOnClickListener(view -> {
            ShowOrderDetails(order);
        });
        return convertView;
    }

    private void ShowOrderDetails(Order order){
        Intent orderDetails = new Intent(getContext(), OrderDetailsActivity.class);
        orderDetails.putExtra("orderID", order.ID);
        orderDetails.putExtra("orderUserClient", order.UserClientID);
        orderDetails.putExtra("orderDate", order.Date);
        orderDetails.putExtra("orderTotal", order.Total);
        getContext().startActivity(orderDetails);
    }
}