package com.ranaus.laundry;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;

public class ConfMake extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout linearLayout;
    private TextView qty,fullName,address,contact,pendingBill,currentBill,totalBill,receivedBill;
    private String receivedUserName,objectId,objId;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conf_make);

        fullName = (TextView) findViewById(R.id.customer_name_ConfMakeAct);
        linearLayout = findViewById(R.id.linear_ConfMakeOrderAct);
        address = findViewById(R.id.customer_address_ConfMakeAct);
        contact = findViewById(R.id.customer_phone_ConfMakeAct);
        qty = findViewById(R.id.customer_qty_ConfMakeAct);
        pendingBill = findViewById(R.id.customer_pending_bill_ConfMakeAct);
        currentBill = findViewById(R.id.customer_current_bill_ConfMakeAct);
        totalBill = findViewById(R.id.customer_total_bill_ConMakeAct);
        receivedBill =findViewById(R.id.customer_received_bill_ConfMakeAct);
        img = findViewById(R.id.img_ConfMakeAct);

        Intent receivedIntentObject = getIntent();
        receivedUserName = receivedIntentObject.getStringExtra("objectId");

        getOrderInfo();
    }

    @Override
    public void onClick(View v) {
        if (receivedBill.getText().toString().equals(""))
        {
            FancyToast.makeText(ConfMake.this,"Enter Received Amount !!!",FancyToast.
                    LENGTH_LONG,FancyToast.ERROR,true).show();
        }
        else
        {
            updateData();
        }


    }

    private void getOrderInfo()
    {
        final ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("customerDetails");
        parseQuery.whereEqualTo("objectId",receivedUserName);

        final ProgressDialog progressDialog = new ProgressDialog(ConfMake.this);
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

                                    //Integer temp = object.getInt("CurrentBill");

                                    ParseFile postPicture = (ParseFile) object.get("customerBagPhoto");

                                    postPicture.getDataInBackground(new GetDataCallback() {
                                        @Override
                                        public void done(byte[] data, ParseException e) {
                                            if (data != null && e == null)
                                            {
                                                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                                img.setImageBitmap(bitmap);
                                            }
                                        }
                                    });
                                }
                                progressDialog.dismiss();
                            }
                        });
                    }
                }
            }
        });
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

    private void updateData()
    {
        int currentBillInt =Integer.parseInt(currentBill.getText().toString());
        int recevedBillInt =Integer.parseInt(receivedBill.getText().toString());
        int pendingBillInt =Integer.parseInt(pendingBill.getText().toString());
        int totalBillInt =Integer.parseInt(totalBill.getText().toString());
         pendingBillInt = totalBillInt-recevedBillInt;

        final ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("customerDetails");
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
                        objectId = String.valueOf(object);
                        objId= object.getObjectId();
                        parseQuery.getInBackground(objId, new GetCallback<ParseObject>() {
                            @Override
                            public void done(ParseObject object, ParseException e) {
                                if (e == null)
                                {

                                    if (finalPendingAmt == 0)
                                    {
                                        object.put("status","completed");
                                        object.put("pendingBill",finalPendingAmt);
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
                                                    FancyToast.makeText(ConfMake.this,"Success !!!",FancyToast.
                                                            LENGTH_LONG,FancyToast.SUCCESS,true).show();
                                                }
                                                else
                                                {
                                                    FancyToast.makeText(ConfMake.this,"Error !!!",FancyToast.
                                                            LENGTH_LONG,FancyToast.ERROR,true).show();
                                                }

                                            }
                                        });
                                        objects.get(0).deleteInBackground(new DeleteCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                if (e == null)
                                                { }
                                            }
                                        });
                                    }
                                    else
                                    {
                                        object.put("status","billPending");
                                        object.put("pendingBill",finalPendingAmt);
                                        object.put("receivedBill",receivedAmt);
                                        object.saveInBackground();

                                        ParseObject billPendingOrders = new ParseObject("billPendingOrders");

                                        billPendingOrders.put("status",object.get("status"));
                                        billPendingOrders.put("customerName",object.get("customerName"));
                                        billPendingOrders.put("customerPhone",object.get("customerPhone"));
                                        billPendingOrders.put("customerAddress",object.get("customerAddress"));
                                        billPendingOrders.put("addedBy",object.get("addedBy"));
                                        billPendingOrders.put("customerQty",object.get("customerQty"));
                                        billPendingOrders.put("CurrentBill",object.get("CurrentBill"));
                                        billPendingOrders.put("TotalBill",object.get("TotalBill"));
                                        billPendingOrders.put("pendingBill",object.get("pendingBill"));
                                        billPendingOrders.put("receivedBill",object.get("receivedBill"));
                                        billPendingOrders.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                if (e == null)
                                                {
                                                    FancyToast.makeText(ConfMake.this,"Success !!!",FancyToast.
                                                            LENGTH_LONG,FancyToast.SUCCESS,true).show();
                                                }
                                                else
                                                {
                                                    FancyToast.makeText(ConfMake.this,"Error !!!",FancyToast.
                                                            LENGTH_LONG,FancyToast.ERROR,true).show();
                                                }
                                            }
                                        });
                                        objects.get(0).deleteInBackground(new DeleteCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                if (e == null){}
                                            }
                                        });
                                    }
                                    Intent intent = new Intent(ConfMake.this,UserSelectable.class);
                                    startActivity(intent);
                                    finish();
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
