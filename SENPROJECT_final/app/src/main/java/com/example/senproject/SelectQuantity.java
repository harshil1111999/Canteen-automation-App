package com.example.senproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SelectQuantity extends AppCompatActivity implements View.OnClickListener{

    private String CanteenNumber,CanteenName,OrderString,CurrentDish,CurrentActivity;
    private TextView dishName,dishPrice,qty;
    private Button add,sub, submit;
    private String name,price;
    private int quantity=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_quantity);
        Intent intent = getIntent();
        CanteenName = intent.getStringExtra("CanteenName");
        CanteenNumber = intent.getStringExtra("CanteenNumber");
        OrderString = intent.getStringExtra("OrderString");
        CurrentDish = intent.getStringExtra("CurrentDish");
        CurrentActivity = intent.getStringExtra("CurrentActivity");
        Log.v("SelectQuantity","+++++++++++++++++++++++++"+OrderString +"\n Length : " + OrderString.length());
        dishName = (TextView) findViewById(R.id.additemname);
        dishPrice = (TextView) findViewById(R.id.additemprice);
        qty = (TextView) findViewById(R.id.selectquantity);
        add = (Button) findViewById(R.id.addition);
        sub = (Button) findViewById(R.id.subtract);
        submit = (Button) findViewById(R.id.QuantitySubmit);

        for(int i=0;i<CurrentDish.length();i++){
            if (CurrentDish.charAt(i)=='\n'){
                name = CurrentDish.substring(0,i);
            }
            if(CurrentDish.charAt(i)>='0' && CurrentDish.charAt(i)<='9'){
                price = CurrentDish.substring(i,CurrentDish.length());
                break;
            }
        }
        dishName.setText(name);
        dishPrice.setText(price);
        add.setOnClickListener(this);
        sub.setOnClickListener(this);
        submit.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i = new Intent(this,ResturantMenu.class);
        i.putExtra("CanteenNumber", CanteenNumber);
        i.putExtra("CanteenName", CanteenName);
        i.putExtra("OrderString", OrderString);
        finish();
        startActivity(i);

    }

    @Override
    public void onClick(View v) {
        if (v == add) {
            if(quantity<10)
                quantity+=1;
            qty.setText(Integer.toString(quantity));
        }
        if (v == sub){
            if(quantity>0)
                quantity-=1;
            qty.setText(Integer.toString(quantity));
        }
        OrderString+="\n";
        if (v == submit){
            String temp="";
            int c=0,flag=0,prev=0,pre=0;
            for(int i=0;i<OrderString.length();i++){
                if(OrderString.charAt(i)=='\n')
                {
                    c+=1;
                    if(c%3==1 && OrderString.substring(prev,i).equals(name)){
                        flag=1;

                    }
                    else if(c%3==0){
                        if(flag==1) {
                            OrderString = OrderString.substring(0, pre) + OrderString.substring(i+1,OrderString.length());
                            flag=0;
                        }
                        pre=i+1;
                    }
                    prev=i+1;
                }
            }
            OrderString = OrderString.trim();
            if (quantity!=0){
                OrderString += "\n" +CurrentDish + "\n" + Integer.toString(quantity) + "\n";
                OrderString = OrderString.trim();
            }
            Log.v("SelectQuantity","----------------------"+OrderString +"\n Length : " + OrderString.length());
            Intent intent1 = new Intent(getApplicationContext(), ResturantMenu.class);
            if(CurrentActivity.equals("PlaceOrder")){
                intent1 = new Intent(getApplicationContext(), PlaceOrder.class);
            }
            intent1.putExtra("OrderString", OrderString);
            intent1.putExtra("CanteenNumber", CanteenNumber);
            intent1.putExtra("CanteenName",CanteenName);
            finish();
            startActivity(intent1);

        }
    }
}
