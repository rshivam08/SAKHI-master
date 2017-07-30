package net.simplifiedcoding.shelounge.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Created by Ravi on 01/06/15.
 */
@SuppressLint("CommitPrefEdits")
public class PrefManager {
    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";
    public static final String Event = "event";
    public static final String Mobile = "mobile";
    public static final String LOGIN = "login";
    // Shared pref file name
    private static final String PREF_NAME = "ScreenCast";
    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
    // Shared Preferences
    SharedPreferences pref;
    SharedPreferences pref1;
    // Editor for Shared preferences
    Editor editor;
    Editor editor1;
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;


    // Constructor
    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        pref1 = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);

        editor = pref.edit();
        editor1 = pref1.edit();

    }

    /**
     * Create login session
     */
    public void createLoginSession(String email) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing email in pref
        editor.putString(KEY_EMAIL, email);

        // commit changes
        editor.commit();
    }

    public void setmobile(String product) {


        editor.putString(Mobile, product);

        // Storing email in pref


        // commit changes
        editor.commit();
    }

    public String getEvent() {
        return pref.getString(Event, null);
    }

    public void setEvent(String product) {


        editor.putString(Event, product);

        // Storing email in pref


        // commit changes
        editor.commit();
    }

    public String getmobile() {
        return pref.getString(Mobile, null);
    }

    public String getLogin() {
        return pref.getString(LOGIN, null);
    }

    public void setLogin(String product) {


        editor1.putString(LOGIN, product);

        // Storing email in pref


        // commit changes
        editor1.commit();
    }

    public String getName() {
        return pref.getString("name", null);
    }

    public void setName(String product) {


        editor1.putString("name", product);

        // Storing email in pref


        // commit changes
        editor1.commit();
    }

    public String getPassword() {
        return pref.getString("password", null);
    }

    public void setPassword(String product) {


        editor1.putString("password", product);

        // Storing email in pref


        // commit changes
        editor1.commit();
    }

    public String getUsername() {
        return pref.getString("username", null);
    }

    public void setUsername(String product) {


        editor1.putString("username", product);

        // Storing email in pref


        // commit changes
        editor1.commit();
    }

    public String getEmail() {
        return pref.getString(KEY_EMAIL, null);
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }


    public void logout() {
        editor1.clear();
        editor1.commit();
    }


}