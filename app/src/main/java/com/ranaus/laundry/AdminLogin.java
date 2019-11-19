package com.ranaus.laundry;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
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

public class AdminLogin extends AppCompatActivity implements View.OnClickListener {
    private Button adminLogin;
    private EditText edtAdminUsername, edtAdminPasswd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        adminLogin = (Button) findViewById(R.id.btn_login_AdmLogAct);
        edtAdminUsername = (EditText) findViewById(R.id.admin_username_AdmLogAct);
        edtAdminPasswd = (EditText) findViewById(R.id.admin_passwd_AdmLogAct);

    }

    @Override
    public void onClick(View v) {
        String login = edtAdminUsername.getText().toString().toLowerCase();

        switch (v.getId())
        {
            case R.id.btn_login_AdmLogAct:
                if (login.equals("admin"))
                {
                    LoginCredentialActivity();
                }
                else
                {
                    FancyToast.makeText(AdminLogin.this,"Admin login Only !!!",FancyToast.
                            LENGTH_LONG,FancyToast.ERROR,true).show();
                }

                break;

            case R.id.forgot_admin_AdmLogAct:
                Intent forgotPasswdIntent = new Intent(AdminLogin.this,TestAC.class);
                startActivity(forgotPasswdIntent);
                finish();
                break;
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
    private void LoginCredentialActivity()
    {
        if (edtAdminUsername.getText().toString().equals("") || edtAdminPasswd.getText().toString().equals(""))
        {
            FancyToast.makeText(AdminLogin.this,"Empty Username or Password !!!",FancyToast.
                    LENGTH_LONG,FancyToast.ERROR,true).show();
        }
        else
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Login Admin...");
            progressDialog.show();
            ParseUser.logInInBackground(edtAdminUsername.getText().toString().toLowerCase(), edtAdminPasswd.getText().toString(), new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (user != null && e == null)
                    {
                        edtAdminUsername.setText("");
                        edtAdminPasswd.setText("");
                        FancyToast.makeText(AdminLogin.this,"Login Successful",FancyToast.
                                LENGTH_LONG,FancyToast.SUCCESS,true).show();
                        Intent intent = new Intent(AdminLogin.this,AdminMain.class);
                        startActivity(intent);
                    }
                    else
                    {
                        FancyToast.makeText(AdminLogin.this,"Incorrect Username or Password !!!",FancyToast.
                                LENGTH_LONG,FancyToast.ERROR,true).show();
                    }
                    progressDialog.dismiss();
                }
            });
        }

    }
}
