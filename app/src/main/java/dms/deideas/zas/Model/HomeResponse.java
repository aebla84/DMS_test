package dms.deideas.zas.Model;

import java.io.Serializable;

/**
 * Created by bnavarro on 15/11/2016.
 */
public class HomeResponse implements Serializable {
    private Integer countOrdersByArea;
    private Integer countOrdersByAreaByUser;
    private Integer countOrdersByAreaByUser_NotProblems;


    public HomeResponse(){
        countOrdersByArea = 0;
        countOrdersByAreaByUser = 0;
        countOrdersByAreaByUser_NotProblems = 0;
    }

    public Integer getCountOrdersByArea() {
        return countOrdersByArea;
    }

    public void setCountOrdersByArea(Integer countOrdersByArea) {
        this.countOrdersByArea = countOrdersByArea;
    }

    public Integer getCountOrdersByAreaByUser() {
        return countOrdersByAreaByUser;
    }

    public void setCountOrdersByAreaByUser(Integer countOrdersByAreaByUser) {
        this.countOrdersByAreaByUser = countOrdersByAreaByUser;
    }

    public Integer getCountOrdersByAreaByUser_NotProblems() {
        return countOrdersByAreaByUser_NotProblems;
    }

    public void setCountOrdersByAreaByUser_NotProblems(Integer countOrdersByAreaByUser_NotProblems) {
        this.countOrdersByAreaByUser_NotProblems = countOrdersByAreaByUser_NotProblems;
    }
}
