package com.ranaus.laundry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AdminMain extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_create_user_AdmMainAct:
                Intent createUsrIntent = new Intent(AdminMain.this,CreateUser.class);
                startActivity(createUsrIntent);
                break;

            case R.id.btn_delete_user_AdmMainAct:
                Intent deleteUsrIntent = new Intent(AdminMain.this,DeleteUser.class);
                startActivity(deleteUsrIntent);
                break;

            case R.id.btn_reset_user_passwd_AdmMainAct:
                Intent resetPasswdIntent = new Intent(AdminMain.this,ResetPassword.class);
                startActivity(resetPasswdIntent);
                break;

            case R.id.btn_get_all_bills_AdmMainAct:
                Intent getBillByUserIntent = new Intent(AdminMain.this,GetBillByUser.class);
                startActivity(getBillByUserIntent);

                break;

            case R.id.btn_logout_AdmMainAct:
                Intent logOutIntent = new Intent(this,MainActivity.class);
                startActivity(logOutIntent);
                finish();

                break;
        }
    }
}
