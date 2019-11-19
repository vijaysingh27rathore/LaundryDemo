package com.ranaus.laundry;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;

public class ConfPending extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout linearLayout;
    private TextView qty,fullName,address,contact,pendingBill,currentBill,totalBill,receivedBill;
    private String receivedUserName,objectId,objId;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conf_pending);

        fullName = (TextView) findViewById(R.id.customer_name_ConfPendingAct);
        linearLayout = findViewById(R.id.linear_ConfPendingAct);
        address = findViewById(R.id.customer_address_ConfPendingAct);
        contact = findViewById(R.id.customer_phone_ConfPendingAct);
        qty = findViewById(R.id.customer_qty_ConfPendingAct);
        pendingBill = findViewById(R.id.customer_pending_bill_ConfPendingAct);
        currentBill = findViewById(R.id.customer_current_bill_ConfPendingAct);
        totalBill = findViewById(R.id.customer_total_bill_ConfPendingAct);
        receivedBill =findViewById(R.id.customer_received_bill_ConfPendingAct);

        Intent receivedIntentObject = getIntent();
        receivedUserName = receivedIntentObject.getStringExtra("objectId");

        getOrderInfo();
    }

    private void getOrderInfo() {
        final ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("billPendingOrders");
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
                                    currentBill.setText(object.get("CurrentBill")+"");
                                    currentBill.setTextColor(Color.WHITE);
                                    totalBill.setText(object.get("TotalBill")+"");
                                    totalBill.setTextColor(Color.WHITE);
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
        if (receivedBill.getText().toString().equals(""))
        {
            FancyToast.makeText(ConfPending.this,"Enter Received Amount !!!",FancyToast.
                    LENGTH_LONG,FancyToast.ERROR,true).show();
        }
        else
        {
            updateData();
        }

    }

    private void updateData() {
        int currentBillInt =Integer.parseInt(currentBill.getText().toString());
        int recevedBillInt =Integer.parseInt(receivedBill.getText().toString());
        int pendingBillInt =Integer.parseInt(pendingBill.getText().toString());
        int totalBillInt =Integer.parseInt(totalBill.getText().toString());
       // pendingBillInt = recevedBillInt+pendingBillInt-totalBillInt;
        final int pend = pendingBillInt;

        final ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("billPendingOrders");
        parseQuery.whereEqualTo("objectId",receivedUserName);
        final int finalPendingAmt = pendingBillInt;
        final int receivedAmt = recevedBillInt;
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(final List<ParseObject> objects, ParseException e) {
                if (e == null)
                {
                    for (ParseObject object : objects)
                    {
                        objId = object.getObjectId();
                        parseQuery.getInBackground(objId, new GetCallback<ParseObject>() {
                            @Override
                            public void done(ParseObject object, ParseException e) {
                                if (e == null)
                                {
                                    if (receivedAmt == finalPendingAmt)
                                    {
                                        object.put("status","completed");
                                        object.put("pendingBill",0);
                                        object.put("receivedBill",receivedAmt);
                                        object.saveInBackground();

                                        ParseObject completedOrders = new ParseObject("completedOrders");
                                        completedOrders.put("status",object.get("status"));
                                        completedOrders.put("customerName",object.get("customerName"));
                                        completedOrders.put("customerPhone",object.get("customerPhone"));
                                        completedOrders.put("customerAddress",object.get("customerAddress"));
                                        completedOrders.put("addedBy",object.get("addedBy"));
                                        completedOrders.put("customerQty",object.get("customerQty"));
                                        completedOrders.put("CurrentBill",object.get("CurrentBill"));
                                        completedOrders.put("TotalBill",object.get("TotalBill"));
                                        completedOrders.put("pendingBill",object.get("pendingBill"));
                                        completedOrders.put("receivedBill",object.get("receivedBill"));

                                        completedOrders.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                if (e == null)
                                                {
                                                    FancyToast.makeText(ConfPending.this,"Success !!!",FancyToast.
                                                            LENGTH_LONG,FancyToast.SUCCESS,true).show();
                                                    Intent intent = new Intent(ConfPending.this,AdminMain.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                                else
                                                {
                                                    FancyToast.makeText(ConfPending.this,"Error !!!",FancyToast.
                                                            LENGTH_LONG,FancyToast.ERROR,true).show();
                                                }
                                            }
                                        });
                                        objects.get(0).deleteInBackground(new DeleteCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                if (e == null)
                                                {

                                                }
                                            }
                                        });
                                    }

                                    else
                                    {
                                        FancyToast.makeText(ConfPending.this,"Enter Correct Amount !!!",FancyToast.
                                                LENGTH_LONG,FancyToast.ERROR,true).show();
                                    }
                                }
                                progressDialog.dismiss();
                            }
                        });
                    }
                }
            }
        });
    }
}
