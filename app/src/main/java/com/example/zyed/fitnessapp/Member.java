package com.example.zyed.fitnessapp;

/**
 * Created by Z'YED on 26/06/2018.
 */

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.orm.SugarRecord;
import com.orm.SugarContext;
import com.orm.dsl.Ignore;
import android.widget.ImageView;


public class Member extends SugarRecord{

    private String gender, name, email, objective, age, phone, height, clock, time_in , time_out;

    private byte[] imagev;
    private String weight;


    @Ignore
    private Bitmap image;


    public Member(){

    }

    public Member(String gender, String name, String age, String email, String phone, String weight, String height, String objective, byte[] imagev, String clock, String time_in, String time_out) {

        this.gender = gender;
        this.name = name;
        this.age = age;
        this.email = email;
        this.phone = phone;
        this.weight = weight;
        this.height = height;
        this.objective = objective;
        this.imagev = imagev;
        this.clock = clock;
        this.time_in=time_in;
        this.time_out=time_out;

    }




    public Member(String name, String email, Bitmap image, String clock) {
        this.name =name;
        this.email =email;
        this.image=image;
        this.clock=clock;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public byte[] getImagev() {
        return imagev;
    }

    public void setImagev(byte[] imagev) {
        this.imagev = imagev;
    }

    public String getClock() {
        return clock;
    }

    public void setClock(String check) {
        this.clock = check;
    }

    public String getTime_in() {
        return time_in;
    }

    public void setTime_in(String time_in) {
        this.time_in = time_in;
    }

    public String getTime_out() {
        return time_out;
    }

    public void setTime_out(String time_out) {
        this.time_out = time_out;
    }
}
