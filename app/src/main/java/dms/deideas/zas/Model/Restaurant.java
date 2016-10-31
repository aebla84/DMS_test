package dms.deideas.zas.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dmadmin on 02/06/2016.
 */
public class Restaurant {

    private String name;
    private String street;
    private String street_number;
    private String phone;
    @SerializedName("data_map")
    private DataMap data_map;

    public Restaurant() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreet_number() {
        return street_number;
    }

    public void setStreet_number(String street_number) {
        this.street_number = street_number;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public DataMap getData_map() {
        return data_map;
    }

    public void setData_map(DataMap data_map) {
        this.data_map = data_map;
    }

}
