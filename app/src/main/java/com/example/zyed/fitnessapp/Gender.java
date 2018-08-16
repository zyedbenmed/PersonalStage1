package com.example.zyed.fitnessapp;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import static android.support.design.widget.Snackbar.LENGTH_SHORT;


public class Gender extends AppCompatActivity {


    String ch="";
    Button b_male , b_female;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gender);


        b_male = findViewById(R.id.gender_btn_male);
        b_female = findViewById(R.id.gender_btn_female);


        b_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ch="male";
                Intent toCreation = new Intent(Gender.this,Creation.class);
                toCreation.putExtra("gender",ch);
                startActivity(toCreation);

            }
        });

        b_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ch="female";
                Intent toCreation = new Intent(Gender.this,Creation.class);
                toCreation.putExtra("gender",ch);
                startActivity(toCreation);

            }
        });


    }

}
