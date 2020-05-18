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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class OrderSummary extends AppCompatActivity {

    private TextView OrderBasic, TotalAmount, dispCookingInstrucction, dispPaymentMethod;
    private Button back, generateQR;
    private ListView listView;
    private ArrayList<String> mItemName;
    private ArrayAdapter<String> arrayAdapter;
    private String orderBasics, orderDetails,StautsType, CookingInstruction, PaymentMethod;
    private int amount=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);
        Intent intent = getIntent();
        orderBasics = intent.getStringExtra("OrderBasic");
        orderDetails = intent.getStringExtra("OrderDetails");
        StautsType = intent.getStringExtra("OperationType");
        CookingInstruction = intent.getStringExtra("CookingInstruction");
        PaymentMethod = intent.getStringExtra("PaymentMethod");
        OrderBasic = (TextView) findViewById(R.id.OrderSummaryText);
        TotalAmount = (TextView) findViewById(R.id.OrderDetailsTotalAmount);
        dispPaymentMethod = (TextView) findViewById(R.id.DisplayPaymentMethod);
//        back = (Button) findViewById(R.id.OrderSummaryBack);
        generateQR = (Button) findViewById(R.id.GenerateQr);
        dispCookingInstrucction = (TextView) findViewById(R.id.OrderSummaryCookingInstruction);
        listView = (ListView) findViewById(R.id.OrderSummaryListView);
        dispCookingInstrucction.setText(CookingInstruction);
        mItemName= new ArrayList<>();
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.iteminfo,R.id.dishnameid,mItemName);
        Log.v("OrderSummary","----------------------" + StautsType);
        if (StautsType.equals("OrderStatus")){
            if (PaymentMethod.equals("1")){
                dispPaymentMethod.setText("PAID");
            }
            else {
                dispPaymentMethod.setText("NOT PAID");
            }
//            generateQR.setEnabled(true);
//            generateQR.setVisibility(View.VISIBLE);
        }
        else {
            if (PaymentMethod.equals("1")){
                dispPaymentMethod.setText("ONLINE");
            }
            else {
                dispPaymentMethod.setText("CASH");
            }
//            generateQR.setEnabled(false);
//            generateQR.setVisibility(View.GONE);
        }
        int c=0,pre=0;
        String temp="";
        OrderBasic.setText(orderBasics);
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
//                Intent intent = new Intent(OrderSummary.this,OrderStatus.class);
//                intent.putExtra("OperationType",StautsType);
//                finish();
//                startActivity(intent);
//            }
//        });
//        generateQR.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String ordernumber="";
//                for (int i=0;i<orderBasics.length();i++){
//                    if (orderBasics.charAt(i) == '\n'){
//                        ordernumber = orderBasics.substring(10,i);
//                        break;
//                    }
//                }
//                Intent intent = new Intent(OrderSummary.this, GenerateQR.class);
//                intent.putExtra("OrderBasic", orderBasics);
//                intent.putExtra("OrderDetails",orderDetails);
//                intent.putExtra("OperationType", StautsType);
//                intent.putExtra("CookingInstruction",CookingInstruction);
//                intent.putExtra("PaymentMethod", PaymentMethod);
//                intent.putExtra("OrderNumber", ordernumber);
//                startActivity(intent);
//            }
//        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(OrderSummary.this,OrderStatus.class);
        intent.putExtra("OperationType",StautsType);
        finish();
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (StautsType.equals("OrderStatus")) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.order_summary_menu, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.GenerateQr:
                String ordernumber="";
                for (int i=0;i<orderBasics.length();i++){
                    if (orderBasics.charAt(i) == '\n'){
                        ordernumber = orderBasics.substring(10,i);
                        break;
                    }
                }
                Intent intent = new Intent(OrderSummary.this, GenerateQR.class);
                intent.putExtra("OrderBasic", orderBasics);
                intent.putExtra("OrderDetails",orderDetails);
                intent.putExtra("OperationType", StautsType);
                intent.putExtra("CookingInstruction",CookingInstruction);
                intent.putExtra("PaymentMethod", PaymentMethod);
                intent.putExtra("OrderNumber", ordernumber);
                finish();
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
