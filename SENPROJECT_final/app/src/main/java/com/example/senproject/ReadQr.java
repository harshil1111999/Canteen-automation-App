package com.example.senproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ReadQr extends Activity implements ZXingScannerView.ResultHandler {

    private String CanteenName, CanteenNumber, OperationType,CanteenAvailable, orderBasics, orderDetails, cookingInstruction, paymentMethod;
    private int flag=0;

    private ZXingScannerView mScannerView;
    String TAG="QRREADER";

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);                // Set the scanner view as the content view
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        Log.v(TAG, rawResult.getText()); // Prints scan results
        Log.v(TAG, rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)

        // call the alert dialog
        Alert(rawResult);

    }


    public void Alert(Result rawResult){
        flag=0;
        final String orderNumber = rawResult.getText().toString().trim();
        for (int i=0; i<orderNumber.length(); i++){
            if (orderNumber.charAt(i)<='0' || orderNumber.charAt(i)>='9'){
                Toast.makeText(getApplicationContext(),"Invalid QR", Toast.LENGTH_SHORT).show();
                mScannerView.resumeCameraPreview(ReadQr.this);
                return;
            }
        }
        Intent intent1 = getIntent();
        CanteenName = intent1.getStringExtra("CanteenName");
        CanteenNumber = intent1.getStringExtra("CanteenNumber");
        OperationType = intent1.getStringExtra("OperationType");
        CanteenAvailable = intent1.getStringExtra("CanteenAvailable");
        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference table_order = db.getReference("CurrentOrder");
        table_order.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Order order = dataSnapshot.child(orderNumber).getValue(Order.class);
                Log.v("ReadQr","----------------------------" + orderNumber);
                if (order == null){
                    flag=1;
                    Toast.makeText(getApplicationContext(),"Invalid QR", Toast.LENGTH_SHORT).show();
                    mScannerView.resumeCameraPreview(ReadQr.this);
                }
                else if (!order.getCanteenId().equals(CanteenNumber)){
                    flag=1;
                    Toast.makeText(getApplicationContext(),"QR of other Canteen", Toast.LENGTH_SHORT).show();
                    mScannerView.resumeCameraPreview(ReadQr.this);
                }
                else {
                    orderBasics = "Order No: " + order.getOrderNo() + "\nCustomerId: " + order.getCustomerId();
                    orderDetails = order.getOrderDetails();
                    cookingInstruction = order.getCookingInstruction();
                    paymentMethod = order.getPaymetMethod();
                    Intent intent = new Intent(ReadQr.this,CanteenOrderDeliver.class);
                    intent.putExtra("CanteenName",CanteenName);
                    intent.putExtra("CanteenNumber",CanteenNumber);
                    intent.putExtra("OrderBasic", orderBasics);
                    intent.putExtra("OrderDetails",orderDetails);
                    intent.putExtra("OrderNumber",orderNumber);
                    intent.putExtra("OperationType",OperationType);
                    intent.putExtra("CookingInstruction",cookingInstruction);
                    intent.putExtra("CanteenAvailable", CanteenAvailable);
                    intent.putExtra("PaymentMethod",paymentMethod);
                    table_order.removeEventListener(this);
                    finish();
                    startActivity(intent);

                }
                table_order.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent1 = getIntent();
        CanteenName = intent1.getStringExtra("CanteenName");
        CanteenNumber = intent1.getStringExtra("CanteenNumber");
        OperationType = intent1.getStringExtra("OperationType");
        CanteenAvailable = intent1.getStringExtra("CanteenAvailable");
        Intent intent = new Intent(ReadQr.this,CanteenManager.class);
        intent.putExtra("OperationType",OperationType);
        intent.putExtra("CanteenNumber", CanteenNumber);
        intent.putExtra("CanteenName",CanteenName);
        intent.putExtra("CanteenAvailable", CanteenAvailable);
        finish();
        startActivity(intent);
    }
}
