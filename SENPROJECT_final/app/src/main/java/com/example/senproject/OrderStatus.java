package com.example.senproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderStatus extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private TextView noPendingOrder,OrderStatusType;
    private Button back;
    private ListView listView;
    private ArrayList<String> mItemName, OrderDetails, CookingInstruction, paymentMethod;
    private ArrayAdapter<String> arrayAdapter;
    private String customerId;
    private int count=0;
    private String StautsType,table_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);
        Intent intent = getIntent();
        StautsType = intent.getStringExtra("OperationType");
        noPendingOrder = (TextView) findViewById(R.id.pendingOrderNumber);
//        back = (Button) findViewById(R.id.OrderStatusBack);
        listView = (ListView) findViewById(R.id.PendingOrderListView);
        OrderStatusType = (TextView) findViewById(R.id.OrderStautsType);
        mItemName= new ArrayList<>();
        paymentMethod = new ArrayList<>();
        OrderDetails= new ArrayList<>();
        CookingInstruction = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.iteminfo,R.id.dishnameid,mItemName);
        if (StautsType.equals("OrderHistory")){
            OrderStatusType.setText("Total History: ");
            table_name = "DeliveredOrder";
        }
        else {
            OrderStatusType.setText("Pending Orders: ");
            table_name = "CurrentOrder";
        }
        Log.v("OrderStatus","Yes ------------------------");
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String email = user.getEmail();
        for (int i=0; i<email.length();i++){
            if (email.charAt(i)=='@'){
                customerId = email.substring(0,i);
                break;
            }
        }
        Log.v("OrderStatus","Yes ------------------------");
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_currorder = database.getReference(table_name);
        final Query query = table_currorder.orderByChild("customerId").equalTo(customerId);
        noPendingOrder.setText(Integer.toString(count));
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    Order order = ds.getValue(Order.class);
                    mItemName.add("Order No: " + order.getOrderNo() + "\nCanteen: " + order.getCanteenName());
                    OrderDetails.add(order.getOrderDetails());
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
//                finish();
//                startActivity(new Intent(OrderStatus.this,ResturantList.class));
//            }
//        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(OrderStatus.this,OrderSummary.class);
                intent.putExtra("OrderBasic", mItemName.get(position));
                intent.putExtra("OrderDetails",OrderDetails.get(position));
                intent.putExtra("OperationType", StautsType);
                intent.putExtra("CookingInstruction",CookingInstruction.get(position));
                intent.putExtra("PaymentMethod", paymentMethod.get(position));
                finish();
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        startActivity(new Intent(OrderStatus.this,ResturantList.class));

    }
}
