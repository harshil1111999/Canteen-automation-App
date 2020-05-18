package com.example.senproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Canteenfeedback extends AppCompatActivity {

    private String CanteenName, CanteenNumber, OperationType,CanteenAvailable;
    private Button back;
    private ListView listView;
    private ArrayList<String> mItemName;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canteenfeedback);

        Intent intent = getIntent();
        CanteenName = intent.getStringExtra("CanteenName");
        CanteenNumber = intent.getStringExtra("CanteenNumber");
//        OperationType = intent.getStringExtra("OperationType");
        CanteenAvailable = intent.getStringExtra("CanteenAvailable");

//        back = (Button) findViewById(R.id.canteenFeedbackBack);
        listView = (ListView) findViewById(R.id.canteenFeedbackListView);

        mItemName = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.iteminfo,R.id.dishnameid,mItemName);

        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference table_Feedback = db.getReference("Feedback");
        final Query query = table_Feedback.orderByChild("canteenId").equalTo(CanteenNumber);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    Feedback feedback = ds.getValue(Feedback.class);
                    mItemName.add(feedback.getMessage());
                }
                listView.setAdapter(arrayAdapter);
                query.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Canteenfeedback.this,CanteenOrderStatus.class);
//                intent.putExtra("OperationType",OperationType);
//                intent.putExtra("CanteenNumber", CanteenNumber);
//                intent.putExtra("CanteenName",CanteenName);
//                intent.putExtra("CanteenAvailable", CanteenAvailable);
//                finish();
//                startActivity(intent);
//            }
//        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(Canteenfeedback.this,CanteenManager.class);
//        intent.putExtra("OperationType",OperationType);
        intent.putExtra("CanteenNumber", CanteenNumber);
        intent.putExtra("CanteenName",CanteenName);
        intent.putExtra("CanteenAvailable", CanteenAvailable);
        finish();
        startActivity(intent);

    }
}
