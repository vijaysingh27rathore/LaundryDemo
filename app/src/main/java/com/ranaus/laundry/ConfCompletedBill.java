package com.ranaus.laundry;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class ConfCompletedBill extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout linearLayout;
    private TextView qty,fullName,address,contact,pendingBill,totalBill,receivedBill;
    private String receivedUserName,objectId,objId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conf_completed_bill);

        fullName = (TextView) findViewById(R.id.customer_name_ConfCompletedBillAct);
        linearLayout = findViewById(R.id.linear_ConfCompletedBillAct);
        address = findViewById(R.id.customer_address_ConfCompletedBillAct);
        contact = findViewById(R.id.customer_phone_ConfCompletedBillAct);
        qty = findViewById(R.id.customer_qty_ConfCompletedBillAct);
        pendingBill = findViewById(R.id.customer_pending_bill_ConfCompletedBillAct);
        totalBill = findViewById(R.id.customer_total_bill_ConfCompletedBillAct);
        receivedBill =findViewById(R.id.customer_received_bill_ConfCompletedBillAct);

        Intent receivedIntentObject = getIntent();
        receivedUserName = receivedIntentObject.getStringExtra("objectId");

        getOrderInfo();
    }

    private void getOrderInfo() {
        final ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("completedOrders");
        parseQuery.whereEqualTo("objectId",receivedUserName);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null)
                {
                    for (ParseObject object : objects)
                    {
                        objId = object.getObjectId();

                        parseQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                            @Override
                            public void done(ParseObject object, ParseException e) {
                                if (e == null)
                                {
                                    fullName.setText(object.get("customerName")+"");
                                    fullName.setTextColor(Color.WHITE);
                                    address.setText(object.get("customerAddress")+"");
                                    address.setTextColor(Color.WHITE);
                                    contact.setText(object.get("customerPhone")+"");
                                    contact.setTextColor(Color.WHITE);
                                    qty.setText(object.get("customerQty")+"");
                                    qty.setTextColor(Color.WHITE);
                                    pendingBill.setText(object.get("pendingBill")+"");
                                    pendingBill.setTextColor(Color.WHITE);
                                    totalBill.setText(object.get("TotalBill")+"");
                                    totalBill.setTextColor(Color.WHITE);
                                    receivedBill.setText(object.get("receivedBill")+"");
                                    receivedBill.setTextColor(Color.WHITE);
                                }
                            }
                        });
                    }
                }
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}
