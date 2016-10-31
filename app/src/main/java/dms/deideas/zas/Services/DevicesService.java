package dms.deideas.zas.Services;

import dms.deideas.zas.Model.Devices;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by bnavarro on 02/08/2016.
 */
public interface DevicesService {
    @GET("wp-json/wp/v2/get_devices")
    Call<String> getDevices_push(@Query("user_id") Integer user_id);

    @POST("wp-json/wp/v2/save_devices")
    Call<Boolean> saveDevices_push(@Body() Devices devices);

    @POST("wp-json/wp/v2/update_devices")
    Call<Boolean> updateDevices_push(@Body() Devices devices);

}
