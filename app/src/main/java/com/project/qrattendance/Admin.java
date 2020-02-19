package com.project.qrattendance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Random;

import static com.project.qrattendance.Student.PERMISSION_REQUEST;

public class Admin extends AppCompatActivity {
        ImageView imageView;
        TextView textView;
        Button btn;
    DatabaseReference ref;
    DatabaseReference CurrentQR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        ref = FirebaseDatabase.getInstance().getReference();
        CurrentQR= ref.child("CurrentQR");

        char[] chars1 = "ABCDEF012GHIJKL345MNOPQR678STUVWXYZ9".toCharArray();
        StringBuilder sb1 = new StringBuilder();
        Random random1 = new Random();
        for (int i = 0; i < 6; i++)
        {
            char c1 = chars1[random1.nextInt(chars1.length)];
            sb1.append(c1);
        }
        final String random_string = sb1.toString();
        CurrentQR.child("CurrentQR").setValue(random_string);
    btn=findViewById(R.id.btn);
        imageView=findViewById(R.id.imgv1);
        textView=findViewById(R.id.tv1);
        textView.setText(random_string);
        new ImageDownloaderTask(imageView).execute(" https://api.qrserver.com/v1/create-qr-code/?size=1000x1000&data=" + random_string);
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST);
        }
btn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
         BitmapDrawable bitmapDrawable;
         Bitmap bitmap1;
//write this code in your share button or function


        bitmapDrawable = (BitmapDrawable) imageView.getDrawable();// get the from imageview or use your drawable from drawable folder
        bitmap1 = bitmapDrawable.getBitmap();
        String imgBitmapPath= MediaStore.Images.Media.insertImage(getContentResolver(),bitmap1,"title",null);
        Uri imgBitmapUri=Uri.parse(imgBitmapPath);
        String shareText="Scan the QR to mark your attendance";
        Intent shareIntent=new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM,imgBitmapUri);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        startActivity(Intent.createChooser(shareIntent,"Share QR using"));
    }
});
                }
}