package com.fisei.athanasiaapp.services;

import com.fisei.athanasiaapp.models.SaleDetails;
import com.fisei.athanasiaapp.objects.AthanasiaGlobal;
import com.fisei.athanasiaapp.objects.Order;
import com.fisei.athanasiaapp.objects.ShopCartItem;
import com.fisei.athanasiaapp.utilities.URLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ShoppingCartService {
    public static List<ShopCartItem> GetShopCartFromUserLogged(int user){
        List<ShopCartItem> toReturn = new ArrayList<>();
        HttpURLConnection connection = null;
        try{
            URL url = new URL(URLs.SHOPPING_CART + "/" + user);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Authorization","Bearer " + AthanasiaGlobal.ACTUAL_USER.JWT);
            int responseCode = connection.getResponseCode();
            StringBuilder response = new StringBuilder();
            if(responseCode == HttpURLConnection.HTTP_OK){
                try(BufferedReader bR = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))){
                    String responseLine = null;
                    while((responseLine = bR.readLine()) != null){
                        response.append(responseLine.trim());
                    }
                }
                JSONObject data = new JSONObject(response.toString());
                JSONArray list = data.getJSONArray("data");
                for(int i = 0; i < list.length(); ++i){
                    JSONObject orders = list.getJSONObject(i);
                    toReturn.add(new ShopCartItem(
                            orders.getInt("idproduct"),
                            orders.getInt("quantity")));
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            if (connection != null){
                connection.disconnect();
            }
        }
        return toReturn;
    }
    public static Boolean DeleteCart(int user){
        HttpURLConnection connection = null;
        try {
            URL url = new URL(URLs.SHOPPING_CART + "/" + user);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Authorization","Bearer " + AthanasiaGlobal.ACTUAL_USER.JWT);

            int responseCode = connection.getResponseCode();
            StringBuilder response = new StringBuilder();
            if(responseCode == HttpURLConnection.HTTP_OK){
                try(BufferedReader bR = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))){
                    String responseLine = null;
                    while((responseLine = bR.readLine()) != null){
                        response.append(responseLine.trim());
                    }
                }
                JSONObject data = new JSONObject(response.toString());
                return data.getBoolean("success");
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            if (connection != null){
                connection.disconnect();
            }
        }
        return false;
    }
    public static Boolean SaveCart(List<ShopCartItem> cart, int user){
        HttpURLConnection connection = null;
        try {
            URL url = new URL(URLs.SHOPPING_CART);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Authorization","Bearer " + AthanasiaGlobal.ACTUAL_USER.JWT);
            connection.setDoInput(true);
            String jsonInputPart1 = "{\"IDUserClient\": " + user + ",\"ShopCartDetails\":[";
            StringBuilder jsonInputPart2 = new StringBuilder();
            for(ShopCartItem item: cart){
                jsonInputPart2.append("{\"IDProduct\": " + item.Id +
                        ",\"Quantity\": " + item.Quantity +
                        "},");
            }
            String jsonInput = jsonInputPart1 + jsonInputPart2.substring(0, jsonInputPart2.length() - 1) + "]}";
            try(OutputStream os = connection.getOutputStream()){
                byte[] input = jsonInput.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int responseCode = connection.getResponseCode();
            StringBuilder response = new StringBuilder();
            if(responseCode == HttpURLConnection.HTTP_OK){
                try(BufferedReader bR = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))){
                    String responseLine = null;
                    while((responseLine = bR.readLine()) != null){
                        response.append(responseLine.trim());
                    }
                }
                JSONObject data = new JSONObject(response.toString());
                return data.getBoolean("success");
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            if (connection != null){
                connection.disconnect();
            }
        }
        return false;
    }
}
