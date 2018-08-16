package com.example.zyed.fitnessapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ajts.androidmads.library.SQLiteToExcel;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.orm.SugarContext;
import com.orm.SugarDb;
import com.orm.SugarRecord;

import java.io.File;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {


    String ch, memail , mid="error" , seperation = "/", path;
    String[] output;
    Button b_create , b_reset , b_scan , b_allmembers;
    Member member_checked_in;
    ArrayList<String> tables ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SugarContext.init(this);


        b_create = findViewById(R.id.main_btn_create);
        b_reset = findViewById(R.id.reset);
        b_scan = findViewById(R.id.main_btn_scan);
        b_allmembers = findViewById(R.id.main_btn_allmembers);
        Button b_export = findViewById(R.id.btn_export);
        Button b_import = findViewById(R.id.btn_import);

        tables = new ArrayList<String>();

        tables.add("Member");
        tables.add("Goals");






        //return memberArrayList;


        b_export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/The Coach" + "/database";
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                SQLiteToExcel sqliteToExcel = new SQLiteToExcel(getApplicationContext(), "coach.db",path);
                sqliteToExcel.exportSpecificTables(tables,"coachDB.xls", new SQLiteToExcel.ExportListener() {
                    @Override
                    public void onStart() {
                        
                    }

                    @Override
                    public void onCompleted(String filePath) {

                        Toast.makeText(MainActivity.this,"File exported to "+path, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Exception e) {

                        Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        b_import.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(MainActivity.this, SugarDb.class.getName(), Toast.LENGTH_SHORT).show();
            }
        });



       b_create.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               Intent toGender = new Intent(MainActivity.this,Gender.class);
               startActivity(toGender);
           }
       });


       b_scan.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
               integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
               integrator.setPrompt("Scan");
               integrator.setCameraId(0);
               integrator.setBeepEnabled(false);
               integrator.setBarcodeImageEnabled(false);
               integrator.initiateScan();

           }
       });


       b_reset.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               List<Member> members = Member.listAll(Member.class);


               Member.deleteAll(Member.class);
               Goals.deleteAll(Goals.class);

               Toast.makeText(MainActivity.this, "Database deleted", Toast.LENGTH_SHORT).show();
           }
       });

       b_allmembers.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Toast.makeText(MainActivity.this, "Processing ...", Toast.LENGTH_SHORT).show();
               Intent toAllMembers = new Intent(MainActivity.this,AllMembers.class);
               startActivity(toAllMembers);


           }
       });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) { // DID NOT scan a QR or a bar code

                ch= result.getContents();
               // throw exception concerning time

            }else if(repetition(result.getContents(),seperation.charAt(0)) != 7){
                //********* QR code does not match with member structure***************

                ch= result.getContents();
                Intent toMain = new Intent(MainActivity.this,MainActivity.class);
                startActivity(toMain);
                Toast.makeText(this, "some member's info are missing!!", Toast.LENGTH_SHORT).show();
            }else{
                //*********** QR code SCANNED succefully ***************

                ch= result.getContents();
                output = ch.split("/");
                memail = output[3];

                List<Member> members = Member.listAll(Member.class);
                for(Member member : members){
                   if (member.getEmail().toString().equals(memail)){
                        mid = member.getId().toString();
                    }
                }

                if(mid.equals("error")){ // Email does not match
                    Intent toMain = new Intent(MainActivity.this,MainActivity.class);
                    startActivity(toMain);
                    Toast.makeText(this, "member not found", Toast.LENGTH_SHORT).show();
                }
                else {

                    Intent toProfil = new Intent(MainActivity.this, Profile.class);
                    Bundle extras2 = new Bundle();
                    extras2.putString("idFromMain", mid);
                    toProfil.putExtras(extras2);
                    startActivity(toProfil);
                }
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    int repetition (String ch , char c){

            int res=0; ;
            for(int i=0;i<ch.length();i++){
                if (ch.charAt(i) == c){
                   res++;
                }
            }
            return res;
    }


    //need to add camera permission

}
