package com.example.senproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonSignup;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView forgetPassword;
    private TextView textViewSignup;
    private TextView textViewCanteenLogin;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.CAMERA},
                1);
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(getApplicationContext(),ResturantList.class));
        }

        progressDialog = new ProgressDialog(this);
        buttonSignup = (Button) findViewById(R.id.buttonLogin);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        textViewSignup = (TextView) findViewById(R.id.textViewSignup);
        textViewCanteenLogin = (TextView) findViewById(R.id.textViewCanteenLogin);
        forgetPassword = (TextView) findViewById(R.id.forgetPassword);
        buttonSignup.setOnClickListener(this);
        textViewCanteenLogin.setOnClickListener(this);
        textViewSignup.setOnClickListener(this);
        forgetPassword.setOnClickListener(this);
    }
    private void userLogin(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Enter your email",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Enter your email",Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("Loging In User...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()){
                    finish();
                    startActivity(new Intent(getApplicationContext(),ResturantList.class));
                }
                else {
                    Toast.makeText(getApplicationContext(),"Enter Correct Credentials",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.activity_main_menu,menu);
//
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()){
//
//            case R.id.textViewSignup:
//                finish();
//                startActivity(new Intent(this, SignUp.class));
//                return true;
//
//            case R.id.textViewCanteenLogin:
//                finish();
//                startActivity(new Intent(this, CanteenLogin.class));
//                return true;
//
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    @Override
    public void onClick(View v) {
        if(v == buttonSignup){
            userLogin();
        }
        if(v == textViewSignup){
            finish();
            startActivity(new Intent(this, SignUp.class));
        }
        if(v == textViewCanteenLogin){
            finish();
            startActivity(new Intent(this, CanteenLogin.class));
        }
        if (v == forgetPassword){
            if (editTextEmail.getText().toString().trim().isEmpty()){
                Toast.makeText(getApplicationContext(),"Enter Email Id to get reset link",Toast.LENGTH_SHORT).show();
            }
            else {
                FirebaseAuth.getInstance().sendPasswordResetEmail(editTextEmail.getText().toString().trim())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(),"Reset Email sent",Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(getApplicationContext(),"Enter Correct Email ID",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        }
    }
}
