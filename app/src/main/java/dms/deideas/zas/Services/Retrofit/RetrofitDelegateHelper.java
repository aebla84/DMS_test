package dms.deideas.zas.Services.Retrofit;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import dms.deideas.zas.Constants;
import dms.deideas.zas.Globals;
import dms.deideas.zas.Model.Incidencia;
import dms.deideas.zas.Model.Order;
import dms.deideas.zas.Model.OrderNote;
import dms.deideas.zas.Model.Reparto;
import dms.deideas.zas.Model.WebConfigurator;
import dms.deideas.zas.R;
import dms.deideas.zas.Services.AreaDeliveryService;
import dms.deideas.zas.Services.GetConfigurationWeb;
import dms.deideas.zas.Services.OrderCount;
import dms.deideas.zas.Services.OrderNoteGet;
import dms.deideas.zas.Services.OrderNoteService;
import dms.deideas.zas.Services.OrderNoteUpdate;
import dms.deideas.zas.Services.OrderSearch;
import dms.deideas.zas.Services.OrderService;
import dms.deideas.zas.Services.OrderUpdate;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by dmadmin on 02/06/2016.
 */
public class RetrofitDelegateHelper {

    //region Initiate variable
    public static final String BASE_URL = Constants.URL_ZAS;
    public static String BASE_URL_CODIFIED = "";
    private final Retrofit retrofit;
    private final OrderService orderService;
    private final GetConfigurationWeb getConfigurationWeb;
    private final OrderNoteService ordernoteService;
    private final AreaDeliveryService areaDeliveryService;
    private final int intResult = 0;
    private String oauth_consumer_key = Constants.OAUTH_CONSUMER_KEY;
    private String oauth_consumer_secret = Constants.OAUTH_CONSUMER_SECRET;
    private String oauth_signature_method = Constants.OAUTH_SIGNATURE_METHOD;
    private String oauth_timestamp = null;
    private String oauth_nonce = null;
    private String oauth_signature = null;
    private int idUser = 0;
    private String strArea = "";
    private int idOrder = 0;
    private Incidencia problem_details;
    private OrderNote order_note;
    private Boolean bResult = false;
    private Integer inumMyOrdersWithoutProblems = 0;
    private SharedPreferences prefs;
    //endregion

    //region Initiate Retrofit (Add Timeout + Constructor Retrofit +  create instance Retrofit)
    public RetrofitDelegateHelper() throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        // Initiate params
        oauth_nonce = getNonce();
        oauth_timestamp = getTimestamp();
        oauth_signature = getSignature(oauth_consumer_secret);

        //Add Timeout in reply Retrofit's
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        //Build Retrofit
        retrofit = new Retrofit.Builder() //constructor
                .baseUrl(BASE_URL)  //Set the API base URL.
                .addConverterFactory(GsonConverterFactory.create()) //Add converter factory for serialization and deserialization of objects.
                .client(okHttpClient) //The HTTP client used for requests.
                .build(); //Create the Retrofit instance using the configured values.

        //Create instance Retrofit
        orderService = retrofit.create(OrderService.class);
        getConfigurationWeb = retrofit.create(GetConfigurationWeb.class);
        ordernoteService = retrofit.create(OrderNoteService.class);
        areaDeliveryService = retrofit.create(AreaDeliveryService.class);

    }

    public RetrofitDelegateHelper(int idOrder, int idUser) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {

        this.idUser = idUser;
        this.idOrder = idOrder;

        // Initiate params
        oauth_nonce = getNonce();
        oauth_timestamp = getTimestamp();
        oauth_signature = getSignature(oauth_consumer_secret);

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        orderService = retrofit.create(OrderService.class);
        getConfigurationWeb = retrofit.create(GetConfigurationWeb.class);
        ordernoteService = retrofit.create(OrderNoteService.class);
        areaDeliveryService = retrofit.create(AreaDeliveryService.class);
    }

    public RetrofitDelegateHelper(int idOrder, int idUser,String strArea) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {

        this.idUser = idUser;
        this.idOrder = idOrder;
        this.strArea = strArea;

        // Initiate params
        oauth_nonce = getNonce();
        oauth_timestamp = getTimestamp();
        oauth_signature = getSignature(oauth_consumer_secret);

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        orderService = retrofit.create(OrderService.class);
        getConfigurationWeb = retrofit.create(GetConfigurationWeb.class);
        ordernoteService = retrofit.create(OrderNoteService.class);
        areaDeliveryService = retrofit.create(AreaDeliveryService.class);
    }

    public RetrofitDelegateHelper(int idOrder, int idUser, Incidencia problem_details) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {

        this.idUser = idUser;
        this.idOrder = idOrder;

        //this.lstIncidencias = lstIncidencias;
        this.problem_details = problem_details;

        // Initiate params
        oauth_nonce = getNonce();
        oauth_timestamp = getTimestamp();
        oauth_signature = getSignature(oauth_consumer_secret);

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        orderService = retrofit.create(OrderService.class);
        getConfigurationWeb = retrofit.create(GetConfigurationWeb.class);
        ordernoteService = retrofit.create(OrderNoteService.class);
        areaDeliveryService = retrofit.create(AreaDeliveryService.class);
    }

    public RetrofitDelegateHelper(int idOrder, int idUser, Incidencia problem_details, OrderNote order_note) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {

        this.idUser = idUser;
        this.idOrder = idOrder;

        //this.lstIncidencias = lstIncidencias;
        this.problem_details = problem_details;
        this.order_note = order_note;

        // Initiate params
        oauth_nonce = getNonce();
        oauth_timestamp = getTimestamp();
        oauth_signature = getSignature(oauth_consumer_secret);

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        orderService = retrofit.create(OrderService.class);
        getConfigurationWeb = retrofit.create(GetConfigurationWeb.class);
        ordernoteService = retrofit.create(OrderNoteService.class);
        areaDeliveryService = retrofit.create(AreaDeliveryService.class);
    }
    //endregion

    //region Methods to initiate params for calls Retrofito's
    //Methods for calculating the nonce of OAuth1.0
    private String getNonce() {

        String oauthNonce = UUID.randomUUID().toString().replaceAll("-", "");
        return oauthNonce;
    }
    // Method that returns the current system time in seconds since 1/1/1970
    private static String getTimestamp() {
        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();
        return ts;
    }
    //Methods for calculating the signature
    public String getSignature(String oauth_consumer_secret) throws
            UnsupportedEncodingException, NoSuchAlgorithmException,
            InvalidKeyException {
        //Get Base String with pareams
        String baseString = getBaseString();
        //Create Secret Key
        SecretKeySpec key = new SecretKeySpec((oauth_consumer_secret).getBytes(Constants.OAUTH_UTF_8), Constants.OAUTH_SIGNATURE_METHOD);
        //Get Mac instance in bytes
        Mac mac = Mac.getInstance(Constants.OAUTH_SIGNATURE_METHOD);
        mac.init(key);
        byte[] bytes = mac.doFinal(baseString.getBytes(Constants.OAUTH_UTF_8));

        // Base64 encode, conducted a trim, and return
        return new String(Base64.encodeToString(bytes, 0).trim());
    }

    //Method that build baseString depending getServiceCode
    private String getBaseString() {

        //region Initiate variables
        String baseString = "";
        Globals g = Globals.getInstance(); //get instance global
        int serviceCode = g.getServiceCode();//get service code
        //endregion

        // In fuction of the service called, we do a post o get signature calculation
        //region Build BASE_URL_CODIFIED
        if (serviceCode == Constants.PROBLEM_drop_food) {
            Log.d("Service Code", "0, haciendo POST");
            BASE_URL_CODIFIED = Constants.URL_ZAS_retrofit + "wc-api%2Fv2%2Forders%2Feditfromapp%2F" + idOrder;
            baseString = "POST&" + BASE_URL_CODIFIED;
        } else if (serviceCode == Constants.SERVICE_CODE_order_get) {
            Log.d("Service Code", "1, haciendo GET");
            BASE_URL_CODIFIED = Constants.URL_ZAS_retrofit + "wc-api%2Fv2%2Forders";
            baseString = "GET&" + BASE_URL_CODIFIED;
        } else if (serviceCode == Constants.SERVICE_CODE_order_accepted) {
            Log.d("Service Code", "2, haciendo GET");
            BASE_URL_CODIFIED = Constants.URL_ZAS_retrofit + "wc-api%2Fv2%2Forders%2Faccepted";
            baseString = "GET&" + BASE_URL_CODIFIED;
        } else if (serviceCode == Constants.SERVICE_CODE_order_accepted_byuser) {
            Log.d("Service Code", "3, haciendo GET");
            BASE_URL_CODIFIED = Constants.URL_ZAS_retrofit + "wc-api%2Fv2%2Forders%2Faccepted%2F" + idUser;
            baseString = "GET&" + BASE_URL_CODIFIED;
        } else if (serviceCode == Constants.SERVICE_CODE_order_problem) {
            Log.d("Service Code", "4, haciendo GET");
            BASE_URL_CODIFIED = Constants.URL_ZAS_retrofit + "wc-api%2Fv2%2Forders%2Fproblem";
            baseString = "GET&" + BASE_URL_CODIFIED;
        } else if (serviceCode == Constants.SERVICE_CODE_order_problem_byuser) {
            Log.d("Service Code", "5, haciendo GET");
            BASE_URL_CODIFIED = Constants.URL_ZAS_retrofit + "wc-api%2Fv2%2Forders%2Fproblem%2F" + idUser;
            baseString = "GET&" + BASE_URL_CODIFIED;
        } else if (serviceCode == Constants.SERVICE_CODE_order_notes) {
            Log.d("Service Code", "6, haciendo GET");
            BASE_URL_CODIFIED = Constants.URL_ZAS_retrofit + "wc-api%2Fv2%2Forders%2F" + idOrder + "%2Fcomments";
            baseString = "GET&" + BASE_URL_CODIFIED;
        } else if (serviceCode ==  Constants.SERVICE_CODE_order_edit_acceptbymotodriver) {
            Log.d("Service Code", "7, haciendo POST");
            BASE_URL_CODIFIED = Constants.URL_ZAS_retrofit + "wc-api%2Fv2%2Forders%2Fedit_order_acceptbymotodriver%2F" + idOrder;
            baseString = "POST&" + BASE_URL_CODIFIED;
        } else if (serviceCode == Constants.SERVICE_CODE_problem_add_completed) {
            Log.d("Service Code", "8, haciendo POST");
            BASE_URL_CODIFIED = Constants.URL_ZAS_retrofit + "wc-api%2Fv2%2Forders%2Faddincidencia_completed%2F" + idOrder;
            baseString = "POST&" + BASE_URL_CODIFIED;
        } else if (serviceCode == Constants.SERVICE_CODE_problem_add_description) {
            Log.d("Service Code", "9, haciendo POST");
            BASE_URL_CODIFIED = Constants.URL_ZAS_retrofit + "wc-api%2Fv2%2Forders%2Faddincidencia_description%2F" + idOrder;
            baseString = "POST&" + BASE_URL_CODIFIED;
        } else if (serviceCode == Constants.SERVICE_CODE_notes_byuser) {
            Log.d("Service Code", "10, haciendo POST");
            BASE_URL_CODIFIED = Constants.URL_ZAS_retrofit + "wc-api%2Fv2%2Forders%2F" + idOrder + "%2Fcomments";
            baseString = "POST&" + BASE_URL_CODIFIED;
        } else if (serviceCode == Constants.SERVICE_CODE_history) {
            Log.d("Service Code", "11, haciendo GET");
            BASE_URL_CODIFIED = Constants.URL_ZAS_retrofit + "wc-api%2Fv2%2Forders%2Fhistory%2F" + idUser;
            baseString = "GET&" + BASE_URL_CODIFIED;
        } else if (serviceCode == Constants.SERVICE_CODE_order_count) {
            Log.d("Service Code", "12, haciendo GET");
            BASE_URL_CODIFIED = Constants.URL_ZAS_retrofit + "wc-api%2Fv2%2Forders%2Fcount_all";
            baseString = "GET&" + BASE_URL_CODIFIED;
        } else if (serviceCode == Constants.SERVICE_CODE_order_count_byuser) {
            Log.d("Service Code", "13, haciendo GET");
            BASE_URL_CODIFIED = Constants.URL_ZAS_retrofit + "wc-api%2Fv2%2Forders%2Fcount_byIdUser%2F" + idUser;
            baseString = "GET&" + BASE_URL_CODIFIED;
        } else if (serviceCode == Constants.SERVICE_CODE_order_edit_cancelbymotodriver) {
            Log.d("Service Code", "14, haciendo POST");
            BASE_URL_CODIFIED = Constants.URL_ZAS_retrofit + "wc-api%2Fv2%2Forders%2Fedit_order_cancelbymotodriver%2F" + idOrder;
            baseString = "POST&" + BASE_URL_CODIFIED;
        } else if (serviceCode == Constants.SERVICE_CODE_order_accepted_byareadelivery) {
            Log.d("Service Code", "15, haciendo GET");
            BASE_URL_CODIFIED = Constants.URL_ZAS_retrofit + "wc-api%2Fv2%2Forders%2Faccepted_byareadelivery%2F" + strArea;
            baseString = "GET&" + BASE_URL_CODIFIED;
        } else if (serviceCode == Constants.SERVICE_CODE_order_accepted_byuser_byareadelivery) {
            Log.d("Service Code", "16, haciendo GET");
            BASE_URL_CODIFIED = Constants.URL_ZAS_retrofit + "wc-api%2Fv2%2Forders%2Faccepted_byareadelivery%2F" + idUser + "%2F" + strArea;
            baseString = "GET&" + BASE_URL_CODIFIED;
        } else if (serviceCode == Constants.SERVICE_CODE_order_notesclient) {
            Log.d("Service Code", "17, haciendo GET");
            BASE_URL_CODIFIED = Constants.URL_ZAS_retrofit + "wc-api%2Fv2%2Forders%2F" + idOrder + "%2Fnotes";
            baseString = "GET&" + BASE_URL_CODIFIED;
        } else if (serviceCode == Constants.SERVICE_CODE_zone) {
            Log.d("Service Code", "18, haciendo GET");
            BASE_URL_CODIFIED = Constants.URL_ZAS_retrofit + "wp-json%2Fwp%2Fv2%2Fget_areadelivery";
            baseString = "GET&" + BASE_URL_CODIFIED;
        } else if (serviceCode == Constants.SERVICE_CODE_number_max_orders_accepted) {
            Log.d("Service Code", "19, haciendo GET");
            BASE_URL_CODIFIED = Constants.URL_ZAS_retrofit + "wp-json%2Fwp%2Fv2%2Fget_maxnumber_orders_accepted";
            baseString = "GET&" + BASE_URL_CODIFIED;
        } else if (serviceCode == Constants.SERVICE_CODE_number_max_orders_visible) {
            Log.d("Service Code", "20, haciendo GET");
            BASE_URL_CODIFIED = Constants.URL_ZAS_retrofit + "wp-json%2Fwp%2Fv2%2Fget_maxnumber_orders_visible_inlist";
            baseString = "GET&" + BASE_URL_CODIFIED;
        } else if (serviceCode == Constants.SERVICE_CODE_order_count_byareadelivery) {
            Log.d("Service Code", "21, haciendo GET");
            BASE_URL_CODIFIED = Constants.URL_ZAS_retrofit + "wc-api%2Fv2%2Forders%2Fcount_byareadelivery%2F" + strArea;
            baseString = "GET&" + BASE_URL_CODIFIED;
        } else if (serviceCode == Constants.SERVICE_CODE_order_count_byuser_byareadelivery) {
            Log.d("Service Code", "22, haciendo GET");
            BASE_URL_CODIFIED = Constants.URL_ZAS_retrofit + "wc-api%2Fv2%2Forders%2Fcount_byuserbyareadelivery%2F" + idUser + "%2F" + strArea;
            baseString = "GET&" + BASE_URL_CODIFIED;
        } else if (serviceCode == Constants.SERVICE_CODE_order_byidorder) {
            Log.d("Service Code", "23, haciendo GET");
            BASE_URL_CODIFIED = Constants.URL_ZAS_retrofit + "wc-api%2Fv2%2Forders%2F" + idOrder;
            baseString = "GET&" + BASE_URL_CODIFIED;
        }
        else if (serviceCode == Constants.SERVICE_CODE_max_time_orderchangecolor_inMyorders) {
            Log.d("Service Code", "24, haciendo GET");
            BASE_URL_CODIFIED = Constants.URL_ZAS_retrofit + "wp-json%2Fwp%2Fv2%2Fget_driver_max_time";
            baseString = "GET&" + BASE_URL_CODIFIED;
        }
        else if (serviceCode == Constants.SERVICE_CODE_order_saveLocation) {
            Log.d("Service Code", "25, haciendo POST");
            BASE_URL_CODIFIED = Constants.URL_ZAS_retrofit + "wc-api%2Fv2%2Forders%2Fsave_shipping_latlong%2F" + idOrder;
            baseString = "POST&" + BASE_URL_CODIFIED;
        }

        //endregion

        baseString = baseString + "&oauth_consumer_key%3D" + oauth_consumer_key
                + "%26oauth_nonce%3D" + oauth_nonce + "%26oauth_signature_method%3D" + "HMAC-SHA1" +
                "%26oauth_timestamp%3D" + oauth_timestamp;
        return baseString;
    }

    //endregion

    //region Methods to access a BBDD

    //Obtain ALL list of orders
    public void getListaPedidos(final AlRecibirListaDelegate delegate) {
        orderService.list(oauth_consumer_key, oauth_nonce, oauth_signature,
                oauth_signature_method, oauth_timestamp).enqueue(new Callback<OrderSearch>() {
            @Override
            public void onResponse(Call<OrderSearch> call, Response<OrderSearch> response) {
                if (response.isSuccessful()) {
                Log.d("URL getListaPedidos: ", response.raw().request().url().toString());
                Log.d("CODE getListaPedidos: ", String.valueOf(response.code()));

                //When receiving data call the method
                delegate.listaRecibida(response.body());
                delegate.closedialog();
                }
                else {
                    Log.d("error: ", String.valueOf(response.errorBody()));
                    delegate.closedialog();
                }
            }

            @Override
            public void onFailure(Call<OrderSearch> call, Throwable t) {

                // If there is no response we launched the method to indicate the error
                delegate.errorRecibido(t);
                Log.d("Error: ", t.toString());
                delegate.closedialog();
            }
        });
    }

    //Obtain all orders filtered by orderstatus = "rest_has_accepted" , motodriver = 0 and shipping_lines_method_id = "distance_rate"
    public void getOrdersAccepted(final listaRecibidaOrdenada delegate) {
        orderService.list_accepted(oauth_consumer_key, oauth_nonce, oauth_signature,
                oauth_signature_method, oauth_timestamp).enqueue(new Callback<OrderSearch>() {
            @Override
            public void onResponse(Call<OrderSearch> call, Response<OrderSearch> response) {
                if (response.isSuccessful()) {
                Log.d("URL getOrdersAccepted: ", response.raw().request().url().toString());
                Log.d("CODE OrdersAcce : ", String.valueOf(response.code()));

                // Al recibir datos llamamos al método
                delegate.listaRecibidaOrdenada(response.body());
                delegate.closedialog();
                }
                else {
                    Log.d("error: ", String.valueOf(response.errorBody()));
                    delegate.closedialog();
                }
            }

            @Override
            public void onFailure(Call<OrderSearch> call, Throwable t) {

                // En caso que no haya respuesta lanzamos el método para que indique el error
                delegate.errorRecibido(t);
                Log.e("excepcion", "onFailure: ", t);
                Log.d("Error: ", t.toString());
                delegate.closedialog();
            }
        });
    }

    //Obtain all orders filtered by orderstatus different (rest_has_accepted,problem, order_delivered, order_delivered_w_problem ) , with motodriver and shipping_lines_method_id = "distance_rate"
    public void getOrdersByUser(final AlRecibirListaDelegate delegate) {
        orderService.list_byuser(idUser, oauth_consumer_key, oauth_nonce, oauth_signature,
                oauth_signature_method, oauth_timestamp).enqueue(new Callback<OrderSearch>() {
            @Override
            public void onResponse(Call<OrderSearch> call, Response<OrderSearch> response) {
                if (response.isSuccessful()) {
                    Log.d("URL getOrdersAccepted: ", response.raw().request().url().toString());
                    Log.d("CODE OrdersAcce : ", String.valueOf(response.code()));

                    // Al recibir datos llamamos al método
                    delegate.listaRecibida(response.body());
                    delegate.closedialog();
                }
                else {
                    Log.d("error: ", String.valueOf(response.errorBody()));
                    delegate.closedialog();
                }
            }

            @Override
            public void onFailure(Call<OrderSearch> call, Throwable t) {
                // En caso que no haya respuesta lanzamos el método para que indique el error
                delegate.errorRecibido(t);
                Log.d("Error: ", t.toString());
                delegate.closedialog();
            }
        });
    }

    //Obtain all orders filtered by orderstatus = "problem" , motodriver = 0  and shipping_lines_method_id = "distance_rate"
    public void getOrdersProblem(final AlRecibirListaDelegate delegate) {
        orderService.list_problem(oauth_consumer_key, oauth_nonce, oauth_signature,
                oauth_signature_method, oauth_timestamp).enqueue(new Callback<OrderSearch>() {
            @Override
            public void onResponse(Call<OrderSearch> call, Response<OrderSearch> response) {
                if (response.isSuccessful()) {
                Log.d("URL getOrdersProblem: ", response.raw().request().url().toString());
                Log.d("CODE getOrdersProblem: ", String.valueOf(response.code()));

                // Al recibir datos llamamos al método
                delegate.listaRecibida(response.body());
                delegate.closedialog();
                }
                else {
                    Log.d("error: ", String.valueOf(response.errorBody()));
                    delegate.closedialog();

                }
            }

            @Override
            public void onFailure(Call<OrderSearch> call, Throwable t) {

                // En caso que no haya respuesta lanzamos el método para que indique el error
                delegate.errorRecibido(t);
                Log.d("Error: ", t.toString());
                delegate.closedialog();
            }
        });
    }

    //Obtain all orders filtered by orderstatus = "problem" , motodriver = id user of aplication and shipping_lines_method_id = "distance_rate"
    public void getOrdersProblemByIdUser(final AlRecibirListaDelegate delegate) {
        orderService.list_problembyiduser(idUser, oauth_consumer_key, oauth_nonce, oauth_signature,
                oauth_signature_method, oauth_timestamp).enqueue(new Callback<OrderSearch>() {
            @Override
            public void onResponse(Call<OrderSearch> call, Response<OrderSearch> response) {
                if (response.isSuccessful()) {
                    Log.d("URL OrdersProblemById: ", response.raw().request().url().toString());
                    Log.d("CODE OrdProblemById: ", String.valueOf(response.code()));
                    // Al recibir datos llamamos al método
                    delegate.listaRecibida(response.body());
                    delegate.closedialog();
                } else {
                    Log.d("error: ", String.valueOf(response.errorBody()));
                    delegate.closedialog();
                }
            }
            @Override
            public void onFailure(Call<OrderSearch> call, Throwable t) {
                // En caso que no haya respuesta lanzamos el método para que indique el error
                delegate.errorRecibido(t);
                Log.d("Error: ", t.toString());
                delegate.closedialog();
            }
        });
    }

    //Obtain the admin order notes for an order
    public void getOrderNotesByIdOrder(final AlRecibirListaCommentsDelegate delegate) {
        ordernoteService.getlistNoteOrder(idOrder, oauth_consumer_key, oauth_nonce, oauth_signature,
                oauth_signature_method, oauth_timestamp).enqueue(new Callback<OrderNoteGet>() {
            @Override
            public void onResponse(Call<OrderNoteGet> call, Response<OrderNoteGet> response) {
                if (response.isSuccessful()) {
                    Log.d("URL OrdersProblemById: ", response.raw().request().url().toString());
                    Log.d("CODE OrdProblemById: ", String.valueOf(response.code()));

                    delegate.listaRecibida(response.body());
                }
                else
                {
                    Log.d("error: ", String.valueOf(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<OrderNoteGet> call, Throwable t) {
                // En caso que no haya respuesta lanzamos el método para que indique el error
                delegate.errorRecibido(t);
                Log.d("Error: ", t.toString());
            }
        });
    }

    //Obtain the admin order notes for an order ( NOT USED - copy of getOrderNotesByIdOrder)
    public void getOrderNotesClientByIdOrder(final AlRecibirListaCommentsDelegate delegate) {
        ordernoteService.getlistNoteClientOrder(idOrder, oauth_consumer_key, oauth_nonce, oauth_signature,
                oauth_signature_method, oauth_timestamp).enqueue(new Callback<OrderNoteGet>() {
            @Override
            public void onResponse(Call<OrderNoteGet> call, Response<OrderNoteGet> response) {
                if (response.isSuccessful()) {
                    Log.d("URL OrdersProblemById: ", response.raw().request().url().toString());
                    Log.d("CODE OrdProblemById: ", String.valueOf(response.code()));

                    delegate.listaRecibida(response.body());
                }
                else
                {
                    Log.d("error: ", String.valueOf(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<OrderNoteGet> call, Throwable t) {

                // En caso que no haya respuesta lanzamos el método para que indique el error
                delegate.errorRecibido(t);
                Log.d("Error: ", t.toString());
            }
        });
    }

    //Obtain all orders filtered by orderstatus = "order_delivered_w_problem"  and "order_delivered"   by motodriver
    public void getOrderHistorical(final AlRecibirListaDelegate delegate) {
        orderService.list_history(idUser, oauth_consumer_key, oauth_nonce, oauth_signature,
                oauth_signature_method, oauth_timestamp).enqueue(new Callback<OrderSearch>() {
            @Override
            public void onResponse(Call<OrderSearch> call, Response<OrderSearch> response) {
                if (response.isSuccessful()) {
                    Log.d("URL OrdersProblemById: ", response.raw().request().url().toString());
                    Log.d("CODE OrdProblemById: ", String.valueOf(response.code()));

                    delegate.listaRecibida(response.body());
                    delegate.closedialog();
                }
                else
                {
                    Log.d("error: ", String.valueOf(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<OrderSearch> call, Throwable t) {

                // En caso que no haya respuesta lanzamos el método para que indique el error
                delegate.errorRecibido(t);
                delegate.closedialog();
            }
        });
    }

    //Update status of order .Receive New Status , the identifier of motodriver (id user of app) and preferences.
    public void updateStatus(String status, Integer idUserDriver, final SharedPreferences prefs) {

        //region Initiate variables
        Globals g = Globals.getInstance(); //Get instance of global
        int screenCode = g.getScreenCode(); //Get code of screen
        Order order = new Order(); //Init new order
        OrderUpdate orderUpdate = new OrderUpdate();//Init new OrderUpdate
        String motodriver = order.getMotodriver();  //Get id of motodriver from order.
        //endregion

        if (screenCode == Constants.screenCode_detailOrders) {

            Log.d("idUser: ", String.valueOf(idUser));
            Log.d("idOrder: ", String.valueOf(idOrder));
            order.setOrderstatus(Constants.ORDER_STATUS_driver_has_accepted); // Setting the ORDER_STATUS = driver_has_accepted
            order.setMotodriver(String.valueOf(idUser)); // Setting the motodriver
            orderUpdate.setOrder(order); //Update order

            //Update status and motodriver from order . Motodriver accepts the order in "PEDIDOS" page.
            orderService.acceptOrderByMotodriver(idOrder, orderUpdate, oauth_consumer_key, oauth_nonce, oauth_signature,
                    oauth_signature_method, oauth_timestamp).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    //If is correct the update of order
                    if (response.isSuccessful()) {
                        Log.d("URL accept: ", response.raw().request().url().toString());
                        Log.d("CODE_POST: ", String.valueOf(response.code()));
                        //Update numMyOrdersWithouTProblems  in preferences on App.
                        SharedPreferences.Editor editor = prefs.edit();
                        inumMyOrdersWithoutProblems = prefs.getInt("numMyOrdersWithouProblems", 0);
                        inumMyOrdersWithoutProblems = inumMyOrdersWithoutProblems + 1;
                        editor.putInt("numMyOrdersWithouProblems", inumMyOrdersWithoutProblems);
                        editor.commit();
                    } else {
                        Log.d("error: ", String.valueOf(response.errorBody()));
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("Error", "No respuesta");
                }
            });
        } else if (screenCode == Constants.screenCode_detailMyOrders || screenCode == Constants.screenCode_detailMyOrders_disallocateProblem) {

            final String newStatus = orderState(status,screenCode);
            order.setOrderstatus(newStatus);
            order.setMotodriver(String.valueOf(idUserDriver));
            orderUpdate.setOrder(order);

            //Update status and motodriver from order . Motodriver change status of the order in "MIS PEDIDOS" page.
            orderService.updateOrderStatus(idOrder, orderUpdate, oauth_consumer_key, oauth_nonce, oauth_signature,
                    oauth_signature_method, oauth_timestamp).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.d("URL 3_POST: ", response.raw().request().url().toString());
                    Log.d("CODE_POST: ", String.valueOf(response.code()));
                    SharedPreferences.Editor editor = prefs.edit();
                    if(newStatus.equals(Constants.ORDER_STATUS_order_delivered))
                    {
                        //Update numMyOrdersWithouProblems  in preferences on App.
                        inumMyOrdersWithoutProblems = prefs.getInt("numMyOrdersWithouProblems", 0);
                        inumMyOrdersWithoutProblems = inumMyOrdersWithoutProblems - 1;
                        editor.putInt("numMyOrdersWithouProblems",inumMyOrdersWithoutProblems);
                        editor.commit();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("Error", "No respuesta");
                }
            });
        }
        else if(screenCode == Constants.screenCode_detailMyOrders_disallocate){
            Log.d("idOrder: ", String.valueOf(idOrder));
            order.setOrderstatus(Constants.ORDER_STATUS_rest_has_accepted);
            order.setMotodriver("0");// Clean - Setting the motodriver
            orderUpdate.setOrder(order);
            // Update status and motodriver from order . This case of use happens in "MIS PEDIDOS" 's  page   when motodriver want to reject an order accepted by him previously.
            orderService.cancelOrderByMotodriver(idOrder, orderUpdate, oauth_consumer_key, oauth_nonce, oauth_signature,
                    oauth_signature_method, oauth_timestamp).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Log.d("URL 3_POST: ", response.raw().request().url().toString());
                        Log.d("CODE_POST: ", String.valueOf(response.code()));
                        //Update numMyOrdersWithouProblems  in preferences on App.
                        SharedPreferences.Editor editor = prefs.edit();
                        inumMyOrdersWithoutProblems = prefs.getInt("numMyOrdersWithouProblems", 0);
                        inumMyOrdersWithoutProblems = inumMyOrdersWithoutProblems - 1;
                        editor.putInt("numMyOrdersWithouProblems", inumMyOrdersWithoutProblems);
                        editor.commit();
                    } else {
                        Log.d("error: ", String.valueOf(response.errorBody()));
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("Error", "No respuesta");
                }
            });
        }
    }

    //Create "incidencia" in order.
    public void addIncidencia(Boolean isNew,final response bResult) {

        //region Iniatiate order + Set problems + set status problem +  set orderUpdate
        Order order = new Order();
        order.setProblem_details(this.problem_details);
        order.setOrderstatus(Constants.ORDER_STATUS_problem);
        OrderUpdate orderUpdate = new OrderUpdate();
        orderUpdate.setOrder(order);
        //endregion

        if (isNew) {
            //Send Typeofproblem and description of problem
            //'lista_incidencias_problemtype'
            //'lista_incidencias_description'
            //Create incidencia  -- problem_details with problem_type and problems . Change status of order
            orderService.addincidencia_completed(idOrder, orderUpdate, oauth_consumer_key, oauth_nonce, oauth_signature,
                    oauth_signature_method, oauth_timestamp).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Log.d("URLaddincide: ", response.raw().request().url().toString());
                        Log.d("CODE_POST: ", String.valueOf(response.code()));
                        bResult.hascreatecomment(true);
                    } else {
                        Log.d("error: ", String.valueOf(response.errorBody()));
                        bResult.hascreatecomment(false);
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("Error", "No respuesta");
                    bResult.hascreatecomment(false);

                }
            });
        } else {
            //Send position to  add new description and description of problem
            //'lista_incidencias_posicion'
            //'lista_incidencias_description'
            //NOT USED  -- Create description of incidencia
            orderService.addincidencia_description(idOrder, orderUpdate, oauth_consumer_key, oauth_nonce, oauth_signature,
                    oauth_signature_method, oauth_timestamp).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Log.d("URL 3_POST: ", response.raw().request().url().toString());
                        Log.d("CODE_POST: ", String.valueOf(response.code()));
                    } else {
                        Log.d("error: ", String.valueOf(response.errorBody()));
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("Error", "No respuesta");
                }
            });
        }

    }

    //Obtain the total number of orders . Filtered by orderstatus = "rest_has_accepted"  and orderstatus = "problem" , motodriver = 0 user of aplication and shipping_lines_method_id = "distance_rate"
    public void getOrdersCount(final numOrders numOrders) {
        orderService.count(oauth_consumer_key, oauth_nonce, oauth_signature,
                oauth_signature_method, oauth_timestamp).enqueue(new Callback<OrderCount>() {
                 @Override
                 public void onResponse(Call<OrderCount> call, Response<OrderCount> response) {
                     Log.d("URL getOrdersCount: ", response.raw().request().url().toString());
                     Log.d("CODE getOrdersCount: ", String.valueOf(response.code()));
                     numOrders.setText(response.body(), "1");
                 }
                 @Override
                 public void onFailure(Call<OrderCount> call, Throwable t) {
                     Log.d("Error", "No respuesta");
                 }
             }
        );
    }

    //Get the total number of orders by IdUser.
    // Filtered by orderstatus different (rest_has_accepted, order_delivered, order_delivered_w_problem ), motodriver = idUser user of aplication and shipping_lines_method_id = "distance_rate"
    public void getOrdersCountByIdUser(final numOrders numOrders) {
        orderService.countByIdUser(idUser, oauth_consumer_key, oauth_nonce, oauth_signature,
                oauth_signature_method, oauth_timestamp).enqueue(new Callback<OrderCount>() {
            @Override
            public void onResponse(Call<OrderCount> call, Response<OrderCount> response) {

                Log.d("URL OrdersProblemById: ", response.raw().request().url().toString());
                Log.d("CODE OrdProblemById: ", String.valueOf(response.code()));
                numOrders.setText(response.body(), "2");
            }

            @Override
            public void onFailure(Call<OrderCount> call, Throwable t) {
                Log.d("Error", "No respuesta");
            }
        });
    }

    //Obtain the total number of orders .
    // Filtered by orderstatus = "rest_has_accepted"  and orderstatus = "problem" , motodriver = 0 user of aplication and shipping_lines_method_id = "distance_rate" and by Area Delivery
    public void getOrdersCountByAreaDelivery(final numOrders numOrders) {
        orderService.countByAreaDelivery(strArea, oauth_consumer_key, oauth_nonce, oauth_signature,
                oauth_signature_method, oauth_timestamp).enqueue(new Callback<OrderCount>() {
            @Override
            public void onResponse(Call<OrderCount> call, Response<OrderCount> response) {
                if (response.isSuccessful()) {
                    Log.d("URL OrdCountByArDel: ", response.raw().request().url().toString());
                    Log.d("CODE OrdCountByArDel: ", String.valueOf(response.code()));
                    numOrders.setText(response.body(), "1");
                }
                else {
                    Log.d("error: ", String.valueOf(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<OrderCount> call, Throwable t) {
                Log.d("Error", "No respuesta");
            }
        });
    }

    //Get the total number of orders by IdUser.
    // Filtered by orderstatus different (rest_has_accepted, order_delivered, order_delivered_w_problem ), motodriver = idUser user of aplication and shipping_lines_method_id = "distance_rate"and by Area Delivery
    public void getOrdersCountByUserAreaDelivery(final numOrders numOrders) {
        orderService.countByUserByAreaDelivery(idUser,strArea, oauth_consumer_key, oauth_nonce, oauth_signature,
                oauth_signature_method, oauth_timestamp).enqueue(new Callback<OrderCount>() {
            @Override
            public void onResponse(Call<OrderCount> call, Response<OrderCount> response) {
                if (response.isSuccessful()) {
                    Log.d("URL OrdCountByArDel: ", response.raw().request().url().toString());
                    Log.d("CODE OrdCountByArDel: ", String.valueOf(response.code()));
                    numOrders.setText(response.body(), "2");
                }
                else {
                    Log.d("error: ", String.valueOf(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<OrderCount> call, Throwable t) {
                Log.d("Error", "No respuesta");
            }
        });
    }

    //Create a new order note for the given order
    public void addOrderNote(final response bResult) {
        OrderNote order = new OrderNote();
        order.setNote(this.order_note.getNote());
        OrderNoteUpdate orderUpdate = new OrderNoteUpdate();
        orderUpdate.setOrder_note(order);

        ordernoteService.createNoteOrder(idOrder, orderUpdate, oauth_consumer_key, oauth_nonce, oauth_signature,
                oauth_signature_method, oauth_timestamp).enqueue(new Callback<OrderNoteGet>() {
            @Override
            public void onResponse(Call<OrderNoteGet> call, Response<OrderNoteGet> response) {
                if (response.isSuccessful()) {
                    Log.d("URLcreateNote: ", response.raw().request().url().toString());
                    Log.d("CODE_POST: ", String.valueOf(response.code()));
                    bResult.hascreatecomment(true);
                } else {
                    bResult.hascreatecomment(false);
                    Log.d("error: ", String.valueOf(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<OrderNoteGet> call, Throwable t) {
                bResult.hascreatecomment(false);
                Log.d("Error", "No respuesta");
            }
        });
    }

    //Get all orders filtered by orderstatus different (rest_has_accepted,problem, order_delivered, order_delivered_w_problem )
    // and not motodriver  and shipping_lines_method_id = "distance_rate" and by Area Delivery
    public void getOrdersByAreaDelivery(final AlRecibirListaDelegate delegate) {
        orderService.listorders_byarea(strArea, oauth_consumer_key, oauth_nonce, oauth_signature,
                oauth_signature_method, oauth_timestamp).enqueue(new Callback<OrderSearch>() {
            @Override
            public void onResponse(Call<OrderSearch> call, Response<OrderSearch> response) {
                if (response.isSuccessful()) {
                    Log.d("URL getOrdersByArea", response.raw().request().url().toString());
                    Log.d("CODE Orders-Area : ", String.valueOf(response.code()));
                    // Al recibir datos llamamos al método
                    delegate.listaRecibida(response.body());
                    delegate.closedialog();
                }
                else
                {
                    Log.d("error: ", String.valueOf(response.errorBody()));
                    delegate.closedialog();

                }

            }

            @Override
            public void onFailure(Call<OrderSearch> call, Throwable t) {
                // En caso que no haya respuesta lanzamos el método para que indique el error
                delegate.errorRecibido(t);
                Log.d("Error: ", t.toString());
                delegate.closedialog();
            }
        });
    }

    //Get all orders filtered by orderstatus different (rest_has_accepted,problem, order_delivered, order_delivered_w_problem )
    // and by motodriver = id user of aplication and shipping_lines_method_id = "distance_rate" and by Area Delivery
    public void getOrdersByUserAndArea(final AlRecibirListaDelegate delegate) {
        orderService.listorders_byuser_byarea(idUser, strArea, oauth_consumer_key, oauth_nonce, oauth_signature,
                oauth_signature_method, oauth_timestamp).enqueue(new Callback<OrderSearch>() {
            @Override
            public void onResponse(Call<OrderSearch> call, Response<OrderSearch> response) {
                if (response.isSuccessful()) {
                    Log.d("URL getOrdersAccepted: ", response.raw().request().url().toString());
                    Log.d("CODE OrdersAcce : ", String.valueOf(response.code()));

                    // Al recibir datos llamamos al método
                    delegate.listaRecibida(response.body());
                    delegate.closedialog();
                }
                else
                {
                    Log.d("error: ", String.valueOf(response.errorBody()));
                    delegate.closedialog();
                }
            }

            @Override
            public void onFailure(Call<OrderSearch> call, Throwable t) {
                // En caso que no haya respuesta lanzamos el método para que indique el error
                delegate.errorRecibido(t);
                Log.d("Error: ", t.toString());
                delegate.closedialog();
            }
        });
    }

    //Get all area delivery from BBDD. You create area delivery in :  http://zascomidaentuboca.es/wp-admin/edit.php?post_type=reparto
    public void getareaDelivery(final AlRecibirListaDelegate delegate) {
        areaDeliveryService.get_areadelivery().enqueue(new Callback<ArrayList<Reparto>>() {
            @Override
            public void onResponse(Call<ArrayList<Reparto>> call, Response<ArrayList<Reparto>> response) {
                if (response.isSuccessful()) {
                    Log.d("URL getareaDelivery: ", response.raw().request().url().toString());
                    Log.d("CODE getareaDelivery : ", String.valueOf(response.code()));

                    // Al recibir datos llamamos al método
                    delegate.areaDeliveryRecived(response.body());
                    delegate.closedialog();
                } else {
                    Log.d("error: ", String.valueOf(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Reparto>> call, Throwable t) {
                // En caso que no haya respuesta lanzamos el método para que indique el error
                delegate.errorRecibido(t);
                Log.d("Error: ", t.toString());
                delegate.closedialog();
            }



        });
    }

    //Get number that especify the maximum number of orders that can be accepted by the user trhough app.
    //It can modify in website. You have tab "Opt" is options. An then tab "App Settings"
    public void get_maxnumber_orders_accepted(final AlRecibirListaDelegate delegate) {
        orderService.get_maxnumber_orders_accepted().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.d("URL get_maxnumber: ", response.raw().request().url().toString());
                    Log.d("CODE get_maxnumber : ", String.valueOf(response.code()));

                    // Al recibir datos llamamos al método
                    delegate.stringReceived("get_maxnumber_orders_accepted",response.body());
                    delegate.closedialog();
                } else {
                    Log.d("error: ", String.valueOf(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // En caso que no haya respuesta lanzamos el método para que indique el error
                delegate.errorRecibido(t);
                Log.d("Error: ", t.toString());
                delegate.closedialog();
            }
        });

    }

    //Get number that especify the maximum number of orders (cards of orders) that can be visibles in ths screen "Pedidos".
    //It can modify in website. You have tab "Opt" is options. An then tab "App Settings"
    public void get_maxnumber_orders_visible(final AlRecibirListaDelegate delegate) {
        orderService.get_maxnumber_orders_visible_inlist().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.d("URL get_maxnumber: ", response.raw().request().url().toString());
                    Log.d("CODE get_maxnumber : ", String.valueOf(response.code()));

                    // Al recibir datos llamamos al método
                    delegate.stringReceived("get_maxnumber_orders_visible",response.body());
                    delegate.closedialog();
                } else {
                    Log.d("error: ", String.valueOf(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // En caso que no haya respuesta lanzamos el método para que indique el error
                delegate.errorRecibido(t);
                Log.d("Error: ", t.toString());
                delegate.closedialog();
            }
        });

    }

    //Get number  that especify the maximum minutes that order (in "MIS PEDIDOS" screen) will not be black color (This means that is prioritary).
    //It can modify in website. You have tab "Opt" is options. An then tab "App Settings"
    public void get_maxtime(final AlRecibirListaDelegate delegate) {
        orderService.get_maxtime_inlist().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.d("URL get_maxtime: ", response.raw().request().url().toString());
                    Log.d("CODE get_max_time : ", String.valueOf(response.code()));

                    // Al recibir datos llamamos al método
                    delegate.stringReceived("get_maxtime", response.body());
                    delegate.closedialog();
                } else {
                    delegate.notMaxTime();
                    Log.d("error: ", String.valueOf(response.errorBody()));
                    Log.d("URL get_maxtime: ", response.raw().request().url().toString());
                    Log.d("CODE get_max_time : ", String.valueOf(response.code()));

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // En caso que no haya respuesta lanzamos el método para que indique el error
                delegate.errorRecibido(t);
                Log.d("Error: ", t.toString());
                delegate.closedialog();
            }
        });

    }

    //Web Configurator
    public void get_webconfiguration(final WebConfigurationDelegate delegate) {
        getConfigurationWeb.get_webconfigurator_byapp().enqueue(new Callback<WebConfigurator>() {
            @Override
            public void onResponse(Call<WebConfigurator> call, Response<WebConfigurator> response) {
                if (response.isSuccessful()) {
                    Log.d("URL get_webconfig: ", response.raw().request().url().toString());
                    Log.d("CODE get_webconfig : ", String.valueOf(response.code()));

                    // Al recibir datos llamamos al método
                    delegate.webConfigRecibido(response.body());
                    delegate.closedialog();
                } else {
                    Log.d("error: ", String.valueOf(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<WebConfigurator> call, Throwable t) {
                // En caso que no haya respuesta lanzamos el método para que indique el error
                delegate.errorRecibido(t);
                Log.d("Error: ", t.toString());
                delegate.closedialog();
            }
        });

    }

    //Get specific order by identifier.
    public void get_orderById(final AlRecibirListaDelegate delegate) {
        orderService.getorderById(idOrder, oauth_consumer_key, oauth_nonce, oauth_signature,
                oauth_signature_method, oauth_timestamp).enqueue(new Callback<OrderUpdate>() {
            @Override
            public void onResponse(Call<OrderUpdate> call, Response<OrderUpdate> response) {
                if (response.isSuccessful()) {
                    Log.d("URL getOrdersAccepted: ", response.raw().request().url().toString());
                    Log.d("CODE OrdersAcce : ", String.valueOf(response.code()));

                    System.out.println("URL IMPORTANT: " + response.raw().request().url().toString());

                    // Al recibir datos llamamos al método
                    delegate.orderReceived(response.body());
                    delegate.closedialog();
                } else {
                    Log.d("error: ", String.valueOf(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<OrderUpdate> call, Throwable t) {
                // En caso que no haya respuesta lanzamos el método para que indique el error
                delegate.errorRecibido(t);
                Log.d("Error: ", t.toString());
                delegate.closedialog();
            }
        });
    }

    //Update post meta Shipping_latlong - Add latitud and longitude from app by motodriver (Button Save location)
    public void saveLocation(final response bResult ,String locationClient) {

        //region Iniatiate order +  set orderUpdate
        Order order = new Order();
        order.setShipping_latlong(locationClient);
        final OrderUpdate orderUpdate = new OrderUpdate();
        orderUpdate.setOrder(order);
        //endregion

        orderService.saveLocation(idOrder, orderUpdate, oauth_consumer_key, oauth_nonce, oauth_signature,
                oauth_signature_method, oauth_timestamp).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //If is correct the update of order
                if (response.isSuccessful()) {
                    Log.d("URL accept: ", response.raw().request().url().toString());
                    Log.d("CODE_POST: ", String.valueOf(response.code()));
                    bResult.hascreatecomment(true);
                } else {
                    Log.d("error: ", String.valueOf(response.errorBody()));
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Error", "No respuesta");
            }
        });
    }

    //endregion

    //Method that provide us the next status of order depending by status actual
    private String orderState(String orderstatus,int screenCode) {

        String newStatus = "";

        switch (orderstatus) {
            case Constants.ORDER_STATUS_driver_has_accepted:
                newStatus = Constants.ORDER_STATUS_driver_in_rest;
                break;
            case Constants.ORDER_STATUS_driver_in_rest:
                newStatus = Constants.ORDER_STATUS_driver_on_road;
                break;
            case Constants.ORDER_STATUS_driver_on_road:
                newStatus = Constants.ORDER_STATUS_order_delivered;
                break;
            case Constants.ORDER_STATUS_problem:
                if(screenCode == 1)
                    newStatus = Constants.ORDER_STATUS_order_delivered_w_problem;
                else
                    newStatus = Constants.ORDER_STATUS_problem;
                break;
            default:
                orderstatus = orderstatus;
                break;

        }
        return newStatus;
    }

    //region  INTERFACES and their response methods.
    public interface AlRecibirListaDelegate {

        void listaRecibida(OrderSearch body);

        void errorRecibido(Object error);

        void closedialog();

        void arrayRecibido(ArrayList<String> body);

        void areaDeliveryRecived(ArrayList<Reparto> body);

        void stringReceived(String namefunction, String body);

        void orderReceived(OrderUpdate order);

        void notMaxTime ();
    }

    public interface WebConfigurationDelegate {
        void webConfigRecibido(WebConfigurator body);
        void closedialog();
        void errorRecibido(Object error);
    }

    public interface AlRecibirListaCommentsDelegate {

        void listaRecibida(OrderNoteGet body);

        void errorRecibido(Object error);

    }

    public interface listaRecibidaOrdenada {

        void listaRecibidaOrdenada(OrderSearch body);

        void errorRecibido(Object error);

        void closedialog( );

    }

    public interface numOrders {
        void setText(OrderCount body, String element);
    }

    public interface response{
        void hascreatecomment(Boolean bResult);
    }
    //endregion
}
