package com.example.zyed.fitnessapp.Utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Z'YED on 31/08/2018.
 */

public class FontCache {


        private static final String FONT_DIR = "fonts";

        private static FontCache instance;
        private Context context;
        private Map<String, Typeface> cache = new HashMap<>();
        private Map<String, String> fontMapping = new HashMap<>();

        private FontCache(Context context) {
            this.context = context;

            AssetManager am = context.getResources().getAssets();
            String fileList[];
            try {
                fileList = am.list(FONT_DIR);
            } catch (IOException e) {
                Log.e("error","Error loading fonts from assets/fonts.");
                return;
            }

            for (String filename : fileList) {
                String alias = filename.substring(0, filename.lastIndexOf('.'));
                fontMapping.put(alias, filename);
                fontMapping.put(alias.toLowerCase(), filename);
            }
        }

        public static FontCache getInstance(Context context) {
            if (instance == null) {
                instance = new FontCache(context);
            }
            return instance;
        }

        public void addFont(String name, String fontFilename) {
            fontMapping.put(name, fontFilename);
        }

        public Typeface get(String fontName) {
            String fontFilename = fontMapping.get(fontName);
            if (fontFilename == null) {
                Log.e("Couldn't find font ", fontName);
                return null;
            }
            if (cache.containsKey(fontName)) {
                return cache.get(fontName);
            } else {
                Typeface typeface = Typeface.createFromAsset(context.getResources().getAssets(), FONT_DIR + "/" + fontFilename);
                cache.put(fontFilename, typeface);
                return typeface;
            }
        }



}
