package dms.deideas.zas.Push;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import dms.deideas.zas.Constants;
import dms.deideas.zas.Model.Devices;
import dms.deideas.zas.Services.DevicesService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by bnavarro on 15/07/2016.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = Constants.TAG_FirebaseInstanceIDService;
    private static int idUser_service= 0;
    private String refreshedToken = "";
    Retrofit retrofit;
    DevicesService device_service;
    Devices dv = new Devices();
    String token = "";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {

        // Get updated InstanceID token.
        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG.concat(" refreshedToken"), refreshedToken);

    }
    public void onTokenRefresh(int idUser) {
        idUser_service = idUser;
        // Get updated InstanceID token.
        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG.concat(" refreshedToken"), refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        if(refreshedToken != null && !refreshedToken.equals("") && (idUser_service > 0)){
            sendRegistrationToServer(refreshedToken);
        }
    }

    public String getToken(int idUser){
        onTokenRefresh(idUser);
        return refreshedToken;
    }

    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     * <p/>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param strToken The new token.
     */
    public void sendRegistrationToServer(String strToken) {
        if(idUser_service == 0 ){
            SharedPreferences prefs = getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE);
            Integer int_IdUser = prefs.getInt(Constants.PREFERENCES_USER_ID, 0);
            idUser_service = int_IdUser;
        }

            // Implement this method to send token to your app server.

            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.URL_ZAS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            device_service = retrofit.create(DevicesService.class);

            dv.setUser_id(idUser_service);
            token = strToken;
            dv.setRegistration_id(token);

            //Check if devices is in BBDD or not

            device_service.getDevices_push(idUser_service).enqueue(
                    new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if (response.isSuccessful()) {
                                if (response.body() != null) {
                                    if (Integer.valueOf(response.body()) > 0) {
                                        updateDevices(token);
                                    } else {
                                        insertDevices(token);
                                    }
                                } else {
                                    Log.d(TAG, "getDevices_push -  Error en response ");
                                }
                            } else {
                                Log.d(TAG, "getDevices_push - Error en response");
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            updateDevices(token);
                            Log.d(TAG, "getDevices_push - onFailure");
                        }
                    }
            );

    }

    private void updateDevices(String token){
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.URL_ZAS)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        device_service = retrofit.create(DevicesService.class);

        dv.setUser_id(idUser_service);
        dv.setRegistration_id(token);
        device_service.updateDevices_push(dv).enqueue(
                new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                if (response.body()) {
                                    Log.d(TAG, "Token update en BBDD");
                                } else {
                                    Log.d(TAG, "No se han updateado el registroId en BBDD");
                                }
                            } else {
                                Log.d(TAG, "updateDevices_push - Error en response");
                            }
                        } else {
                            Log.d(TAG, "updateDevices_push - Error en response");
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        Log.d(TAG, "updateDevices_push - onFailure");
                    }
                }
        );
    }

    private void insertDevices(String token){
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.URL_ZAS)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        device_service = retrofit.create(DevicesService.class);

        dv.setUser_id(idUser_service);
        dv.setRegistration_id(token);
        device_service.saveDevices_push(dv).enqueue(
                new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                if (response.body()) {
                                    Log.d(TAG, "Token guardado en BBDD");
                                } else {
                                    Log.d(TAG, "No se han guardado los datos correctamente en BBDD");
                                }
                            } else {
                                Log.d(TAG, "saveDevices_push - Error en response");
                            }
                        }
                        else {
                            Log.d(TAG, "saveDevices_push - Error en response");
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        Log.d(TAG, "saveDevices_push - onFailure ");
                    }
                }
        );
    }

}

