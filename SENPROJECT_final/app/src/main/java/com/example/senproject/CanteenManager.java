package com.example.senproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CanteenManager extends AppCompatActivity implements View.OnClickListener{

    private TextView CanteenName, DisplayAvailibility, currentBalance;
    private Button CanteenLogout, canteenStatus, OrderHistory, ChangeAvailibility;
    private Button AddItem;
    private String CanteenNumber,CanteenAvailable;
    private String CanteenNam;
    private DatabaseReference mDatabase, nab;
    private ValueEventListener newlistner;
    private ListView disp;
    private ArrayList<String> mItemName, availibility, mItemName1;
    private ArrayAdapter<String>arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canteen_manager);
        Intent intent = getIntent();
        CanteenNam = intent.getStringExtra("CanteenName");
        CanteenNumber = intent.getStringExtra("CanteenNumber");
        Log.v("CanteenManager","********************* No Problem Here");
        CanteenAvailable = intent.getStringExtra("CanteenAvailable");
        Log.v("CanteenManager","********************* No Problem Here" + CanteenAvailable);
        CanteenName = (TextView) findViewById(R.id.textViewCanteenName);
        DisplayAvailibility = (TextView) findViewById(R.id.DisplayCanteenAvailibility);
        currentBalance = (TextView) findViewById(R.id.CanteenCurrentBalance);
        ChangeAvailibility = (Button) findViewById(R.id.ChangeCanteenAvailibility);
        CanteenName.setText(intent.getStringExtra("CanteenName"));
//        CanteenLogout = (Button) findViewById(R.id.buttonLogoutCanteen);
//        canteenStatus = (Button) findViewById(R.id.ViewCanteenStatus);
//        OrderHistory = (Button) findViewById(R.id.CanteenOrderHistory);
        AddItem = (Button) findViewById(R.id.AddItemToMenu);
        disp = (ListView) findViewById(R.id.CanteenListView);

        final FirebaseDatabase database9 = FirebaseDatabase.getInstance();
        nab = database9.getReference("Canteen/"+CanteenNumber);
        newlistner = nab.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String currBalance = dataSnapshot.child("Virtual_Money").getValue(String.class);
                currentBalance.setText("₹" + currBalance);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (CanteenAvailable.equals("1")){
            DisplayAvailibility.setText("Available");
        }
        else {
            DisplayAvailibility.setText("Unavailable");
        }
        mDatabase = FirebaseDatabase.getInstance().getReference("Items"+CanteenNumber);
        mItemName= new ArrayList<>();
        mItemName1= new ArrayList<>();
        availibility = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.iteminfo,R.id.dishnameid,mItemName1);
        disp.setAdapter(arrayAdapter);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    Item item = ds.getValue(Item.class);
                    mItemName.add(item.getName()+ "\nPrice: " + item.getPrice());
                    if (item.getAcailability().equals("1")){
                        mItemName1.add(item.getName()+ "\nPrice: " + item.getPrice() + "\nAvailable");
                    }
                    else {
                        mItemName1.add(item.getName()+ "\nPrice: " + item.getPrice() + "\nUnavailable");
                    }
                    availibility.add(item.getAcailability());
                }
                disp.setAdapter(arrayAdapter);
                mDatabase.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        disp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                nab.removeEventListener(newlistner);
                Intent intent1 = new Intent(getApplicationContext(), EditMenu.class);
                intent1.putExtra("ItemString", mItemName.get(position));
                intent1.putExtra("CanteenNumber", CanteenNumber);
                intent1.putExtra("CanteenName",CanteenNam);
                intent1.putExtra("Availibility",availibility.get(position));
                intent1.putExtra("CanteenAvailable", CanteenAvailable);
                CanteenManager.this.finish();
                startActivity(intent1);
            }
        });
//        CanteenLogout.setOnClickListener(this);
        AddItem.setOnClickListener(this);
//        canteenStatus.setOnClickListener(this);
//        OrderHistory.setOnClickListener(this);
        ChangeAvailibility.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.canteen_manager_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.buttonLogoutCanteen:
                nab.removeEventListener(newlistner);
                CanteenManager.this.finish();
                startActivity(new Intent(this, CanteenLogin.class));
                return true;
            case R.id.CanteenOrderHistory:
                nab.removeEventListener(newlistner);
                Intent intent2 = new Intent(this,CanteenOrderStatus.class);
                intent2.putExtra("OperationType","OrderHistory");
                intent2.putExtra("CanteenNumber", CanteenNumber);
                intent2.putExtra("CanteenName",CanteenNam);
                intent2.putExtra("CanteenAvailable", CanteenAvailable);
                CanteenManager.this.finish();
                startActivity(intent2);
                return true;
            case R.id.ViewCanteenStatus:
                Intent intent3 = new Intent(this,CanteenOrderStatus.class);
                intent3.putExtra("OperationType","OrderStatus");
                intent3.putExtra("CanteenNumber", CanteenNumber);
                intent3.putExtra("CanteenName",CanteenNam);
                intent3.putExtra("CanteenAvailable", CanteenAvailable);
                CanteenManager.this.finish();
                startActivity(intent3);
                return true;
            case R.id.contactFeedbackCanteen:
                Intent intent1 = new Intent(this,Canteenfeedback.class);
                intent1.putExtra("CanteenNumber",CanteenNumber);
                intent1.putExtra("CanteenName",CanteenNam);
//                intent1.putExtra("OperationType",OperationType);
                intent1.putExtra("CanteenAvailable", CanteenAvailable);
                finish();
                startActivity(intent1);
                return true;
            case R.id.ScanQr:
                Intent intent = new Intent(this,ReadQr.class);
                intent.putExtra("CanteenNumber",CanteenNumber);
                intent.putExtra("CanteenName",CanteenNam);
                intent.putExtra("OperationType","OrderStatus");
                intent.putExtra("CanteenAvailable", CanteenAvailable);
                finish();
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
//        if (v == CanteenLogout) {
//            nab.removeEventListener(newlistner);
//            CanteenManager.this.finish();
//            startActivity(new Intent(this, CanteenLogin.class));
//        }
        if(v == AddItem){
            nab.removeEventListener(newlistner);
            Intent intent = new Intent(this, add_item.class);
            intent.putExtra("CanteenAvailable", CanteenAvailable);
            intent.putExtra("CanteenNumber", CanteenNumber);
            intent.putExtra("CanteenName",CanteenNam);
            CanteenManager.this.finish();
            startActivity(intent);
        }
//        if (v==canteenStatus){
//            Intent intent = new Intent(this,CanteenOrderStatus.class);
//            intent.putExtra("OperationType","OrderStatus");
//            intent.putExtra("CanteenNumber", CanteenNumber);
//            intent.putExtra("CanteenName",CanteenNam);
//            intent.putExtra("CanteenAvailable", CanteenAvailable);
//            CanteenManager.this.finish();
//            startActivity(intent);
//        }
//        if (v == OrderHistory){
//            nab.removeEventListener(newlistner);
//            Intent intent = new Intent(this,CanteenOrderStatus.class);
//            intent.putExtra("OperationType","OrderHistory");
//            intent.putExtra("CanteenNumber", CanteenNumber);
//            intent.putExtra("CanteenName",CanteenNam);
//            intent.putExtra("CanteenAvailable", CanteenAvailable);
//            CanteenManager.this.finish();
//            startActivity(intent);
//        }
        if (v == ChangeAvailibility){
            if (CanteenAvailable.equals("1")){
                CanteenAvailable = "0";
                DisplayAvailibility.setText("Unavailable");
            }
            else {
                CanteenAvailable = "1";
                DisplayAvailibility.setText("Available");
            }
            final FirebaseDatabase db = FirebaseDatabase.getInstance();
            final DatabaseReference table_Canteen = db.getReference("Canteen/"+CanteenNumber);
            table_Canteen.child("Available").setValue(CanteenAvailable);
        }
    }
}
