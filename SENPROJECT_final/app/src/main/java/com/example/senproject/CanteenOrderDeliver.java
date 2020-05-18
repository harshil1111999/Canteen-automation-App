package com.example.senproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CanteenOrderDeliver extends AppCompatActivity {

    private TextView OrderBasic, TotalAmount, dispCookingInstruction, dispPaymentMethod;
    private Button back,deliverOrder;
    private ListView listView;
    private ArrayList<String> mItemName;
    private ArrayAdapter<String> arrayAdapter;
    private String orderBasics, orderDetails, CanteenName, CanteenNumber,OrderNumber,OperationType,CanteenAvailable, CookingInstruction, PaymentMethod;
    private Order order;
    int flag=0, amount=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canteen_order_deliver);
        Intent intent = getIntent();
        orderBasics = intent.getStringExtra("OrderBasic");
        orderDetails = intent.getStringExtra("OrderDetails");
        CanteenName = intent.getStringExtra("CanteenName");
        CanteenNumber = intent.getStringExtra("CanteenNumber");
        OrderNumber = intent.getStringExtra("OrderNumber");
        OperationType=intent.getStringExtra("OperationType");
        CanteenAvailable = intent.getStringExtra("CanteenAvailable");
        CookingInstruction = intent.getStringExtra("CookingInstruction");
        PaymentMethod = intent.getStringExtra("PaymentMethod");
        Log.v("CanteenOrderDelivery","--------------" + CookingInstruction);
        OrderBasic = (TextView) findViewById(R.id.OrderDeliverText);
        TotalAmount = (TextView)findViewById(R.id.OrderDeliverDetailsTotalAmount);
        dispCookingInstruction = (TextView) findViewById(R.id.OrderDeliveryCookingInstruction);
        dispPaymentMethod = (TextView) findViewById(R.id.canteenDisplayPaymentMethod);
//        back = (Button) findViewById(R.id.OrderDeliverBack);
        deliverOrder = (Button) findViewById(R.id.DeliverOrder);
        listView = (ListView) findViewById(R.id.OrderDeliverListView);
        dispCookingInstruction.setText(CookingInstruction);
        mItemName= new ArrayList<>();
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.iteminfo,R.id.dishnameid,mItemName);
        if (OperationType.equals("OrderStatus")){
            if (PaymentMethod.equals("1")){
                dispPaymentMethod.setText("PAID");
            }
            else {
                dispPaymentMethod.setText("NOT PAID");
            }
        }
        else {
            if (PaymentMethod.equals("1")){
                dispPaymentMethod.setText("ONLINE");
            }
            else {
                dispPaymentMethod.setText("CASH");
            }
        }

        int c=0,pre=0;
        String temp="";
        OrderBasic.setText(orderBasics);
        if (OperationType.equals("OrderHistory")){
            deliverOrder.setEnabled(false);
            deliverOrder.setVisibility(View.GONE);
        }
        else {
            deliverOrder.setEnabled(true);
            deliverOrder.setVisibility(View.VISIBLE);
        }
        for(int i=0;i<orderDetails.length();i++){
            if (orderDetails.charAt(i)=='\n'){
                c++;
                if(c%3==2){
                    temp = orderDetails.substring(pre,i);
                    pre=i+1;
                }
                else if(c%3==0){
                    temp = temp + "\nQuantity: " + orderDetails.substring(pre,i);
                    pre=i+1;
                    mItemName.add(temp);
                }
            }
        }
        temp += "\nQuantity: " + orderDetails.substring(pre,orderDetails.length());
        mItemName.add(temp);
        listView.setAdapter(arrayAdapter);
        for (int i=0;i<mItemName.size();i++){
            int pree=0,flg=0;
            String amou="",quty="";
            for(int j=0;j<mItemName.get(i).length();j++){
                if (flg==1 && mItemName.get(i).charAt(j)=='\n'){
                    amou = mItemName.get(i).substring(pree,j);
                }
                if (flg==0 && mItemName.get(i).charAt(j)>='0' && mItemName.get(i).charAt(j)<='9' ){
                    flg=1;
                    pree=j;
                }
            }
            flg=0;
            for(int j=0;j<mItemName.get(i).length();j++){
                if (mItemName.get(i).charAt(j)=='\n'){
                    flg++;
                }
                if (flg==2 && mItemName.get(i).charAt(j)>='0' && mItemName.get(i).charAt(j)<='9' ){
                    quty = mItemName.get(i).substring(j,mItemName.get(i).length());
                    break;
                }
            }
            Log.v("PlaceOrder","---------------" + amou + "-----------" + quty);
            amount += Integer.parseInt(amou)*Integer.parseInt(quty);

        }
        TotalAmount.setText(Integer.toString(amount));
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(CanteenOrderDeliver.this,CanteenOrderStatus.class);
//                intent.putExtra("CanteenNumber",CanteenNumber);
//                intent.putExtra("CanteenName",CanteenName);
//                intent.putExtra("OperationType",OperationType);
//                intent.putExtra("CanteenAvailable", CanteenAvailable);
//                finish();
//                startActivity(intent);
//            }
//        });
        deliverOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FirebaseDatabase db1 = FirebaseDatabase.getInstance();
                final DatabaseReference table_CurrentOrder = db1.getReference("CurrentOrder");
                table_CurrentOrder.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(flag==0) {
                            order = dataSnapshot.child(OrderNumber).getValue(Order.class);
                            flag = 1;
                            final DatabaseReference table_DeliveredOrder = db1.getReference("DeliveredOrder");
                            table_DeliveredOrder.child(OrderNumber).setValue(order);
                            final DatabaseReference tble_temp = db1.getReference("CurrentOrder");
                            tble_temp.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    dataSnapshot.child(OrderNumber).getRef().removeValue();
                                    Intent intent = new Intent(CanteenOrderDeliver.this, CanteenManager.class);
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

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(CanteenOrderDeliver.this,CanteenOrderStatus.class);
        intent.putExtra("CanteenNumber",CanteenNumber);
        intent.putExtra("CanteenName",CanteenName);
        intent.putExtra("OperationType",OperationType);
        intent.putExtra("CanteenAvailable", CanteenAvailable);
        finish();
        startActivity(intent);

    }
}
