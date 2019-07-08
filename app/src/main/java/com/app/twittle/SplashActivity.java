package com.app.twittle;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.app.twittle.utils.Preferences;
import com.app.twittle.utils.Utility;

import java.util.Random;

public class SplashActivity extends AppCompatActivity {

    boolean is_verfied = false;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mContext = this;
        if (!Preferences.isgenerateUniqueKey(mContext)) {
            generateUniqueId();
            Preferences.setUniqueKey(mContext, true);
        }

        is_verfied = Preferences.getisVerified(mContext);
        if (is_verfied) {
            goToHomeActivity();
        } else {
            goToRegistrationActivity();
        }
    }

    private void goToHomeActivity() {

        Thread background = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(3 * 1000);
                    Intent i = new Intent(getBaseContext(), Dashboard.class);
                    startActivity(i);
                    finish();
                } catch (Exception e) {
                }
            }
        };
        background.start();
    }

    private void generateUniqueId() {
        Random rand = new Random();
        String uniqueId = System.currentTimeMillis() + "" + rand.nextInt(500);
        Log.d("uniqueId", uniqueId);
        Preferences.set_UniqueId(mContext, uniqueId);
    }

    private void goToRegistrationActivity() {
        Thread background = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(3 * 1000);
                    Intent i = new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(i);
                    finish();
                } catch (Exception e) {
                }
            }
        };
        background.start();
    }

}
