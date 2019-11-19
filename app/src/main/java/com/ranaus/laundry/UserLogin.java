package com.ranaus.laundry;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class UserLogin extends AppCompatActivity implements View.OnClickListener {

    private EditText username,password;
    private Button userLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        userLogin = (Button) findViewById(R.id.btn_login_UsrLogAct);
        username = findViewById(R.id.username_UsrLogAct);
        password = findViewById(R.id.usr_passwd_UsrLogAct);
    }

    @Override
    public void onClick(View view) {
        String login = username.getText().toString().toLowerCase();
        if (login.equals("admin"))
        {
            Toast.makeText(this, "admin login not allowed here !!!"+login, Toast.LENGTH_SHORT).show();
        }
        else
        {
            loginActivity();
        }
    }

    private void loginActivity()
    {
        if (username.getText().toString().equals("") || password.getText().toString().equals(""))
        {
            FancyToast.makeText(UserLogin.this,"Empty Username or Password !!!",FancyToast.
                    LENGTH_LONG,FancyToast.ERROR,true).show();
        }
        else
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Login User...");
            progressDialog.show();

            ParseUser.logInInBackground(username.getText().toString().toLowerCase(), password.getText().toString(), new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (user != null && e == null)
                    {
                        FancyToast.makeText(UserLogin.this,"Login Successful",FancyToast.
                                LENGTH_LONG,FancyToast.SUCCESS,true).show();
                        Intent intent = new Intent(UserLogin.this,UserSelectable.class);
                        startActivity(intent);
                    }
                    else
                    {
                        FancyToast.makeText(UserLogin.this,"Incorrect Username or Password !!!",FancyToast.
                                LENGTH_LONG,FancyToast.ERROR,true).show();
                    }
                    progressDialog.dismiss();
                }
            });
        }
    }

    public void rootLayoutTapped(View view) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
