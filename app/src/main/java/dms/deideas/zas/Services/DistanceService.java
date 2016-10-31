package dms.deideas.zas.Services;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by dmadmin on 21/07/2016.
 */
public interface DistanceService {

    @GET("json?")
    Call<DistanceSearch> getdistance(@Query("origins") String origins,
                                     @Query("destinations") String destinations,
                                     @Query("key") String key);

}
