package com.example.capitaview_page_1;

import android.content.Context;
import android.content.SharedPreferences;

public class cacheManager {
    private static final String PREF_NAME = "YourAppCache";
    private static final String KEY_LISTINGS = "listings";

    // Saves the data to the cache
    public static void saveListings(Context context, String listingsData) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_LISTINGS, listingsData);
        editor.apply();
    }

    //Gets the data from cache
    public static String getListings(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_LISTINGS, null);
    }

    //Clears cache, is called by the broadcast receiver to clear cache every night and update the listings every day
    //Also clears cache when the phone is booted up
    public static void clearCache(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_LISTINGS);
        editor.apply();
    }
}

