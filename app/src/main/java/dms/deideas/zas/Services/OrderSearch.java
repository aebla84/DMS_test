package dms.deideas.zas.Services;

import java.util.ArrayList;
import java.util.List;

import dms.deideas.zas.Model.Order;

/**
 * Created by Jordi on 29/05/2016.
 */
public class OrderSearch {

    Integer count;
    Integer countOrders;
    List<Order> orders;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<Order> orders) {
        this.orders = orders;
    }

    public Integer getCountOrders() {
        return countOrders;
    }

    public void setCountOrders(Integer countOrders) {
       this.countOrders = countOrders;
    }

}

