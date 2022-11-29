package com.guitartuner.tuningfeedback;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import android.os.Bundle;


public class MainActivity extends AppCompatActivity {


    ToggleButton beginTuning;
    Button tester;
    ImageView guitarDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        beginTuning = findViewById(R.id.toggleButton);
        tester = findViewById(R.id.test_button);


        guitarDisplay = findViewById(R.id.guitarImage);


        guitarDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            openActivity2();
            }
        });

        tester.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               openActivity2();
            }
        });


//
//        beginTuning.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                if(mic.isChecked()) {
//                    mic_status.setText("Mic on");
//                }
//                else {
//                    mic_status.setText("Mic off ");
//                }
//            }
//
//        });


//        beginTuning.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    // The toggle is enabled'
//                 //   beginTuning.setBackgroundColor(beginTuning.getContext().getResources().getColor(R.color.black));
//
//
//
//
//                } else {
//
//                    // The toggle is disabled
//                }
//            }
      //  });




    }
    public void openActivity2(){
        Intent intent = new Intent(this, Activity2.class);
        startActivity(intent);
    }
}