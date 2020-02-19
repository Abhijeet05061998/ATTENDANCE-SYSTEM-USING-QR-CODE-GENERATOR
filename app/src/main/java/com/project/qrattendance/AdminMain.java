package com.project.qrattendance;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AdminMain extends AppCompatActivity {
    DatabaseReference ref;
    DatabaseReference dbStudent;
    DatabaseReference dbAttendance;
    DatabaseReference dbadmin;
    DatabaseReference period;
    private static long back_pressed;
    ArrayList Studentlist = new ArrayList<>();

    String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        ref = FirebaseDatabase.getInstance().getReference();
        dbStudent = ref.child("Student");
        dbAttendance = ref.child("attendance");





    }
    public void AddTeacherButton(View v){
//        Intent intent = new Intent(this, addteacher.class);
//        startActivity(intent);
    }
    public void AddStudentButton(View v){
        Intent intent = new Intent(this, AddStudent.class);
        startActivity(intent);
    }
    public void attendanceRecord(View v){
        Intent intent = new Intent(this, AdminAttendanceSheet.class);
        startActivity(intent);
    }

    public void CreateAttendance(View v){

        period=ref.child("Period").child("Current");

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Select Current Period");
        final LayoutInflater inflater = this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.currentperiod, null);
        final Spinner spinner=add_menu_layout.findViewById(R.id.spinner2);
        alertDialog.setView(add_menu_layout);
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(final DialogInterface dialog, int which) {
                if (!TextUtils.isEmpty(spinner.getSelectedItem().toString()))
                {
                    period.setValue(spinner.getSelectedItem().toString());
                    Toast.makeText(AdminMain.this, "Successfully Created Period", Toast.LENGTH_SHORT).show();

                    dbStudent.addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String sid,P1="A",P2="A",P3="A",P4="A",P5="A",P6="A",P7="A",P8="A";
                            AttendanceSheet a = new AttendanceSheet(P1,P2,P3,P4,P5,P6,P7,P8);

                            for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                                sid=dsp.child("sid").getValue().toString();
                                dbAttendance.child(date).child(sid).setValue(a);

                            }
                            Toast.makeText(getApplicationContext(),"successfully created "+date+" db", Toast.LENGTH_LONG).show();
                            Intent nw=new Intent(AdminMain.this,Admin.class);
                            nw.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(nw);
                        }



                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(getApplicationContext(), "something wrong", Toast.LENGTH_LONG).show();
                        }

                    });



                }
                else
                {
                    Toast.makeText(AdminMain.this, "Please Select", Toast.LENGTH_SHORT).show();
                }



            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();




        //  Toast.makeText(getApplicationContext(),date, Toast.LENGTH_LONG).show();







    }


    public void logout(View view) {

        Intent logout=new Intent(AdminMain.this,Login.class);
        logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(logout);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void changepassword(View view) {
        dbadmin=ref.child("Admin");

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Set your new password");
        final LayoutInflater inflater = this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.changepassword, null);
        final EditText password=(EditText)add_menu_layout.findViewById(R.id.newpassword);
        alertDialog.setView(add_menu_layout);
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(final DialogInterface dialog, int which) {
                if (!TextUtils.isEmpty(password.getText().toString()))
                {
                    dbadmin.child("Admin").setValue(password.getText().toString());
                    Toast.makeText(AdminMain.this, "Successfully Changed", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(AdminMain.this, "Please Enter New Password", Toast.LENGTH_SHORT).show();
                }



            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();


    }
    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis()){
            super.onBackPressed();
            finish();
            ActivityCompat.finishAffinity(this);
            System.exit(0);
        }
        else {
            Toast.makeText(getBaseContext(), "Press once again to exit", Toast.LENGTH_SHORT).show();
            back_pressed = System.currentTimeMillis();
        }
    }

}

