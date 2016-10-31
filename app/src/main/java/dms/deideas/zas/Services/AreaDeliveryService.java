package dms.deideas.zas.Services;

import java.util.ArrayList;

import dms.deideas.zas.Model.Reparto;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by dmadmin on 20/09/2016.
 */
public interface AreaDeliveryService {
    @GET("wp-json/wp/v2/get_areadelivery")
    Call<ArrayList<Reparto>> get_areadelivery();
}
