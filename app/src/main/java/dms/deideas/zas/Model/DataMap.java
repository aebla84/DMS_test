package dms.deideas.zas.Model;

import java.io.Serializable;

/**
 * Created by dmadmin on 22/06/2016.
 */
public class DataMap implements Serializable {


    public DataMap() {
    }

    private String address;
    private String lat;
    private String lng;


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
