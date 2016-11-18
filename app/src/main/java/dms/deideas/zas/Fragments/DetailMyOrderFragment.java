package dms.deideas.zas.Fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import dms.deideas.zas.Activities.MainActivity;
import dms.deideas.zas.Constants;
import dms.deideas.zas.Globals;
import dms.deideas.zas.Model.Incidencia;
import dms.deideas.zas.Model.Order;
import dms.deideas.zas.Model.OrderNote;
import dms.deideas.zas.Model.Reparto;
import dms.deideas.zas.R;
import dms.deideas.zas.Services.OrderNoteGet;
import dms.deideas.zas.Services.OrderSearch;
import dms.deideas.zas.Services.OrderUpdate;
import dms.deideas.zas.Services.Retrofit.RetrofitDelegateHelper;
import dms.deideas.zas.Utils;
import retrofit2.Converter;

/**
 * Created by dmadmin on 30/06/2016.
 */
public class DetailMyOrderFragment extends Fragment implements View.OnClickListener, OnMapReadyCallback,RetrofitDelegateHelper.AlRecibirListaDelegate , RetrofitDelegateHelper.AlRecibirListaCommentsDelegate, RetrofitDelegateHelper.response {

    //region Declare variables
    private Order order;
    private String title, phone,prevpage_title,originPage;
    private Integer idUser;

    private TextView id_order, restaurant_name, time_left_of_order, restaurant_direction, tv_phone_restaurant, customer_name, hour_order, payment_total, commentsCount, problemsCount, customer_direction;
    private ImageView phone_restaurant, phone_customer, imgpointClock, imgLocationFail;
    private TextView tv_phone_customer, state_of_payment, comments, incidents;
    private Button accept, disallocate, saveLocation, savePhone, btnincidencia;
    private RelativeLayout dtorder_problems, warningLayout;
    private List<OrderNote> lstcomments;
    private List<Incidencia> lstproblems;
    private ProgressDialog progress;
    private Integer numMaxOrdersAccepted_BBDD;
    private Boolean isOrderChanged;
    private SharedPreferences prefs;
    private int MAP_CONTROL = Constants.MAP_CONTROL_Restaurant;

    private String questionStatus = "";

    private ImageView icRestaurantWhite,icRestaurantRed,icMotoWhite,icRecogidoWhite,icRecogidoRed,icIncidenceRed,icIncidenceWhite,icFinishedWhite,icFinishedRed,icFinishedIncidenceRed,icFinishedIncidenceWhite,imgBigOrder,imgDrinks;
    private TextView txtRestaurantWhite,txtRestaurantRed,txtMotoWhite,txtRecogidoWhite,txtRecogidoRed,txtIncidenceRed,txtIncidenceWhite, txtFinishedWhite,txtFinishedRed,txtFinishedIncidenceRed,txtFinishedIncidenceWhite;

    private RetrofitDelegateHelper restHelper;
    private LocationManager locationManager;
    private String provider;

    private double latitudeClient, longitudeClient;
    private double latitude = 0.0, longitude = 0.0;

    private int comCount = 0,probCount = 0;
    private Utils util = new Utils();
    //endregion

    public DetailMyOrderFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.detail_my_order_fragment, container, false);
        Globals g = Globals.getInstance();
        g.setIdFragment(Constants.MYORDERSFRAGMENT_CODE);
        g.setIdFragmentDetail(Constants.DETAILMYORDERSFRAGMENT_CODE);
        ((MainActivity) getActivity()).setTitle(getArguments().getString(Constants.ARGUMENT_TITLE));

        findViewById(view);
        readPreferences();
        lstcomments = new ArrayList<OrderNote>();
        setComments(view);
        setOnClickListener();
        setVisibility();
        if(isOrderChanged) {
            getNewOrder(); //Call BBDD and reload fields
        } else {
            setValues();  //Load fields
        }
        return view;
    }

    private void readPreferences() {
        final SharedPreferences prefs = getActivity().getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE);
        idUser = prefs.getInt(Constants.PREFERENCES_USER_ID, 0);
        numMaxOrdersAccepted_BBDD = Integer.getInteger(prefs.getString(Constants.PREFERENCES_NUMBER_MAX_ORDERS_ACCEPTED_BYDRIVER, "0"));
        isOrderChanged = prefs.getBoolean(Constants.PREFERENCES_IS_ORDER_CHANGED, false);
    }

    private void getNewOrder() {
        Globals g = Globals.getInstance();
        g.setServiceCode(Constants.SERVICE_CODE_order_byidorder);
        try {
            restHelper = new RetrofitDelegateHelper(order.getId(), 0);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        restHelper.get_orderById(this);
    }

    private void setValues() {

        //id_order.setText(String.valueOf(order.getId()));
        payment_total.setText(String.valueOf(order.getTotal()) + " €");
        restaurant_name.setText(order.getRestaurant().getName());
        restaurant_direction.setText(order.getRestaurant().getStreet());
        customer_name.setText(order.getBilling_address().getFirst_name() + " " + order.getBilling_address().getLast_name());
        hour_order.setText(order.getCreated_at().substring(11, 16) + "h");
        //hour_order_rest_accepted.setText(order.getTime_rest_accepted().substring(11, 16) + "h");
        time_left_of_order.setText(order.getTimeKitchen() + " min");
        //hour_order_title.setText(order.getCreated_at().substring(11, 16) + "h");
        if(order.getShipping_address().getIndications().length() > 0)
        {
            customer_direction.setText(order.getShipping_address().getAddress_1() + " " + order.getShipping_address().getAddress_2() + " (" + order.getShipping_address().getIndications() +") ");
        }
        else {
            customer_direction.setText(order.getShipping_address().getAddress_1() + " " + order.getShipping_address().getAddress_2());
        }

        if (order.getPayment_details().getMethod_title().contentEquals(getContext().getResources().getString(R.string.paid_home)) && order.getPayment_details().getPaid() == false) {
            state_of_payment.setText(R.string.cash_payment);
        } else {
            state_of_payment.setText(R.string.online_payment);
        }

        tv_phone_restaurant.setText(String.valueOf(order.getRestaurant().getPhone()));
        tv_phone_customer.setText(String.valueOf(order.getBilling_address().getPhone()));

        if(order.getLista_incidencias() != null)
            problemsCount.setText(String.valueOf(order.getLista_incidencias().size()));

        //countCommentsAndProblems();

    }

    private void findViewById(View view) {
        //Initiate Views
        //id_order = (TextView) view.findViewById(R.id.idorder);
        payment_total = (TextView) view.findViewById(R.id.payment_total);
        restaurant_name = (TextView) view.findViewById(R.id.restaurant_name);
        time_left_of_order = (TextView) view.findViewById(R.id.time_left_of_order);
        restaurant_direction = (TextView) view.findViewById(R.id.restaurant_direction);
        phone_restaurant = (ImageView) view.findViewById(R.id.imgPhoneRestaurant);
        imgpointClock = (ImageView) view.findViewById(R.id.imgpointClock);
        imgLocationFail = (ImageView) view.findViewById(R.id.imgLocationFail);
        tv_phone_restaurant = (TextView) view.findViewById(R.id.phoneRest);
        customer_name = (TextView) view.findViewById(R.id.customer_name);
        hour_order = (TextView) view.findViewById(R.id.hour_order);
        //hour_order_rest_accepted = (TextView) view.findViewById(R.id.hourOrderRestaurantAccepted);
        customer_direction = (TextView) view.findViewById(R.id.customer_direction);
        phone_customer = (ImageView) view.findViewById(R.id.imgPhoneCustomer);
        tv_phone_customer = (TextView) view.findViewById(R.id.phoneCustomer);
        state_of_payment = (TextView) view.findViewById(R.id.state_of_payment);
        comments = (TextView) view.findViewById(R.id.comments);
        incidents = (TextView) view.findViewById(R.id.incidents);
        commentsCount = (TextView) view.findViewById(R.id.commentsCount);
        problemsCount = (TextView) view.findViewById(R.id.problemsCount);
        accept = (Button) view.findViewById(R.id.accept);
        disallocate = (Button) view.findViewById(R.id.disallocate);
        btnincidencia = (Button) view.findViewById(R.id.btnincidencia);

        icMotoWhite = (ImageView) view.findViewById(R.id.icMotoWhite);
        txtMotoWhite = (TextView) view.findViewById(R.id.txtMotoWhite);

        icRestaurantWhite = (ImageView) view.findViewById(R.id.icRestaurantWhite);
        icRestaurantRed = (ImageView) view.findViewById(R.id.icRestaurantRed);
        txtRestaurantWhite = (TextView) view.findViewById(R.id.txtRestaurantWhite);
        txtRestaurantRed = (TextView) view.findViewById(R.id.txtRestaurantRed);

        icRecogidoWhite = (ImageView) view.findViewById(R.id.icRecogidoWhite);
        icRecogidoRed = (ImageView) view.findViewById(R.id.icRecogidoRed);
        txtRecogidoWhite = (TextView) view.findViewById(R.id.txtRecogidoWhite);
        txtRecogidoRed = (TextView) view.findViewById(R.id.txtRecogidoRed);

        icIncidenceWhite = (ImageView) view.findViewById(R.id.icIncidenceWhite);
        txtIncidenceWhite = (TextView) view.findViewById(R.id.txtIncidenceWhite);

        icFinishedWhite = (ImageView) view.findViewById(R.id.icFinishedWhite);
        icFinishedRed = (ImageView) view.findViewById(R.id.icFinishedRed);
        txtFinishedWhite = (TextView) view.findViewById(R.id.txtFinishedWhite);
        txtFinishedRed = (TextView) view.findViewById(R.id.txtFinishedRed);

        icFinishedIncidenceRed = (ImageView) view.findViewById(R.id.icFinishedIncidenceRed);
        txtFinishedIncidenceRed = (TextView) view.findViewById(R.id.txtFinishedIncidentRed);
        icFinishedIncidenceWhite = (ImageView) view.findViewById(R.id.icFinishedIncidenceWhite);
        txtFinishedIncidenceWhite = (TextView) view.findViewById(R.id.txtFinishedIncidenceWhite);
        dtorder_problems = (RelativeLayout) view.findViewById(R.id.relLay_dtorder_problems);
        warningLayout = (RelativeLayout) view.findViewById(R.id.warningLayout);

        saveLocation = (Button) view.findViewById(R.id.saveLocation);
        savePhone =(Button) view.findViewById(R.id.savePhone);

        imgBigOrder = (ImageView) view.findViewById(R.id.imgBigOrder);
        imgDrinks = (ImageView) view.findViewById(R.id.imgDrink);
    }

    private void setOnClickListener() {
        // Setting onClickListener
        restaurant_direction.setOnClickListener(this);
        phone_restaurant.setOnClickListener(this);
        customer_direction.setOnClickListener(this);
        phone_customer.setOnClickListener(this);
        comments.setOnClickListener(this);
        incidents.setOnClickListener(this);
        accept.setOnClickListener(this);
        disallocate.setOnClickListener(this);
        btnincidencia.setOnClickListener(this);
        saveLocation.setOnClickListener(this);
        savePhone.setOnClickListener(this);
        imgLocationFail.setOnClickListener(this);
    }

    // In function of status setting visibility of components (buttons - disallocate, btnincidencia // Price total of payment)
    private void setVisibility() {
        String status = order.getOrderstatus();
        switch (status) {
            case Constants.ORDER_STATUS_driver_has_accepted:
                disallocate.setVisibility(View.VISIBLE);
                btnincidencia.setVisibility(View.INVISIBLE);
                payment_total.setVisibility(View.INVISIBLE);
                break;
            case Constants.ORDER_STATUS_driver_in_rest:
                btnincidencia.setVisibility(View.INVISIBLE);
                payment_total.setVisibility(View.VISIBLE);
                disallocate.setVisibility(View.INVISIBLE);
                break;
            case Constants.ORDER_STATUS_driver_on_road:
                btnincidencia.setVisibility(View.VISIBLE);
                payment_total.setVisibility(View.VISIBLE);
                disallocate.setVisibility(View.INVISIBLE);
                break;
            case Constants.ORDER_STATUS_problem:
                btnincidencia.setVisibility(View.VISIBLE);
                payment_total.setVisibility(View.VISIBLE);
                disallocate.setVisibility(View.VISIBLE);
                break;
            case Constants.ORDER_STATUS_order_delivered:
                btnincidencia.setVisibility(View.VISIBLE);
                accept.setVisibility(View.INVISIBLE);
                payment_total.setVisibility(View.VISIBLE);
                disallocate.setVisibility(View.INVISIBLE);
            case Constants.ORDER_STATUS_order_delivered_w_problem:
                btnincidencia.setVisibility(View.VISIBLE);
                payment_total.setVisibility(View.VISIBLE);
                disallocate.setVisibility(View.INVISIBLE);
            default:
                break;
        }
        ArrayList<Incidencia> lstIncidencias = (ArrayList<Incidencia>) order.getLista_incidencias();
        if (lstIncidencias != null && lstIncidencias.size() > 0) {
            dtorder_problems.setVisibility(View.VISIBLE);
        } else {
            dtorder_problems.setVisibility(View.GONE);
        }
        order_status_visibility_and_settext();

        if(order.getShipping_address().getIs_wrong_location().equals("1")){
            imgLocationFail.setVisibility(View.VISIBLE);
        } else {imgLocationFail.setVisibility(View.INVISIBLE);
        }

        if(order.getOrder_big()){
            warningLayout.setVisibility(View.VISIBLE);
            imgBigOrder.setVisibility(View.VISIBLE);
        } else {imgBigOrder.setVisibility(View.INVISIBLE);
        }
        if(order.getOrder_has_drinks()){
            warningLayout.setVisibility(View.VISIBLE);
            imgDrinks.setVisibility(View.VISIBLE);
        } else {imgDrinks.setVisibility(View.INVISIBLE);
        }

    }

    //Set text for button to change status and set the question that ask you the dialog
    private void order_status_visibility_and_settext() {

        String status = order.getOrderstatus();

        switch (status) {
            case Constants.ORDER_STATUS_driver_has_accepted:
                accept.setText(getResources().getString(R.string.driver_in_rest_status));
                questionStatus = getResources().getString(R.string.in_rest);
                break;

            case Constants.ORDER_STATUS_driver_in_rest:
                accept.setText(getResources().getString(R.string.driver_on_road_status));
                questionStatus = getResources().getString(R.string.order_collected);

                icRestaurantRed.setVisibility(View.GONE);
                txtRestaurantRed.setVisibility(View.GONE);
                icRestaurantWhite.setVisibility(View.VISIBLE);
                txtRestaurantWhite.setVisibility(View.VISIBLE);
                break;

            case Constants.ORDER_STATUS_driver_on_road:
                accept.setText(getResources().getString(R.string.order_delivered_status));
                questionStatus = getResources().getString(R.string.order_finished);

                icRestaurantRed.setVisibility(View.GONE);
                txtRestaurantRed.setVisibility(View.GONE);
                icRestaurantWhite.setVisibility(View.VISIBLE);
                txtRestaurantWhite.setVisibility(View.VISIBLE);

                icRecogidoRed.setVisibility(View.GONE);
                txtRecogidoRed.setVisibility(View.GONE);
                icRecogidoWhite.setVisibility(View.VISIBLE);
                txtRecogidoWhite.setVisibility(View.VISIBLE);

                saveLocation.setVisibility(View.VISIBLE);
                savePhone.setVisibility(View.VISIBLE);

                break;

            case Constants.ORDER_STATUS_order_delivered:

                icRestaurantRed.setVisibility(View.GONE);
                txtRestaurantRed.setVisibility(View.GONE);
                icRestaurantWhite.setVisibility(View.VISIBLE);
                txtRestaurantWhite.setVisibility(View.VISIBLE);

                icRecogidoRed.setVisibility(View.GONE);
                txtRecogidoRed.setVisibility(View.GONE);
                icRecogidoWhite.setVisibility(View.VISIBLE);
                txtRecogidoWhite.setVisibility(View.VISIBLE);

                icFinishedRed.setVisibility(View.GONE);
                txtFinishedRed.setVisibility(View.GONE);
                icFinishedWhite.setVisibility(View.VISIBLE);
                txtFinishedWhite.setVisibility(View.VISIBLE);

                accept.setVisibility(View.GONE);
                saveLocation.setVisibility(View.VISIBLE);
                savePhone.setVisibility(View.VISIBLE);
                break;

            case Constants.ORDER_STATUS_problem:

                icMotoWhite.setVisibility(View.GONE);
                txtMotoWhite.setVisibility(View.GONE);

                icRestaurantRed.setVisibility(View.GONE);
                txtRestaurantRed.setVisibility(View.GONE);
                icRestaurantWhite.setVisibility(View.GONE);
                txtRestaurantWhite.setVisibility(View.GONE);

                icRecogidoRed.setVisibility(View.GONE);
                txtRecogidoRed.setVisibility(View.GONE);
                icRecogidoWhite.setVisibility(View.GONE);
                txtRecogidoWhite.setVisibility(View.GONE);

                icIncidenceWhite.setVisibility(View.VISIBLE);
                txtIncidenceWhite.setVisibility(View.VISIBLE);

                icFinishedRed.setVisibility(View.GONE);
                txtFinishedRed.setVisibility(View.GONE);
                icFinishedWhite.setVisibility(View.GONE);
                txtFinishedWhite.setVisibility(View.GONE);

                icFinishedIncidenceRed.setVisibility(View.VISIBLE);
                txtFinishedIncidenceRed.setVisibility(View.VISIBLE);

                saveLocation.setVisibility(View.VISIBLE);
                savePhone.setVisibility(View.VISIBLE);

                accept.setText(getResources().getString(R.string.order_delivered_status));
                questionStatus = getResources().getString(R.string.order_finished);
                break;

            case Constants.ORDER_STATUS_order_delivered_w_problem:
                //Toast.makeText(getContext(), "2:"+status, Toast.LENGTH_SHORT).show();

                icMotoWhite.setVisibility(View.INVISIBLE);
                txtMotoWhite.setVisibility(View.INVISIBLE);

                icRestaurantRed.setVisibility(View.GONE);
                txtRestaurantRed.setVisibility(View.GONE);
                icRestaurantWhite.setVisibility(View.GONE);
                txtRestaurantWhite.setVisibility(View.GONE);

                icRecogidoRed.setVisibility(View.GONE);
                txtRecogidoRed.setVisibility(View.GONE);
                icRecogidoWhite.setVisibility(View.GONE);
                txtRecogidoWhite.setVisibility(View.GONE);

                //icIncidenceRed.setVisibility(View.VISIBLE);
                //txtIncidenceRed.setVisibility(View.VISIBLE);
                icIncidenceWhite.setVisibility(View.GONE);
                txtIncidenceWhite.setVisibility(View.GONE);

                icFinishedRed.setVisibility(View.GONE);
                txtFinishedRed.setVisibility(View.GONE);
                icFinishedWhite.setVisibility(View.GONE);
                txtFinishedWhite.setVisibility(View.GONE);

                icFinishedIncidenceRed.setVisibility(View.GONE);
                txtFinishedIncidenceRed.setVisibility(View.GONE);
                icFinishedIncidenceWhite.setVisibility(View.VISIBLE);

                txtFinishedIncidenceWhite.setVisibility(View.VISIBLE);
                saveLocation.setVisibility(View.VISIBLE);
                savePhone.setVisibility(View.VISIBLE);
                accept.setVisibility(View.GONE);

            default:
                break;
        }
    }

    public static DetailMyOrderFragment newInstance(String title, Order order, String originPage) {
        Globals g = Globals.getInstance();

        g.setScreenCode(Constants.screenCode_detailMyOrders);
        Bundle b = new Bundle();
        b.putString(Constants.ARGUMENT_TITLE, title);
        b.putSerializable(Constants.ARGUMENT_ORDER, order);
        b.putString(Constants.ARGUMENT_ORIGINPAGE, originPage);
        originPage = originPage;
        DetailMyOrderFragment fragment = new DetailMyOrderFragment();
        fragment.setArguments(b);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(Constants.ARGUMENT_TITLE);
            order = (Order) getArguments().getSerializable(Constants.ARGUMENT_ORDER);
            prevpage_title = getArguments().getString(Constants.ARGUMENT_PREVPAGE_TITLE);
        }
    }

    @Override
    public void onClick(View v) {
        int idOrder = order.getId();
        ArrayList<Incidencia> lstIncidencias = (ArrayList<Incidencia>) order.getLista_incidencias();

        if (v == restaurant_direction) {
            String title = order.getRestaurant().getName();
            MAP_CONTROL = Constants.MAP_CONTROL_Restaurant;
            latitude = Double.parseDouble(order.getRestaurant().getData_map().getLat());
            longitude = Double.parseDouble(order.getRestaurant().getData_map().getLng());
            MapFragment fragment = MapFragment.newInstance(title);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frame, fragment)
                    .addToBackStack(null)
                    .commit();

            fragment.getMapAsync(this);
            ((MainActivity) getActivity()).setTitle(title);

        } else if (v == customer_direction) {
            String title = order.getShipping_address().getFirst_name() + " " + order.getShipping_address().getLast_name();
            MAP_CONTROL = Constants.MAP_CONTROL_Client;
            clientDataMap();
            ((MainActivity) getActivity()).setTitle(title);

        } else if (v == phone_restaurant) {
            String posted_by = order.getRestaurant().getPhone();
            String uri = "tel:" + posted_by.trim();
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse(uri));
            startActivity(intent);
        } else if (v == phone_customer) {
            String posted_by = order.getBilling_address().getPhone();
            String uri = "tel:" + posted_by.trim();
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse(uri));
            startActivity(intent);
        } else if (v == comments) {
            String title = getResources().getString(R.string.comments);
            OrderNoteFragment fragment = OrderNoteFragment.newInstance(title, order);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frame, fragment)
                    .addToBackStack(null)
                    .commit();
            ((MainActivity) getActivity()).setTitle(title);

        } else if (v == incidents) {
            String title = getResources().getString(R.string.incidents);
            OrderIncidenciasFragment fragment = OrderIncidenciasFragment.newInstance(title, order, originPage);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frame, fragment)
                    .addToBackStack(null)
                    .commit();

            ((MainActivity) getActivity()).setTitle(title);
        } else if (v == accept) {
            DialogFragment dialogFragment = DialogFragment.newInstance(questionStatus, order, idUser,originPage);
            dialogFragment.show(getFragmentManager(), Constants.DIALOGFRAGMENT);
        } else if (v == disallocate) { // Button "desasignar"
            Globals g = Globals.getInstance();
            if (order.getOrderstatus().equals(Constants.ORDER_STATUS_problem)) {
                g.setScreenCode(Constants.screenCode_detailMyOrders_disallocateProblem);
            } else {
                g.setScreenCode(Constants.screenCode_detailMyOrders_disallocate);
            }
            questionStatus = getResources().getString(R.string.deallocated_order);
            DialogFragment dialogFragment = DialogFragment.newInstance(questionStatus, order, idUser, originPage);
            dialogFragment.show(getFragmentManager(), Constants.DIALOGFRAGMENT);
        } else if (v == btnincidencia) {
            String title = getResources().getString(R.string.incidents);
            OrderIncidenciasFragment fragment = OrderIncidenciasFragment.newInstance(title, order, originPage);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frame, fragment)
                    .addToBackStack(null)
                    .commit();
            ((MainActivity) getActivity()).setTitle(title);
        } else if (v == saveLocation) {
            saveLocation();
        }  else if (v == imgLocationFail) {
            Toast.makeText(v.getContext(), R.string.toast_wrong_location, Toast.LENGTH_LONG).show();
        }else if (v==savePhone){
            String newTitle = getResources().getString(R.string.phone);
            String strphone = (tv_phone_customer.getText()!= null)? tv_phone_customer.getText().toString() : "";
            PhoneFragment fragment = PhoneFragment.newInstance(newTitle, tv_phone_customer.getText().toString(),order,title,originPage);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frame, fragment)
                    .addToBackStack(null)
                    .commit();

            ((MainActivity) getActivity()).setTitle(title);
        }
    }

    private void setComments(View v) {

        //region Create and Show progress dialog
        progress = new ProgressDialog(getContext());
        progress.show();
        progress.setContentView(R.layout.custom);
        progress.setCanceledOnTouchOutside(false);
        TextView text = (TextView) progress.findViewById(R.id.text);
        ImageView image = (ImageView) progress.findViewById(R.id.zasSpin);
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.zas_spin);
        image.startAnimation(animation);
        //endregion

        int idOrder = order.getId();

        //region Get Order notes by id Order
        Globals g = Globals.getInstance();
        g.setServiceCode(Constants.SERVICE_CODE_order_notes);
        try {
            restHelper = new RetrofitDelegateHelper(idOrder, 0);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        restHelper.getOrderNotesByIdOrder(this);
        //endregion
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        String title = null;
        // Restaurant Map called
        if (MAP_CONTROL == Constants.MAP_CONTROL_Restaurant) {
            title = order.getRestaurant().getName();
        }
        // Customer Map called
        else if (MAP_CONTROL == Constants.MAP_CONTROL_Client) {
            title = order.getBilling_address().getFirst_name() + " " + order.getBilling_address().getLast_name();
        }

        LatLng cali = new LatLng(latitude, longitude);
        Marker marker = googleMap.addMarker(new MarkerOptions()
                .position(cali)
                .title(title));
        marker.showInfoWindow();

        CameraPosition cameraPosition = CameraPosition.builder()
                .target(cali)
                .zoom(15)
                .build();

        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public void clientDataMap() {

        Geocoder coder = new Geocoder(getContext());
        List<Address> address = null;

        try {
            String latlong = order.getShipping_address().getData_map().toString();
            if(latlong != null && latlong !="")
            {
                String[] strlatlong = latlong.split(",");
                latitude = Double.valueOf(strlatlong[0].trim());
                longitude =  Double.valueOf(strlatlong[1].trim());
            }
            else
            {
                String locationName = order.getShipping_address().getAddress_1().toString() + " " + order.getShipping_address().getAddress_2().toString()  + " "+   order.getShipping_address().getCity().toString()+ " " + order.getShipping_address().getCountry().toString()  +" " + order.getShipping_address().getPostcode().toString() ;
                address = coder.getFromLocationName(locationName, 1);
                if (address == null || address.size() == 0) {
                    //Toast.makeText(getContext(),R.string.toast_without_address, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Address location = address.get(0);
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }
            }
            if(latitude != 0.0 && longitude != 0.0)
            {
                MapFragment fragment = MapFragment.newInstance(title);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.frame, fragment)
                        .addToBackStack(null)
                        .commit();

                fragment.getMapAsync(this);
            }
            else
            {
                Toast.makeText(getContext(),R.string.toast_without_address_correctly, Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            Toast.makeText(getContext(),R.string.toast_without_address_correctly, Toast.LENGTH_LONG).show();
        }
    }

    //region Save location of motodriver like comment.
    //1.Initialize the locationManager and services
    public void saveLocation() {
    /*    locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        provider = locationManager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        Location location = locationManager.getLastKnownLocation(provider);
        Location getLastLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

        // Initialize the location fields
        if (!getLastLocation.equals(null)) {
            onLocationChanged(getLastLocation);
        } else {
            Toast.makeText(getContext(), R.string.toast_without_address_correctly, Toast.LENGTH_SHORT).show();
        }*/


        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED  && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)  == PackageManager.PERMISSION_GRANTED) {
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            provider = locationManager.getBestProvider(criteria, false);
            Location location = locationManager.getLastKnownLocation(provider);
            Location getLastLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            onLocationChanged(getLastLocation);
        } else {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
    }
    //2.Initialize the location fields (latitud, longitud) , save in preferences and create comment
    public void onLocationChanged(Location location) {
        if(location != null) {
            this.latitudeClient = (double) (location.getLatitude());
            this.longitudeClient = (double) (location.getLongitude());

            SharedPreferences prefs =
                    getActivity().getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE);
            //Save data of client in preferences
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("latitudeClient", String.valueOf(latitudeClient));
            editor.putString("latitudeClient", String.valueOf(longitudeClient));

            editor.commit();

            //Create comment in order with location information of motodriver
            addcomment_location(this.latitudeClient,this.longitudeClient);
        }
        else
        {
            Toast.makeText(getContext(),R.string.toast_without_address_correctly, Toast.LENGTH_SHORT).show();
        }



    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Location location = null;
        if (permissions.length == 2 &&
                permissions[0] == android.Manifest.permission.ACCESS_FINE_LOCATION &&  permissions[1] == Manifest.permission.ACCESS_COARSE_LOCATION &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                provider = locationManager.getBestProvider(criteria, false);
                location = locationManager.getLastKnownLocation(provider);
                onLocationChanged(location);
                return;
            }
        } else {
            Toast.makeText(getContext(), R.string.toast_without_address_correctly, Toast.LENGTH_SHORT).show();
        }
    }

    //endregion

    @Override
    public void listaRecibida(OrderNoteGet body) {
        try {
            if (body != null) {
                lstcomments = new ArrayList<OrderNote>();
                OrderNote clientNote = setClientNote();
                if (order.getNote() != "" && clientNote != null) { //If order have note and clientNote is different of null
                    // If list of comments have elements and the first of list is not 0 (its means that is clientnote add in list) OR list of comments NO have elements
                    if ((lstcomments.size() > 0 && lstcomments.get(0).getId() != "0") || lstcomments.size() == 0) {
                        clientNote.setId("0"); //SET Id of clientNote to 0.
                        lstcomments.add(clientNote); //And clientNote in list of comments
                    }
                }
                if (body.getOrder_notes().isEmpty() && clientNote == null) { //if list of orders is empty and clientNote is null
                    comCount = 0; //Count of comments is 0.
                } else {
                    lstcomments.addAll(body.getOrder_notes()); // All all list of comments BBDD in object list of comments.
                    order.setLista_comments(lstcomments); //Set this list in order object.
                    comCount = order.getLista_comments().size(); // Count of comments is the size of list of comments
                }
                commentsCount.setText(String.valueOf(comCount)); //Set the value of field in detail of order
            }
        } catch (Exception ex) {
        }
        if(progress!= null)  progress.dismiss(); // Close progress dialog
    }

    @Override
    public void listaRecibida(OrderSearch body) {

    }

    @Override
    public void errorRecibido(Object error) {

    }

    @Override
    public void closedialog() {
        if(progress!= null)  progress.dismiss();
    }

    @Override
    public void arrayRecibido(ArrayList<String> body) {

    }

    @Override
    public void areaDeliveryRecived(ArrayList<Reparto> body) {

    }

    @Override
    public void stringReceived(String namefunction, String body) {

    }

    @Override
    public void orderReceived(OrderUpdate orderNew) {
        if(orderNew!=null) {
            order = orderNew.getOrder();
            SharedPreferences prefs = getActivity().getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(Constants.PREFERENCES_IS_ORDER_CHANGED,false);
            editor.commit();

            //region Create and Show progress dialog
            progress = new ProgressDialog(getContext());
            progress.show();
            progress.setContentView(R.layout.custom);
            progress.setCanceledOnTouchOutside(false);

            TextView text = (TextView) progress.findViewById(R.id.text);
            ImageView image = (ImageView) progress.findViewById(R.id.zasSpin);

            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.zas_spin);
            image.startAnimation(animation);
            //endregion

            setValues();

            progress.dismiss();// Close progress dialog
        }
    }

    @Override
    public void notMaxTime() {

    }

    //Load Note of client
    public OrderNote setClientNote()
    {
        OrderNote clientNote = new OrderNote();
        Order orderNew = new Order();
        orderNew.getOrderstatus();

        if(order.getNote() != "") {
            String notetext = order.getNote();
            if(order.getNote().contains("[:es]"))
            {
                notetext = notetext.replace("[:es]","").replace("[:]","");
            }
            if(!notetext.equals(""))
            {
                clientNote.setNote(notetext);
                clientNote.setCreatedAt(order.getCreated_at());
            }
            else clientNote = null;
        }
        return  clientNote;
    }

    public void addcomment_location(double dlat, double dlong){
        String strLocation = dlat+","+dlong;

        //region Indicates the service call addOrderNote
        Globals g = Globals.getInstance();
        try {
            g.setServiceCode(Constants.SERVICE_CODE_order_saveLocation);
            restHelper = new RetrofitDelegateHelper(order.getId(), 0);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        //restHelper.addOrderNote(this);
        restHelper.saveLocation(this,strLocation);
        //endregion
    }

    @Override
    public void hascreatecomment(Boolean bResult) {
        if(bResult)
        {
            Toast.makeText(getContext(),getContext().getResources().getString(R.string.toast_location), Toast.LENGTH_LONG).show();
            setComments(getView());
        }
    }
}
