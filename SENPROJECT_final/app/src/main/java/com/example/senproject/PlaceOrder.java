package com.example.senproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PlaceOrder extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth firebaseAuth;
    private String CanteenName,CanteenNumber,OrderString,customerId;
    private Button back, placeOrderB, changePaymentMethod;
    private TextView amt, currentBalance, paymentMethod;
    private EditText CookingInstruction;
    private int amount=0;
    private int flag=0;
    private Order OrderNew;
    private ListView disp;
    private ArrayList<String> mItemName;
    private ArrayAdapter<String> arrayAdapter;
    private String availableBalance, currentPaymentMethod = "0";
    private ValueEventListener newlistner;
    private DatabaseReference db4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);
        Intent intent = getIntent();
        CanteenName = intent.getStringExtra("CanteenName");
        CanteenNumber = intent.getStringExtra("CanteenNumber");
        OrderString = intent.getStringExtra("OrderString");
        Log.v("SelectQuantity","**************************************"+OrderString +"\n Length : " + OrderString.length());
//        back = (Button) findViewById(R.id.PlaceOrderBack);
        placeOrderB = (Button) findViewById(R.id.PlaceOrderFinal12);
        changePaymentMethod = (Button) findViewById(R.id.changePaymentType);
        amt = (TextView) findViewById(R.id.amount);
        currentBalance = (TextView) findViewById(R.id.placeOrderCurrentBalance);
        paymentMethod = (TextView) findViewById(R.id.paymentType);
        disp = (ListView) findViewById(R.id.PlaceOrderList);
        CookingInstruction = (EditText) findViewById(R.id.addCookingInstruction);
        mItemName= new ArrayList<>();
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.iteminfo,R.id.dishnameid,mItemName);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user2 = firebaseAuth.getCurrentUser();
        final String userid = user2.getEmail().substring(0,9);
        final FirebaseDatabase db3 = FirebaseDatabase.getInstance();
        db4 = db3.getReference("User");
        newlistner =  db4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User usertemp = dataSnapshot.child(userid).getValue(User.class);
                availableBalance = usertemp.getVirtual_Money();
                currentBalance.setText("₹" + availableBalance);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (OrderString.length() == 0){
            Intent intent1 = new Intent(getApplicationContext(), ResturantMenu.class);
            intent1.putExtra("OrderString", OrderString);
            intent1.putExtra("CanteenNumber", CanteenNumber);
            intent1.putExtra("CanteenName",CanteenName);
            finish();
            startActivity(intent1);
        }
        else {
            int c = 0, pre = 0;
            String temp = "";

            for (int i = 0; i < OrderString.length(); i++) {
                if (OrderString.charAt(i) == '\n') {
                    c++;
                    if (c % 3 == 2) {
                        temp = OrderString.substring(pre, i);
                        pre = i + 1;
                    } else if (c % 3 == 0) {
                        temp = temp + "\nQuantity: " + OrderString.substring(pre, i);
                        pre = i + 1;
                        mItemName.add(temp);
                    }
                }
            }
            temp += "\nQuantity: " + OrderString.substring(pre, OrderString.length());
            mItemName.add(temp);
            disp.setAdapter(arrayAdapter);

            for (int i = 0; i < mItemName.size(); i++) {
                int pree = 0, flg = 0;
                String amou = "", quty = "";
                for (int j = 0; j < mItemName.get(i).length(); j++) {
                    if (flg == 1 && mItemName.get(i).charAt(j) == '\n') {
                        amou = mItemName.get(i).substring(pree, j);
                    }
                    if (flg == 0 && mItemName.get(i).charAt(j) >= '0' && mItemName.get(i).charAt(j) <= '9') {
                        flg = 1;
                        pree = j;
                    }
                }
                flg = 0;
                for (int j = 0; j < mItemName.get(i).length(); j++) {
                    if (mItemName.get(i).charAt(j) == '\n') {
                        flg++;
                    }
                    if (flg == 2 && mItemName.get(i).charAt(j) >= '0' && mItemName.get(i).charAt(j) <= '9') {
                        quty = mItemName.get(i).substring(j, mItemName.get(i).length());
                        break;
                    }
                }
                Log.v("PlaceOrder", "---------------" + amou + "-----------" + quty);
                amount += Integer.parseInt(amou) * Integer.parseInt(quty);
            }

            disp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String temp = "";
                    int c = 0;
                    for (int i = 0; i < mItemName.get(position).length(); i++) {
                        if (mItemName.get(position).charAt(i) == '\n') {
                            c++;
                        }
                        if (c == 2) {
                            temp = mItemName.get(position).substring(0, i);
                            break;
                        }
                    }
                    Intent intent1 = new Intent(getApplicationContext(), SelectQuantity.class);
                    intent1.putExtra("CurrentActivity", "PlaceOrder");
                    intent1.putExtra("CurrentDish", temp);
                    intent1.putExtra("OrderString", OrderString);
                    intent1.putExtra("CanteenNumber", CanteenNumber);
                    intent1.putExtra("CanteenName", CanteenName);
                    finish();
                    startActivity(intent1);
                }
            });
            amt.setText(Integer.toString(amount));
//            back.setOnClickListener(this);
            placeOrderB.setOnClickListener(this);
            changePaymentMethod.setOnClickListener(this);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        db4.removeEventListener(newlistner);
        Intent intent1 = new Intent(getApplicationContext(), ResturantMenu.class);
        intent1.putExtra("OrderString", OrderString);
        intent1.putExtra("CanteenNumber", CanteenNumber);
        intent1.putExtra("CanteenName",CanteenName);
        finish();
        startActivity(intent1);

    }

    @Override
    public void onClick(View v) {
        if (v == changePaymentMethod){
            if (currentPaymentMethod.equals("0")) {
                if (amount > Integer.parseInt(availableBalance)) {
                    Toast.makeText(getApplicationContext(), "Not Enough Balance", Toast.LENGTH_SHORT).show();
                } else {
                    currentPaymentMethod = "1";
                    paymentMethod.setText("Online");
                }
            }
            else {
                currentPaymentMethod = "0";
                paymentMethod.setText("Cash");
            }
        }
//        if (v == back) {
//            db4.removeEventListener(newlistner);
//            Intent intent1 = new Intent(getApplicationContext(), ResturantMenu.class);
//            intent1.putExtra("OrderString", OrderString);
//            intent1.putExtra("CanteenNumber", CanteenNumber);
//            intent1.putExtra("CanteenName",CanteenName);
//            finish();
//            startActivity(intent1);
//        }
        if (v == placeOrderB){
            if(firebaseAuth.getCurrentUser() == null){
                finish();
                startActivity(new Intent(this,MainActivity.class));
            }
            FirebaseUser user = firebaseAuth.getCurrentUser();
            String email = user.getEmail();
            for (int i=0; i<email.length();i++){
                if (email.charAt(i)=='@'){
                    customerId = email.substring(0,i);
                    break;
                }
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
            final String currentDateandTime = sdf.format(new Date());
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference temp = database.getReference("OrderNumber");
            Log.v("PlaceOrder","-------------"  + "Yes Its Done");
            temp.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    if (currentPaymentMethod.equals("1") && amount > Integer.parseInt(availableBalance)) {
                        Toast.makeText(getApplicationContext(), "Not Enough Balance pls change payment Method", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else {
                        Log.v("PlaceOrder", "-------------" + "Yes Its Done");
                        String orderNo1 = dataSnapshot.getValue(String.class);
                        Log.v("PlaceOrder", "-------------" + orderNo1 + "Yes Its Done");
                        OrderNew = new Order(orderNo1, customerId, CanteenNumber, CanteenName, OrderString, currentDateandTime.toString(), CookingInstruction.getText().toString(), currentPaymentMethod);
                        final FirebaseDatabase database1 = FirebaseDatabase.getInstance();
                        final DatabaseReference table_order = database1.getReference("CurrentOrder");
                        table_order.child(OrderNew.getOrderNo()).setValue(OrderNew);
                        final FirebaseDatabase db9 = FirebaseDatabase.getInstance();
                        final DatabaseReference database9 = db9.getReference("OrderNumber");
                        database9.child("OrdNo").setValue(Integer.toString(Integer.parseInt(OrderNew.getOrderNo()) + 1));
                        if (currentPaymentMethod.equals("1")){
                            final FirebaseDatabase dab = FirebaseDatabase.getInstance();
                            final DatabaseReference tab = dab.getReference("User/" + customerId);
                            tab.child("virtual_Money").setValue(Integer.toString(Integer.parseInt(availableBalance)-amount));
                            final DatabaseReference lab = dab.getReference("Canteen/" + CanteenNumber);
                            lab.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                                    String canteenAmount = dataSnapshot1.child("Virtual_Money").getValue(String.class);
                                    lab.removeEventListener(this);
                                    final DatabaseReference fab = dab.getReference("Canteen/" + CanteenNumber);
                                    fab.child("Virtual_Money").setValue(Integer.toString(Integer.parseInt(canteenAmount) - amount));
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                        temp.removeEventListener(this);
                        db4.removeEventListener(newlistner);
                        final Intent intentnew = new Intent(PlaceOrder.this, Thanks.class);
                        intentnew.putExtra("OrderNo", OrderNew.getOrderNo());
                        startActivity(intentnew);
                        finish();
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
