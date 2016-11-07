package dms.deideas.zas.Model;

import java.io.Serializable;

/**
 * Created by bnavarro on 31/08/2016.
 */
public class CustomerBillingAddress implements Serializable{
    private String first_name;
    private String last_name;
    private String address_1;
    private String email;
    private String phone;
    public CustomerBillingAddress() {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
