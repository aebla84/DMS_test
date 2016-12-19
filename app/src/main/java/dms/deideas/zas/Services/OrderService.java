package dms.deideas.zas.Services;

import dms.deideas.zas.Model.HomeResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Jordi on 29/05/2016.
 */
public interface OrderService {

    // ServiceCode = 0;
    @POST("wc-api/v2/orders/editfromapp/{id}")
    Call<ResponseBody> updateOrderStatus(@Path("id") int idOrder,
                                         @Body() OrderUpdate order,
                                         @Query("oauth_consumer_key") String oauth_consumer_key,
                                         @Query("oauth_nonce") String oauth_nonce,
                                         @Query("oauth_signature") String oauth_signature,
                                         @Query("oauth_signature_method") String oauth_signature_method,
                                         @Query("oauth_timestamp") String oauth_timestamp);

    // ServiceCode = 1;
    @GET("wc-api/v2/orders?")
    Call<OrderSearch> list(@Query("oauth_consumer_key") String oauth_consumer_key,
                           @Query("oauth_nonce") String oauth_nonce,
                           @Query("oauth_signature") String oauth_signature,
                           @Query("oauth_signature_method") String oauth_signature_method,
                           @Query("oauth_timestamp") String oauth_timestamp);

    // ServiceCode = 2;
    @GET("wc-api/v2/orders/accepted?")
    Call<OrderSearch> list_accepted(@Query("oauth_consumer_key") String oauth_consumer_key,
                                    @Query("oauth_nonce") String oauth_nonce,
                                    @Query("oauth_signature") String oauth_signature,
                                    @Query("oauth_signature_method") String oauth_signature_method,
                                    @Query("oauth_timestamp") String oauth_timestamp);

    // ServiceCode = 3;
    @GET("wc-api/v2/orders/accepted/{id}")
    Call<OrderSearch> list_byuser(@Path("id") int id,
                                  @Query("oauth_consumer_key") String oauth_consumer_key,
                                  @Query("oauth_nonce") String oauth_nonce,
                                  @Query("oauth_signature") String oauth_signature,
                                  @Query("oauth_signature_method") String oauth_signature_method,
                                  @Query("oauth_timestamp") String oauth_timestamp);

    // ServiceCode = 4;
    @GET("wc-api/v2/orders/problem?")
    Call<OrderSearch> list_problem(@Query("oauth_consumer_key") String oauth_consumer_key,
                                   @Query("oauth_nonce") String oauth_nonce,
                                   @Query("oauth_signature") String oauth_signature,
                                   @Query("oauth_signature_method") String oauth_signature_method,
                                   @Query("oauth_timestamp") String oauth_timestamp);

    // ServiceCode = 5;
    @GET("wc-api/v2/orders/problem/{id}")
    Call<OrderSearch> list_problembyiduser(@Path("id") int id,
                                           @Query("oauth_consumer_key") String oauth_consumer_key,
                                           @Query("oauth_nonce") String oauth_nonce,
                                           @Query("oauth_signature") String oauth_signature,
                                           @Query("oauth_signature_method") String oauth_signature_method,
                                           @Query("oauth_timestamp") String oauth_timestamp);

    // ServiceCode = 7;
    @POST("wc-api/v2/orders/edit_order_acceptbymotodriver/{id}")
    Call<ResponseBody> acceptOrderByMotodriver(@Path("id") int idOrder,
                                               @Body() OrderUpdate order,
                                               @Query("oauth_consumer_key") String oauth_consumer_key,
                                               @Query("oauth_nonce") String oauth_nonce,
                                               @Query("oauth_signature") String oauth_signature,
                                               @Query("oauth_signature_method") String oauth_signature_method,
                                               @Query("oauth_timestamp") String oauth_timestamp);

    // ServiceCode = 8;
    @POST("wc-api/v2/orders/addincidencia_completed/{id}")
    Call<ResponseBody> addincidencia_completed(@Path("id") int idOrder,
                                               @Body() OrderUpdate order,
                                               @Query("oauth_consumer_key") String oauth_consumer_key,
                                               @Query("oauth_nonce") String oauth_nonce,
                                               @Query("oauth_signature") String oauth_signature,
                                               @Query("oauth_signature_method") String oauth_signature_method,
                                               @Query("oauth_timestamp") String oauth_timestamp);

    // ServiceCode = 9;
    @POST("wc-api/v2/orders/addincidencia_description/{id}")
    Call<ResponseBody> addincidencia_description(@Path("id") int idOrder,
                                                 @Body() OrderUpdate order,
                                                 @Query("oauth_consumer_key") String oauth_consumer_key,
                                                 @Query("oauth_nonce") String oauth_nonce,
                                                 @Query("oauth_signature") String oauth_signature,
                                                 @Query("oauth_signature_method") String oauth_signature_method,
                                                 @Query("oauth_timestamp") String oauth_timestamp);

    // ServiceCode = 11;
    @GET("wc-api/v2/orders/history/{id}")
    Call<OrderSearch> list_history(@Path("id") int id,
                                   @Query("oauth_consumer_key") String oauth_consumer_key,
                                   @Query("oauth_nonce") String oauth_nonce,
                                   @Query("oauth_signature") String oauth_signature,
                                   @Query("oauth_signature_method") String oauth_signature_method,
                                   @Query("oauth_timestamp") String oauth_timestamp);



    // ServiceCode = 14;
    @POST("wc-api/v2/orders/edit_order_cancelbymotodriver/{id}")
    Call<ResponseBody> cancelOrderByMotodriver(@Path("id") int idOrder,
                                               @Body() OrderUpdate order,
                                               @Query("oauth_consumer_key") String oauth_consumer_key,
                                               @Query("oauth_nonce") String oauth_nonce,
                                               @Query("oauth_signature") String oauth_signature,
                                               @Query("oauth_signature_method") String oauth_signature_method,
                                               @Query("oauth_timestamp") String oauth_timestamp);

    // ServiceCode = 15;
    @GET("wc-api/v2/orders/accepted_byareadelivery/{area}")
    Call<OrderSearch> listorders_byarea(@Path("area") String strArea,
                                        @Query("oauth_consumer_key") String oauth_consumer_key,
                                        @Query("oauth_nonce") String oauth_nonce,
                                        @Query("oauth_signature") String oauth_signature,
                                        @Query("oauth_signature_method") String oauth_signature_method,
                                        @Query("oauth_timestamp") String oauth_timestamp);

    // ServiceCode = 16;
    @GET("wc-api/v2/orders/accepted_byareadelivery/{id}/{area}")
    Call<OrderSearch> listorders_byuser_byarea(@Path("id") int id,
                                               @Path("area") String strArea,
                                               @Query("oauth_consumer_key") String oauth_consumer_key,
                                               @Query("oauth_nonce") String oauth_nonce,
                                               @Query("oauth_signature") String oauth_signature,
                                               @Query("oauth_signature_method") String oauth_signature_method,
                                               @Query("oauth_timestamp") String oauth_timestamp);


    // ServiceCode = 22;
    @GET("wc-api/v2/orders/count_byuserbyareadelivery/{id}/{area}")
    Call<HomeResponse> countOrders_tohomepage(@Path("id") int id,
                                               @Path("area") String strArea,
                                               @Query("oauth_consumer_key") String oauth_consumer_key,
                                               @Query("oauth_nonce") String oauth_nonce,
                                               @Query("oauth_signature") String oauth_signature,
                                               @Query("oauth_signature_method") String oauth_signature_method,
                                               @Query("oauth_timestamp") String oauth_timestamp);


    // ServiceCode = 23;
    @GET("wc-api/v2/orders/{id}")
    Call<OrderUpdate> getorderById(@Path("id") int id,
                                   @Query("oauth_consumer_key") String oauth_consumer_key,
                                   @Query("oauth_nonce") String oauth_nonce,
                                   @Query("oauth_signature") String oauth_signature,
                                   @Query("oauth_signature_method") String oauth_signature_method,
                                   @Query("oauth_timestamp") String oauth_timestamp);



    // ServiceCode = 25;
    @POST("wc-api/v2/orders/save_shipping_latlong/{id}")
    Call<ResponseBody> saveLocation(@Path("id") int idOrder,
                                    @Body() OrderUpdate order,
                                    @Query("oauth_consumer_key") String oauth_consumer_key,
                                    @Query("oauth_nonce") String oauth_nonce,
                                    @Query("oauth_signature") String oauth_signature,
                                    @Query("oauth_signature_method") String oauth_signature_method,
                                    @Query("oauth_timestamp") String oauth_timestamp);


    // ServiceCode = 27;
    @GET("wc-api/v2/orders/accepted_byareadelivery_bymaxdistance/{area}/{kilometers}")
    Call<OrderSearch> listorders_byarea_bymaxkilometers(@Path("area")String strArea,
                                                       @Path("kilometers") String strKilometers,
                                                       @Query("oauth_consumer_key") String oauth_consumer_key,
                                                       @Query("oauth_nonce") String oauth_nonce,
                                                       @Query("oauth_signature") String oauth_signature,
                                                       @Query("oauth_signature_method") String oauth_signature_method,
                                                       @Query("oauth_timestamp") String oauth_timestamp);

    // ServiceCode = 28;
    @GET("wc-api/v2/orders/count_byuserbyareadelivery_bymaxkilometers/{id}/{area}/{kilometers}")
    Call<HomeResponse> countOrders_tohomepage_byusernew(@Path("id") int id,
                                              @Path("area") String strArea,
                                              @Path("kilometers") String strKilometers,
                                              @Query("oauth_consumer_key") String oauth_consumer_key,
                                              @Query("oauth_nonce") String oauth_nonce,
                                              @Query("oauth_signature") String oauth_signature,
                                              @Query("oauth_signature_method") String oauth_signature_method,
                                              @Query("oauth_timestamp") String oauth_timestamp);
}
