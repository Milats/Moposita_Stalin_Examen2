package com.fisei.athanasiaapp.services;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageService {

    public static Bitmap GetImageByURL(String url){
        Bitmap bitmap = null;
        HttpURLConnection connection = null;
        try{
            URL Url = new URL(url);
            connection = (HttpURLConnection) Url.openConnection();
            try(InputStream inputStream = connection.getInputStream()){
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (Exception ex){
                ex.printStackTrace();
            }
        } catch (Exception ex){
            ex.printStackTrace();
        } finally {
            if(connection != null) {
                connection.disconnect();
            }
        }
        return bitmap;
    }
}
