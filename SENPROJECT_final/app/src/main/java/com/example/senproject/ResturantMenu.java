package com.example.senproject;

import androidx.annotation.NonNull;
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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ResturantMenu extends AppCompatActivity implements View.OnClickListener{

    private Button back,placeOrder, contactFeedback;
    private String CanteenName,CanteenNumber,OrderString;
    private DatabaseReference mDatabase;
    private ListView disp;
    private ArrayList<String> mItemName,mItemName1, availibility;
    private ArrayAdapter<String> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resturant_menu);
        Intent intent = getIntent();
        TextView temp = (TextView) findViewById(R.id.MenuResturantName);
        temp.setText(intent.getStringExtra("CanteenName"));
        CanteenName = intent.getStringExtra("CanteenName");
        CanteenNumber = intent.getStringExtra("CanteenNumber");
        OrderString = intent.getStringExtra("OrderString");
//        back = (Button) findViewById(R.id.back);
        placeOrder = (Button) findViewById(R.id.PlaceOrder12);
//        contactFeedback = (Button) findViewById(R.id.ContactFeedback);
        disp = (ListView) findViewById(R.id.ListMenuView);

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
                    availibility.add(item.getAcailability());
                    if (item.getAcailability().equals("1")){
                        mItemName1.add(item.getName()+ "\nPrice: " + item.getPrice() + "\nAvailable");
                    }
                    else {
                        mItemName1.add(item.getName()+ "\nPrice: " + item.getPrice() + "\nUnavailable");
                    }
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
                if (availibility.get(position).equals("0")){
                    Toast.makeText(getApplicationContext(),"Item is unaivable currently", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent1 = new Intent(getApplicationContext(), SelectQuantity.class);
                intent1.putExtra("CurrentActivity","ResturantMenu");
                intent1.putExtra("CurrentDish", mItemName.get(position));
                intent1.putExtra("OrderString", OrderString);
                intent1.putExtra("CanteenNumber", CanteenNumber);
                intent1.putExtra("CanteenName",CanteenName);
                finish();
                startActivity(intent1);
            }
        });

//        back.setOnClickListener(this);
        placeOrder.setOnClickListener(this);
//        contactFeedback.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        startActivity(new Intent(this, ResturantList.class));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.resturantmenu_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ContactFeedback:
                Intent intent1 = new Intent(getApplicationContext(), ContactFeedback.class);
                intent1.putExtra("OrderString", OrderString);
                intent1.putExtra("CanteenNumber", CanteenNumber);
                intent1.putExtra("CanteenName",CanteenName);
                finish();
                startActivity(intent1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
//        if (v == back) {
//            finish();
//            startActivity(new Intent(this, ResturantList.class));
//        }
        if (v == placeOrder){
            if (OrderString.isEmpty()){
                Toast.makeText(getApplicationContext(),"Pls add Items before Proceding",Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent1 = new Intent(getApplicationContext(), PlaceOrder.class);
            intent1.putExtra("OrderString", OrderString);
            intent1.putExtra("CanteenNumber", CanteenNumber);
            intent1.putExtra("CanteenName",CanteenName);
            finish();
            startActivity(intent1);
        }
//        if (v == contactFeedback){
//            Intent intent1 = new Intent(getApplicationContext(), ContactFeedback.class);
//            intent1.putExtra("OrderString", OrderString);
//            intent1.putExtra("CanteenNumber", CanteenNumber);
//            intent1.putExtra("CanteenName",CanteenName);
////            finish();
//            startActivity(intent1);
//        }
    }
}
