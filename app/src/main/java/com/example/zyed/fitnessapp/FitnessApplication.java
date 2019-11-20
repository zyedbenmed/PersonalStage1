package com.example.zyed.fitnessapp;

import android.app.Application;

import com.example.zyed.fitnessapp.Utils.FontCache;

/**
 * Created by Z'YED on 31/08/2018.
 */

public class FitnessApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initFont();
    }
   public void initFont()
   {
       FontCache.getInstance(getApplicationContext()).addFont("fitnessfont","font_sp.ttf");
   }
}

