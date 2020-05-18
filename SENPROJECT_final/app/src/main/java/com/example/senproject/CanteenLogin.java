package com.example.senproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class CanteenLogin extends AppCompatActivity implements View.OnClickListener{

    private TextView Email;
    private TextView Password;
    private TextView CustomerLogin;
    private Button Login;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canteen_login);
        Email = (TextView) findViewById(R.id.editTextEmailCanteen);
        Password = (TextView) findViewById(R.id.editTextPasswordCanteen);
        CustomerLogin = (TextView) findViewById(R.id.textViewCustomerLogin);
        Login = (Button) findViewById(R.id.buttonLoginCanteen);
        progressDialog = new ProgressDialog(this);
        CustomerLogin.setOnClickListener(this);
        Login.setOnClickListener(this);

    }

    private void canteenLogin(){
        final String email = Email.getText().toString().trim();
        final String password = Password.getText().toString().trim();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_canteen = database.getReference("Canteen");
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Enter your email",Toast.LENGTH_SHORT).show();
            return;
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Enter your Password",Toast.LENGTH_SHORT).show();
            return;
        }
        else if (email.equals("Admin")){
            progressDialog.setMessage("Loging In User...");
            progressDialog.show();
            final FirebaseDatabase db = FirebaseDatabase.getInstance();
            final  DatabaseReference table_admin = db.getReference("Admin");
            table_admin.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    String passw = dataSnapshot.getValue(String.class);
                    //Toast.makeText(getApplicationContext(),"-------------Password is: " + password, Toast.LENGTH_SHORT).show();
                    if (passw.equals(password)){
                        CanteenLogin.this.finish();
                        startActivity(new Intent(CanteenLogin.this,Admin.class));
                    }
                    progressDialog.dismiss();
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
        else {
            progressDialog.setMessage("Loging In User...");
            progressDialog.show();
            table_canteen.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int flag = 0, i = 0;
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Canteen canteen = ds.getValue(Canteen.class);
                        if (canteen.getEmail().equals(email)) {
                            flag = 1;
                            if (canteen.getPassword().equals(password)) {
                                progressDialog.dismiss();
                                Intent intent = new Intent(CanteenLogin.this, CanteenManager.class);
                                intent.putExtra("CanteenNumber", Integer.toString(i + 1));
                                intent.putExtra("CanteenName", canteen.getName());
                                intent.putExtra("CanteenAvailable", canteen.getAvailable());
                                CanteenLogin.this.finish();
                                startActivity(intent);
                            }
                        }
                        i++;
                    }
                    table_canteen.removeEventListener(this);
                    progressDialog.dismiss();
                    if (flag == 0) {
                        Toast.makeText(getApplicationContext(), "Invalid EmailId/Password", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.canteen_login_menu,menu);
//
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.textViewCustomerLogin:
//                finish();
//                startActivity(new Intent(this, MainActivity.class));
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    @Override
    public void onClick(View v) {
        if(v == Login){
            canteenLogin();
        }
        if(v == CustomerLogin){
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

    }
}
