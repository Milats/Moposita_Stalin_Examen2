package com.fisei.athanasiaapp.services;

import com.fisei.athanasiaapp.models.ResponseAthanasia;
import com.fisei.athanasiaapp.objects.AthanasiaGlobal;
import com.fisei.athanasiaapp.utilities.URLs;
import com.fisei.athanasiaapp.objects.UserClient;
import com.fisei.athanasiaapp.utilities.Utils;

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

public class UserClientService {

    public static UserClient Login(String email, String passwd){
        UserClient user = new UserClient();
        HttpURLConnection connection = null;
        try {
            URL url = new URL(URLs.LOGIN);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoInput(true);
            String jsonInput = "{\"Email\": \"" + email +
                    "\",\"Password\": \"" + passwd + "\"}";
            try(OutputStream os = connection.getOutputStream()){
                byte[] input = jsonInput.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int responseCode = connection.getResponseCode();
            StringBuilder response = new StringBuilder();
            if (responseCode == HttpURLConnection.HTTP_OK){
                try(BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {

                    String responseLine = null;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                }

                JSONObject data = new JSONObject(response.toString()).getJSONObject("data");
                user.ID = data.getInt("id");
                user.JWT = data.getString("token");
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            if (connection != null){
                connection.disconnect();
            }
        }
        return user;
    }
    public static UserClient GetUserInfoByID(int id){
        UserClient user = new UserClient();
        HttpURLConnection connection = null;
        try{
            URL url = new URL(URLs.CLIENT_BY_ID + id);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Authorization","Bearer " + AthanasiaGlobal.ACTUAL_USER.JWT);
            int responseCode = connection.getResponseCode();
            StringBuilder response = new StringBuilder();
            if(responseCode == HttpURLConnection.HTTP_OK){
                try(BufferedReader br = new BufferedReader( new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    String responseLine = null;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                }
                JSONObject data = new JSONObject(response.toString());
                JSONArray userData = data.getJSONArray("data");
                user.ID = userData.getJSONObject(0).getInt("id");
                user.Name = userData.getJSONObject(0).getString("name");
                user.Email = userData.getJSONObject(0).getString("email");
                user.Cedula = userData.getJSONObject(0).getString("cedula");
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            if(connection != null){
                connection.disconnect();
            }
        }
        return user;
    }
    public static ResponseAthanasia SignUpNewUser(UserClient newUser, String newPasswd){
        ResponseAthanasia responseAth = new ResponseAthanasia(false, "An unexpected error ocurred");
        HttpURLConnection connection = null;
        try{
            URL url = new URL(URLs.SIGN_UP);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoInput(true);

            String jsonInput = "{\"name\": \""+ newUser.Name + "\"," +
                    " \"email\": \"" + newUser.Email +  "\"," +
                    " \"password\": \"" + newPasswd +  "\"," +
                    " \"cedula\": \"" + newUser.Cedula +  "\"}";
            try(OutputStream os = connection.getOutputStream()){
                byte[] input = jsonInput.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            } catch (IOException e){
                e.printStackTrace();
            }
            int responseCode = connection.getResponseCode();
            StringBuilder response = new StringBuilder();
            JSONObject jsonObject = null;
            if(responseCode == HttpURLConnection.HTTP_OK){
                try(BufferedReader bR = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))){
                    String responseLine = null;
                    while((responseLine = bR.readLine()) != null){
                        response.append(responseLine.trim());
                    }
                }
                responseAth.Success = true;
            } else if(responseCode == HttpURLConnection.HTTP_BAD_REQUEST){
                try(BufferedReader bR = new BufferedReader(new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8))){
                    String responseLine = null;
                    while((responseLine = bR.readLine()) != null){
                        response.append(responseLine.trim());
                    }
                }
                jsonObject = new JSONObject(response.toString());
                JSONObject errors = jsonObject.getJSONObject("errors");
                responseAth.Message = "";
                boolean emailError = false;
                try{
                    responseAth.Message += Utils.CleanString(errors.getString("email"));
                    emailError = true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try{
                    if (emailError){
                        responseAth.Message += "\n" + Utils.CleanString(errors.getString("cedula"));
                    } else {
                        responseAth.Message += Utils.CleanString(errors.getString("cedula"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return responseAth;
    }
}