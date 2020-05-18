package com.example.senproject;

import androidx.annotation.NonNull;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    private Button buttonRegister;
    private EditText editTextEmail;
    private EditText userName;
    private EditText userPhone;
    private EditText editTextPassword;
    private TextView textViewSignup;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(getApplicationContext(),ResturantList.class));
        }
        progressDialog = new ProgressDialog(this);
        buttonRegister = (Button) findViewById(R.id.buttonSignup);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        textViewSignup = (TextView) findViewById(R.id.textViewLogin);
        userName = (EditText) findViewById(R.id.editTextName);
        userPhone = (EditText) findViewById(R.id.editTextNmber);

        buttonRegister.setOnClickListener(this);
        textViewSignup.setOnClickListener(this);
    }
    private void registerUser(){
        String temp="201701170@daiict.ac.in";
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String name = userName.getText().toString().trim();
        String number = userPhone.getText().toString().trim();
        if (email.length()!=temp.length()) {
            Toast.makeText(SignUp.this,"Enter Valid1 Email Address", Toast.LENGTH_SHORT).show();
            return;
        }
        for(int i=0;i<9;i++){
            if (email.charAt(i)>='0' && email.charAt(i)<='9')
                continue;
            else{
                Toast.makeText(SignUp.this,"Enter Valid2 Email Address", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if(!email.substring(9,email.length()).equals(temp.substring(9,temp.length()))){
            Toast.makeText(SignUp.this,"Enter Valid3 Email Address" + email.substring(9,email.length()), Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Enter your email",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Enter your email",Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("Registrating User...");
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()){
                    Toast.makeText(SignUp.this,"Registered Sucessfully", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(getApplicationContext(),ResturantList.class));
                }
                else{
                    Toast.makeText(SignUp.this,"Could not register! Please try again", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");
        // Create a new user with a first and last name
        User user = new User(name, number, "0");
        table_user.child(email.substring(0,9)).setValue(user);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.signup_menu,menu);
//
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.textViewLogin:
//                finish();
//                startActivity(new Intent(this, MainActivity.class));
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    @Override
    public void onClick(View v) {
        if(v == buttonRegister){
            registerUser();
        }
        if(v == textViewSignup){
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}
