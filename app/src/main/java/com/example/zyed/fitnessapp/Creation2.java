package com.example.zyed.fitnessapp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.orm.SugarContext;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;



public class Creation2 extends AppCompatActivity {

    static boolean isImageExist=false , PicEmpty;
    static final int REQUEST_IMAGE_CAPTURE = 1 ,RESULT_LOAD_IMG = 3;;
    String gender, name, age, mail, phone, height, weight, objective, member_id ,
            clock ="empty" , time_in="" , time_out="" , note="empty";
    String allDATA;
    Button btn_save;
    String data;
    EditText ed_weight, ed_height;
    Spinner spinner;
    TextView tv_create , tv_height , tv_weight , ed_objective;
    Button b_next, b_confirm;
    ImageView QRimage;
    Bitmap bitmap, profil_image;
    public final static int QRcodeWidth = 500 , MaxWidth = 720 , MaxHeight = 1080;
    byte[] imagev;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation2);


        SugarContext.init(this);

        tv_create = findViewById(R.id.textView2);
        tv_weight= findViewById(R.id.textView3);
        tv_height= findViewById(R.id.textView4);

        //get data from the previous activity
        Bundle extras = getIntent().getExtras();
        gender = extras.getString("gender");
        name = extras.getString("name");
        age = extras.getString("age");
        mail = extras.getString("mail");
        phone = extras.getString("phone");

        ed_height = findViewById(R.id.creation2_ed_height);
        ed_weight = findViewById(R.id.creation2_ed_weight);
       // QRimage = findViewById(R.id.qrimage);
        ed_objective = findViewById(R.id.creation2_ed_objective);
        b_next = findViewById(R.id.creation2_btn_next);
        b_confirm = findViewById(R.id.creation2_btn_confirm);

        // Generate unique ID  w/ length = 6
        final String ID = IdSelector(UUID.randomUUID().toString());

        data = gender + "/" + name + "/" + age + "/" + mail + "/" + phone;


        List<String> spinnerArray =  new ArrayList<String>();
        spinnerArray.add("Select an objective");
        spinnerArray.add("Gain weight/muscle");
        spinnerArray.add("Lose weight/fat");
        spinnerArray.add("Stay fit");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, R.layout.spinner_objective, spinnerArray){
            @Override
            public boolean isEnabled(int position) {
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }

        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner = findViewById(R.id.sp_objective);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                switch (parent.getItemAtPosition(position).toString()){
                    case "Select an objective": objective = "nothing"; break;
                    case "Gain weight/muscle": objective="gain";break;
                    case "Loose weight/fat": objective="loose";break;
                    case "Stay fit": objective="maintain";break;
                }
            }
            public void onNothingSelected(AdapterView<?> parent){

                objective="nothing";
            }
        });


        b_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Empty(ed_weight.getText().toString()) || Integer.parseInt(ed_weight.getText().toString())<20){

                    ed_weight.setError("please enter a valid weight");
                    ed_weight.requestFocus();
                }

                else if(Empty(ed_height.getText().toString())){

                    ed_weight.setError("please enter a height");
                    ed_height.requestFocus();
                }
                else if(objective.equals("nothing")){

                    Toast.makeText(Creation2.this, "Please choose objective", Toast.LENGTH_SHORT).show();
                }

                else {
                    // All the info are set
                    //time to set the profile image
                    // there will be 3 choices

                    AlertDialog.Builder builder_photo = new AlertDialog.Builder(Creation2.this);
                    LayoutInflater inflater = getLayoutInflater();

                    View dialogView = inflater.inflate(R.layout.alert_dialog_photo,null);
                    builder_photo.setView(dialogView);

                    Button btn_take_now = dialogView.findViewById(R.id.btn_take);
                    Button btn_set_default = dialogView.findViewById(R.id.btn_default);
                    Button btn_gallery = dialogView.findViewById(R.id.btn_get_gallery);

                    final AlertDialog alertDialog = builder_photo.create();

                    alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    btn_take_now.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.cancel();
                            if (ContextCompat.checkSelfPermission(Creation2.this, Manifest.permission.CAMERA)
                                    != PackageManager.PERMISSION_GRANTED) {
                                // demanding Permission
                                ActivityCompat.requestPermissions(Creation2.this,
                                        new String[]{Manifest.permission.CAMERA},
                                        REQUEST_IMAGE_CAPTURE);
                            }
                            else{
                                dispatchTakePictureIntent();
                                //exist = (imagev == null);
                            }
                        }
                    });

                    btn_set_default.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //set default image
                            alertDialog.dismiss();
                            Bitmap icon = BitmapFactory.decodeResource(Creation2.this.getResources(),
                                    R.drawable.default_profil);
                            imagev = getBytes(icon);
                            Toast.makeText(Creation2.this, "Default Image set", Toast.LENGTH_SHORT).show();

                            tv_height.setVisibility(View.GONE);
                            tv_weight.setVisibility(View.GONE);
                            ed_height.setVisibility(View.GONE);
                            ed_weight.setVisibility(View.GONE);
                            ed_objective.setVisibility(View.GONE);
                            spinner.setVisibility(View.GONE);
                            b_next.setVisibility(View.GONE);
                            b_confirm.setVisibility(View.VISIBLE);

                            PicEmpty =(imagev == null);
                        }
                    });

                    btn_gallery.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // open gallery
                            alertDialog.dismiss();
                            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                            photoPickerIntent.setType("image/*");
                            startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
                        }
                    });
                    alertDialog.show();
                }
            }
        });

        //save the member to database

        b_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                height = ed_height.getText().toString();
                weight = ed_weight.getText().toString();

                PicEmpty = (imagev ==null);
                if (imagev!=null){isImageExist=true;}

                //Member member = new Member(gender, name, age, mail, phone, weight, height, objective, imagev);
                //member.save();
                //member_id = member.getId().toString();

                allDATA = data + "/" + height + "/" + weight + "/" + objective;

                AlertDialog.Builder builder_photo = new AlertDialog.Builder(Creation2.this);
                LayoutInflater inflater = getLayoutInflater();

                View dialogView = inflater.inflate(R.layout.alert_dialog_layout,null);
                builder_photo.setView(dialogView);

                final TextView title = dialogView.findViewById(R.id.textView7);
                final TextView title2 =dialogView.findViewById(R.id.qrtext);
                final Button btn_qr_code = dialogView.findViewById(R.id.btn_qr);
                final Button btn_id_code = dialogView.findViewById(R.id.btn_id);
                btn_save = dialogView.findViewById(R.id.btn_save);
                QRimage = dialogView.findViewById(R.id.qrimage);
                QRimage.setImageResource(R.drawable.loading);

                final AlertDialog alertDialog = builder_photo.create();

                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                btn_qr_code.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //alertDialog.cancel();

                        title.setVisibility(View.GONE);
                        btn_id_code.setVisibility(View.GONE);
                        btn_qr_code.setVisibility(View.GONE);
                        title2.setVisibility(View.VISIBLE);
                        QRimage.setVisibility(View.VISIBLE);

                        new GenerateImageasync(allDATA).execute();



                        //QRimage.setImageBitmap(bitmap);

                        btn_save.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.cancel();
                                PicEmpty = (imagev ==null);
                                Member member = new Member(gender, name, age, mail, phone, weight, height, objective, imagev, clock, time_in, time_out, note);
                                member.save();
                                member_id = member.getId().toString();
                                Intent CrtoProfil = new Intent(Creation2.this, Profile.class);
                                Bundle extras = new Bundle();
                                extras.putString("id", member_id);
                                // Toast.makeText(Creation2.this, member_id, Toast.LENGTH_SHORT).show();
                                CrtoProfil.putExtras(extras);
                                startActivity(CrtoProfil);
                            }
                        });

                        //prepare the layout to show the QR code
                       /* b_next.setVisibility(View.GONE);
                        b_confirm.setVisibility(View.GONE);
                        tv_create.setVisibility(View.GONE);
                        b_save.setVisibility(View.VISIBLE);
                        QRimage.setVisibility(View.VISIBLE);*/
                    }
                });

                btn_id_code.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //generate id code

                    }
                });


                alertDialog.show();



                /*

                AlertDialog.Builder builder = new AlertDialog.Builder(Creation2.this);
                LayoutInflater inflater1 = Creation2.this.getLayoutInflater();
                builder.setView(inflater1.inflate(R.layout.alert_dialog_layout, null))
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                                try {
                                    bitmap = TextToImageEncode(allDATA);
                                    //Toast.makeText(Creation2.this, allDATA, Toast.LENGTH_SHORT).show();

                                } catch (WriterException e) {
                                    e.printStackTrace();
                                }
                                QRimage.setImageBitmap(bitmap);

                                //prepare the layout to show the QR code
                                b_next.setVisibility(View.GONE);
                                b_confirm.setVisibility(View.GONE);
                                tv_create.setVisibility(View.GONE);
                                b_save.setVisibility(View.VISIBLE);
                                QRimage.setVisibility(View.VISIBLE);
                            }
                        });
                builder.show();

                */
            }
        });
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

    String IdSelector(String ch) {

        String res = "";
        int i = 0;
        while (i < 6) {
            res += ch.charAt(i);
            i++;
        }

        return res;

    }

    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            profil_image = (Bitmap) extras.get("data");
            imagev = getBytes(profil_image);
            PicEmpty = (imagev == null);
            Toast.makeText(this, "Image set", Toast.LENGTH_SHORT).show();
            if(!PicEmpty){

                tv_height.setVisibility(View.GONE);
                tv_weight.setVisibility(View.GONE);
                ed_height.setVisibility(View.GONE);
                ed_weight.setVisibility(View.GONE);
                ed_objective.setVisibility(View.GONE);
                spinner.setVisibility(View.GONE);
                b_next.setVisibility(View.GONE);
                b_confirm.setVisibility(View.VISIBLE);
            }

        }
        else if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                int widthRatio = selectedImage.getWidth() ; //1080
                int heightRatio = selectedImage.getHeight() ; //2160

                if(widthRatio>MaxWidth || heightRatio>MaxHeight){
                    final Bitmap bitmapscaled = (Bitmap.createScaledBitmap(selectedImage, MaxWidth, MaxHeight, false));
                    imagev =getBytes(bitmapscaled);
                }
                else {imagev =getBytes(selectedImage);}

                PicEmpty = (imagev == null);

                Toast.makeText(this, "Image set", Toast.LENGTH_SHORT).show();
                if(!PicEmpty){
                    tv_height.setVisibility(View.GONE);
                    tv_weight.setVisibility(View.GONE);
                    ed_height.setVisibility(View.GONE);
                    ed_weight.setVisibility(View.GONE);
                    ed_objective.setVisibility(View.GONE);
                    spinner.setVisibility(View.GONE);
                    b_next.setVisibility(View.GONE);
                    b_confirm.setVisibility(View.VISIBLE);
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(Creation2.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(Creation2.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }


    // convert from bitmap to byte array
    public byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    boolean Empty(String number){
        return TextUtils.isEmpty(number.trim());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_IMAGE_CAPTURE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                dispatchTakePictureIntent();
                PicEmpty = (imagev == null);

            } else {

                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();

            }

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


            QRimage.setImageBitmap(result);
            btn_save.setVisibility(View.VISIBLE);

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
