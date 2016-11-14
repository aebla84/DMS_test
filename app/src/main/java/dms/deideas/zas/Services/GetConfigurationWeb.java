package dms.deideas.zas.Services;

import java.util.ArrayList;
import dms.deideas.zas.Model.WebConfigurator;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by jotabono on 14/11/16.
 */

public interface GetConfigurationWeb {

    @GET("wp-json/wp/v2/get_webconfigurator_byapp")
    Call<WebConfigurator> get_webconfigurator_byapp();
}
