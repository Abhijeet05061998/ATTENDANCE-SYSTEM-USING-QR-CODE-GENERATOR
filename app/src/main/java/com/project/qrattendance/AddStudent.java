package com.project.qrattendance;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddStudent extends AppCompatActivity {
    EditText Sname;
    EditText Sid,spassword;
    String sname,sid,classname,spass;
    Spinner classes;
    DatabaseReference databaseStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        databaseStudent = FirebaseDatabase.getInstance().getReference("Student");

        Sname =  (EditText) findViewById(R.id.editText1);
        Sid =  (EditText) findViewById(R.id.editText3);
        classes = (Spinner) findViewById(R.id.spinner3);
        spassword = (EditText) findViewById(R.id.editText4);
        getSupportActionBar().setTitle("Add/Remove Student");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void addStudent(View v){


        if (!(TextUtils.isEmpty(Sid.getText().toString()))) {
            //String id = databaseStudent.push().getKey();
            sname = Sname.getText().toString();
            sid = Sid.getText().toString();
            classname = classes.getSelectedItem().toString();
            spass = spassword.getText().toString();

            Nstudent student =new Nstudent(sname ,sid,classname,spass );
            databaseStudent.child(sid).setValue(student);
            Toast.makeText(getApplicationContext(),"student added successfully", Toast.LENGTH_LONG).show();

        }else {
            Toast.makeText(getApplicationContext(),"fields cannot be empty", Toast.LENGTH_LONG).show();
        }
    }
    public void removeStudent(View v){
        if (!TextUtils.isEmpty(Sid.getText().toString())) {
            sid = Sid.getText().toString();
            databaseStudent.child(sid).setValue(null);
            Toast.makeText(getApplicationContext(),"teacher removed successfully", Toast.LENGTH_LONG).show();

        }else {
            Toast.makeText(getApplicationContext(),"id cannot be empty", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}

