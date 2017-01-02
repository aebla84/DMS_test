package dms.deideas.zas.Model;

import android.text.format.DateFormat;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by dmadmin on 01/06/2016.
 */
public class Order implements Serializable {

    @SerializedName("restaurant")
    private Restaurant restaurant;
    @SerializedName("shipping_address")
    private CustomerShippingAddress shipping_address;
    @SerializedName("billing_address")
    private CustomerBillingAddress billing_address;
    @SerializedName("payment_details")
    private Payment payment_details;
    private Incidencia problem_details; //Object that is used to add new problem in BBDD
    @SerializedName("id")
    private int id;
    @SerializedName("created_at")
    private String created_at;
    @SerializedName("completed_at")
    private String completed_at;
    @SerializedName("time_rest_accepted")
    private String time_rest_accepted;
    @SerializedName("orderstatus")
    private String orderstatus;
    @SerializedName("motodriver")
    private String motodriver;
    @SerializedName("motodriver_problem")
    private String motodriver_problem;
    @SerializedName("motodriver_problem_name")
    private String motodriver_problem_name;
    @SerializedName("note")
    private String note;
    @SerializedName("minutesMotoDriverPickupInRestaurant")
    private int minutesMotoDriverPickupInRestaurant;
    @SerializedName("total")
    private String total;
    @SerializedName("lista_incidencias")
    private List<Incidencia> lista_incidencias;
    @SerializedName("order_big")
    private Boolean order_big;
    @SerializedName("lista_comments")
    private List<OrderNote> lista_comments;
    @SerializedName("minutesDriverOnRoad")
    private int minutesDriverOnRoad;
    @SerializedName("restaurant_foodpriority")
    private String restaurant_foodpriority;
    @SerializedName("shipping_latlong")
    private String shipping_latlong;
    @SerializedName("order_has_drinks")
    private Boolean order_has_drinks;
    @SerializedName("order_has_dessert")
    private Boolean order_has_dessert;
    @SerializedName("minutesDriverInRestaurant")
    private int minutesDriverInRestaurant;
    @SerializedName("minutesOrderWithProblem")
    private int minutesOrderWithProblem;
    public Order() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getCompleted_at() {
        return completed_at;
    }

    public void setCompleted_at(String completed_at) {
        this.completed_at = completed_at;
    }

    public String getTime_rest_accepted() {
        return time_rest_accepted;
    }

    public void setTime_rest_accepted(String time_rest_accepted) {
        this.time_rest_accepted = time_rest_accepted;
    }

    public String getOrderstatus() {
        return orderstatus;
    }

    public void setOrderstatus(String orderstatus) {
        this.orderstatus = orderstatus;
    }

    public String getMotodriver() {
        return motodriver;
    }

    public void setMotodriver(String motodriver) {
        this.motodriver = motodriver;
    }

    public String getMotodriver_problem() {return motodriver_problem; }

    public void setMotodriver_problem(String motodriver_problem) {this.motodriver_problem = motodriver_problem; }

    public String getMotodriver_problem_name() {return motodriver_problem_name; }

    public void setMotodriver_problem_name(String motodriver_problem_name) { this.motodriver_problem_name = motodriver_problem_name; }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public CustomerShippingAddress getShipping_address() {
        return shipping_address;
    }

    public void setShipping_address(CustomerShippingAddress shipping_address) {
        this.shipping_address = shipping_address;
    }

    public Payment getPayment_details() {
        return payment_details;
    }

    public void setPayment_details(Payment payment_details) {
        this.payment_details = payment_details;
    }

    public Incidencia getProblem_details() {
        return problem_details;
    }

    public void setProblem_details(Incidencia problem_details) {
        this.problem_details = problem_details;
    }


    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<Incidencia> getLista_incidencias() {
        return lista_incidencias;
    }

    public void setLista_incidencias(List<Incidencia> lista_incidencias) {
        this.lista_incidencias = lista_incidencias;
    }


    public String getTimeKitchen() {
        return String.valueOf(minutesMotoDriverPickupInRestaurant);
    }

    public void setTimeKitchen(String minutesMotoDriverPickupInRestaurant) {
        this.minutesMotoDriverPickupInRestaurant = Integer.parseInt(minutesMotoDriverPickupInRestaurant);
    }

    public int getMinutesMotoDriverPickupInRestaurant() {
        return minutesMotoDriverPickupInRestaurant;
    }

    public void setMinutesMotoDriverPickupInRestaurant(int minutesMotoDriverPickupInRestaurant) {
        this.minutesMotoDriverPickupInRestaurant = minutesMotoDriverPickupInRestaurant;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    //TODO
    @Override
    public String toString() {
        return "Order{" +
                "orderstatus='" + orderstatus + '\'' +
                '}';
    }


    public CustomerBillingAddress getBilling_address() {
        return billing_address;
    }

    public void setBilling_address(CustomerBillingAddress billing_address) {
        this.billing_address = billing_address;
    }

    public Boolean getOrder_big() {
        return order_big;
    }

    public void setOrder_big(Boolean order_big) {
        this.order_big = order_big;
    }

    public List<OrderNote> getLista_comments() {
        return lista_comments;
    }
    public void setLista_comments(List<OrderNote> lista_comments) {
        this.lista_comments = lista_comments;
    }


    public int getMinutesDriverOnRoad() {
        return minutesDriverOnRoad;
    }

    public void setMinutesDriverOnRoad(int minutesDriverOnRoad) {
        this.minutesDriverOnRoad = minutesDriverOnRoad;
    }

    public String getRestaurant_foodpriority() {
        return restaurant_foodpriority;
    }

    public void setRestaurant_foodpriority(String restaurant_foodpriority) {
        this.restaurant_foodpriority = restaurant_foodpriority;
    }

    public String getShipping_latlong() {
        return shipping_latlong;
    }

    public void setShipping_latlong(String shipping_latlong) {
        this.shipping_latlong = shipping_latlong;
    }

    public Boolean getOrder_has_drinks() {
        return order_has_drinks;
    }

    public void setOrder_has_drinks(Boolean order_has_drinks) {
        this.order_has_drinks = order_has_drinks;
    }

    public Boolean getOrder_has_dessert() {
        return order_has_dessert;
    }

    public void setOrder_has_dessert(Boolean order_has_dessert) {
        this.order_has_dessert = order_has_dessert;
    }

    public int getMinutesDriverInRestaurant() {
        return minutesDriverInRestaurant;
    }

    public void setMinutesDriverInRestaurant(int minutesDriverInRestaurant) {
        this.minutesDriverInRestaurant = minutesDriverInRestaurant;
    }
    public int getMinutesOrderWithProblem() {
        return minutesOrderWithProblem;
    }

    public void setMinutesOrderWithProblem(int minutesOrderWithProblem) {
        this.minutesOrderWithProblem = minutesOrderWithProblem;
    }

}
