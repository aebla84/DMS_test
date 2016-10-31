package dms.deideas.zas.Services.Retrofit;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import dms.deideas.zas.Constants;
import dms.deideas.zas.Globals;
import dms.deideas.zas.Model.Devices;
import dms.deideas.zas.Model.Incidencia;
import dms.deideas.zas.Model.Order;
import dms.deideas.zas.Model.OrderNote;
import dms.deideas.zas.Model.Reparto;
import dms.deideas.zas.R;
import dms.deideas.zas.Services.AreaDeliveryService;
import dms.deideas.zas.Services.DevicesService;
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

    public static final String BASE_URL = Constants.URL_ZAS;
    public static String BASE_URL_CODIFIED = "";
    private final Retrofit retrofit;
    private final OrderService orderService;
    private final OrderNoteService ordernoteService;
    private final AreaDeliveryService areaDeliveryService;
    private final int intResult = 0;
    private String oauth_consumer_key = Constants.OAUTH_CONSUMER_KEY;
    private String oauth_consumer_secret = Constants.OAUTH_CONSUMER_SECRET;
    private String oauth_signature_method = "HMAC-SHA1";
    private String oauth_timestamp = null;
    private String oauth_nonce = null;
    private String oauth_signature = null;
    private int idUser = 0;
    private String strArea = "";
    private int idOrder = 0;
    private Incidencia problem_details;
    private OrderNote order_note;
    private List<String> lstIncidencias;
    private Boolean bResult = false;

    private Integer inumMyOrdersWithouProblems = 0;
    private SharedPreferences prefs;


    public RetrofitDelegateHelper() throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        // Initiate params
        oauth_nonce = getNonce();
        oauth_timestamp = getTimestamp();
        oauth_signature = getSignature(oauth_consumer_secret);

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        orderService = retrofit.create(OrderService.class);
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
        ordernoteService = retrofit.create(OrderNoteService.class);
        areaDeliveryService = retrofit.create(AreaDeliveryService.class);
    }

    // Método para calcular el nonce de OAuth1.0
    private String getNonce() {

        String oauthNonce = UUID.randomUUID().toString().replaceAll("-", "");
        return oauthNonce;
    }

    // Método que devuelve el current time del sistema en segundos desde 1-1-1970
    private static String getTimestamp() {
        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();
        return ts;
    }

    // Método para calcular el signature
    public String getSignature(String oauth_consumer_secret) throws
            UnsupportedEncodingException, NoSuchAlgorithmException,
            InvalidKeyException {
        String baseString = getBaseString();
        SecretKeySpec key = new SecretKeySpec((oauth_consumer_secret).getBytes("UTF-8"), "HMAC-SHA1");
        Mac mac = Mac.getInstance("HMAC-SHA1");
        mac.init(key);

        byte[] bytes = mac.doFinal(baseString.getBytes("UTF-8"));

        // Codificamos en Base64, realizamos un trim y retornamos
        return new String(Base64.encodeToString(bytes, 0).trim());
    }

    public void getListaPedidos(final AlRecibirListaDelegate delegate) {
        orderService.list(oauth_consumer_key, oauth_nonce, oauth_signature,
                oauth_signature_method, oauth_timestamp).enqueue(new Callback<OrderSearch>() {
            @Override
            public void onResponse(Call<OrderSearch> call, Response<OrderSearch> response) {
                if (response.isSuccessful()) {
                Log.d("URL getListaPedidos: ", response.raw().request().url().toString());
                Log.d("CODE getListaPedidos: ", String.valueOf(response.code()));

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

    public void updateStatus(String status, Integer idUserDriver, final SharedPreferences prefs) {


        Globals g = Globals.getInstance();
        int screenCode = g.getScreenCode();

        Order order = new Order();
        OrderUpdate orderUpdate = new OrderUpdate();

        String motodriver = order.getMotodriver();

        if (screenCode == 0) {

            Log.d("idUser: ", String.valueOf(idUser));
            Log.d("idOrder: ", String.valueOf(idOrder));
            order.setOrderstatus(Constants.ORDER_STATUS_driver_has_accepted);
            // Setting the motodriver
            order.setMotodriver(String.valueOf(idUser));
            orderUpdate.setOrder(order);
            orderService.acceptOrderByMotodriver(idOrder, orderUpdate, oauth_consumer_key, oauth_nonce, oauth_signature,
                    oauth_signature_method, oauth_timestamp).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Log.d("URL accept: ", response.raw().request().url().toString());
                        Log.d("CODE_POST: ", String.valueOf(response.code()));
                        SharedPreferences.Editor editor = prefs.edit();
                        inumMyOrdersWithouProblems = prefs.getInt("numMyOrdersWithouProblems", 0);
                        inumMyOrdersWithouProblems = inumMyOrdersWithouProblems + 1;
                        editor.putInt("numMyOrdersWithouProblems", inumMyOrdersWithouProblems);
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
        } else if (screenCode == 1 || screenCode == 3) {
            final String newStatus = orderState(status,screenCode);
            order.setOrderstatus(newStatus);
            order.setMotodriver(String.valueOf(idUserDriver));
            orderUpdate.setOrder(order);
            orderService.updateOrderStatus(idOrder, orderUpdate, oauth_consumer_key, oauth_nonce, oauth_signature,
                    oauth_signature_method, oauth_timestamp).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.d("URL 3_POST: ", response.raw().request().url().toString());
                    Log.d("CODE_POST: ", String.valueOf(response.code()));
                    SharedPreferences.Editor editor = prefs.edit();
                    if(newStatus.equals(Constants.ORDER_STATUS_order_delivered))
                    {
                        inumMyOrdersWithouProblems = prefs.getInt("numMyOrdersWithouProblems", 0);
                        inumMyOrdersWithouProblems = inumMyOrdersWithouProblems - 1;
                        editor.putInt("numMyOrdersWithouProblems",inumMyOrdersWithouProblems);
                        editor.commit();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("Error", "No respuesta");
                }
            });
        }
        else if(screenCode == 2){
            Log.d("idOrder: ", String.valueOf(idOrder));
            order.setOrderstatus(Constants.ORDER_STATUS_rest_has_accepted);
            // Setting the motodriver
            order.setMotodriver("0");
            orderUpdate.setOrder(order);
            orderService.cancelOrderByMotodriver(idOrder, orderUpdate, oauth_consumer_key, oauth_nonce, oauth_signature,
                    oauth_signature_method, oauth_timestamp).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Log.d("URL 3_POST: ", response.raw().request().url().toString());
                        Log.d("CODE_POST: ", String.valueOf(response.code()));
                        SharedPreferences.Editor editor = prefs.edit();
                        inumMyOrdersWithouProblems = prefs.getInt("numMyOrdersWithouProblems", 0);
                        inumMyOrdersWithouProblems = inumMyOrdersWithouProblems - 1;
                        editor.putInt("numMyOrdersWithouProblems", inumMyOrdersWithouProblems);
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

    public void addIncidencia(Boolean isNew,final response bResult) {
        Order order = new Order();
        order.setProblem_details(this.problem_details);
        order.setOrderstatus(Constants.ORDER_STATUS_problem);
        OrderUpdate orderUpdate = new OrderUpdate();
        orderUpdate.setOrder(order);

        if (isNew) {
            //Send Typeofproblem and description of problem
            //'lista_incidencias_problemtype'
            //'lista_incidencias_description'
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
                if(screenCode ==1)
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

    //Method that build baseString depending getServiceCode
    private String getBaseString() {
        String baseString = "";
        Globals g = Globals.getInstance();
        int serviceCode = g.getServiceCode();

       /* switch (g.getServiceCode()){
            case  Constants.SERVICE_CODE_order_edit:
                Log.d("Service Code", "0, haciendo POST");
                BASE_URL_CODIFIED = "http%3A%2F%2Fzascomidaentuboca.es%2Fwc-api%2Fv2%2Forders%2Feditfromapp%2F" + idOrder;
                baseString = "POST&" + BASE_URL_CODIFIED ;
                break;
            case Constants.SERVICE_CODE_order_get:
                Log.d("Service Code", "1, haciendo GET");
                BASE_URL_CODIFIED = "http%3A%2F%2Fzascomidaentuboca.es%2Fwc-api%2Fv2%2Forders";
                baseString = "GET&" + BASE_URL_CODIFIED ;
                break;
        }*/

        // In fuction of the service called, we do a post o get signature calculation
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
        } else if (serviceCode == 3) {
            Log.d("Service Code", "3, haciendo GET");
            BASE_URL_CODIFIED = Constants.URL_ZAS_retrofit + "wc-api%2Fv2%2Forders%2Faccepted%2F" + idUser;
            baseString = "GET&" + BASE_URL_CODIFIED;
        } else if (serviceCode == 4) {
            Log.d("Service Code", "4, haciendo GET");
            BASE_URL_CODIFIED = Constants.URL_ZAS_retrofit + "wc-api%2Fv2%2Forders%2Fproblem";
            baseString = "GET&" + BASE_URL_CODIFIED;
        } else if (serviceCode == 5) {
            Log.d("Service Code", "5, haciendo GET");
            BASE_URL_CODIFIED = Constants.URL_ZAS_retrofit + "wc-api%2Fv2%2Forders%2Fproblem%2F" + idUser;

            baseString = "GET&" + BASE_URL_CODIFIED;
        } else if (serviceCode == 6) {
            Log.d("Service Code", "6, haciendo GET");
            BASE_URL_CODIFIED = Constants.URL_ZAS_retrofit + "wc-api%2Fv2%2Forders%2F" + idOrder + "%2Fcomments";
            baseString = "GET&" + BASE_URL_CODIFIED;
        } else if (serviceCode == 7) {
            Log.d("Service Code", "7, haciendo POST");
            BASE_URL_CODIFIED = Constants.URL_ZAS_retrofit + "wc-api%2Fv2%2Forders%2Fedit_order_acceptbymotodriver%2F" + idOrder;
            baseString = "POST&" + BASE_URL_CODIFIED;

        } else if (serviceCode == 8) {
            Log.d("Service Code", "8, haciendo POST");
            BASE_URL_CODIFIED = Constants.URL_ZAS_retrofit + "wc-api%2Fv2%2Forders%2Faddincidencia_completed%2F" + idOrder;
            baseString = "POST&" + BASE_URL_CODIFIED;
        } else if (serviceCode == 9) {
            Log.d("Service Code", "9, haciendo POST");
            BASE_URL_CODIFIED = Constants.URL_ZAS_retrofit + "wc-api%2Fv2%2Forders%2Faddincidencia_description%2F" + idOrder;
            baseString = "POST&" + BASE_URL_CODIFIED;
        } else if (serviceCode == 10) {
            Log.d("Service Code", "10, haciendo POST");
            BASE_URL_CODIFIED = Constants.URL_ZAS_retrofit + "wc-api%2Fv2%2Forders%2F" + idOrder + "%2Fcomments";
            baseString = "POST&" + BASE_URL_CODIFIED;
        } else if (serviceCode == 11) {
            Log.d("Service Code", "11, haciendo GET");
            BASE_URL_CODIFIED = Constants.URL_ZAS_retrofit + "wc-api%2Fv2%2Forders%2Fhistory%2F" + idUser;
            baseString = "GET&" + BASE_URL_CODIFIED;
        } else if (serviceCode == 12) {
            Log.d("Service Code", "12, haciendo GET");
            BASE_URL_CODIFIED = Constants.URL_ZAS_retrofit + "wc-api%2Fv2%2Forders%2Fcount_all";
            baseString = "GET&" + BASE_URL_CODIFIED;
        } else if (serviceCode == 13) {
            Log.d("Service Code", "13, haciendo GET");
            BASE_URL_CODIFIED = Constants.URL_ZAS_retrofit + "wc-api%2Fv2%2Forders%2Fcount_byIdUser%2F" + idUser;
            baseString = "GET&" + BASE_URL_CODIFIED;
        } else if (serviceCode == 14) {
            Log.d("Service Code", "14, haciendo POST");
            BASE_URL_CODIFIED = Constants.URL_ZAS_retrofit + "wc-api%2Fv2%2Forders%2Fedit_order_cancelbymotodriver%2F" + idOrder;
            baseString = "POST&" + BASE_URL_CODIFIED;
        } else if (serviceCode == 15) {
            Log.d("Service Code", "15, haciendo GET");
            BASE_URL_CODIFIED = Constants.URL_ZAS_retrofit + "wc-api%2Fv2%2Forders%2Faccepted_byareadelivery%2F" + strArea;
            baseString = "GET&" + BASE_URL_CODIFIED;
        } else if (serviceCode == 16) {
            Log.d("Service Code", "16, haciendo GET");
            BASE_URL_CODIFIED = Constants.URL_ZAS_retrofit + "wc-api%2Fv2%2Forders%2Faccepted_byareadelivery%2F" + idUser + "%2F" + strArea;
            baseString = "GET&" + BASE_URL_CODIFIED;
        } else if (serviceCode == 17) {
            Log.d("Service Code", "17, haciendo GET");
            BASE_URL_CODIFIED = Constants.URL_ZAS_retrofit + "wc-api%2Fv2%2Forders%2F" + idOrder + "%2Fnotes";
            baseString = "GET&" + BASE_URL_CODIFIED;
        } else if (serviceCode == 18) {
            Log.d("Service Code", "18, haciendo GET");
            BASE_URL_CODIFIED = Constants.URL_ZAS_retrofit + "wp-json%2Fwp%2Fv2%2Fget_areadelivery";
            baseString = "GET&" + BASE_URL_CODIFIED;
        } else if (serviceCode == 19) {
            Log.d("Service Code", "19, haciendo GET");
            BASE_URL_CODIFIED = Constants.URL_ZAS_retrofit + "wp-json%2Fwp%2Fv2%2Fget_maxnumber_orders_accepted";
            baseString = "GET&" + BASE_URL_CODIFIED;
        } else if (serviceCode == 20) {
            Log.d("Service Code", "20, haciendo GET");
            BASE_URL_CODIFIED = Constants.URL_ZAS_retrofit + "wp-json%2Fwp%2Fv2%2Fget_maxnumber_orders_visible_inlist";
            baseString = "GET&" + BASE_URL_CODIFIED;
        } else if (serviceCode == 21) {
            Log.d("Service Code", "21, haciendo GET");
            BASE_URL_CODIFIED = Constants.URL_ZAS_retrofit + "wc-api%2Fv2%2Forders%2Fcount_byareadelivery%2F" + strArea;
            baseString = "GET&" + BASE_URL_CODIFIED;
        } else if (serviceCode == 22) {
            Log.d("Service Code", "22, haciendo GET");
            BASE_URL_CODIFIED = Constants.URL_ZAS_retrofit + "wc-api%2Fv2%2Forders%2Fcount_byuserbyareadelivery%2F" + idUser + "%2F" + strArea;
            baseString = "GET&" + BASE_URL_CODIFIED;
        } else if (serviceCode == 23) {
            Log.d("Service Code", "23, haciendo GET");
            BASE_URL_CODIFIED = Constants.URL_ZAS_retrofit + "wc-api%2Fv2%2Forders%2F" + idOrder;
            baseString = "GET&" + BASE_URL_CODIFIED;
        }

        baseString = baseString + "&oauth_consumer_key%3D" + oauth_consumer_key
                + "%26oauth_nonce%3D" + oauth_nonce + "%26oauth_signature_method%3D" + "HMAC-SHA1" +
                "%26oauth_timestamp%3D" + oauth_timestamp;
        return baseString;
    }



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
}
