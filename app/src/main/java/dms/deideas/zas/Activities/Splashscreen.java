package dms.deideas.zas.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import dms.deideas.zas.Constants;
import dms.deideas.zas.Globals;
import dms.deideas.zas.Model.Order;
import dms.deideas.zas.Model.Reparto;
import dms.deideas.zas.R;
import dms.deideas.zas.Services.OrderSearch;
import dms.deideas.zas.Services.OrderUpdate;
import dms.deideas.zas.Services.Retrofit.RetrofitDelegateHelper;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Splashscreen extends AppCompatActivity {


    Handler handler = new Handler();
    Runnable runnable;

    private int idUser = 0;
    private String areaDelivery = "";
    private long timelastLogin = 0;
    private long timeNow = 0;

    private RetrofitDelegateHelper restHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        final SharedPreferences prefs = getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE);
        idUser = prefs.getInt(Constants.PREFERENCES_USER_ID, 0);
        areaDelivery = prefs.getString(Constants.PREFERENCES_AREA_DELIVERY, "");

        // Keep current time in preferences
        timeNow = System.currentTimeMillis() / 1000;

        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("timeNow", timeNow);
        editor.commit();
        timelastLogin = prefs.getLong(Constants.PREFERENCES_USER_TIME_LASTLOGIN, 0);

        if (idUser == 0) {
            setContentView(R.layout.activity_splashscreen);

            runnable = new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(Splashscreen.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            };
        }

        // while the value of last login time is less than 8 hours, login is unnecessary
        if (timeNow - timelastLogin < Constants.MAX_LOGIN_TIME) {
            Intent intent = new Intent(Splashscreen.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        // in other way, login is mandatory
        else {
            setContentView(R.layout.activity_splashscreen);
            runnable = new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(Splashscreen.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            };

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(runnable, 2500);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }


}