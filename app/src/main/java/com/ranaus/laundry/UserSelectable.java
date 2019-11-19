package com.ranaus.laundry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UserSelectable extends AppCompatActivity implements View.OnClickListener {
    private Button makeDelivery,acceptDelivery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_selectable);

        makeDelivery = (Button) findViewById(R.id.btn_make_delivery_UsrSelAct);
        acceptDelivery = (Button) findViewById(R.id.btn_accept_ord_UsrSelAct);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_accept_ord_UsrSelAct:
                Intent acceptIntent = new Intent(UserSelectable.this,UserMain.class);
                startActivity(acceptIntent);

                break;

            case R.id.btn_make_delivery_UsrSelAct:
                Intent makeIntent = new Intent(UserSelectable.this,MakeDelivery.class);
                startActivity(makeIntent);

                break;

            case R.id.btn_pre_delivery_UsrSelAct:
                Intent preIntent = new Intent(UserSelectable.this,PreDelivery.class);
                startActivity(preIntent);
                break;
        }
    }
}
