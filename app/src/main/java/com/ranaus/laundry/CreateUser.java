package com.ranaus.laundry;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.ranaus.laundry.R.id.UIDAI_img_CreateUsrAct;

public class CreateUser extends AppCompatActivity implements View.OnClickListener {

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    EditText usrName,usrFullName,usrAddress,usrPhone,usrUIDAI;
    ImageView imgUserUIDAIPhoto;
    Button btnSubmit;
    Bitmap receivedImageBitmap,photo;
    private ArrayList<String> arrayList;
    private ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        usrName = (EditText) findViewById(R.id.create_user_CreateUsrAct);
        usrFullName = (EditText) findViewById(R.id.create_fullname_CreateUsrAct);
        usrAddress = (EditText) findViewById(R.id.create_address_CreateUsrAct);
        usrPhone = (EditText) findViewById(R.id.create_contact_CreateUsrAct);
        usrUIDAI = (EditText) findViewById(R.id.create_aadhar_CreateUsrAct);
        imgUserUIDAIPhoto = (ImageView) findViewById(UIDAI_img_CreateUsrAct);

        arrayList = new ArrayList();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case UIDAI_img_CreateUsrAct:
                    getImageChoosen();
                break;

            case R.id.btn_submit_CreateUsrAct:

                if (receivedImageBitmap != null)
                {

                    if (usrName.getText().toString().equals("") || usrFullName.getText().toString().equals("") ||
                            usrAddress.getText().toString().equals("") || usrUIDAI.getText().toString().equals("") ||
                            usrPhone.getText().toString().equals(""))
                    {
                        FancyToast.makeText(CreateUser.this,"Please Fill All Field !!!",FancyToast.
                                LENGTH_LONG,FancyToast.ERROR,true).show();
                    }
                    else
                    {
                        createNewUserAccount();
                    }
                }

                else
                {
                    FancyToast.makeText(CreateUser.this,"Image not Added !!!",FancyToast.
                            LENGTH_LONG,FancyToast.ERROR,true).show();
                }

                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                FancyToast.makeText(CreateUser.this,"Camera Accessible",FancyToast.
                        LENGTH_LONG,FancyToast.SUCCESS,true).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                FancyToast.makeText(CreateUser.this,"Permission denied !!!",FancyToast.
                        LENGTH_LONG,FancyToast.ERROR,true).show();
            }
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

    private void createNewUserAccount()
    {
        final ParseUser appUser = new ParseUser();
        appUser.setUsername(usrName.getText().toString().toLowerCase());
        appUser.setPassword("123456");

        appUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null)
                {
                    userExtraInfo();
                    final ParseUser parseUser = ParseUser.getCurrentUser();
                    parseUser.put("CreadedBy","ADMIN");
                    parseUser.put("FullName",usrFullName.getText().toString());
                    parseUser.put("UserAddress",usrAddress.getText().toString());
                    parseUser.put("UserContact",usrPhone.getText().toString());
                    parseUser.put("UserUIDAI",usrUIDAI.getText().toString());
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
                    byte[] bytes = byteArrayOutputStream.toByteArray();
                    ParseFile parseFile = new ParseFile("UIDAI.jpeg",bytes);
                    parseUser.put("UserUIDAIPhoto",parseFile);

                    final ProgressDialog progressDialog = new ProgressDialog(CreateUser.this);
                    progressDialog.setMessage("Signing up...");
                    progressDialog.show();

                    parseUser.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            Toast.makeText(CreateUser.this, "Inserted", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            usrName.setText("");
                            usrFullName.setText("");
                            usrAddress.setText("");
                            usrPhone.setText("");
                            usrUIDAI.setText("");

                        }
                    });
                   // progressDialog.dismiss();

                    FancyToast.makeText(CreateUser.this,
                            appUser.getUsername() + " is signed up",
                            Toast.LENGTH_SHORT, FancyToast.SUCCESS,
                            true).show();
                    Intent intent = new Intent(CreateUser.this,AdminLogin.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    FancyToast.makeText(CreateUser.this,
                            "There was an error: " + e.getMessage(),
                            Toast.LENGTH_LONG, FancyToast.ERROR,
                            true).show();
                }
            }
        });

    }

    private void userExtraInfo()
    {

    }
}
