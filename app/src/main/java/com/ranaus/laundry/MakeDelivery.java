package com.ranaus.laundry;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class MakeDelivery extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listView,listViewId;
    private ArrayList<String> arrayList,arrayListId;
    private ArrayAdapter arrayAdapter,arrayAdapterId;
    private TextView loadingUsers;
    private String objId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_delivery);

        listView = findViewById(R.id.list_view_MakeDeliveryAct);
        loadingUsers = findViewById(R.id.text_view_MakeDeliveryAct);
        listViewId = findViewById(R.id.objectId_MakeDeliveryAct);
        arrayList = new ArrayList();
        arrayListId = new ArrayList();
        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayList);
        arrayAdapterId = new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayListId);

        listView.setOnItemClickListener(this);
        getData();
    }

    private void getData()
    {
        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("customerDetails");
        parseQuery.whereEqualTo("status","ready");
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (objects.size() > 0)
                {
                    for (ParseObject user : objects)
                    {
                        objId = user.getObjectId();
                        arrayListId.add(objId);
                        arrayList.add(user.get("customerName")+"");
                    }
                    listView.setAdapter(arrayAdapter);
                    listViewId.setAdapter(arrayAdapterId);
                    loadingUsers.animate().alpha(0).setDuration(2000);
                    listView.setVisibility(View.VISIBLE);
                }
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this,ConfMake.class);
        intent.putExtra("objectId",arrayListId.get(position));
        startActivity(intent);
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
