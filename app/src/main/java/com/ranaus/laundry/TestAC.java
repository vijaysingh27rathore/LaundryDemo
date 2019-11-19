package com.ranaus.laundry;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class TestAC extends AppCompatActivity implements View.OnClickListener {

    private EditText userAdmin,usrPassword,chPasswd;
    private Button btnCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_ac);
        userAdmin = (EditText) findViewById(R.id.user_name_TestAct);
        usrPassword = (EditText) findViewById(R.id.password_TestAct);
        btnCreateAccount = (Button) findViewById(R.id.create_admin_TestAct);
        chPasswd = (EditText) findViewById(R.id.ch_password_TestAct);
    }

    @Override
    public void onClick(View v) {

        if (userAdmin.getText().toString().equals("") || usrPassword.getText().toString().equals(""))
        {
            FancyToast.makeText(TestAC.this,"Username or Password missing",FancyToast.
                    LENGTH_LONG,FancyToast.ERROR,true).show();
        }
        else
        {
            ParseUser.requestPasswordResetInBackground("ganeshlaundry007@gmail.com", new RequestPasswordResetCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null)
                    {
                        FancyToast.makeText(TestAC.this,"Link sent",FancyToast.
                                LENGTH_LONG,FancyToast.SUCCESS,true).show();
                    }
                    else
                    {
                        FancyToast.makeText(TestAC.this,"Error",FancyToast.
                                LENGTH_LONG,FancyToast.ERROR,true).show();
                    }
                }
            });
        }
    }
}
