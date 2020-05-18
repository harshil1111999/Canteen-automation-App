package com.example.senproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditMenu extends AppCompatActivity implements View.OnClickListener{

    private String CanteenNumber,CanteenName, availible, CanteenAvailable;
    private String ItemString;
    private String name,price;
    private EditText  dishPrice;
    private TextView dishName,availability;
    private Button submit, delete,ChangeAvailibility;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_menu);
        Intent intent = getIntent();
        CanteenNumber = intent.getStringExtra("CanteenNumber");
        CanteenName = intent.getStringExtra("CanteenName");
        ItemString = intent.getStringExtra("ItemString");
        availible = intent.getStringExtra("Availibility");
        CanteenAvailable = intent.getStringExtra("CanteenAvailable");
        int flag=0;
        for(int i=0;i<ItemString.length();i++){
            if (flag==0 && ItemString.charAt(i)=='\n'){
                name = ItemString.substring(0,i);
                flag=1;
            }
            if(flag==1 && ItemString.charAt(i)>='0' && ItemString.charAt(i)<='9'){
                price = ItemString.substring(i,ItemString.length());
                break;
            }
        }
        dishName = (TextView) findViewById(R.id.editItemName);
        dishPrice = (EditText) findViewById(R.id.editItemPrice);
        availability = (TextView) findViewById(R.id.ItemAvailability);
        submit = (Button) findViewById(R.id.editSubmitItem);
        delete = (Button) findViewById(R.id.editDeleteItem);
        ChangeAvailibility = (Button) findViewById(R.id.ItemChangeAvailability);

        dishName.setText(name);
        dishPrice.setText(price);
        if (availible.equals("1")){
            availability.setText("Available");
        }
        else {
            availability.setText("Unavailable");
        }
        ChangeAvailibility.setOnClickListener(this);
        submit.setOnClickListener(this);
        delete.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i = new Intent(this,CanteenManager.class);
        i.putExtra("CanteenName", CanteenName);
        i.putExtra("CanteenNumber", CanteenNumber);
        i.putExtra("CanteenAvailable", CanteenAvailable);
        finish();
        startActivity(i);

    }

    @Override
    public void onClick(View v) {
        if (v == submit){
            if(dishName.getText().toString().equals("")){
                Toast.makeText(getApplicationContext(),"Enter Name",Toast.LENGTH_SHORT);
                return;
            }
            if (dishPrice.getText().toString().equals("")){
                Toast.makeText(getApplicationContext(),"Enter Price",Toast.LENGTH_SHORT);
                return;
            }
            Item dish = new Item(dishName.getText().toString(),dishPrice.getText().toString(),availible);
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference table_items = database.getReference("Items"+CanteenNumber);
            table_items.child(dishName.getText().toString()).setValue(dish);
            Log.v("EditMenu","Not problem Here-----------" + CanteenAvailable);
            Intent intent = new Intent(this, CanteenManager.class);
            intent.putExtra("CanteenNumber", CanteenNumber);
            intent.putExtra("CanteenAvailable", CanteenAvailable);
            intent.putExtra("CanteenName",CanteenName);
            finish();
            startActivity(intent);
        }
        if (v == ChangeAvailibility){
            if (availible.equals("1")){
                availible="0";
                availability.setText("Unavailable");
            }
            else {
                availible="1";
                availability.setText("Available");
            }
        }
        if (v == delete){
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference table_items = database.getReference("Items"+CanteenNumber);
            table_items.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    dataSnapshot.child(name).getRef().removeValue();
                    Intent intent = new Intent(EditMenu.this, CanteenManager.class);
                    intent.putExtra("CanteenNumber", CanteenNumber);
                    intent.putExtra("CanteenName",CanteenName);
                    intent.putExtra("CanteenAvailable", CanteenAvailable);
                    finish();
                    startActivity(intent);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
