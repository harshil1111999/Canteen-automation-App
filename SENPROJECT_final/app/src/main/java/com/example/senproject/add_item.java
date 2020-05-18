package com.example.senproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class add_item extends AppCompatActivity implements View.OnClickListener{

    private String CanteenNumber,CanteenName,CanteenAvailable;
    private Item dish;
    private EditText DName;
    private EditText DPrice;
    private Button Submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        Intent intent = getIntent();
        CanteenNumber = intent.getStringExtra("CanteenNumber");
        CanteenName = intent.getStringExtra("CanteenName");
        CanteenAvailable = intent.getStringExtra("CanteenAvailable");
        DName = (EditText) findViewById(R.id.ItemName);
        DPrice = (EditText) findViewById(R.id.ItemPrice);
        Submit = (Button) findViewById(R.id.SubmitItem);

        Submit.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(this, CanteenManager.class);
        intent.putExtra("CanteenNumber", CanteenNumber);
        intent.putExtra("CanteenName",CanteenName);
        intent.putExtra("CanteenAvailable", CanteenAvailable);
        finish();
        startActivity(intent);

    }

    @Override
    public void onClick(View v) {
        if (v == Submit){
            if(DName.getText().toString().equals("")){
                Toast.makeText(getApplicationContext(),"Enter Name",Toast.LENGTH_SHORT);
                return;
            }
            if (DPrice.getText().toString().equals("")){
                Toast.makeText(getApplicationContext(),"Enter Price",Toast.LENGTH_SHORT);
                return;
            }
            dish = new Item(DName.getText().toString(),DPrice.getText().toString(),"1");
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference table_items = database.getReference("Items"+CanteenNumber);
            table_items.child(DName.getText().toString()).setValue(dish);
            Intent intent = new Intent(this, CanteenManager.class);
            intent.putExtra("CanteenNumber", CanteenNumber);
            intent.putExtra("CanteenName",CanteenName);
            intent.putExtra("CanteenAvailable", CanteenAvailable);
            finish();
            startActivity(intent);
        }
    }
}
