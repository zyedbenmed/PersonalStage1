package com.example.zyed.fitnessapp;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.provider.FontRequest;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.support.v7.widget.AppCompatTextView;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.Locale;

import static android.support.design.widget.Snackbar.LENGTH_SHORT;


public class Gender extends AppCompatActivity {


    String ch="";
    Button b_male , b_female;
    AppCompatTextView  t1 ;
    TextView t2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gender);


        b_male = findViewById(R.id.gender_btn_male);
        b_female = findViewById(R.id.gender_btn_female);

        t1 =findViewById(R.id.textV);
        t2 = findViewById(R.id.textView);

      /*  Typeface typeface = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            typeface = getResources().getFont(R.font.font_special);
        }
        else{
            typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.font_special);
        }
        t1.setTypeface(typeface);
*/


       // Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.allura);
       // t1.setTypeface(typeface);
       // t2.setTypeface(typeface);
        //t1.setTypeface(typeface);
        //t2.setTypeface(typeface);

        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/fon.otf");



        t1.setTypeface(custom_font);
        t2.setTypeface(custom_font);


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
