package com.project.qrattendance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Student extends AppCompatActivity {
    String message;
    String scannedData;
    Button scanBtn;
    TextView result,id;
    public static final int REQUEST_CODE = 100;
    public static final int PERMISSION_REQUEST = 200;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseRef;
    String QR,PERIOD;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    DatabaseReference dbAttendance;
    String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
       // Bundle bundle = getIntent().getExtras();
        Intent i= getIntent();
        message = i.getStringExtra("name");
        id=findViewById(R.id.textView2);
        id.setText(message);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseRef= firebaseDatabase.getReference().child("CurrentQR").child("CurrentQR");
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               QR = dataSnapshot.getValue(String.class);
               //result.setText(QR);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        scanBtn = findViewById(R.id.scan_btn);
        result=findViewById(R.id.textView);

    }
    private void addItemToSheet() {
        dbAttendance= firebaseDatabase.getReference().child("Period").child("Current");
        dbAttendance.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                PERIOD = dataSnapshot.getValue(String.class);
                Toast.makeText(getApplicationContext(),PERIOD, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        if (PERIOD!=null) {
            String present = "P";
            DatabaseReference attended = ref.child("attendance").child(date).child(message).child(PERIOD);
            attended.setValue(present);
        }

//        final ProgressDialog loading = ProgressDialog.show(this,"Sending Data","Please wait");
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://script.google.com/macros/s/AKfycbzNaPbu2oNa-miuW6h6R2aWetLcITxEkkieIsppF4LXQMru5K4/exec",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//
//                        loading.dismiss();
//                        Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
//                        Intent intent = new Intent(getApplicationContext(), Student.class);
//                        startActivity(intent);
//                        finish();
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                    }
//                }
//        ) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> parmas = new HashMap<>();
//                parmas.put("sdata",scannedData);
//                return parmas;
//            }
//        };
//
//        int socketTimeOut = 50000;
//
//        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        stringRequest.setRetryPolicy(retryPolicy);
//
//        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
//
//        queue.add(stringRequest);
//

    }
    public void viewAttendance(View v){
        Bundle basket = new Bundle();
        basket.putString("sid", message);
        Intent intent = new Intent(this, StudentAttendanceSheet.class);
        intent.putExtras(basket);
        startActivity(intent);
    }
    public void log(View v){
        Intent logout=new Intent(Student.this,Login.class);
        logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(logout);
    }
    public void scan(View v){
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST);
        }

                Intent intent = new Intent(getApplicationContext(), ScanActivity.class);
                startActivityForResult(intent, REQUEST_CODE);


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            if(data != null){
                final Barcode barcode = data.getParcelableExtra("barcode");
                result.post(new Runnable() {
                    @Override
                    public void run() {
                        result.setText(barcode.displayValue);
                        scannedData=result.getText().toString();
                        if(scannedData.equals(QR)) {
                            addItemToSheet();
                        }else{
                            Toast.makeText(getApplicationContext(),"Wrong QR", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }
    }
}
