package com.example.senproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ContactFeedback extends AppCompatActivity {

    private String CanteenName,CanteenNumber,OrderString;
    private EditText message;
    private TextView contactInfo;
    private Button back, submit;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_feedback);
        Intent intent = getIntent();
        CanteenName = intent.getStringExtra("CanteenName");
        CanteenNumber = intent.getStringExtra("CanteenNumber");
        OrderString = intent.getStringExtra("OrderString");

        message = (EditText) findViewById(R.id.feedback);
        contactInfo = (TextView) findViewById(R.id.ContactInfo);
//        back = (Button) findViewById(R.id.ContactInfoBack);
        submit = (Button) findViewById(R.id.feedbackSubmit);
        progressDialog = new ProgressDialog(this);

        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference table_canteeninfo = db.getReference("CanteenInfo");
        table_canteeninfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String contactNumber = dataSnapshot.child("Canteen" + CanteenNumber).getValue(String.class);
                contactInfo.setText("Canteen: " + CanteenName + "\nContact: " + contactNumber);
                table_canteeninfo.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent1 = new Intent(ContactFeedback.this, ResturantMenu.class);
//                intent1.putExtra("OrderString", OrderString);
//                intent1.putExtra("CanteenNumber", CanteenNumber);
//                intent1.putExtra("CanteenName",CanteenName);
//                finish();
//                startActivity(intent1);
//            }
//        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (message.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Enter Feedback", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    progressDialog.setMessage("Sending Feedback...");
                    progressDialog.show();
                    final FirebaseDatabase db2 = FirebaseDatabase.getInstance();
                    final DatabaseReference table_feedbackNumber = db2.getReference("FeedbackNumber");
                    table_feedbackNumber.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            String feedbackNumber = dataSnapshot.getValue(String.class);
                            Feedback feedback = new Feedback(CanteenNumber, message.getText().toString());
                            final DatabaseReference table_feedback = db2.getReference("Feedback");
                            table_feedback.child(feedbackNumber).setValue(feedback);
                            table_feedbackNumber.removeEventListener(this);
                            final DatabaseReference table_feedbackNumber1 = db2.getReference("FeedbackNumber");
                            table_feedbackNumber1.child("Number").setValue(Integer.toString(Integer.parseInt(feedbackNumber) + 1));
                            progressDialog.dismiss();
                            message.setText("");
                            Toast.makeText(getApplicationContext(), "Feedback Submited", Toast.LENGTH_SHORT).show();
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
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent1 = new Intent(ContactFeedback.this, ResturantMenu.class);
        intent1.putExtra("OrderString", OrderString);
        intent1.putExtra("CanteenNumber", CanteenNumber);
        intent1.putExtra("CanteenName",CanteenName);
        finish();
        startActivity(intent1);

    }
}
