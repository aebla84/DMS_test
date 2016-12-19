package dms.deideas.zas.Model;

import java.io.Serializable;

/**
 * Created by dmadmin on 22/06/2016.
 */
public class UserMeta implements Serializable {


    public UserMeta() {
    }

    private String is_driver_new;
    private String maxdistance;


    public String getIs_driver_new() {
        return is_driver_new;
    }

    public void setIs_driver_new(String is_driver_new) {
        this.is_driver_new = is_driver_new;
    }

    public String getMaxdistance() {
        return maxdistance;
    }

    public void setMaxdistance(String maxdistance) {
        this.maxdistance = maxdistance;
    }
}
