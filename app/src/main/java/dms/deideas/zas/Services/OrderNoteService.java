package dms.deideas.zas.Services;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by bnavarro on 18/07/2016.
 */
public interface OrderNoteService {

    @GET("wc-api/v2/orders/{order_id}/comments")
    Call<OrderNoteGet> getlistNoteOrder(@Path("order_id") int order_id,
                                        @Query("oauth_consumer_key") String oauth_consumer_key,
                                        @Query("oauth_nonce") String oauth_nonce,
                                        @Query("oauth_signature") String oauth_signature,
                                        @Query("oauth_signature_method") String oauth_signature_method,
                                        @Query("oauth_timestamp") String oauth_timestamp);
    @POST("wc-api/v2/orders/{order_id}/comments")
    Call<OrderNoteGet> createNoteOrder(@Path("order_id") int order_id,
                                 @Body() OrderNoteUpdate ordernote,
                                 @Query("oauth_consumer_key") String oauth_consumer_key,
                                 @Query("oauth_nonce") String oauth_nonce,
                                 @Query("oauth_signature") String oauth_signature,
                                 @Query("oauth_signature_method") String oauth_signature_method,
                                 @Query("oauth_timestamp") String oauth_timestamp);

    @GET("wc-api/v2/orders/{order_id}/notes")
    Call<OrderNoteGet> getlistNoteClientOrder(@Path("order_id") int order_id,
                                            @Query("oauth_consumer_key") String oauth_consumer_key,
                                            @Query("oauth_nonce") String oauth_nonce,
                                            @Query("oauth_signature") String oauth_signature,
                                            @Query("oauth_signature_method") String oauth_signature_method,
                                            @Query("oauth_timestamp") String oauth_timestamp);
}
