package dms.deideas.zas.Services;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by bnavarro on 07/07/2016.
 */
public interface MotodriverService {

    @GET("wp-json/wp/v2/login?")
    Call<MotodriverGet> String(@Query("username") String username,
                               @Query("password") String password);

    @GET("wp-json/wp/v2/logout?")
    Call<MotodriverGet> StringLogout(@Query("username") String username,
                                     @Query("password") String password);

}
