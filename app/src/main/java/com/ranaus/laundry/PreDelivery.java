package com.ranaus.laundry;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PreDelivery extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listView,listViewId;
    private ArrayList<String> arrayList,arrayListId;
    private ArrayAdapter arrayAdapter,arrayAdapterId;
    private TextView loadingUsers;
    private String objId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_delivery);

        listView = findViewById(R.id.list_view_PreDeliveryAct);
        loadingUsers = findViewById(R.id.text_view_PreDeliveryAct);
        listViewId = findViewById(R.id.objectId);
        arrayList = new ArrayList();
        arrayListId = new ArrayList();
        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayList);
        arrayAdapterId = new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayListId);

        listView.setOnItemClickListener(this);

        getDetails();
    }


    private void getDetails()
    {
        ParseQuery<ParseObject> parseQuery = new ParseQuery<ParseObject>("customerDetails");
        parseQuery.whereEqualTo("status", "active");

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();

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
                        //arrayList.add(String.valueOf(user));
                    }
                    listView.setAdapter(arrayAdapter);
                    listViewId.setAdapter(arrayAdapterId);
                    loadingUsers.animate().alpha(0).setDuration(2000);
                    listView.setVisibility(View.VISIBLE);
                }
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this,ConfPreDelete.class);
        intent.putExtra("objectId",arrayListId.get(position));
        startActivity(intent);
        finish();

    }
}
