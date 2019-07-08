package com.app.twittle.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {

    public static boolean getisVerified(Context mContext) {
        SharedPreferences loginPreferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        boolean flag = loginPreferences.getBoolean("d_ride_later", false);
        return flag;
    }

    public static void setisVerified(Context mContext, boolean isVerified) {
        SharedPreferences preferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("d_ride_later", isVerified);
        editor.apply();
    }

    public static String get_userPhone(Context mContext) {
        SharedPreferences loginPreferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        String a_key = loginPreferences.getString("user_phone", "0");
        return a_key;
    }

    public static void set_userPhone(Context mContext, String a_key) {
        SharedPreferences preferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user_phone", a_key);
        editor.commit();
    }


    public static String get_userName(Context mContext) {
        SharedPreferences loginPreferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        String a_key = loginPreferences.getString("user_name", "0");
        return a_key;
    }

    public static void set_userName(Context mContext, String a_key) {
        SharedPreferences preferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user_name", a_key);
        editor.commit();
    }

    public static String get_userEmail(Context mContext) {
        SharedPreferences loginPreferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        String a_key = loginPreferences.getString("user_Email", "0");
        return a_key;
    }

    public static void set_userEmail(Context mContext, String a_key) {
        SharedPreferences preferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user_Email", a_key);
        editor.commit();
    }

    public static String get_userId(Context mContext) {
        SharedPreferences loginPreferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        String a_key = loginPreferences.getString("user_ID", "0");
        return a_key;
    }

    public static void set_userId(Context mContext, String a_key) {
        SharedPreferences preferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user_ID", a_key);
        editor.commit();
    }


    public static String get_UniqueId(Context mContext) {
        SharedPreferences loginPreferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        String a_key = loginPreferences.getString("_UniqueId", "0");
        return a_key;
    }

    public static void set_UniqueId(Context mContext, String a_key) {
        SharedPreferences preferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("_UniqueId", a_key);
        editor.commit();
    }

    public static boolean isgenerateUniqueKey(Context mContext) {
        SharedPreferences loginPreferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        boolean flag = loginPreferences.getBoolean("isCheckedOut", false);
        return flag;
    }

    public static void setUniqueKey(Context mContext, boolean isVerified) {
        SharedPreferences preferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isCheckedOut", isVerified);
        editor.apply();
    }

    public static String get_Zip(Context mContext) {
        SharedPreferences loginPreferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        String a_key = loginPreferences.getString("zip", "0");
        return a_key;
    }

    public static void set_Zip(Context mContext, String a_key) {
        SharedPreferences preferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("zip", a_key);
        editor.commit();
    }


    public static String get_Cartount(Context mContext) {
        SharedPreferences loginPreferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        String a_key = loginPreferences.getString("cartcount", "0");
        return a_key;
    }

    public static void set_Cartount(Context mContext, String a_key) {
        SharedPreferences preferences = mContext.getSharedPreferences("Kppref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("cartcount", a_key);
        editor.commit();
    }

}
