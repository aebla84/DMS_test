package dms.deideas.zas.Model;

import java.io.Serializable;

/**
 * Created by dmadmin on 16/06/2016.
 */
public class Payment implements Serializable {



    private String method_id;
    private String method_title;
    private Boolean paid;

    public Payment() {

    }

    public String getMethod_id() {
        return method_id;
    }

    public void setMethod_id(String method_id) {
        this.method_id = method_id;
    }

    public String getMethod_title() {
        return method_title;
    }

    public void setMethod_title(String method_title) {
        this.method_title = method_title;
    }

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }
}
