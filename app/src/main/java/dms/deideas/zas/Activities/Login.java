package dms.deideas.zas.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dms.deideas.zas.Constants;
import dms.deideas.zas.Globals;
import dms.deideas.zas.Model.Reparto;
import dms.deideas.zas.Model.UserMeta;
import dms.deideas.zas.Model.WebConfigurator;
import dms.deideas.zas.Push.MyFirebaseInstanceIDService;
import dms.deideas.zas.R;
import dms.deideas.zas.Services.MotodriverGet;
import dms.deideas.zas.Services.MotodriverService;
import dms.deideas.zas.Services.OrderSearch;
import dms.deideas.zas.Services.OrderUpdate;
import dms.deideas.zas.Services.Retrofit.RetrofitDelegateHelper;
import dms.deideas.zas.Services.UserMetaGet;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Login extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener, RetrofitDelegateHelper.AlRecibirListaDelegate,RetrofitDelegateHelper.WebConfigurationDelegate {
    protected ArrayAdapter adapter;
    HashMap<String, Integer> hashMuppets = new HashMap<String, Integer>();
    private EditText user, password;
    private Button enterApp;
    private TextView message;

    private int iduser;
    private String name;
    private String email;
    private String idAreaDelivery = Constants.STRING_EMPTY;
    private String strAreaDelivery = Constants.STRING_EMPTY;
    private Integer IntnumberMaxOrders = 0;
    private String numberMaxOrders = Constants.STRING_EMPTY;
    private String numberMaxOrdersVisible = Constants.STRING_EMPTY;

    private Spinner area_delivery;
    private Spinner state_area_delivery;
    private Intent intent;
    private RetrofitDelegateHelper restHelper;
    private String timeMax = "";
    private Boolean isUserNew = false;
    private String distanceMax = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        readPreferences();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        area_deliverySpinners();
        setViews();
        //check if parameter is load. Is possible that user logout and then login and this parameters could be empty. It's a problem
        if (IntnumberMaxOrders == 0) {
            getConfigurationByWeb();
        }
        message.setText("");
        enterApp.setOnClickListener(this);
    }

    private void setViews() {
        user = (EditText) findViewById(R.id.user);
        password = (EditText) findViewById(R.id.password);
        enterApp = (Button) findViewById(R.id.enter);
        area_delivery = (Spinner) findViewById(R.id.area_delivery);
        state_area_delivery = (Spinner) findViewById(R.id.state_area_delivery);
        message = (TextView) findViewById(R.id.message);
    }

    protected void readPreferences() {
        SharedPreferences prefs = getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE);
        IntnumberMaxOrders = Integer.valueOf(prefs.getString(Constants.PREFERENCES_NUMBER_MAX_ORDERS_ACCEPTED_BYDRIVER, Constants.STRING_0));
    }

    private void area_deliverySpinners() {
        Retrofit retrofit;
        Globals g = Globals.getInstance();
        try {
            g.setServiceCode(Constants.SERVICE_CODE_zone);
            restHelper = new RetrofitDelegateHelper();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        restHelper.getareaDelivery(this);
    }

    @Override
    public void onClick(View v) {
        final Retrofit retrofit;
        final MotodriverService motodriverService;
        final SharedPreferences prefs =
                getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE);

        if (v == enterApp) {

            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.URL_ZAS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            motodriverService = retrofit.create(MotodriverService.class);

            motodriverService.String(user.getText().toString(), password.getText().toString()).enqueue(new Callback<MotodriverGet>() {
                @Override
                public void onResponse(Call<MotodriverGet> call, Response<MotodriverGet> response) {
                    if (response.body() != null) {

                        if (!strAreaDelivery.equals(Constants.STRING_EMPTY)) {
                            //Obtain data of user
                            iduser = response.body().getID();
                            name = response.body().getData().getDisplay_name();
                            email = response.body().getData().getUser_email();

                            UserMetaGet userMetaGet;
                            //Get More Data User from userMeta table
                            userMetaGet = retrofit.create(UserMetaGet.class);

                            userMetaGet.getUserMeta(user.getText().toString(), password.getText().toString()).enqueue(new Callback<UserMeta>() {
                                @Override
                                public void onResponse(Call<UserMeta> call, Response<UserMeta> response) {
                                        if (response.body() != null) {
                                            //Save User Meta
                                            //if user is new
                                            isUserNew = (response.body().getIs_driver_new().equals("1"))?true : false ;
                                            //The maximum km that the user can take orders
                                            distanceMax = response.body().getMaxdistance();


                                            long timelastLogin = System.currentTimeMillis() / 1000;

                                            //Save data of user in preferences
                                            SharedPreferences.Editor editor = savePreferences(timelastLogin, prefs);

                                            //Save idUser and Token in BBDD
                                            String refreshedToken = prefs.getString(Constants.PREFERENCES_REFRESH_TOKEN, Constants.STRING_0);
                                            MyFirebaseInstanceIDService fb = new MyFirebaseInstanceIDService();
                                            if (refreshedToken == Constants.STRING_0) {
                                                refreshedToken = fb.getToken(iduser);
                                                editor.putString(Constants.PREFERENCES_REFRESH_TOKEN, refreshedToken);
                                                editor.commit();
                                            } else if (!refreshedToken.isEmpty() && iduser > 0) {
                                                fb.sendRegistrationToServer(refreshedToken);
                                            }


                                            //Start activity Main
                                            Intent intent = new Intent(Login.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                        else {
                                            message.setText(getString(R.string.fail));
                                        }
                                    }
                                @Override
                                public void onFailure(Call<UserMeta> call, Throwable t) {
                                }
                            });


                        } else {
                            message.setText(getString(R.string.fail_area));
                        }

                    } else {

                        message.setText(getString(R.string.fail));
                    }
                }

                @Override
                public void onFailure(Call<MotodriverGet> call, Throwable t) {
                }
            });
        }
    }

    @NonNull
    private SharedPreferences.Editor savePreferences(long timelastLogin, SharedPreferences prefs) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(Constants.PREFERENCES_USER_ID, iduser);
        editor.putString(Constants.PREFERENCES_USER_DISPLAYNAME, name);
        editor.putString(Constants.PREFERENCES_USER, user.getText().toString());
        editor.putString(Constants.PREFERENCES_USER_PASSWORD, password.getText().toString());
        editor.putString(Constants.PREFERENCES_USER_EMAIL, email);
        editor.putLong(Constants.PREFERENCES_USER_TIME_LASTLOGIN, timelastLogin);
        editor.putString(Constants.PREFERENCES_AREA_DELIVERY, idAreaDelivery);
        editor.putString(Constants.PREFERENCES_AREA_DELIVERY_STRING, strAreaDelivery);
        editor.putBoolean(Constants.PREFERENCES_IS_ORDER_CHANGED, false);
        editor.putBoolean(Constants.PREFERENCES_USERMETA_ISUSERNEW, isUserNew);
        editor.putString(Constants.PREFERENCES_USERMETA_DISTANCEMAX, distanceMax);
        editor.commit();
        return editor;
    }


    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

        //get selected content
        String strName = area_delivery.getSelectedItem().toString();
        if (strName != getBaseContext().getResources().getString(R.string.prompt_area)) {
            int intCatID = hashMuppets.get(strName);
            idAreaDelivery = String.valueOf(intCatID);
            strAreaDelivery = strName;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void listaRecibida(OrderSearch body) {

    }

    @Override
    public void errorRecibido(Object error) {

    }

    @Override
    public void arrayRecibido(ArrayList<String> body) {
        List<String> zoneList = new ArrayList<>();
        zoneList.add(getResources().getString(R.string.prompt_area));
        for (int i = 0; i < body.size(); i++) {
            zoneList.add(body.get(i));
        }
        adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, zoneList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        area_delivery.setAdapter(adapter);
        area_delivery.setOnItemSelectedListener(this);
        area_delivery.setSelection(-1);
    }

    @Override
    public void areaDeliveryRecived(ArrayList<Reparto> body) {

        String arrNewArray[] = new String[body.size() + 1];
        String arrNewArray2[] = new String[body.size() + 1];
        arrNewArray[0] = getResources().getString(R.string.prompt_area);
        arrNewArray2[0] = getResources().getString(R.string.prompt_area);
        int j = 1;
        int j2 = 1;
        for (int i = 0; i < body.size(); i++) {
            arrNewArray[j++] = body.get(i).getState();
            arrNewArray2[j2++] = body.get(i).getAreadelivery();
            hashMuppets.put(body.get(i).getAreadelivery(), Integer.valueOf(body.get(i).getId()));
        }
        //Application of the Array to the Spinner
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                arrNewArray
        );
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                arrNewArray2
        );
        spinnerArrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        state_area_delivery.setAdapter(spinnerArrayAdapter);
        state_area_delivery.setOnItemSelectedListener(this);
        area_delivery.setAdapter(spinnerArrayAdapter2);
        area_delivery.setOnItemSelectedListener(this);
    }

    @Override
    public void stringReceived(String namefunction, String body) {

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
    public void webConfigRecibido(WebConfigurator body) {
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
        }
    }

    @Override
    public void closedialog() {
        savingVariablesWebConfig();
    }
    // Save fixed values for variables if the web configurator request is failing.
    public void savingVariablesWebConfig(){
        SharedPreferences prefs =
                getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE);
        //Save data of user in preferences
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(Constants.PREF_VALUE_MAX_ORDERS_ACCEPTED, "3");
        editor.putString(Constants.PREF_VALUE_MAX_ORDERS_VISIBLE, "10");
        editor.putString(Constants.PREF_VALUE_MAX_TIME_ORDERS, "15");
        editor.commit();
    }
}
