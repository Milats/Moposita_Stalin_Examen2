package com.fisei.athanasiaapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fisei.athanasiaapp.objects.AthanasiaGlobal;
import com.fisei.athanasiaapp.objects.Product;
import com.fisei.athanasiaapp.objects.ShopCartItem;
import com.fisei.athanasiaapp.objects.UserClient;
import com.fisei.athanasiaapp.services.ProductService;
import com.fisei.athanasiaapp.services.ShoppingCartService;
import com.fisei.athanasiaapp.services.UserAdminService;
import com.fisei.athanasiaapp.services.UserClientService;
import org.json.JSONObject;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwdEditText;
    private TextView warningTextView;

    private UserClient user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        InitializeViewComponents();
    }
    private void Login(){
        if(SIMP_CheckIfPasswordIsValid(passwdEditText.getText().toString())) {
            warningTextView.setText("");
            LoginTask loginTask = new LoginTask();
            loginTask.execute();
        }
    }
    private Boolean LoginAdmin(){
        if(SIMP_CheckIfPasswordIsValid(passwdEditText.getText().toString())){
            warningTextView.setText("");
            LoginAdminTask loginAdminTask = new LoginAdminTask();
            loginAdminTask.execute();
        }
        return true;
    }
    private void SignUp(){
        Intent register = new Intent(this, SingUpActivity.class);
        startActivity(register);
    }
    private class LoginTask extends AsyncTask<URL, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(URL... urls) {
            user = UserClientService.Login(emailEditText.getText().toString(), passwdEditText.getText().toString());
            return null;
        }
        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if(user.JWT != null){
                AthanasiaGlobal.ACTUAL_USER.JWT = user.JWT;
                AthanasiaGlobal.ACTUAL_USER.ID = user.ID;
                GetUserCartTask getUserCartTask = new GetUserCartTask();
                getUserCartTask.execute();
            } else {
                warningTextView.setText(R.string.label_wrong_email_password);
            }
        }
    }
    private class LoginAdminTask extends AsyncTask<URL, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(URL... urls) {
            user = UserAdminService.Login(emailEditText.getText().toString(), passwdEditText.getText().toString());
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if(user.JWT != null){
                AthanasiaGlobal.ACTUAL_USER.JWT = user.JWT;
                AthanasiaGlobal.ADMIN_PRIVILEGES = true;
                StartAthanasiaActivity();
            } else {
                warningTextView.setText(R.string.label_wrong_email_password);
            }
        }
    }
    private class GetUserCartTask extends AsyncTask<URL, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(URL... urls) {
            AthanasiaGlobal.SHOPPING_CART = ShoppingCartService.GetShopCartFromUserLogged(user.ID);
            List<ShopCartItem> tempList = new ArrayList<>();
            for (ShopCartItem item: AthanasiaGlobal.SHOPPING_CART) {
                Product p = ProductService.GetSpecifiedProductByID(item.Id);
                if(p.quantity < item.Quantity){
                    item.Quantity = 1;
                }
                tempList.add(new ShopCartItem(p.id, p.name, p.imageURL, item.Quantity, p.unitPrice, p.quantity));
            }
            AthanasiaGlobal.SHOPPING_CART = tempList;
            return null;
        }
        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            StartAthanasiaActivity();
        }
    }

    private void StartAthanasiaActivity(){
        Intent loginSuccesful = new Intent(this, AthanasiaActivity.class);
        startActivity(loginSuccesful);
        finish();
    }
    private void InitializeViewComponents(){
        emailEditText = findViewById(R.id.editTextEmailLogin);
        passwdEditText = findViewById(R.id.editTextPassword);
        Button loginButton = findViewById(R.id.btnLogin);
        loginButton.setOnClickListener(loginButtonClicked);
        loginButton.setOnLongClickListener(loginAdminButtonClicked);
        Button signUpButton = findViewById(R.id.btnRegister);
        signUpButton.setOnClickListener(signUpButtonClicked);
        warningTextView = findViewById(R.id.textViewLoginFailed);
    }
    private final View.OnClickListener loginButtonClicked = view -> Login();
    private final View.OnClickListener signUpButtonClicked = view -> SignUp();
    private final View.OnLongClickListener loginAdminButtonClicked = view -> LoginAdmin();

    private boolean SIMP_CheckIfPasswordIsValid(String passwd){
        String error_message = "";
        boolean flag = true;
        Resources res = getResources();
        if(!SIMP_CheckPasswLenght(passwd)){
            flag = false;
            error_message += "\n" + res.getString(R.string.password_no_lenght);
        }
        if (!SIMP_CheckPasswLowerCase(passwd)){
            flag = false;
            error_message += "\n" + res.getString(R.string.password_no_lower);
        }
        if(!SIMP_CheckPasswUpperCase(passwd)){
            flag = false;
            error_message += "\n" + res.getString(R.string.password_no_upper);
        }
        if (!SIMP_CheckPasswSpecial(passwd)){
            flag = false;
            error_message += "\n" + res.getString(R.string.password_no_special);
        }
        if(!SIMP_CheckPasswNumber(passwd)){
            flag = false;
            error_message += "\n" + res.getString(R.string.password_no_digit);
        }
        warningTextView.setText(error_message);
        return flag;
    }
    private boolean SIMP_CheckPasswLenght(String passwd){
        return passwd.length() >= 6 && passwd.length() <= 10;
    }
    private boolean SIMP_CheckPasswUpperCase(String passwd){
        char c;
        for (int i = 0; i < passwd.length(); i++) {
            c = passwd.charAt(i);
            if (Character.isUpperCase(c)) {
                return true;
            }
        }
        return false;
    }
    private boolean SIMP_CheckPasswLowerCase(String passwd) {
        char c;
        for (int i = 0; i < passwd.length(); i++) {
            c = passwd.charAt(i);
            if (Character.isLowerCase(c)) {
                return true;
            }
        }
        return false;
    }
    private boolean SIMP_CheckPasswSpecial(String passwd){
        Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(passwd);

        return m.find();
    }
    private boolean SIMP_CheckPasswNumber(String passwd) {
        char c;
        for (int i = 0; i < passwd.length(); i++) {
            c = passwd.charAt(i);
            if (Character.isDigit(c)) {
                return true;
            }
        }
        return false;
    }
    private boolean SIMP_CheckCedulaLenght(String cedula){
        return cedula.length() == 10;
    }
    private boolean SIMP_CheckCedulaOnlyDigits(String cedula){
        char c;
        for (int i = 0; i < cedula.length(); i++) {
            c = cedula.charAt(i);
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }
}