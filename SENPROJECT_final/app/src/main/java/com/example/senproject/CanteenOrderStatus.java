package com.example.senproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

public class CanteenOrderStatus extends AppCompatActivity {

    private TextView noPendingOrder, StatusType;
    private Button back, scanQr, contactFeedback;
    private ListView listView;
    private ArrayList<String> mItemName, OrderDetails,saveOrderNumber, CookingInstruction, paymentMethod;
    private ArrayAdapter<String> arrayAdapter;
    private String CanteenName, CanteenNumber, OperationType,table_name,CanteenAvailable;
    private int count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canteen_order_status);
        Intent intent = getIntent();
        CanteenName = intent.getStringExtra("CanteenName");
        CanteenNumber = intent.getStringExtra("CanteenNumber");
        OperationType = intent.getStringExtra("OperationType");
        CanteenAvailable = intent.getStringExtra("CanteenAvailable");
        noPendingOrder = (TextView) findViewById(R.id.pendingOrderCanteen);
        StatusType = (TextView) findViewById(R.id.CanteenOrderStatusType);
//        back = (Button) findViewById(R.id.OrderStatusCanteenBack);
//        scanQr = (Button) findViewById(R.id.ScanQr);
//        contactFeedback = (Button) findViewById(R.id.contactFeedbackCanteen);
        listView = (ListView) findViewById(R.id.PendingOrderCanteenListView);
        mItemName= new ArrayList<>();
        CookingInstruction = new ArrayList<>();
        saveOrderNumber = new ArrayList<>();
        OrderDetails= new ArrayList<>();
        paymentMethod = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.iteminfo,R.id.dishnameid,mItemName);
        noPendingOrder.setText(Integer.toString(count));
        if (OperationType.equals("OrderHistory")){
            StatusType.setText("Total History: ");
            table_name = "DeliveredOrder";
//            scanQr.setEnabled(false);
//            scanQr.setVisibility(View.GONE);
//            contactFeedback.setEnabled(true);
//            contactFeedback.setVisibility(View.VISIBLE);
        }
        else {
            StatusType.setText("Pending Orders: ");
            table_name = "CurrentOrder";
//            scanQr.setEnabled(true);
//            scanQr.setVisibility(View.VISIBLE);
//            contactFeedback.setEnabled(false);
//            contactFeedback.setVisibility(View.GONE);
        }
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_currorder = database.getReference(table_name);
        final Query query = table_currorder.orderByChild("canteenId").equalTo(CanteenNumber);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    Order order = ds.getValue(Order.class);
                    mItemName.add("Order No: " + order.getOrderNo() + "\nCustomerId: " + order.getCustomerId());
                    OrderDetails.add(order.getOrderDetails());
                    saveOrderNumber.add(order.getOrderNo());
                    CookingInstruction.add(order.getCookingInstruction());
                    paymentMethod.add(order.getPaymetMethod());
                    count+=1;
                    noPendingOrder.setText(Integer.toString(count));
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
//                Intent intent = new Intent(CanteenOrderStatus.this,CanteenManager.class);
//                intent.putExtra("CanteenName",CanteenName);
//                intent.putExtra("CanteenNumber",CanteenNumber);
//                intent.putExtra("CanteenAvailable", CanteenAvailable);
//                CanteenOrderStatus.this.finish();
//                startActivity(intent);
//            }
//        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CanteenOrderStatus.this,CanteenOrderDeliver.class);
                intent.putExtra("CanteenName",CanteenName);
                intent.putExtra("CanteenNumber",CanteenNumber);
                intent.putExtra("OrderBasic", mItemName.get(position));
                intent.putExtra("OrderDetails",OrderDetails.get(position));
                intent.putExtra("OrderNumber",saveOrderNumber.get(position));
                intent.putExtra("OperationType",OperationType);
                intent.putExtra("CookingInstruction",CookingInstruction.get(position));
                intent.putExtra("CanteenAvailable", CanteenAvailable);
                intent.putExtra("PaymentMethod",paymentMethod.get(position));
                CanteenOrderStatus.this.finish();
                startActivity(intent);
            }
        });
//        scanQr.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(CanteenOrderStatus.this,ReadQr.class);
//                intent.putExtra("CanteenNumber",CanteenNumber);
//                intent.putExtra("CanteenName",CanteenName);
//                intent.putExtra("OperationType",OperationType);
//                intent.putExtra("CanteenAvailable", CanteenAvailable);
//                finish();
//                startActivity(intent);
//            }
//        });
//        contactFeedback.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(CanteenOrderStatus.this,Canteenfeedback.class);
//                intent.putExtra("CanteenNumber",CanteenNumber);
//                intent.putExtra("CanteenName",CanteenName);
//                intent.putExtra("OperationType",OperationType);
//                intent.putExtra("CanteenAvailable", CanteenAvailable);
//                finish();
//                startActivity(intent);
//            }
//        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(CanteenOrderStatus.this,CanteenManager.class);
        intent.putExtra("CanteenName",CanteenName);
        intent.putExtra("CanteenNumber",CanteenNumber);
        intent.putExtra("CanteenAvailable", CanteenAvailable);
        CanteenOrderStatus.this.finish();
        startActivity(intent);

    }

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        MenuInflater inflater = getMenuInflater();
//        if(OperationType.equals("OrderHistory"))
//            inflater.inflate(R.menu.canteen_order_history_menu,menu);
//        else
//            inflater.inflate(R.menu.canteen_order_status_menu,menu);
//
//        return true;
//
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.contactFeedbackCanteen:
//                Intent intent1 = new Intent(CanteenOrderStatus.this,Canteenfeedback.class);
//                intent1.putExtra("CanteenNumber",CanteenNumber);
//                intent1.putExtra("CanteenName",CanteenName);
//                intent1.putExtra("OperationType",OperationType);
//                intent1.putExtra("CanteenAvailable", CanteenAvailable);
////                finish();
//                startActivity(intent1);
//                return true;
//            case R.id.ScanQr:
//                Intent intent = new Intent(CanteenOrderStatus.this,ReadQr.class);
//                intent.putExtra("CanteenNumber",CanteenNumber);
//                intent.putExtra("CanteenName",CanteenName);
//                intent.putExtra("OperationType",OperationType);
//                intent.putExtra("CanteenAvailable", CanteenAvailable);
////                finish();
//                startActivity(intent);
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
}
