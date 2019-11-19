package com.ranaus.laundry;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.ranaus.laundry.CreateUser;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;

public class UserMain extends AppCompatActivity implements View.OnClickListener {
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =1 ;
    private Button btnSubmit;
    private String phoneNo,message;
    private EditText custName,custAdd,custPhone,custQty;
    private ImageView imgUserUIDAIPhoto;
    Bitmap receivedImageBitmap,photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);
        btnSubmit = (Button) findViewById(R.id.btn_submit_UsrMainAct);
        custName = (EditText) findViewById(R.id.customer_name_UsrMainAct);
        custAdd = (EditText) findViewById(R.id.customer_address_UsrMainAct);
        custPhone = (EditText) findViewById(R.id.customer_contact_UsrMainAct);
        custQty = (EditText) findViewById(R.id.customer_quantity_UsrMainAct);
        imgUserUIDAIPhoto =findViewById(R.id.customer_package_img_UsrMainAct);
        //CreateUser createUser = new CreateUser();

    }

    private void sendSMSMessage() {

        String strCustName = custName.getText().toString();
        String strCustAdd = custAdd.getText().toString();
        String strCustPhone = custPhone.getText().toString();
        String strCustQty= custQty.getText().toString();

        phoneNo = strCustPhone;
        message = "Dear "+strCustName+" your order with qty:"+strCustQty+ " received." +
                "\n\nShree Ganesh dry cleaners & Shree Ganesh travels 9765076749 9923695749";


        if (custPhone.length() >= 10 && custPhone.length() <= 13)
        {
            if (ContextCompat.checkSelfPermission(UserMain.this, Manifest.permission.SEND_SMS) !=
                    PackageManager.PERMISSION_GRANTED)
            {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.SEND_SMS)) {
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.SEND_SMS},
                            MY_PERMISSIONS_REQUEST_SEND_SMS);
                }
            }
            if (ContextCompat.checkSelfPermission(UserMain.this, Manifest.permission.SEND_SMS) ==
                    PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }

        else
        {
            Toast.makeText(this, "ERROR: Enter valid phone number !!!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case MY_PERMISSIONS_REQUEST_SEND_SMS:
            {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNo,null,message,null,null);
                    Toast.makeText(getApplicationContext(), "SMS sent", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(),
                            "SMS failed, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        CreateUser createUser = new CreateUser();
        switch (view.getId())
        {
            case R.id.customer_package_img_UsrMainAct:
                getImageChoosen();
                break;

            case R.id.btn_submit_UsrMainAct:
                if (receivedImageBitmap != null)
                {
                    if (custName.getText().toString().equals("") || custAdd.getText().toString().equals("") ||
                            custPhone.getText().toString().equals("") || custQty.getText().toString().equals(""))
                    {
                        FancyToast.makeText(UserMain.this,"Please Fill All Field !!!",FancyToast.
                                LENGTH_LONG,FancyToast.ERROR,true).show();
                    }
                    else
                    {
                        createNewUserAccount();
                        sendSMSMessage();
                    }
                }

                else
                {
                    FancyToast.makeText(UserMain.this,"Image not Added !!!",FancyToast.
                            LENGTH_LONG,FancyToast.ERROR,true).show();
                }


                break;
        }

    }

    private void createNewUserAccount() {
        ParseObject parseObject = new ParseObject("customerDetails");
        parseObject.put("customerName",custName.getText().toString());
        parseObject.put("addedBy", ParseUser.getCurrentUser().getUsername());
        parseObject.put("customerAddress",custAdd.getText().toString());
        parseObject.put("customerPhone",custPhone.getText().toString());
        parseObject.put("customerQty",custQty.getText().toString());
        parseObject.put("status","active");
        parseObject.put("pendingBill",0.00);
        parseObject.put("TotalBill",0.00);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        ParseFile parseFile = new ParseFile("img.jpeg",bytes);
        parseObject.put("customerBagPhoto",parseFile);

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();

        parseObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    FancyToast.makeText(UserMain.this, "Data uploaded!", Toast.LENGTH_SHORT, FancyToast.INFO, true).show();
                    custName.setText("");
                    custAdd.setText("");
                    custPhone.setText("");
                    custQty.setText("");
                    receivedImageBitmap = null;
                } else {
                    FancyToast.makeText(UserMain.this, "Unknown error: " + e.getMessage(), Toast.LENGTH_SHORT, FancyToast.ERROR, true).show();

                }
                dialog.dismiss();
            }
        });
    }

    //private void
    protected void getImageChoosen()
    {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        }
        else
        {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");
            receivedImageBitmap = photo;
            imgUserUIDAIPhoto.setImageBitmap(receivedImageBitmap);
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
}
