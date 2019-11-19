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

public class ConfPreDelete extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout linearLayout;
    private TextView qty,fullName,address,contact,pendingBill,currentBill,totalBill;
    private String receivedUserName,objectId,objId;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conf_pre_delete);
        fullName = (TextView) findViewById(R.id.customer_name_ConfDeleteAct);
        linearLayout = findViewById(R.id.linear_ConfDeleteOrderAct);
        address = findViewById(R.id.customer_address_ConfDeleteAct);
        contact = findViewById(R.id.customer_phone_ConfDeleteAct);
        qty = findViewById(R.id.customer_qty_ConfDeleteAct);
        pendingBill = findViewById(R.id.customer_pending_bill_ConfDeleteAct);
        currentBill = findViewById(R.id.customer_current_bill_ConfDeleteAct);
        totalBill = findViewById(R.id.customer_total_bill_ConfDeleteAct);
        img = findViewById(R.id.img_ConfDeleteAct);

        Intent receivedIntentObject = getIntent();
        receivedUserName = receivedIntentObject.getStringExtra("objectId");

        getOrderInfo();
    }

    private void getOrderInfo() {

        final ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("customerDetails");
        parseQuery.whereEqualTo("objectId",receivedUserName);

        final ProgressDialog progressDialog = new ProgressDialog(ConfPreDelete.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null)
                {
                    for (ParseObject object : objects)
                    {
                        objectId = String.valueOf(object);
                       objId= object.getObjectId();
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

    private void updateInfo() {

        final ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("customerDetails");
        parseQuery.whereEqualTo("objectId",receivedUserName);

        final ProgressDialog progressDialog1 = new ProgressDialog(ConfPreDelete.this);
        progressDialog1.setMessage("Loading...");
        progressDialog1.show();
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
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
                                    Integer IntCurrentBill = Integer.valueOf(currentBill.getText().toString());
                                    Integer IntTotalBill = Integer.valueOf(totalBill.getText().toString());
                                    Integer x = (Integer) object.get("TotalBill"+"");
                                    object.put("status","ready");
                                    object.put("CurrentBill",IntCurrentBill);
                                    object.put("TotalBill",IntCurrentBill);
                                    object.saveInBackground();
                                }
                            }
                        });
                    }
                }
                progressDialog1.dismiss();
                Intent intent = new Intent(ConfPreDelete.this,UserSelectable.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        checkBill();
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

    private void checkBill()
    {
        if (currentBill.getText().toString().equals(""))
        {
            FancyToast.makeText(ConfPreDelete.this,"Enter Current Bill !!!",FancyToast.
                    LENGTH_LONG,FancyToast.ERROR,true).show();
        }
        else
        {
            updateInfo();
            /*Intent intent = new Intent(this,UserSelectable.class);
            startActivity(intent);
            finish();*/
        }
    }
}
