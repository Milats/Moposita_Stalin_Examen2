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
import android.widget.Toast;

import com.fisei.athanasiaapp.models.ResponseAthanasia;
import com.fisei.athanasiaapp.objects.UserClient;
import com.fisei.athanasiaapp.services.UserClientService;

import org.json.JSONObject;

import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SingUpActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextName;
    private EditText editTextCedula;
    private EditText editTextPassword;
    private TextView errorTextView;
    private Button buttonSignUp;
    private ResponseAthanasia responseTask = new ResponseAthanasia(false, "");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);
        InitializeViewComponents();



    }
    private class SignUpTask extends AsyncTask<URL, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(URL... urls) {
            UserClient newUser = new UserClient(0, editTextName.getText().toString(),
                    editTextEmail.getText().toString() + "@ath.com",
                    editTextCedula.getText().toString(), "");
            responseTask = UserClientService.SignUpNewUser(newUser, editTextPassword.getText().toString());
            return null;
        }
        @Override
        protected void onPostExecute(JSONObject jsonObject){
            if(responseTask.Success){
                StartLoginActivity();
            } else {
                if(!errorTextView.getText().toString().isEmpty() || errorTextView.getText().toString().equals("")){
                    errorTextView.setText(responseTask.Message);
                }
            }
            responseTask.Success = false;
        }
    }
    private void InitializeViewComponents(){
        editTextEmail = (EditText) findViewById(R.id.editTextSignUpEmail);
        editTextName = (EditText) findViewById(R.id.editTextSignUpName);
        editTextCedula = (EditText) findViewById(R.id.editTextSignUpCedula);
        editTextPassword = (EditText) findViewById(R.id.editTextSignUpPassword);
        errorTextView = (TextView) findViewById(R.id.textViewSignUpFail1);
        buttonSignUp = (Button) findViewById(R.id.btnSignUp);
        buttonSignUp.setOnClickListener(signUpButtonClicked);
    }
    private void SignUp(){
        if(editTextEmail.getText().toString().isEmpty() || editTextName.getText().toString().isEmpty() ||
                editTextCedula.getText().toString().isEmpty() || editTextPassword.getText().toString().isEmpty()){
            errorTextView.setText(R.string.fields_empty_error);
        } else {
            if(SIMP_CheckIfPasswordIsValid(editTextPassword.getText().toString())){
                errorTextView.setText("");
                SignUpTask signUpTask = new SignUpTask();
                signUpTask.execute();
            }
        }
    }
    private void StartLoginActivity(){
        Intent backLogin = new Intent(this, LoginActivity.class);
        startActivity(backLogin);
        Toast.makeText(this, "Your register was successful", Toast.LENGTH_SHORT).show();
    }
    private final View.OnClickListener signUpButtonClicked = view -> SignUp();


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
        errorTextView.setText(error_message);
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
}