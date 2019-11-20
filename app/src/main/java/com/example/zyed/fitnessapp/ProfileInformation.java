package com.example.zyed.fitnessapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.preference.EditTextPreference;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.EditText;

import com.orm.SugarContext;
import com.squareup.picasso.Picasso;

import android.widget.Button;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static java.lang.Integer.parseInt;



public class ProfileInformation extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int RESULT_LOAD_IMG = 3;
    static final int REQUEST_WRITE = 2;
    public final static int MaxWidth = 720 , MaxHeight = 1080;
    String TAG = "test";
    boolean isImageUpdated=false;
    String member_id_Creation, member_id_Main;
    TextView t1, t2, t3, t4, t5, t6, t7;
    Button picture_btn, update_btn;
    int IdFinal;
    CircleImageView profil_image;
    Bitmap bitmap_profil;
    byte[] NewImage;
    Bundle extras;

    String titles , actuals , sgoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profileinformation);

        SugarContext.init(this);

        // Define the Toolbar as the new Actionbar
        Toolbar myToolbar = findViewById(R.id.allmembers_appbar);
        setSupportActionBar(myToolbar);

        // Get a support ActionBar corresponding to this toolbar
        getSupportActionBar().setTitle("");

        // Enable the Up button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Change the back button on the action bar to a custom drawable
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_white);

        // to know which activity initiated the intent

        CardView cardView =findViewById(R.id.c1);
        cardView.setBackgroundColor(Color.TRANSPARENT);

        TextView textView =findViewById(R.id.description);

        textView.setTextColor(Color.TRANSPARENT);


        Bundle extras = getIntent().getExtras();
        String member_id = extras.getString("idFromProfile");
        IdFinal = Integer.parseInt(member_id);


        boolean ok = Creation2.PicEmpty;

        profil_image = findViewById(R.id.profil_imv);
        picture_btn = findViewById(R.id.button);
        update_btn = findViewById(R.id.button2);

        t1 = findViewById(R.id.textView8);
        t2 = findViewById(R.id.textView9);
        t3 = findViewById(R.id.textView10);
        t4 = findViewById(R.id.textView11);
        t5 = findViewById(R.id.textView12);
        t6 = findViewById(R.id.textView13);
        t7 = findViewById(R.id.textView20);



        final Member member = Member.findById(Member.class, IdFinal);

        t2.setText(member.getAge());
        t1.setText(member.getName());
        t3.setText(member.getPhone());
        t4.setText(member.getEmail());
        t5.setText(member.getWeight());
        t6.setText(member.getHeight());
        t7.setText(member.getObjective());

        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder_photo = new AlertDialog.Builder(ProfileInformation.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.alert_dialog_edit_informations,null);
                builder_photo.setView(dialogView);

                final TextView tv_title =dialogView.findViewById(R.id.tv_edit_title);
                final TextView tv_info =dialogView.findViewById(R.id.tv_show_info);
                final EditText ed_info =dialogView.findViewById(R.id.ed_info);
                Button btn_positive = dialogView.findViewById(R.id.btn_save);
                Button btn_negative = dialogView.findViewById(R.id.btn_cancel);

                tv_title.setText("Change Name");
                tv_info.setText(member.getName());

                final AlertDialog alertDialog = builder_photo.create();

                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                btn_positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.cancel();

                        String name = ed_info.getText().toString();

                        if(Empty(name)){
                            ed_info.setError("Please enter info");
                            ed_info.requestFocus();
                        }

                        member.setName(name);
                        member.save();


                        Toast.makeText(ProfileInformation.this, "Please Refresh", Toast.LENGTH_LONG).show();
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

        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder_photo = new AlertDialog.Builder(ProfileInformation.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.alert_dialog_edit_informations,null);
                builder_photo.setView(dialogView);

                final TextView tv_title =dialogView.findViewById(R.id.tv_edit_title);
                final TextView tv_info =dialogView.findViewById(R.id.tv_show_info);
                final EditText ed_info =dialogView.findViewById(R.id.ed_info);
                Button btn_positive = dialogView.findViewById(R.id.btn_save);
                Button btn_negative = dialogView.findViewById(R.id.btn_cancel);

                tv_title.setText("Change Age");
                tv_info.setText(member.getAge());

                final AlertDialog alertDialog = builder_photo.create();

                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                btn_positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.cancel();

                        String age = ed_info.getText().toString();

                        if(Empty(age)){
                            ed_info.setError("Please enter info");
                            ed_info.requestFocus();
                        }

                        member.setAge(age);
                        member.save();


                        Toast.makeText(ProfileInformation.this, "Please Refresh", Toast.LENGTH_LONG).show();
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

        t3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder_photo = new AlertDialog.Builder(ProfileInformation.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.alert_dialog_edit_informations,null);
                builder_photo.setView(dialogView);

                final TextView tv_title =dialogView.findViewById(R.id.tv_edit_title);
                final TextView tv_info =dialogView.findViewById(R.id.tv_show_info);
                final EditText ed_info =dialogView.findViewById(R.id.ed_info);
                Button btn_positive = dialogView.findViewById(R.id.btn_save);
                Button btn_negative = dialogView.findViewById(R.id.btn_cancel);

                tv_title.setText("Change Phone");
                tv_info.setText(member.getPhone());

                final AlertDialog alertDialog = builder_photo.create();

                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                btn_positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.cancel();

                        String phone = ed_info.getText().toString();

                        if(Empty(phone)){
                            ed_info.setError("Please enter info");
                            ed_info.requestFocus();
                        }

                        member.setPhone(phone);
                        member.save();


                        Toast.makeText(ProfileInformation.this, "Please Refresh", Toast.LENGTH_LONG).show();
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

        t4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder_photo = new AlertDialog.Builder(ProfileInformation.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.alert_dialog_edit_informations,null);
                builder_photo.setView(dialogView);

                final TextView tv_title =dialogView.findViewById(R.id.tv_edit_title);
                final TextView tv_info =dialogView.findViewById(R.id.tv_show_info);
                final EditText ed_info =dialogView.findViewById(R.id.ed_info);
                Button btn_positive = dialogView.findViewById(R.id.btn_save);
                Button btn_negative = dialogView.findViewById(R.id.btn_cancel);

                tv_title.setText("Change Email");
                tv_info.setText(member.getEmail());

                final AlertDialog alertDialog = builder_photo.create();

                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                btn_positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.cancel();

                        String email = ed_info.getText().toString();

                        if(Empty(email)){
                            ed_info.setError("Please enter info");
                            ed_info.requestFocus();
                        }

                        member.setEmail(email);
                        member.save();


                        Toast.makeText(ProfileInformation.this, "Please Refresh", Toast.LENGTH_LONG).show();
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

        t5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder_photo = new AlertDialog.Builder(ProfileInformation.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.alert_dialog_edit_informations,null);
                builder_photo.setView(dialogView);

                final TextView tv_title =dialogView.findViewById(R.id.tv_edit_title);
                final TextView tv_info =dialogView.findViewById(R.id.tv_show_info);
                final EditText ed_info =dialogView.findViewById(R.id.ed_info);
                Button btn_positive = dialogView.findViewById(R.id.btn_save);
                Button btn_negative = dialogView.findViewById(R.id.btn_cancel);

                tv_title.setText("Change Weight");
                tv_info.setText(member.getWeight());

                final AlertDialog alertDialog = builder_photo.create();

                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                btn_positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.cancel();

                        String weight = ed_info.getText().toString();

                        if(Empty(weight)){
                            ed_info.setError("Please enter info");
                            ed_info.requestFocus();
                        }

                        member.setWeight(weight);
                        member.save();


                        Toast.makeText(ProfileInformation.this, "Please Refresh", Toast.LENGTH_LONG).show();
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

        t6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder_photo = new AlertDialog.Builder(ProfileInformation.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.alert_dialog_edit_informations,null);
                builder_photo.setView(dialogView);

                final TextView tv_title =dialogView.findViewById(R.id.tv_edit_title);
                final TextView tv_info =dialogView.findViewById(R.id.tv_show_info);
                final EditText ed_info =dialogView.findViewById(R.id.ed_info);
                Button btn_positive = dialogView.findViewById(R.id.btn_save);
                Button btn_negative = dialogView.findViewById(R.id.btn_cancel);

                tv_title.setText("Change Height");
                tv_info.setText(member.getHeight());

                final AlertDialog alertDialog = builder_photo.create();

                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                btn_positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.cancel();

                        String height = ed_info.getText().toString();

                        if(Empty(height)){
                            ed_info.setError("Please enter info");
                            ed_info.requestFocus();
                        }

                        member.setHeight(height);
                        member.save();


                        Toast.makeText(ProfileInformation.this, "Please Refresh", Toast.LENGTH_LONG).show();
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

        t7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //*******************************
        //t5.setText(member.getPhone());



        // Convert bytes data into a Bitmap
        Bitmap bmp = BitmapFactory.decodeByteArray(member.getImagev(), 0, member.getImagev().length);
        profil_image.setImageBitmap(bmp);
        //Picasso.with(ProfileInformation.this).load(getImageUri(ProfileInformation.this, bmp)).placeholder(R.drawable.default_profil).error(R.drawable.default_profil).into(profil_image);


        //b_goals.setOnClickListener(new View.OnClickListener() {
          //  @Override
           // public void onClick(View v) {

               /* titles = title.getText().toString();
                actuals = actual.getText().toString();
                sgoal = goal.getText().toString();


                Goals goals = new Goals(titles,actuals,sgoal,member);
                goals.save();

                t5.setText(goals.getTitle());
                t6.setText(member.getEmail());
                t7.setText(goals.getMember().getEmail());
                 //= member.getId().toString();
*/

               // List<Goals> gl = Goals.listAll(Goals.class);

               // List<Goals> gl1 = Goals.find(Goals.class, "member = ?", member.getId().toString());
               // Toast.makeText(ProfileInformation.this, "ok ok ", Toast.LENGTH_SHORT).show();
        //    }
        //});



        picture_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder_photo = new AlertDialog.Builder(ProfileInformation.this);
                LayoutInflater inflater = getLayoutInflater();

                View dialogView = inflater.inflate(R.layout.alert_dialog_change_photo,null);
                builder_photo.setView(dialogView);

                Button btn_take_now = dialogView.findViewById(R.id.btn_take);
                Button btn_gallery = dialogView.findViewById(R.id.btn_get_gallery);

                final AlertDialog alertDialog = builder_photo.create();

                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                btn_take_now.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.cancel();
                        if (ContextCompat.checkSelfPermission(ProfileInformation.this, Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            // demanding Permission
                            ActivityCompat.requestPermissions(ProfileInformation.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    REQUEST_IMAGE_CAPTURE);
                        }
                        else{
                            dispatchTakePictureIntent();
                            picture_btn.setVisibility(View.GONE);
                            update_btn.setVisibility(View.VISIBLE);
                            //exist = (imagev == null);
                        }
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

                        picture_btn.setVisibility(View.GONE);
                        update_btn.setVisibility(View.VISIBLE);
                    }
                });
                alertDialog.show();

            }
        });

        //test
        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (ContextCompat.checkSelfPermission(ProfileInformation.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ProfileInformation.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_WRITE);
                }
                else {
                    Member m = Member.findById(Member.class, IdFinal);

                    if (NewImage != null) {
                        m.setImagev(NewImage);
                        m.save();
                        Bitmap bmp1 = BitmapFactory.decodeByteArray(NewImage, 0, NewImage.length);
                        isStoragePermissionGranted();
                        Picasso.with(ProfileInformation.this).load(getImageUri(ProfileInformation.this, bmp1)).placeholder(R.drawable.default_profil).into(profil_image);
                        isImageUpdated = true;
                        Toast.makeText(ProfileInformation.this, "Photo updated successuflly", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ProfileInformation.this, "Please make sure to capture an image", Toast.LENGTH_SHORT).show();
                    }
                    update_btn.setVisibility(View.GONE);
                    picture_btn.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        super.onCreateOptionsMenu(menu);

        //
        getMenuInflater().inflate(R.menu.menu_profileinformation, menu);


        MenuItem actionMenuItem = menu.findItem(R.id.menu_profil_active_members);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == android.R.id.home) { // MANAGE THE UP BAR (return to MainActivity)

            //Toast.makeText(this, , Toast.LENGTH_SHORT).show();
            if( Creation2.PicEmpty && !isImageUpdated){
                Toast.makeText(this, "Please add/update a photo !! ", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent toProfil = new Intent(ProfileInformation.this,Profile.class);
                Bundle extras = new Bundle();
                extras.putString("idFromInfo", String.valueOf(IdFinal));
                toProfil.putExtras(extras);
                startActivity(toProfil);
            }
        }

        if (item.getItemId() == R.id.menu_profil_active_members) {

            Intent toProfilInformation = new Intent(ProfileInformation.this , ProfileInformation.class);
            Bundle extras = new Bundle();
            extras.putString("idFromProfile", String.valueOf(IdFinal));
            toProfilInformation.putExtras(extras);
            startActivity(toProfilInformation);

        }

        if (item.getItemId() == R.id.menu_profil_all_members) {

            if(Creation2.PicEmpty && !isImageUpdated){
                Toast.makeText(this, "Please add/update a photo !! ", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent toAllMemebers = new Intent(ProfileInformation.this, AllMembers.class);
                startActivity(toAllMemebers);
            }
        }

        return true;
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            extras = data.getExtras();
            bitmap_profil = (Bitmap) extras.get("data");
            NewImage = getBytes(bitmap_profil);
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
                    NewImage =getBytes(bitmapscaled);
                }
                else {NewImage =getBytes(selectedImage);}


                Toast.makeText(this, "image has been set", Toast.LENGTH_SHORT).show();


            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(ProfileInformation.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(ProfileInformation.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }

    }


    public byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {

                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "camera permission granted", Toast.LENGTH_SHORT).show();
                dispatchTakePictureIntent();
                picture_btn.setVisibility(View.GONE);
                update_btn.setVisibility(View.VISIBLE);

            } else {

                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();

            }
        }
        if (requestCode == REQUEST_WRITE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, " external write permission granted", Toast.LENGTH_LONG).show();
                Member m = Member.findById(Member.class, IdFinal);

                if (NewImage != null) {
                    m.setImagev(NewImage);
                    m.save();
                    Bitmap bmp1 = BitmapFactory.decodeByteArray(NewImage, 0, NewImage.length);
                    isStoragePermissionGranted();
                    Picasso.with(ProfileInformation.this).load(getImageUri(ProfileInformation.this, bmp1)).placeholder(R.drawable.default_profil).into(profil_image);
                    isImageUpdated = true;
                    Toast.makeText(ProfileInformation.this, "Photo updated successuflly", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProfileInformation.this, "Please make sure to capture an image", Toast.LENGTH_SHORT).show();
                }
                update_btn.setVisibility(View.GONE);
                picture_btn.setVisibility(View.VISIBLE);

            } else {

                Toast.makeText(this, "external write permission denied", Toast.LENGTH_LONG).show();

            }
        }

    }

    boolean Empty(String number){
        return TextUtils.isEmpty(number.trim());
    }

}
