package com.example.zyed.fitnessapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.ContentFrameLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.Button;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.FrameLayout;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Header;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.orm.SugarContext;
import com.orm.SugarRecord;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {

    String member_id_Creation, member_id_Main, member_id_Adapter , member_id_Goal, pdfName="";
    int IdFinal;
    static final int REQUEST_WRITE = 2;
    public final static int QRcodeWidth = 500;
    TextView name;
    Bitmap bitmap;
    ImageView iv_qr;
    Member member;
    TextView tv_note;
    String allDATA, note;
    ImageView pic;
    String path;
    String formattedDate , formattedTime , formattedTime_in="" , formattedTime_out="";
    String titles , actuals , objectives;
    Button b_objective , b_share , b_save , b_not , b_check;
    //EditText ed_title , ed_actual , ed_objective;
    CardView cardView1, cardView2, cardView3;
    Drawable toolbar_drawable;
    SimpleDateFormat time_in = null , time_out = null;
    java.util.Date c = Calendar.getInstance().getTime();
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        SugarContext.init(this);


        CardView cardView =findViewById(R.id.c1);
        cardView.setBackgroundColor(Color.TRANSPARENT);

        Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow);

        TextView textView =findViewById(R.id.description);

        textView.setTextColor(Color.TRANSPARENT);

       // Toolbar myToolbar = findViewById(R.id.new_profil_appbar);
       // setSupportActionBar(myToolbar);

      //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      //  getSupportActionBar().setTitle("");

       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      //  getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow);


        //getSupportActionBar().setLogo(R.drawable.ic_arrow);

        b_objective = findViewById(R.id.profil_objective_btn);
        b_not = findViewById(R.id.profil_notes_btn);
        b_check = findViewById(R.id.profil_check_btn);
        b_share = findViewById(R.id.btn_share);
        b_save = findViewById(R.id.btn_save);
       // ConstraintLayout constraintLayout = findViewById(R.layout.);

        /*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar_drawable = getDrawable(R.drawable.background_profile);

            myToolbar.setBackground(toolbar_drawable);

        }
        else {
            myToolbar.setBackgroundResource(R.drawable.background_profile);

        }
        */


        // Define the Toolbar as the new Actionbar



        if (getIntent().hasExtra("idFromMain")) {

            Bundle extras2 = getIntent().getExtras();
            member_id_Main = extras2.getString("idFromMain");
            IdFinal = Integer.parseInt(member_id_Main);

        } else if (getIntent().hasExtra("id")) {

            Bundle extras = getIntent().getExtras();
            member_id_Creation = extras.getString("id");
            IdFinal = Integer.parseInt(member_id_Creation);
        } else if(getIntent().hasExtra("idFromGoals")) {

                Bundle extras4 = getIntent().getExtras();
                member_id_Goal = extras4.getString("idFromGoals");
                IdFinal = Integer.parseInt(member_id_Goal);
        }else if(getIntent().hasExtra("idFromInfo")) {

            Bundle extras5 = getIntent().getExtras();
            member_id_Goal = extras5.getString("idFromInfo");
            IdFinal = Integer.parseInt(member_id_Goal);
        }else {
            Bundle extras3 = getIntent().getExtras();
            member_id_Adapter = extras3.getString("adapter_id");
            IdFinal = Integer.parseInt(member_id_Adapter);
        }

        pic = findViewById(R.id.profil_imv);
        name = findViewById(R.id.tv_name);

        member = Member.findById(Member.class, IdFinal);

        Bitmap bmp = BitmapFactory.decodeByteArray(member.getImagev(), 0, member.getImagev().length);
        pic.setImageBitmap(bmp);

        name.setText(member.getName());


        b_not.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder_note = new AlertDialog.Builder(Profile.this);

                LayoutInflater inflater = getLayoutInflater();

                View dialogView = inflater.inflate(R.layout.alert_dialog_general_note,null);
                builder_note.setView(dialogView);

                final EditText ed_note =dialogView.findViewById(R.id.ed_note);
                final Button btn_positive_note = dialogView.findViewById(R.id.btn_add_note);
                final Button btn_negative_note = dialogView.findViewById(R.id.btn_cancel_note);


                final AlertDialog alertDialog = builder_note.create();

                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));



                ed_note.setHint(member.getNote());

                btn_positive_note.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //cpu_member = pu.getMember();

                        note = ed_note.getText().toString();

                        if(Empty(note)) {
                            ed_note.setError("Enter your note here");
                            ed_note.requestFocus();
                        }

                        member.setNote(note);
                        member.save();

                        alertDialog.cancel();

                        Toast.makeText(Profile.this, "Note added successfully", Toast.LENGTH_SHORT).show();

                    }
                });

                btn_negative_note.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();

                    }
                });

                alertDialog.show();

            }
        });
        
        b_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(member.getClock().equals("empty") || member.getClock().equals("out")) {

                    member.setClock("in");
                    member.save();
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        time_in = new SimpleDateFormat("HH:mm");
                        formattedTime_in = time_in.format(c);
                        member.setTime_in(formattedTime_in);
                        member.setTime_out("");
                        member.save();

                    }
                    Toast.makeText(Profile.this, "Check IN", Toast.LENGTH_SHORT).show();
                }
                else{

                    member.setClock("out");
                    member.save();
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        time_out = new SimpleDateFormat("HH:mm");
                        formattedTime_out = time_out.format(c);
                        member.setTime_out(formattedTime_out);
                        member.save();
                    }
                    Toast.makeText(Profile.this, "Check OUT", Toast.LENGTH_SHORT).show();
                }


            }
        });



        b_objective.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                AlertDialog.Builder builder_photo = new AlertDialog.Builder(Profile.this);
                LayoutInflater inflater = getLayoutInflater();

                View dialogView = inflater.inflate(R.layout.alert_dialog_objective,null);
                builder_photo.setView(dialogView);

                final EditText ed_title =dialogView.findViewById(R.id.ed_title);
                final EditText ed_actual =dialogView.findViewById(R.id.ed_actual);
                final EditText ed_objective =dialogView.findViewById(R.id.ed_goal);
                Button btn_positive = dialogView.findViewById(R.id.btn_ok);
                Button btn_negative = dialogView.findViewById(R.id.btn_cancel);


                final AlertDialog alertDialog = builder_photo.create();

                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                btn_positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        titles = ed_title.getText().toString();
                        actuals = ed_actual.getText().toString();
                        objectives = ed_objective.getText().toString();

                        if(Empty(titles)){
                            ed_title.setError("Please enter TITLE");
                            ed_title.requestFocus();
                        }
                        else if(Empty(actuals)){
                            ed_actual.setError("Please enter actual state");
                            ed_actual.requestFocus();
                        }
                        else if(Empty(objectives)){
                            ed_objective.setError("Please enter objective");
                            ed_objective.requestFocus();
                        }
                        else {


                            Goals goal = new Goals(titles, actuals, objectives, member, "", "", "");
                            goal.save();
                            alertDialog.cancel();
                            Toast.makeText(Profile.this, "objective added successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                btn_negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();

                    }
                });
                alertDialog.show();
            }
        });

       cardView1 = findViewById(R.id.cardview1);
       cardView2 = findViewById(R.id.cardview2);
       cardView3 = findViewById(R.id.cardview3);

        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent toProfilInformation = new Intent(Profile.this , ProfileInformation.class);
                Bundle extras = new Bundle();
                extras.putString("idFromProfile", String.valueOf(IdFinal));
                toProfilInformation.putExtras(extras);
                startActivity(toProfilInformation);

            }
        });

        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show qrcode

                AlertDialog.Builder builder_photo = new AlertDialog.Builder(Profile.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.alert_dialog_show_qr,null);
                builder_photo.setView(dialogView);


                iv_qr =dialogView.findViewById(R.id.qrimage);
                Button btn_positive = dialogView.findViewById(R.id.btn_done);
                iv_qr.setImageResource(R.drawable.loading);

                allDATA = member.getGender() + "/" + member.getName() + "/" + member.getAge() + "/" + member.getEmail()
                        + "/" + member.getPhone() + "/" + member.getHeight() + "/" + member.getWeight() + "/" + member.getObjective();

                new GenerateImageasync(allDATA).execute();

               /* mProgressDialog = new ProgressDialog(Profile.this);
                mProgressDialog.setTitle("Loading QR Code...");
                mProgressDialog.setMessage("Please wait this should take a couple of seconds");
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.show();*/



                final AlertDialog alertDialog = builder_photo.create();

                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));



                btn_positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();

                    }
                });

                //iv_qr.setImageBitmap(bitmap);
                alertDialog.show();


            }
        });

        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent toGoals = new Intent(Profile.this , GoalsActivity.class);
                Bundle extras = new Bundle();
                extras.putString("idFromProfile", String.valueOf(IdFinal));
                toGoals.putExtras(extras);
                startActivity(toGoals);

            }
        });


        b_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(pdfName.equals("")){
                    Toast.makeText(Profile.this, "you need to save the file first", Toast.LENGTH_SHORT).show();
                }
                else {

                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setType("application/pdf");

                    //path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/The Coach" + "/"+member.getName()+"_"+member.getPhone();

                    //Uri uri = Uri.parse("file://" + reportFile.getAbsolutePath());
                    Uri uri = Uri.parse("file:" + Environment.getExternalStorageDirectory().getAbsolutePath()
                            + "/The Coach" + "/" + member.getName() + "_" + member.getPhone() + "/"+pdfName);
                    intent.putExtra(Intent.EXTRA_STREAM, uri);


                    intent.setData(Uri.parse("mailto:" + member.getEmail()));
                    intent.putExtra(Intent.EXTRA_SUBJECT, "The Coach ");
                    intent.putExtra(Intent.EXTRA_TEXT, "We've attached you tracker sheet in this mail.");

                    try {
                        // startActivity(Intent.createChooser(intent, "Share PDF file"));

                        startActivity(intent);
                        //startActivity(new Intent(Intent.ACTION_VIEW).setDataAndType(Uri.fromFile(uri), "application/pdf")));

                    } catch (Exception e) {
                        Toast.makeText(Profile.this, "Error: Cannot open or share created PDF report.", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });

        b_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (ContextCompat.checkSelfPermission(Profile.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(Profile.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                REQUEST_WRITE);
                    } else {CreatePDF(member);}

                }


        });


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        super.onCreateOptionsMenu(menu);

        //
        getMenuInflater().inflate(R.menu.menu_profile, menu);


        MenuItem actionMenuItem = menu.findItem(R.id.menu_goals_refresh);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == android.R.id.home) { // MANAGE THE UP BAR (return to MainActivity)

            NavUtils.navigateUpFromSameTask(this);

        }

        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_WRITE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, " external write permission granted", Toast.LENGTH_LONG).show();

                CreatePDF(member);


            } else {

                Toast.makeText(this, "external write permission denied", Toast.LENGTH_LONG).show();

            }
        }

    }


    String IdSelector(String ch) {

        String res = "";
        int i = 0;
        while (i < 6) {
            res += ch.charAt(i);
            i++;
        }

        return res;

    }

    Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.black) : getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }

    boolean Empty(String number){
        return TextUtils.isEmpty(number.trim());
    }

    public void CreatePDF(Member member){

        Document doc = new Document();


        try {
            //folder name = NAME_PHONE;  every member will have his own folder
            path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/The Coach" + "/"+member.getName()+"_"+member.getPhone();

            //create file and folder if doesn't exist
            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();

            //file name = Document+UniqueID.pdf
            //String ID = IdSelector(UUID.randomUUID().toString());
            pdfName = "Document_" + member.getName()+ ".pdf";
            File file = new File(dir, pdfName);
            FileOutputStream fOut = new FileOutputStream(file);
            PdfWriter.getInstance(doc, fOut);

            //********************
            // Header = date & time
            Paragraph header = new Paragraph();
            /*String title ="The Coach Gym";
            Paragraph reportName = new Paragraph(title);
            reportName.setAlignment(Element.ALIGN_RIGHT);
            header.add(reportName);*/

            SimpleDateFormat df = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                df = new SimpleDateFormat("dd-MMM-yyyy");
                formattedDate = df.format(c);
            }else{
                Date currentDate = new Date(System.currentTimeMillis());
                formattedDate = currentDate.toString();
            }
            Paragraph reportDate = new Paragraph(formattedDate);
            reportDate.setAlignment(Element.ALIGN_CENTER);
            header.add(reportDate);

            SimpleDateFormat time = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                time = new SimpleDateFormat("HH : mm");
                formattedTime = time.format(c);
                Paragraph reportTime = new Paragraph(formattedTime);
                reportTime.setAlignment(Element.ALIGN_LEFT);
                header.add(reportTime);
            }

            HeaderFooter head = new HeaderFooter(new Phrase(header), false);
            head.setAlignment(Element.ALIGN_RIGHT);
            head.setBorder(Rectangle.BOTTOM);
            doc.setHeader(head);
            //end header
            //************************



            //open document
            doc.open();

            //add gym logo
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Bitmap bitmap = BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.icon_logo_yellow);
            Bitmap bitmapscaled = (Bitmap.createScaledBitmap(bitmap, 150, 150, false));
            bitmapscaled.compress(Bitmap.CompressFormat.PNG, 0, stream);
            Image myImg = Image.getInstance(stream.toByteArray());
            myImg.setAlignment(Image.MIDDLE);
            doc.add(myImg);

            //add overall title
            PdfPTable Table_sheet = new PdfPTable(1);
            Paragraph Parag_sheet = new Paragraph("Tracking Sheet",FontFactory.getFont(FontFactory.TIMES_BOLD,22));
            PdfPCell Cell_sheet = new PdfPCell(Parag_sheet);
            Cell_sheet.setHorizontalAlignment(Element.ALIGN_CENTER);
            Cell_sheet.setBorderWidth(0);
            Cell_sheet.setPadding(15);
            Table_sheet.addCell(Cell_sheet);
            doc.add(Table_sheet);

            //***********************************
            // First tab ( without borders ) ==> image and personal information

           /* ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
            Bitmap bitmap1 = BitmapFactory.decodeByteArray(member.getImagev(), 0, member.getImagev().length);
            Bitmap bitmapscaled1 = (Bitmap.createScaledBitmap(bitmap1, 30, 30, false));
            bitmapscaled1.compress(Bitmap.CompressFormat.PNG, 100, stream1);
            Image myImg1 = Image.getInstance(stream1.toByteArray());
            myImg1.setAlignment(Image.LEFT);

            PdfPTable Table_PersonalInfo = new PdfPTable(2);
            Table_PersonalInfo.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            Table_PersonalInfo.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
            Table_PersonalInfo.getDefaultCell().setBorderWidth(0);
            Table_PersonalInfo.getDefaultCell().setPadding(60);
            Paragraph Parag_Personalinfo = new Paragraph(member.getName()+"\n"+"\n"+member.getPhone()+"\n"+"\n"+member.getEmail(),
                    FontFactory.getFont(FontFactory.COURIER_BOLD, 12 ));
            PdfPCell Cell_PersonalInfo = new PdfPCell(Parag_Personalinfo);
            Cell_PersonalInfo.setBorderWidth(0);
            Cell_PersonalInfo.setPaddingTop(70);
            Cell_PersonalInfo.setPaddingLeft(70);
            //Cell_PersonalInfo.setBackgroundColor(harmony.java.awt.Color.green);
            Table_PersonalInfo.addCell(myImg1);
            Table_PersonalInfo.addCell(Cell_PersonalInfo);
            doc.add(Table_PersonalInfo);
*/
            // end First tab
            //***************************************************


            // First sub_title
            PdfPTable Table_PersonalInfo_Header = new PdfPTable(1);
            Paragraph Parag = new Paragraph("Personal Information:",FontFactory.getFont(FontFactory.HELVETICA_BOLD,17));
            PdfPCell Cell_Title = new PdfPCell(Parag);
            Cell_Title.setHorizontalAlignment(Element.ALIGN_LEFT);
            Cell_Title.setBorderWidth(0);
            Cell_Title.setPaddingTop(20);
            Cell_Title.setPaddingBottom(10);
            Table_PersonalInfo_Header.addCell(Cell_Title);
            doc.add(Table_PersonalInfo_Header);

            // First table (no borders : separation in the middle) ==> personal informations
            PdfPTable Table_PersonalInfo = new PdfPTable(2);

            Paragraph Parag_Name = new Paragraph("Name: "+member.getName(),FontFactory.getFont(FontFactory.COURIER_BOLD, 12 ));
            Paragraph Parag_Email = new Paragraph("Email: "+member.getEmail(),FontFactory.getFont(FontFactory.COURIER_BOLD, 12 ));
            Paragraph Parag_Phone = new Paragraph("Phone: "+member.getPhone(),FontFactory.getFont(FontFactory.COURIER_BOLD, 12 ));
            Paragraph Parag_Height = new Paragraph("Height: "+member.getHeight()+" cm",FontFactory.getFont(FontFactory.COURIER_BOLD, 12 ));
            Paragraph Parag_Weight = new Paragraph("Weight: "+member.getWeight()+" Kg",FontFactory.getFont(FontFactory.COURIER_BOLD, 12 ));
            Paragraph Parag_Age = new Paragraph("Age: "+member.getAge()+" ans",FontFactory.getFont(FontFactory.COURIER_BOLD, 12 ));

            PdfPCell Cell_Name = new PdfPCell(Parag_Name);
            Cell_Name.setHorizontalAlignment(Element.ALIGN_LEFT);
            Cell_Name.setPadding(5);
            Cell_Name.setBorderWidthLeft(0);
            Cell_Name.setBorderWidthBottom(0);
            Cell_Name.setBorderWidthTop(0);
            PdfPCell Cell_Email = new PdfPCell(Parag_Email);
            Cell_Email.setHorizontalAlignment(Element.ALIGN_LEFT);
            Cell_Email.setPaddingLeft(10);
            Cell_Email.setPaddingTop(5);
            Cell_Email.setPaddingBottom(5);
            Cell_Email.setBorderWidthTop(0);
            Cell_Email.setBorderWidthBottom(0);
            Cell_Email.setBorderWidthRight(0);
            PdfPCell Cell_Phone = new PdfPCell(Parag_Phone);
            Cell_Phone.setHorizontalAlignment(Element.ALIGN_LEFT);
            Cell_Phone.setPadding(5);
            Cell_Phone.setBorderWidthLeft(0);
            Cell_Phone.setBorderWidthBottom(0);
            Cell_Phone.setBorderWidthTop(0);
            PdfPCell Cell_Height = new PdfPCell(Parag_Height);
            Cell_Height.setHorizontalAlignment(Element.ALIGN_LEFT);
            Cell_Height.setPadding(5);
            Cell_Height.setBorderWidthLeft(0);
            Cell_Height.setBorderWidthBottom(0);
            Cell_Height.setBorderWidthTop(0);
            PdfPCell Cell_Weight = new PdfPCell(Parag_Weight);
            Cell_Weight.setHorizontalAlignment(Element.ALIGN_LEFT);
            Cell_Weight.setPaddingLeft(10);
            Cell_Weight.setPaddingTop(5);
            Cell_Weight.setPaddingBottom(5);
            Cell_Weight.setBorderWidthTop(0);
            Cell_Weight.setBorderWidthBottom(0);
            Cell_Weight.setBorderWidthRight(0);
            PdfPCell Cell_Age = new PdfPCell(Parag_Age);
            Cell_Age.setHorizontalAlignment(Element.ALIGN_LEFT);
            Cell_Age.setPaddingLeft(10);
            Cell_Age.setPaddingTop(5);
            Cell_Age.setPaddingBottom(5);
            Cell_Age.setBorderWidthTop(0);
            Cell_Age.setBorderWidthBottom(0);
            Cell_Age.setBorderWidthRight(0);

            Table_PersonalInfo.addCell(Cell_Name);
            Table_PersonalInfo.addCell(Cell_Age);
            Table_PersonalInfo.addCell(Cell_Height);
            Table_PersonalInfo.addCell(Cell_Weight);
            Table_PersonalInfo.addCell(Cell_Phone);
            Table_PersonalInfo.addCell(Cell_Email);
            doc.add(Table_PersonalInfo);

            //Second sub_title
            PdfPTable Table_Goals_Header = new PdfPTable(1);
            Paragraph Paragr = new Paragraph("Objectives' Tracker:",FontFactory.getFont(FontFactory.HELVETICA_BOLD,17));
            PdfPCell Cell_goal_Title = new PdfPCell(Paragr);
            Cell_goal_Title.setHorizontalAlignment(Element.ALIGN_LEFT);
            Cell_goal_Title.setBorderWidth(0);
            Cell_goal_Title.setPaddingTop(20);
            Cell_goal_Title.setPaddingBottom(15);
            Table_Goals_Header.addCell(Cell_goal_Title);
            doc.add(Table_Goals_Header);


            //table for overall note
            if (!member.getNote().equals("empty")) {
                PdfPTable Table_overallNote = new PdfPTable(1);

                Paragraph Paragr_note = new Paragraph("Overall Remarque:  "
                        + member.getNote(), FontFactory.getFont(FontFactory.COURIER_BOLD, 12));

                PdfPCell Cell_note = new PdfPCell(Paragr_note);
                Cell_note.setHorizontalAlignment(Element.ALIGN_LEFT);
                Cell_note.setBorderWidth(0);
                Cell_note.setPaddingTop(10);
                Cell_note.setPaddingBottom(15);
                Table_overallNote.addCell(Cell_note);
                doc.add(Table_overallNote);
            }


            //table for check_in/out
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                PdfPTable Table_Check = new PdfPTable(1);

                Paragraph Paragr_in = new Paragraph("Last check in:  " + member.getTime_in(), FontFactory.getFont(FontFactory.COURIER_BOLD, 12));
                Paragraph Paragr_out = new Paragraph("Last check out:  " + member.getTime_out(), FontFactory.getFont(FontFactory.COURIER_BOLD, 12));
                PdfPCell Cell_in = new PdfPCell(Paragr_in);
                PdfPCell Cell_out = new PdfPCell(Paragr_out);
                Cell_in.setHorizontalAlignment(Element.ALIGN_LEFT);
                Cell_in.setBorderWidth(0);
                Cell_in.setPaddingTop(10);
                Cell_out.setHorizontalAlignment(Element.ALIGN_LEFT);
                Cell_out.setBorderWidth(0);
                Cell_out.setPaddingBottom(15);
                Table_Check.addCell(Cell_in);
                Table_Check.addCell(Cell_out);
                doc.add(Table_Check);
            }

            //Second table ==> goals
            PdfPTable Table_Goals = new PdfPTable(5);
            Table_Goals.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            Table_Goals.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
            //table.getDefaultCell().setBorderWidth(0);
            Paragraph Parag_Title = new Paragraph("Title",FontFactory.getFont(FontFactory.HELVETICA_BOLD,12));
            Paragraph Parag_current = new Paragraph("Current state",FontFactory.getFont(FontFactory.HELVETICA_BOLD,12));
            Paragraph Parag_impro = new Paragraph("Improvement",FontFactory.getFont(FontFactory.HELVETICA_BOLD,12));
            Paragraph Parag_objective = new Paragraph("Objective",FontFactory.getFont(FontFactory.HELVETICA_BOLD,12));
            Paragraph Parag_note = new Paragraph("Note",FontFactory.getFont(FontFactory.HELVETICA_BOLD,12));
            Table_Goals.addCell(Parag_Title);
            Table_Goals.addCell(Parag_current);
            Table_Goals.addCell(Parag_impro);
            Table_Goals.addCell(Parag_objective);
            Table_Goals.addCell(Parag_note);


            List<Goals> gl1 = Goals.find(Goals.class, "member = ?", member.getId().toString());
            for(Goals goal: gl1){

                Table_Goals.addCell(goal.getTitle());
                PdfPCell Cell_actual = new PdfPCell(new Paragraph(goal.getActual(),FontFactory.getFont(FontFactory.HELVETICA_BOLD,10)));
                Cell_actual.setHorizontalAlignment(Element.ALIGN_CENTER);
                Cell_actual.setVerticalAlignment(Element.ALIGN_CENTER);
                PdfPCell Cell_goal = new PdfPCell(new Paragraph(goal.getGoal(),FontFactory.getFont(FontFactory.HELVETICA_BOLD,10)));
                Cell_goal.setHorizontalAlignment(Element.ALIGN_CENTER);
                Cell_goal.setVerticalAlignment(Element.ALIGN_CENTER);
                PdfPCell Cell_note = new PdfPCell(new Paragraph(goal.getNote(),FontFactory.getFont(FontFactory.HELVETICA_BOLD,10)));
                Cell_note.setHorizontalAlignment(Element.ALIGN_CENTER);
                Cell_note.setVerticalAlignment(Element.ALIGN_CENTER);
                PdfPCell Cell_impor_empty = new PdfPCell(new Paragraph("--",FontFactory.getFont(FontFactory.HELVETICA_BOLD,10)));
                Cell_impor_empty.setHorizontalAlignment(Element.ALIGN_CENTER);
                Cell_impor_empty.setVerticalAlignment(Element.ALIGN_CENTER);

                if(goal.getOld_actual().equals("")){
                    Table_Goals.addCell(Cell_actual);
                    Table_Goals.addCell(Cell_impor_empty);
                    Table_Goals.addCell(Cell_goal);
                    Table_Goals.addCell(Cell_note);

                }else {

                    int Actual = Integer.parseInt(goal.getActual());
                    int oldActual = Integer.parseInt(goal.getOld_actual());
                    float delta = Actual-oldActual;

                    PdfPCell Cell_impor = new PdfPCell(new Paragraph(String.valueOf(delta),FontFactory.getFont(FontFactory.HELVETICA_BOLD,10)));
                    Cell_impor.setHorizontalAlignment(Element.ALIGN_CENTER);
                    Cell_impor.setVerticalAlignment(Element.ALIGN_CENTER);


                    if (Actual > oldActual && member.getObjective().equals("gain")) {
                        Cell_actual.setBackgroundColor(harmony.java.awt.Color.green);
                    } else if(Actual < oldActual && member.getObjective().equals("gain")) {
                        Cell_actual.setBackgroundColor(harmony.java.awt.Color.red);
                    } else if(Actual > oldActual && !member.getObjective().equals("gain")){
                        Cell_actual.setBackgroundColor(harmony.java.awt.Color.red);
                    } else if(Actual < oldActual && !member.getObjective().equals("gain")){
                        Cell_actual.setBackgroundColor(harmony.java.awt.Color.green);
                    }

                    if(Integer.parseInt(goal.getGoal()) != Integer.parseInt(goal.getOld_goal())) {
                        Cell_goal.setBackgroundColor(harmony.java.awt.Color.lightGray);
                    }
                    //green ==> step closer to objective
                    //red ==> decline
                    //light gray ==> new objective
                    Table_Goals.addCell(Cell_actual);
                    Table_Goals.addCell(Cell_impor);
                    Table_Goals.addCell(Cell_goal);
                    Table_Goals.addCell(Cell_note);
                }
            }
            doc.add(Table_Goals);

            /*
            Paragraph name = new Paragraph(member.getName() );
            Font paraFont = new Font(Font.BOLDITALIC);
            name.setAlignment(Paragraph.ALIGN_CENTER);
            name.setFont(paraFont);

            //add paragraph to document
            doc.add(name);

            Paragraph email = new Paragraph(member.getEmail() );
            Font emailFont = new Font(Font.BOLD);
            email.setAlignment(Paragraph.ALIGN_CENTER);
            email.setFont(emailFont);

            //add paragraph to document
            doc.add(email);

            Paragraph phone = new Paragraph(member.getPhone() );
            Font phoneFont = new Font(Font.HELVETICA);
            name.setAlignment(Paragraph.ALIGN_RIGHT);
            name.setFont(phoneFont);

            //add paragraph to document
            doc.add(phone);
            */

            /*
            doc.add(new Phrase(new Chunk("This font is of type ITALIC",
                    FontFactory.getFont(FontFactory.COURIER_BOLD, 25 ))));
            Chunk ck = new Chunk("This text has a yellow background color", FontFactory.getFont(FontFactory.HELVETICA, 12));
            ck.setBackground(harmony.java.awt.Color.gray);
            doc.add(new Phrase(ck));
            */
            //font = FontFactory.getFont(FontFactory.HELVETICA, Font.DEFAULTSIZE, Font.STRIKETHRU);

            //set footer


//            Toast.makeText(getApplicationContext(), "success pdf", Toast
//                    .LENGTH_LONG).show();

        } catch (DocumentException de) {
            Toast.makeText(this, "Could not create Document", Toast.LENGTH_SHORT).show();
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Toast.makeText(this, "Could not create File", Toast.LENGTH_SHORT).show();
            Log.e("PDFCreator", "ioException:" + e);
        } finally {
            Toast.makeText(Profile.this, "go to "+path, Toast.LENGTH_LONG).show();
            doc.close();
        }
    }

    public  class GenerateImageasync extends AsyncTask<Bitmap, Void, Bitmap> {

        String val;

        GenerateImageasync(String val){ this.val=val; }

        @Override
        protected Bitmap doInBackground(Bitmap... bitmaps) {
            BitMatrix bitMatrix = null;
            try {
                bitMatrix = new MultiFormatWriter().encode(
                        val,
                        BarcodeFormat.DATA_MATRIX.QR_CODE,
                        QRcodeWidth, QRcodeWidth, null
                );

            } catch (IllegalArgumentException Illegalargumentexception) {

                return null;
            } catch (WriterException e) {
                e.printStackTrace();
            }
            int bitMatrixWidth = bitMatrix.getWidth();

            int bitMatrixHeight = bitMatrix.getHeight();

            int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

            for (int y = 0; y < bitMatrixHeight; y++) {
                int offset = y * bitMatrixWidth;

                for (int x = 0; x < bitMatrixWidth; x++) {

                    pixels[offset + x] = bitMatrix.get(x, y) ?
                            getResources().getColor(R.color.black) : getResources().getColor(R.color.white);
                }
            }
            Bitmap bitmap1 = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

            bitmap1.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);

            // **********************


            return bitmap1;
        }

        protected void onPostExecute( Bitmap result )  {


            iv_qr.setImageBitmap(result);

            //bitmap.compress(Bitmap.CompressFormat.JPEG, 100, result);
            //bitmap = result;
/*
            if (ContextCompat.checkSelfPermission(SpalshScreen.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(SpalshScreen.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_WRITE);

                //mProgressDialog.dismiss();
            } else {

                SaveBitmap(bitmap);
                //mProgressDialog.dismiss();
            }
*/


            //mProgressDialog.dismiss();



/*
            if(!permission){

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Magic here
                    }
                }, 5000); // Millisecond 1000 = 1 sec



                Toast.makeText(SpalshScreen.this, String.valueOf(permission), Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(SpalshScreen.this, String.valueOf(permission), Toast.LENGTH_SHORT).show();
            String id = member.getId().toString();
            Intent toMain = new Intent(SpalshScreen.this, MainActivity.class);
            Bundle extras = new Bundle();
            extras.putString("id", id);
            toMain.putExtras(extras);
            startActivity(toMain);
*/
        }
    }



}
