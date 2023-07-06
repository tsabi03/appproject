package com.example.test3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Scanner;


public class MainActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST_CODE = 123;

    Button Camera,info;

    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView) findViewById(R.id.textViewResult);
        Log.d("TextRecon", "start log: ");

        info = (Button) findViewById(R.id.button5);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MainActivity.this, InfoActivity.class);
                startActivity(in);
            }
        });

        Camera = (Button) findViewById(R.id.button6);
        Camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainActivity.this, ScannerActivity.class);
                startActivity(i);
            }
        });


    }
}