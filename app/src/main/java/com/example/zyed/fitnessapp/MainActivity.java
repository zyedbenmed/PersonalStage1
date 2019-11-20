package com.example.zyed.fitnessapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ajts.androidmads.library.ExcelToSQLite;
import com.ajts.androidmads.library.SQLiteToExcel;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.orm.SugarContext;
import com.orm.SugarDb;
import com.orm.SugarRecord;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class MainActivity extends AppCompatActivity {


    String ch, memail , mid="error" , seperation = "/", path;
    String[] output;
    Button b_create , b_reset , b_scan , b_allmembers;
    Member member_checked_in;
    ArrayList<String> tables ;
    static byte[] imagev;

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


       /* b_export.setOnClickListener(new View.OnClickListener() {
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
        });*/

        b_import.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                readExcelMemberSheet(getApplicationContext());
                readExcelGoalSheet(getApplicationContext());

              /*  path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/The Coach" + "/database/coachDB.xls";

                File file = new File(path);
                if (!file.exists()) {
                    Toast.makeText(MainActivity.this, "No such file in the specified folder", Toast.LENGTH_SHORT).show();
                    return;
                }

                SugarContext.init(getApplicationContext());
                ExcelToSQLite excelToSQLite = new ExcelToSQLite(getApplicationContext(), "coach.db", false);
                
                excelToSQLite.importFromFile(path, new ExcelToSQLite.ImportListener() {
                    @Override
                    public void onStart() {
                        
                    }

                    @Override
                    public void onCompleted(String dbName) {

                        Toast.makeText(MainActivity.this, "completed", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Exception e) {

                        Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });


                Toast.makeText(MainActivity.this, SugarDb.class.getName(), Toast.LENGTH_SHORT).show();*/
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


       b_reset.setVisibility(View.INVISIBLE);
       b_reset.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {



               List<Goals> membe = Goals.listAll(Goals.class);
               List<Member> members2 = Member.listAll(Member.class);

               Member.deleteAll(Member.class);
               Goals.deleteAll(Goals.class);

               Toast.makeText(MainActivity.this, "Database deleted successfully", Toast.LENGTH_SHORT).show();


           }
       });

       b_allmembers.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
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

    private void readExcelMemberSheet(Context context) {



        if (!isExternalStorageAvailable() || isExternalStorageReadOnly())
        {
            Toast.makeText(context, "Storage not available or read only", Toast.LENGTH_SHORT).show();
            return;
        }

        //Environment.getExternalStorageDirectory().getAbsolutePath() + "/The Coach" + "/database/coachDB.xls";

        try{
            int test = 1;
            boolean skiprow = true;
            // Creating Input Stream
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/The Coach" + "/database/coachDB.xls");
            //File file = new File(context.getExternalFilesDir(null), Environment.getExternalStorageDirectory().getAbsolutePath() + "/The Coach" + "/database/coachDB.xls");
            FileInputStream myInput = new FileInputStream(file);

            // Create a POIFSFileSystem object
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);

            // Create a workbook using the File System
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);

            // Get the first sheet from workbook ==> 0:Member   1: Goals
            HSSFSheet mySheet = myWorkBook.getSheetAt(0);

            /** We now need something to iterate through the cells.**/
            Iterator rowIter = mySheet.rowIterator();

            while(rowIter.hasNext()){

                String id ="0";
                int count =1 ;
                Member member = null;
                Member newMember = new Member();
                boolean testforExistence = false;

                HSSFRow myRow = (HSSFRow) rowIter.next();
                Iterator cellIter = myRow.cellIterator();
                if (test > 1){ skiprow = false;}
                while(cellIter.hasNext()) {
                    HSSFCell myCell = (HSSFCell) cellIter.next();
                    if (test == 1) {test ++;}

                    if (!skiprow) { // first row contains the name of each columns no need to scan it

                        if (count == 1) { //count == 1 means the ID cell
                            id = myCell.toString();
                            if (MemberExistence(id)) {
                                //if member exist already in database we need to look for uncompilable data and update it
                                // else (member does not exist) we need to create new member with the same unique ID in the Excel sheet
                                testforExistence = true;
                                member = Member.findById(Member.class, Integer.parseInt(id));

                            }
                        }
                        if (testforExistence) {
                            // member exist ==> update data from Excel sheet
                            switch (count) {

                                case 2:
                                    if (!member.getAge().equals(myCell.toString())) {
                                        member.setAge(myCell.toString());
                                        member.save();
                                    }
                                    break;
                                case 3:
                                    if (!member.getEmail().equals(myCell.toString())) {
                                        member.setEmail(myCell.toString());
                                        member.save();
                                    }
                                    break;
                                case 4:
                                    if (!member.getGender().equals(myCell.toString())) {
                                        member.setGender(myCell.toString());
                                        member.save();
                                    }
                                    break;
                                case 5:
                                    if (!member.getHeight().equals(myCell.toString())) {
                                        member.setHeight(myCell.toString());
                                        member.save();
                                    }
                                    break;
                                case 6:
                                    // cannot read photo from Excel file  so there's no need to change it
                                    break;

                                case 7:
                                    if (!member.getName().equals(myCell.toString())) {
                                        member.setName(myCell.toString());
                                        member.save();
                                    }
                                    break;
                                case 8:
                                    if (!member.getObjective().equals(myCell.toString())) {
                                        member.setObjective(myCell.toString());
                                        member.save();
                                    }
                                    break;
                                case 9:
                                    if (!member.getPhone().equals(myCell.toString())) {
                                        member.setPhone(myCell.toString());
                                        member.save();
                                    }
                                    break;
                                case 10:
                                    if (!member.getWeight().equals(myCell.toString())) {
                                        member.setWeight(myCell.toString());
                                        member.save();
                                    }
                                    break;
                                case 11:
                                    if (!member.getClock().equals(myCell.toString())) {
                                        member.setClock(myCell.toString());
                                        member.save();
                                    }
                                    break;
                                case 12:
                                    if (!member.getTime_in().equals(myCell.toString())) {
                                        member.setTime_in(myCell.toString());
                                        member.save();
                                    }
                                    break;
                                case 13:
                                    if (!member.getTime_out().equals(myCell.toString())) {
                                        member.setTime_out(myCell.toString());
                                        member.save();
                                    }
                                    break;
                                default:
                                    break;
                            }
                            count++;
                        } else {
                            // member doesn't exist in database ==> create new member with the same exact id

                            switch (count) {

                                case 2:
                                    newMember.setAge(myCell.toString());
                                    break;

                                case 3:
                                    newMember.setEmail(myCell.toString());
                                    break;

                                case 4:
                                    newMember.setGender(myCell.toString());
                                    break;

                                case 5:
                                    newMember.setHeight(myCell.toString());
                                    break;

                                case 6: // we'll set default image because we couldn't read the bitmap from Excel sheet
                                    Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.default_profil);
                                    imagev = getBytes(icon);
                                    newMember.setImagev(imagev);
                                    break;

                                case 7:
                                    newMember.setName(myCell.toString());
                                    break;

                                case 8:
                                    newMember.setObjective(myCell.toString());
                                    break;

                                case 9:
                                    newMember.setPhone(myCell.toString());
                                    break;

                                case 10:
                                    newMember.setWeight(myCell.toString());
                                    break;

                                case 11:
                                    newMember.setClock(myCell.toString());
                                    break;

                                case 12:
                                    newMember.setTime_in(myCell.toString());
                                    break;

                                case 13:

                                    newMember.setTime_out(myCell.toString());
                                    newMember.setId(Long.parseLong(id));
                                    newMember.update();
                                    break;

                                default:
                                    break;
                            }
                            count++;
                        }

                    }
                }
            }
            List<Member> members = Member.listAll(Member.class);
            Toast.makeText(context, "Members' sheet added", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(context, "Excel or sheet not found ", Toast.LENGTH_SHORT).show();
        }

        return;
    }

    private void readExcelGoalSheet(Context context) {



        if (!isExternalStorageAvailable() || isExternalStorageReadOnly())
        {
            Toast.makeText(context, "Storage not available or read only", Toast.LENGTH_SHORT).show();
            return;
        }

        //Environment.getExternalStorageDirectory().getAbsolutePath() + "/The Coach" + "/database/coachDB.xls";

        try{
            int test = 1;
            boolean skiprow = true;
            // Creating Input Stream
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/The Coach" + "/database/coachDB.xls");
            //File file = new File(context.getExternalFilesDir(null), Environment.getExternalStorageDirectory().getAbsolutePath() + "/The Coach" + "/database/coachDB.xls");
            FileInputStream myInput = new FileInputStream(file);

            // Create a POIFSFileSystem object
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);

            // Create a workbook using the File System
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);

            // Get the first sheet from workbook ==> 0:Member   1: Goals
            HSSFSheet mySheet = myWorkBook.getSheetAt(1);

            /** We now need something to iterate through the cells.**/
            Iterator rowIter = mySheet.rowIterator();

            while(rowIter.hasNext()){

                String id = "0";
                int count =1 ;
                Goals goal = null;
                Goals newGoal = new Goals();
                boolean testforExistence = false;

                HSSFRow myRow = (HSSFRow) rowIter.next();
                Iterator cellIter = myRow.cellIterator();
                if (test > 1){ skiprow = false;}
                while(cellIter.hasNext()) {
                    HSSFCell myCell = (HSSFCell) cellIter.next();
                    if (test == 1) {test ++;}

                    if (!skiprow) { // first row contains the name of each columns no need to scan it

                        if (count == 1) { //count == 1 means the ID cell
                            id = myCell.toString();
                            if (GoalExistence(id)) {
                                //if member exist already in database we need to look for uncompilable data and update it
                                // else (member does not exist) we need to create new member with the same unique ID in the Excel sheet
                                testforExistence = true;
                                goal = Goals.findById(Goals.class, Integer.parseInt(id));

                            }
                        }
                        if (testforExistence) {
                            // member exist ==> update data from Excel sheet
                            switch (count) {

                                case 2:
                                    if (!goal.getActual().equals(myCell.toString())) {
                                        goal.setActual(myCell.toString());
                                        goal.save();
                                    }
                                    break;
                                case 3:
                                    if (!goal.getGoal().equals(myCell.toString())) {
                                        goal.setGoal(myCell.toString());
                                        goal.save();
                                    }
                                    break;
                                case 4:
                                    if (!goal.getMember().equals(myCell.toString())) {
                                        goal.setMember(Member.findById(Member.class,Integer.parseInt(myCell.toString())));
                                        goal.save();
                                    }
                                    break;
                                case 5:
                                    if (!goal.getOld_actual().equals(myCell.toString())) {
                                        goal.setOld_actual(myCell.toString());
                                        goal.save();
                                    }
                                    break;
                                case 6:
                                    if (!goal.getOld_goal().equals(myCell.toString())) {
                                        goal.setOld_goal(myCell.toString());
                                        goal.save();
                                    }
                                    break;

                                case 7:
                                    if (!goal.getTitle().equals(myCell.toString())) {
                                        goal.setTitle(myCell.toString());
                                        goal.save();
                                    }
                                    break;
                                case 8:
                                    if (!goal.getNote().equals(myCell.toString())) {
                                        goal.setNote(myCell.toString());
                                        goal.save();
                                    }
                                    break;

                                default:
                                    break;
                            }
                            count++;
                        } else {
                            // member doesn't exist in database ==> create new member with the same exact id

                            switch (count) {

                                case 2:
                                    newGoal.setActual(myCell.toString());
                                    break;

                                case 3:
                                    newGoal.setGoal(myCell.toString());
                                    break;

                                case 4:
                                    newGoal.setMember(Member.findById(Member.class,Integer.parseInt(myCell.toString())));
                                    break;

                                case 5:
                                    newGoal.setOld_actual(myCell.toString());
                                    break;

                                case 6: // we'll set default image because we couldn't read the bitmap from Excel sheet
                                    newGoal.setOld_goal(myCell.toString());
                                    break;

                                case 7:
                                    newGoal.setTitle(myCell.toString());
                                    break;

                                case 8:
                                    newGoal.setNote(myCell.toString());
                                    newGoal.setId(Long.parseLong(id));
                                    newGoal.update();

                                    List<Goals> membe = Goals.listAll(Goals.class);
                                    break;

                                default:
                                    break;
                            }
                            count++;
                        }

                    }
                }
            }
            List<Goals> goalsList = Goals.listAll(Goals.class);
            Toast.makeText(context, "finished", Toast.LENGTH_SHORT).show();

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(context, "Goals' sheet is empty", Toast.LENGTH_SHORT).show();
        }

        return;
    }

    public static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    public static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    public static  boolean GoalExistence (String id){
        Goals goalInDB;

        goalInDB =Goals.findById(Goals.class,Integer.parseInt(id));

        if (goalInDB == null) {return false;}
        else {return true;}
    }

    public static boolean MemberExistence(String id){

        Member memberInDB;

        memberInDB = Member.findById(Member.class,Integer.parseInt(id));

        if (memberInDB == null){ return false;}
        else {return true;}
    }

    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

}
