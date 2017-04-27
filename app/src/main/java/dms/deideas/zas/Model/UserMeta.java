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
    private String driver_level;
    private String max_orders_accepted;

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

    public String getType() {
        return driver_level;
    }

    public void setType(String type) {
        this.driver_level = type;
    }

    public String getMax_orders_accepted() {
        return max_orders_accepted;
    }

    public void setMax_orders_accepted(String max_orders_accepted) {
        this.max_orders_accepted = max_orders_accepted;
    }
}
