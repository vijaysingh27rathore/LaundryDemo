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
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GetBillByUser extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listView,listViewId;
    private ArrayList<String> arrayList,arrayListId;
    private ArrayAdapter arrayAdapter,arrayAdapterId;
    private TextView loadingUsers;
    private String objId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_bill_by_user);

        listView = findViewById(R.id.list_view_GetBillByUserAct);
        loadingUsers = findViewById(R.id.text_view_GetBillByUserAct);
        listViewId = findViewById(R.id.objectId_GetBillByUserAct);
        arrayList = new ArrayList();
        arrayListId = new ArrayList();
        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayList);
        arrayAdapterId = new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayListId);

        listView.setOnItemClickListener(this);
        getData();
    }

    private void getData() {
        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("completedOrders");
        parseQuery.whereEqualTo("status","completed");
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
        Intent intent = new Intent(this,ConfCompletedBill.class);
        intent.putExtra("objectId",arrayListId.get(position));
        startActivity(intent);

    }
}
