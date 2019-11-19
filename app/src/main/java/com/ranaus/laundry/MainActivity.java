package com.ranaus.laundry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_admin_login_MainAct:
                Intent adminIntent = new Intent(MainActivity.this,AdminLogin.class);
                startActivity(adminIntent);
                break;

            case R.id.btn_user_login_MainAct:
                Intent userIntent = new Intent(MainActivity.this,UserLogin.class);
                startActivity(userIntent);
                break;
        }
    }
}
