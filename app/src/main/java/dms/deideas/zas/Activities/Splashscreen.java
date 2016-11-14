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
import dms.deideas.zas.Model.WebConfigurator;
import dms.deideas.zas.R;
import dms.deideas.zas.Services.OrderSearch;
import dms.deideas.zas.Services.OrderUpdate;
import dms.deideas.zas.Services.Retrofit.RetrofitDelegateHelper;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Splashscreen extends AppCompatActivity implements RetrofitDelegateHelper.WebConfigurationDelegate {


    Handler handler = new Handler();
    Runnable runnable;

    private int idUser = 0;
    private String areaDelivery = "";
    private long timelastLogin = 0;
    private long timeNow = 0;
    private Boolean bopenLogin = false;

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
                    bopenLogin = true;
                    getConfigurationByWeb();
                }
            };
        }

        // while the value of last login time is less than 8 hours, login is unnecessary
        if (timeNow - timelastLogin < Constants.MAX_LOGIN_TIME) {
            getConfigurationByWeb();
        }
        // in other way, login is mandatory
        else {
            setContentView(R.layout.activity_splashscreen);
            runnable = new Runnable() {
                @Override
                public void run() {
                    bopenLogin = true;
                    getConfigurationByWeb();
                }
            };

        }

    }

    private void getConfigurationByWeb() {
        Globals g = Globals.getInstance();

        g.setServiceCode(Constants.SERVICE_CODE_configuratorweb);
        callRetrofit();
        restHelper.get_webconfiguration(this);
    }

    private void callRetrofit() {
        try {
            restHelper = new RetrofitDelegateHelper();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
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


    @Override
    public void webConfigRecibido(WebConfigurator body) {
        /*String array[] = new String[0];
        for (int i = 0; i < 3; i++) {
            array[0] = body.getMaxOrdersAccepted();
            array[1] = body.getMaxOrdersVisible();
            array[2] = body.getMaxTime();
        }
        System.out.println(array);
*/
        SharedPreferences prefs =
                getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE);
        //Save data of user in preferences
        SharedPreferences.Editor editor = prefs.edit();
        if(body != null)
        {
            if (body.getMaxOrdersAccepted()!= null && Integer.valueOf(body.getMaxOrdersAccepted()) > 0) {
                editor.putString(Constants.PREFERENCES_NUMBER_MAX_ORDERS_ACCEPTED_BYDRIVER, body.getMaxOrdersAccepted());
                editor.commit();
            }
            if (body.getMaxOrdersVisible()!= null && Integer.valueOf(body.getMaxOrdersVisible()) > 0) {
                editor.putString(Constants.PREFERENCES_NUMBER_MAX_ORDERS_VISIBLE,body.getMaxOrdersVisible());
                editor.commit();
            }
            if (body.getMaxTime()!= null && Integer.valueOf(body.getMaxTime()) > 0) {
                editor.putString(Constants.PREFERENCES_MAXTIME_ORDERS_CHANGE_MAXPRIORITY,body.getMaxTime());
                editor.commit();
            }
            Intent intent = new Intent();
            if(bopenLogin)
            {
                 intent = new Intent(Splashscreen.this, Login.class);
            }
            else
            {
                 intent = new Intent(Splashscreen.this, MainActivity.class);
            }
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void closedialog() {

    }

    @Override
    public void errorRecibido(Object error) {

    }

}