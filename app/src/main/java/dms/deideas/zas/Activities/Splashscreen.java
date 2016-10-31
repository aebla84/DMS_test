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

public class Splashscreen extends AppCompatActivity implements RetrofitDelegateHelper.AlRecibirListaDelegate {


    Handler handler = new Handler();
    Runnable runnable;

    private int idUser = 0;
    private String areaDelivery = "";
    private long timelastLogin = 0;
    private long timeNow = 0;

    private RetrofitDelegateHelper restHelper;
    private String numberMaxOrders = "";
    private String numberMaxOrdersVisible = "";
    private String timeMax = "";

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
                    getConfigurationByWeb();
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
//            handler = new Handler();
            runnable = new Runnable() {
                @Override
                public void run() {
                    getConfigurationByWeb();
                }
            };

        }

    }

    private void getConfigurationByWeb() {
        Globals g = Globals.getInstance();

        g.setServiceCode(Constants.SERVICE_CODE_number_max_orders_accepted);
        callRetrofit();
        restHelper.get_maxnumber_orders_accepted(this);

        g.setServiceCode(Constants.SERVICE_CODE_number_max_orders_visible);
        callRetrofit();
        restHelper.get_maxnumber_orders_visible(this);


        g.setServiceCode(Constants.SERVICE_CODE_max_time);
        callRetrofit();
        restHelper.get_maxtime(this);

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
    public void listaRecibida(OrderSearch body) {

    }

    @Override
    public void errorRecibido(Object error) {

    }

    @Override
    public void closedialog() {

    }

    @Override
    public void arrayRecibido(ArrayList<String> body) {

    }

    @Override
    public void areaDeliveryRecived(ArrayList<Reparto> body) {

    }


    @Override
    public void stringReceived(String nameFunction, String body) {
        SharedPreferences prefs =
                getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE);
        //Save data of user in preferences
        SharedPreferences.Editor editor = prefs.edit();
        if (nameFunction == "get_maxnumber_orders_accepted") {
            numberMaxOrders = body;
            editor.putString(Constants.PREFERENCES_NUMBER_MAX_ORDERS_ACCEPTED_BYDRIVER, numberMaxOrders);
            editor.commit();
        } else if (nameFunction == "get_maxnumber_orders_visible") {
            numberMaxOrdersVisible = body;
            editor.putString(Constants.PREFERENCES_NUMBER_MAX_ORDERS_VISIBLE, numberMaxOrdersVisible);
            editor.commit();
            Intent intent = new Intent(Splashscreen.this, Login.class);
            startActivity(intent);
            finish();
        } else if (nameFunction == "get_maxtime") {

            // Max time to become priority in minutes
            timeMax = body;
            editor.putString("timeMax", timeMax);
            editor.commit();
        }

    }

    @Override
    public void orderReceived(OrderUpdate order) {

    }

    // For error 500, not time specified on server set value 120 or other for default
    @Override
    public void notMaxTime() {
        SharedPreferences prefs =
                getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE);
        //Save data of user in preferences
        SharedPreferences.Editor editor = prefs.edit();
        timeMax = "120";
        editor.putString("timeMax", timeMax);
        editor.commit();
    }


}