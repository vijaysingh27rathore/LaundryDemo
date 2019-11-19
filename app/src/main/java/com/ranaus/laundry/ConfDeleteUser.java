package com.ranaus.laundry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class ConfDeleteUser extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout linearLayout;
    private TextView textView,fullName,address,contact;
    private String receivedUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conf_delete_user);
        linearLayout = findViewById(R.id.linear_ConfDeleteAct);
        textView = findViewById(R.id.txt_username_ConfDeleteAct);
        fullName = findViewById(R.id.fullname_ConfDeleteAct);
        address = findViewById(R.id.address_ConfDeleteAct);
        contact = findViewById(R.id.contact_ConfDeleteAct);

        Intent receivedIntentObject = getIntent();
        receivedUserName = receivedIntentObject.getStringExtra("username");
        textView.setText(receivedUserName);
        textView.setTextColor(Color.WHITE);

        userInfo();
    }

    private void userInfo()
    {
        ParseUser.logInInBackground(receivedUserName, "123456", new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (user != null && e == null)
                {
                    final ParseUser parseUser = ParseUser.getCurrentUser();
                    fullName.setText(parseUser.get("FullName")+"");
                    fullName.setTextColor(Color.WHITE);
                    address.setText(parseUser.get("UserAddress")+"");
                    address.setTextColor(Color.WHITE);
                    contact.setText(parseUser.get("UserContact")+"");
                    contact.setTextColor(Color.WHITE);
                }
            }
        });
    }

    private void deleteUser()
    {
        ParseUser.logInInBackground(receivedUserName, "123456", new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (user != null && e == null)
                {
                    final ParseUser parseUser = ParseUser.getCurrentUser();
                    parseUser.deleteInBackground();
                    finish();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        deleteUser();
    }
}
