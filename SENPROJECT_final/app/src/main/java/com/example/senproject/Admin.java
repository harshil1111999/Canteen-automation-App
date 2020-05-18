package com.example.senproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.zip.Inflater;

public class Admin extends AppCompatActivity {

    private Button submit, add, logout;
    private EditText id, addbalance;
    private TextView customerName, customerBalance, changeName;
    private LinearLayout layout1, layout2, layout3;
    private String balance;
    private int flag=0;
    private String name;
    private ValueEventListener listnerUser, listnerCanteen;
    private DatabaseReference table_User, table_Canteen;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        submit = (Button) findViewById(R.id.adminSubmit);
        add = (Button) findViewById(R.id.adminAdd);
//        logout = (Button) findViewById(R.id.adminLogout);
        id = (EditText) findViewById(R.id.adminCustomerId);
        addbalance = (EditText) findViewById(R.id.adminAddBalance);
        customerName = (TextView) findViewById(R.id.adminCustomerName);
        customerBalance = (TextView) findViewById(R.id.adminCustomerBalance);
        changeName = (TextView) findViewById(R.id.adminAddBalanceName);
        layout1 = (LinearLayout) findViewById(R.id.layout1);
        layout2 = (LinearLayout) findViewById(R.id.layout2);
        layout3 = (LinearLayout) findViewById(R.id.layout3);
        firebaseAuth = FirebaseAuth.getInstance();

        layout1.setEnabled(false);
        layout1.setVisibility(View.GONE);
        layout2.setEnabled(false);
        layout2.setVisibility(View.GONE);
        layout3.setEnabled(false);
        layout3.setVisibility(View.GONE);
        add.setEnabled(false);
        add.setVisibility(View.GONE);

//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                table_User.removeEventListener(listnerUser);
//                table_Canteen.removeEventListener(listnerCanteen);
//                Admin.this.finish();
//                startActivity(new Intent(Admin.this, MainActivity.class));
//            }
//        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FirebaseDatabase db = FirebaseDatabase.getInstance();
                table_User = db.getReference("User");
                table_Canteen = db.getReference("Canteen");
                listnerUser = table_User.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.child(id.getText().toString().trim()).getValue(User.class);
                        if (user!=null){
                            flag = 1;
                            balance = user.getVirtual_Money();
                            layout1.setEnabled(true);
                            layout1.setVisibility(View.VISIBLE);
                            layout2.setEnabled(true);
                            layout2.setVisibility(View.VISIBLE);
                            layout3.setEnabled(true);
                            layout3.setVisibility(View.VISIBLE);
                            add.setEnabled(true);
                            add.setVisibility(View.VISIBLE);
                            customerName.setText(user.getName());
                            customerBalance.setText(balance);
                            add.setText("Add");
                            changeName.setText("Add Balance: ");
                            name = id.getText().toString().trim();
                        }
                        else {
                            listnerCanteen = table_Canteen.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Canteen canteen = dataSnapshot.child(id.getText().toString().trim()).getValue(Canteen.class);
                                    if (canteen!=null){
                                        flag = 2;
                                        balance = canteen.getVirtual_Money();
                                        layout1.setEnabled(true);
                                        layout1.setVisibility(View.VISIBLE);
                                        layout2.setEnabled(true);
                                        layout2.setVisibility(View.VISIBLE);
                                        layout3.setEnabled(true);
                                        layout3.setVisibility(View.VISIBLE);
                                        add.setEnabled(true);
                                        add.setVisibility(View.VISIBLE);
                                        changeName.setText("Give Money: ");
                                        add.setText("Give");
                                        customerName.setText(canteen.getName());
                                        name = id.getText().toString().trim();
                                        customerBalance.setText(balance);
                                    }
                                    else {
                                        flag = 0;
                                        layout1 = (LinearLayout) findViewById(R.id.layout1);
                                        layout2 = (LinearLayout) findViewById(R.id.layout2);
                                        layout3 = (LinearLayout) findViewById(R.id.layout3);
                                        layout1.setEnabled(false);
                                        layout1.setVisibility(View.GONE);
                                        layout2.setEnabled(false);
                                        layout2.setVisibility(View.GONE);
                                        layout3.setEnabled(false);
                                        layout3.setVisibility(View.GONE);
                                        add.setEnabled(false);
                                        add.setVisibility(View.GONE);
                                        Toast.makeText(getApplicationContext(),"Invalid ID", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                        //table_User.removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == 1) {
                    int addNewBalance = Integer.parseInt(addbalance.getText().toString().trim());
                    final FirebaseDatabase db = FirebaseDatabase.getInstance();
                    final DatabaseReference table_temp = db.getReference("User/" + name);
                    table_temp.child("virtual_Money").setValue(Integer.toString(addNewBalance + Integer.parseInt(balance)));
                }
                else if (flag == 2){
                    int addNewBalance = Integer.parseInt(addbalance.getText().toString().trim());
                    final FirebaseDatabase db = FirebaseDatabase.getInstance();
                    final DatabaseReference table_temp = db.getReference("Canteen/" + name);
                    table_temp.child("Virtual_Money").setValue(Integer.toString(addNewBalance + Integer.parseInt(balance)));
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.admin_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.logout:
                firebaseAuth.signOut();
                if(listnerUser!=null)
                    table_User.removeEventListener(listnerUser);
                if(listnerCanteen!=null)
                    table_Canteen.removeEventListener(listnerCanteen);
                Admin.this.finish();
                startActivity(new Intent(Admin.this, MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
