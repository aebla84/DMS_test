package dms.deideas.zas.Services;

import dms.deideas.zas.Model.Data;

/**
 * Created by bnavarro on 07/07/2016.
 */
public class MotodriverGet {

    int ID;

    boolean status;

    Data data;

    String display_name;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
