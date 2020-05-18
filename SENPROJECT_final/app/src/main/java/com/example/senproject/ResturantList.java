package com.example.senproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ResturantList extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private TextView textiewUserEmail, currentBalance;
    private Button buttonLogout, OrderSatus, OrderHistory;
    private TextView[] namesOfResturant = new TextView[7];
    public String[] ResturantNames = new String[7];
    public int Number ;
    private String userId;

    private ListView listView;
    private ArrayList<String> mResturantName, canteenAvailable;
    private ArrayAdapter<String> arrayAdapter;
    private DatabaseReference table_user;
    private ValueEventListener listner1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resturant_list);
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this,MainActivity.class));
        }
        FirebaseUser user = firebaseAuth.getCurrentUser();
        textiewUserEmail = (TextView) findViewById(R.id.textViewUserEmail);
        userId = user.getEmail().substring(0,9);
        //textiewUserEmail.setText("Welcome " + user.getEmail());
//        buttonLogout = (Button) findViewById(R.id.buttonLogout);
//        OrderSatus = (Button) findViewById(R.id.buttonCurrentOrderStatus);
        listView = (ListView) findViewById(R.id.ResutrantListView);
//        OrderHistory = (Button) findViewById(R.id.CustomerOrderHistory);
        currentBalance = (TextView) findViewById(R.id.resturantListCurrentBalance);

        mResturantName= new ArrayList<>();
        canteenAvailable = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.iteminfo,R.id.dishnameid,mResturantName);
        listView.setAdapter(arrayAdapter);
//        OrderSatus.setOnClickListener(this);
//        buttonLogout.setOnClickListener(this);
//        OrderHistory.setOnClickListener(this);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        getResturantList();

        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        table_user = db.getReference("User");
        listner1 = table_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.child(userId).getValue(User.class);
                textiewUserEmail.setText("Welcome: " + user.getName());
                currentBalance.setText("₹" + user.getVirtual_Money());
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (canteenAvailable.get(position).equals("0")){
                    Toast.makeText(getApplicationContext(),"Canteen Currently Unavailable", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    table_user.removeEventListener(listner1);
                    Intent intent1 = new Intent(getApplicationContext(), ResturantMenu.class);
                    intent1.putExtra("CanteenNumber", Integer.toString(position + 1));
                    intent1.putExtra("CanteenName", mResturantName.get(position).substring(0, mResturantName.get(position).length() - 10));
                    intent1.putExtra("OrderString", "");
                    finish();
                    startActivity(intent1);
                }
            }
        });


    }

    private void getResturantList(){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_canteen = database.getReference("Canteen");
        table_canteen.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    Canteen item = ds.getValue(Canteen.class);
                    if (item.getAvailable().equals("1")) {
                        mResturantName.add(item.getName() + "\nAvailable");
                    }
                    else{
                        mResturantName.add(item.getName() + "\nUnavailable");
                    }
                    canteenAvailable.add(item.getAvailable());
                }
                listView.setAdapter(arrayAdapter);
                table_canteen.removeEventListener(this);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.resturant_list_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.buttonCurrentOrderStatus:
                table_user.removeEventListener(listner1);
                Intent intent1 = new Intent(this, OrderStatus.class);
                intent1.putExtra("OperationType", "OrderStatus");
                finish();
                startActivity(intent1);
                return true;
            case R.id.CustomerOrderHistory:
                table_user.removeEventListener(listner1);
                Intent intent = new Intent(this, OrderStatus.class);
                intent.putExtra("OperationType", "OrderHistory");
                finish();
                startActivity(intent);
                return true;
            case R.id.buttonLogout:
                table_user.removeEventListener(listner1);
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(this, MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    @Override
//        public void onClick(View v) {
//            if(v == buttonLogout){
//                table_user.removeEventListener(listner1);
//                firebaseAuth.signOut();
//                finish();
//                startActivity(new Intent(this,MainActivity.class));
//            }
//            if (v == OrderSatus){
//                table_user.removeEventListener(listner1);
//                Intent intent = new Intent(this,OrderStatus.class);
//                intent.putExtra("OperationType","OrderStatus");
////            finish();
//                startActivity(intent);
//            }
//            if (v == OrderHistory){
//                table_user.removeEventListener(listner1);
//                Intent intent = new Intent(this,OrderStatus.class);
//                intent.putExtra("OperationType","OrderHistory");
////            finish();
//                startActivity(intent);
//            }
//    }
}
