package com.project.qrattendance;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StudentAttendanceSheet extends AppCompatActivity {
    public static int count=0,P=0,A=0;
    float average= (float) 0.0;
    TextView t;
    String avg,p1,p2,p3,p4,p5,p6,p7,p8;
    String student_id;
    ArrayList dates = new ArrayList<>();;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    DatabaseReference dbAttendance;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_attendance_sheet);
        t=findViewById(R.id.textView3);

        listView =findViewById(R.id.list);
        Bundle bundle = getIntent().getExtras();
        student_id = bundle.getString("sid");
        t.setText(student_id);

        dates.clear();
        dates.add("       Date          "+"p1  "+"p2  "+"p3  "+"p4   "+ "p5   "+"p6  "+"p7  "+"p8");

        dbAttendance = ref.child("attendance");
        dbAttendance.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    p1 = dsp.child(student_id).child("p1").getValue().toString();
                    p2 = dsp.child(student_id).child("p2").getValue().toString();
                    p3 = dsp.child(student_id).child("p3").getValue().toString();
                    p4 = dsp.child(student_id).child("p4").getValue().toString();
                    p5 = dsp.child(student_id).child("p5").getValue().toString();
                    p6 = dsp.child(student_id).child("p6").getValue().toString();
                    p7 = dsp.child(student_id).child("p7").getValue().toString();
                    p8 = dsp.child(student_id).child("p8").getValue().toString();
                    dates.add(dsp.getKey() + "    " + p1 + "     " + p2 + "     " + p3 + "     " + p4 + "      " + p5 + "       " + p6 + "      " + p7 + "      " + p8); //add result into array list


                    //  Toast.makeText(getApplicationContext(),dsp.child(student_id).child("p1").getValue().toString(),Toast.LENGTH_LONG).show();



//                    
//                    if (p1.equals("P")||p2.equals("P")||p3.equals("P")||p4.equals("P")||p5.equals("P")||p6.equals("P")||p7.equals("P")||p8.equals("P")) {
//
//                        P++;
//                        count++;
//                    }
//                    if(p1.equals("A")||p2.equals("A")||p3.equals("A")||p4.equals("A")||p5.equals("A")||p6.equals("A")||p7.equals("A")||p8.equals("A")) {
//                        A++;
//                        count++;
//                    }






                }
                list(dates,P,count,A);


//                  Toast.makeText(getApplicationContext(), dates.toString(), Toast.LENGTH_LONG).show();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "something went wrong", Toast.LENGTH_LONG).show();
            }
        });

    }
    public void list(ArrayList studentlist,int P,int count,int A){
        // Toast.makeText(this,NOP+TOC,Toast.LENGTH_LONG).show();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, studentlist);
        listView.setAdapter(adapter);
        try {

            average =(float)((P*100)/count);
            avg=Float.toString(average);
            t.setText("Your Attendance is :");
            if(average>=75)
                t.setTextColor(Color.GREEN);
            if(average<75)
                t.setTextColor(Color.RED);
        }
        catch (Exception e){e.printStackTrace();}


    }

}
