package com.example.zyed.fitnessapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Creation extends AppCompatActivity {

    String gender , name , age , mail , phone;
    EditText ed_name , ed_age , ed_mail , ed_phone;
    Button b_next;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation);

        //get data from the previous activity
        Bundle extra = getIntent().getExtras();
        gender = extra.getString("gender");


        ed_name = findViewById(R.id.creation_ed_name);
        ed_age = findViewById(R.id.creation_ed_age);
        ed_mail = findViewById(R.id.creation_ed_mail);
        ed_phone = findViewById(R.id.creation_ed_phone);
        b_next = findViewById(R.id.creation_btn_next);


        b_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = ed_name.getText().toString();
                age = ed_age.getText().toString();
                mail = ed_mail.getText().toString();
                phone = ed_phone.getText().toString();

                if(ed_name.length()<2){

                    ed_name.setError("please enter a valid name");
                    ed_name.requestFocus();
                }

               else if (Empty(age) || Integer.parseInt(age)<15 || Integer.parseInt(age)>100){

                   ed_age.setError("please enter a valid age");
                   ed_age.requestFocus();
               }

                else if(!isEmailValid(ed_mail.getText().toString())) {

                    ed_mail.setError("please enter a valid e-mail address");
                    ed_mail.requestFocus();
                }
                else if(Empty(phone)){

                   ed_phone.setError("Please enter a phone number");
                   ed_phone.requestFocus();
                }

                else {

                    Intent toCreation2 = new Intent(Creation.this, Creation2.class);

                    toCreation2.putExtra("gender", gender);
                    toCreation2.putExtra("name", name);
                    toCreation2.putExtra("age", age);
                    toCreation2.putExtra("mail", mail);
                    toCreation2.putExtra("phone", phone);

                    startActivity(toCreation2);

                }
            }
        });
    }

    boolean Empty(String number){
        return TextUtils.isEmpty(number.trim());
    }

    boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}
