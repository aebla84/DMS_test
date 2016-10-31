package dms.deideas.zas.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bnavarro on 14/07/2016.
 */
public class Error_Retrofit {
    @SerializedName("code")
    public int code;
    @SerializedName("error")
    public String errorDetails;

}
