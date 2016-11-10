package dms.deideas.zas.Model;

import java.io.Serializable;

/**
 * Created by dmadmin on 16/06/2016.
 */
public class CustomerShippingAddress implements Serializable {

    private String first_name;
    private String last_name;
    private String address_1;
    private String address_2;
    private String city;
    private String state;
    private String postcode;
    private String country;
    private String data_map;
    private String indications;
    private String is_wrong_location;


    public CustomerShippingAddress() {
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getAddress_1() {
        return address_1;
    }

    public void setAddress_1(String address_1) {
        this.address_1 = address_1;
    }

    public String getAddress_2() {
        return address_2;
    }

    public void setAddress_2(String address_2) {
        this.address_2 = address_2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }


    public String getData_map() {
        return data_map;
    }

    public void setData_map(String data_map) {
        this.data_map = data_map;
    }

    public String getIndications() {
        return indications;
    }

    public void setIndications(String indications) {
        this.indications = indications;
    }

    public String getIs_wrong_location() {
        return is_wrong_location;
    }

    public void setIs_wrong_location(String is_wrong_location) {
        this.is_wrong_location = is_wrong_location;
    }
}
