package dms.deideas.zas.Services;

import dms.deideas.zas.Model.UserMeta;
import dms.deideas.zas.Model.WebConfigurator;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by jotabono on 14/11/16.
 */

public interface UserMetaGet {

    @GET("wp-json/wp/v2/get_usermeta_from_app?")
    Call<UserMeta> getUserMeta(@Query("username") String username,
                                @Query("password") String password);
}
