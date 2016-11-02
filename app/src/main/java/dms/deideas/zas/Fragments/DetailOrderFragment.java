package dms.deideas.zas.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
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
import dms.deideas.zas.Push.MyFirebaseInstanceIDService;
import dms.deideas.zas.R;
import dms.deideas.zas.Services.MotodriverGet;
import dms.deideas.zas.Services.MotodriverService;
import dms.deideas.zas.Services.OrderNoteGet;
import dms.deideas.zas.Services.OrderSearch;
import dms.deideas.zas.Services.OrderUpdate;
import dms.deideas.zas.Services.Retrofit.RetrofitDelegateHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by dmadmin on 14/06/2016.
 */
public class DetailOrderFragment extends Fragment implements View.OnClickListener, OnMapReadyCallback,RetrofitDelegateHelper.AlRecibirListaDelegate, RetrofitDelegateHelper.AlRecibirListaCommentsDelegate {

    private Order order;
    private Integer idUser;

    private TextView id_order, restaurant_name,time_left_of_order, restaurant_direction;
    private ImageView phone_restaurant;
    private TextView customer_name,hour_order,customer_direction,commentsCount,problemsCount;
    private ImageView phone_customer;
    private TextView state_of_payment, observation;
    private Button accept, cancel;
    private RelativeLayout rellay_comments,relLay_detorder_statuspayment;
    private TextView incidents;
    private RelativeLayout dtorder_problems;
    private List<OrderNote> lstcomments;
    private ProgressDialog progress;

    private ImageView icRestaurantWhite;
    private ImageView icRestaurantRed;
    private TextView txtRestaurantWhite;
    private TextView txtRestaurantRed;

    private ImageView icMotoPink;
    private TextView txtMotoPink;

    private ImageView icRecogidoWhite;
    private ImageView icRecogidoRed;
    private TextView txtRecogidoWhite;
    private TextView txtRecogidoRed;

    private ImageView icIncidenceRed;
    private ImageView icIncidenceWhite;
    private TextView txtIncidenceRed;
    private TextView txtIncidenceWhite;


    private ImageView icFinishedWhite;
    private ImageView icFinishedRed;
    private TextView txtFinishedWhite;
    private TextView txtFinishedRed;

    private ImageView icFinishedIncidenceRed;
    private TextView txtFinishedIncidenceRed;
    private ImageView icFinishedIncidenceWhite;
    private TextView txtFinishedIncidenceWhite;

    private int MAP_CONTROL = 0;

    private List<String> notes;

    private Globals g = Globals.getInstance();

    private RetrofitDelegateHelper restHelperOrders;
    private RetrofitDelegateHelper restHelper;


    private int numOfOrdersAccepted = 0;
    private int numOfOrdersAcceptedByDriver = 0;
    private Integer numMaxOrdersAccepted_BBDD = 0;
    private int comCount = 0;
    private Boolean isOrderChanged;
    private SharedPreferences prefs;
    private String originPage;
    public DetailOrderFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.detail_order_fragment, container, false);

        Globals g = Globals.getInstance();
        g.setIdFragment(Constants.ORDERSFRAGMENT_CODE);
        g.setIdFragmentDetail(Constants.DETAILORDERSFRAGMENT_CODE);
        ((MainActivity) getActivity()).setTitle(getArguments().getString(Constants.ARGUMENT_TITLE));

        findViewById(view);
        readPreferences();
        lstcomments = new ArrayList<OrderNote>();
        setComments(view);
        setOnClickListener();
        setVisibility();

        if(isOrderChanged) {
            getNewOrder();
        }
        else{
            setValues();
        }

        return view;
    }

    private void findViewById(View view) {
        //Initiate Views
        //id_order = (TextView) view.findViewById(R.id.idorder);
        restaurant_name = (TextView)view.findViewById(R.id.restaurant_name);
        time_left_of_order = (TextView) view.findViewById(R.id.time_left_of_order);
        restaurant_direction = (TextView) view.findViewById(R.id.restaurant_direction);
        //phone_restaurant = (ImageView) view.findViewById(R.id.imgPhoneRestaurant);
        customer_name = (TextView) view.findViewById(R.id.customer_name);
        hour_order = (TextView) view.findViewById(R.id.hour_order);
        //hour_order_rest_accepted = (TextView) view.findViewById(R.id.hourOrderRestaurantAccepted);
        customer_direction = (TextView) view.findViewById(R.id.customer_direction);
        //phone_customer = (ImageView) view.findViewById(R.id.imgPhoneCustomer);
        state_of_payment = (TextView) view.findViewById(R.id.state_of_payment);
        relLay_detorder_statuspayment = (RelativeLayout) view.findViewById(R.id.relLay_detorder_statuspayment);
        accept = (Button) view.findViewById(R.id.accept);
        cancel = (Button) view.findViewById(R.id.cancel);
        rellay_comments = (RelativeLayout) view.findViewById(R.id.relLay_comments);
        incidents = (TextView) view.findViewById(R.id.incidents);
        dtorder_problems = (RelativeLayout)view.findViewById(R.id.relLay_dtorder_problems);
        commentsCount = (TextView) view.findViewById(R.id.commentsCount);
        problemsCount = (TextView) view.findViewById(R.id.problemsCount);


        icMotoPink = (ImageView) view.findViewById(R.id.icMotoPink);
        txtMotoPink = (TextView) view.findViewById(R.id.txtMotoPink);

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

        progress = new ProgressDialog(getContext(),R.style.CustomDialog);
    }

    private void readPreferences(){
        final SharedPreferences prefs = getActivity().getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE);
        idUser = prefs.getInt(Constants.PREFERENCES_USER_ID, 0);
        numOfOrdersAccepted = prefs.getInt("numMyOrders", 0);
        numOfOrdersAcceptedByDriver = prefs.getInt("numMyOrdersWithouProblems", 0);
        if(prefs.getString(Constants.PREFERENCES_NUMBER_MAX_ORDERS_ACCEPTED_BYDRIVER, "0") != null)
            numMaxOrdersAccepted_BBDD = Integer.valueOf(prefs.getString(Constants.PREFERENCES_NUMBER_MAX_ORDERS_ACCEPTED_BYDRIVER, "0"));
        else {numMaxOrdersAccepted_BBDD = 0;}
        isOrderChanged = prefs.getBoolean(Constants.PREFERENCES_IS_ORDER_CHANGED, false);

    }
    private void getNewOrder()
    {
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
        restaurant_name.setText(order.getRestaurant().getName());
        time_left_of_order.setText(order.getTimeKitchen() + " min");
        restaurant_direction.setText(order.getRestaurant().getStreet());
        //customer_name.setText(order.getShipping_address().getFirst_name() + " " + order.getShipping_address().getLast_name());
        customer_name.setText(order.getBilling_address().getFirst_name() + " " + order.getBilling_address().getLast_name());
        hour_order.setText(order.getCreated_at().substring(11, 16));
        //hour_order_rest_accepted.setText(order.getTime_rest_accepted().substring(11, 16));
        customer_direction.setText(order.getShipping_address().getAddress_1());
        /*if (order.getPayment_details().getMethod_title().contentEquals("Pagar en domicilio") && order.getPayment_details().getPaid() == false) {
            state_of_payment.setText("Pago en EFECTIVO ");
        } else {
            state_of_payment.setText("Pago ONLINE REALIZADO ");
        }*/
        if(order.getLista_incidencias() != null)
            problemsCount.setText(String.valueOf(order.getLista_incidencias().size()));
    }


    private void setOnClickListener() {
        restaurant_direction.setOnClickListener(this);
        //phone_restaurant.setOnClickListener(this);
        customer_direction.setOnClickListener(this);
        //phone_customer.setOnClickListener(this);
        accept.setOnClickListener(this);
        cancel.setOnClickListener(this);
        rellay_comments.setOnClickListener(this);
        incidents.setOnClickListener(this);
    }



    private void setVisibility() {
        state_of_payment.setVisibility(View.GONE);
        relLay_detorder_statuspayment.setVisibility(View.GONE);
        String status = order.getOrderstatus();
        if(numOfOrdersAcceptedByDriver < numMaxOrdersAccepted_BBDD || status.equals(Constants.ORDER_STATUS_problem) )
        {

            switch (status) {
                case Constants.ORDER_STATUS_rest_has_accepted:
                    cancel.setVisibility(View.VISIBLE);
                    break;
                case Constants.ORDER_STATUS_problem:
                    cancel.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        }
       else
        {
            accept.setVisibility(View.INVISIBLE);
        }

        order_status_visibility();

    }
    private void order_status_visibility() {

        String status = order.getOrderstatus();

        switch (status) {
            case Constants.ORDER_STATUS_rest_has_accepted:
                break;
            case Constants.ORDER_STATUS_problem:

                //Toast.makeText(getContext(), "1:"+status, Toast.LENGTH_SHORT).show();
                icMotoPink.setVisibility(View.GONE);
                txtMotoPink.setVisibility(View.GONE);

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

                break;

            default:
                break;

        }
        ArrayList<Incidencia> lstIncidencias = (ArrayList<Incidencia>) order.getLista_incidencias();
        if(lstIncidencias!=null && lstIncidencias.size()> 0)
        {
            dtorder_problems.setVisibility(View.VISIBLE);
        }
        else
        {
            dtorder_problems.setVisibility(View.GONE);
        }
    }


    public static DetailOrderFragment newInstance(String title, Order order,String originPage) {
        Globals g = Globals.getInstance();
        g.setScreenCode(Constants.screenCode_detailOrders);
        Bundle b = new Bundle();
        b.putString(Constants.ARGUMENT_TITLE, title);
        b.putString(Constants.ARGUMENT_ORIGINPAGE, originPage);
        b.putSerializable(Constants.ARGUMENT_ORDER, order);
        DetailOrderFragment fragment = new DetailOrderFragment();
        fragment.setArguments(b);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        order = (Order) getArguments().getSerializable(Constants.ARGUMENT_ORDER);
    }

    @Override
    public void onClick(View v) {

        if(v==rellay_comments){
            String title = getResources().getString(R.string.comments);
            OrderNoteFragment fragment = OrderNoteFragment.newInstance(title,order);
            loadNextFragment(fragment,title);
        }
        else if (v == accept) {
            DialogFragment dialogFragment = DialogFragment.newInstance(getResources().getString(R.string.accept_order), order, idUser,originPage);
            dialogFragment.show(getFragmentManager(), Constants.DIALOGFRAGMENT);
            g.setServiceCode(Constants.SERVICE_CODE_order_get);

        } else if (v == cancel) {

            String title = getResources().getString(R.string.orders_sb_title);
            OrdersFragment fragment = OrdersFragment.newInstance(title);
            loadNextFragment(fragment,title);
        }
        else if (v == incidents) {
            String title = getResources().getString(R.string.incidents);
            OrderIncidenciasFragment fragment = OrderIncidenciasFragment.newInstance(title, order,originPage);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frame, fragment)
                    .addToBackStack(null)
                    .commit();
            ((MainActivity) getActivity()).setTitle(title);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


        double latitude=0.0, longitude = 0.0;

        String title = null;

        // Restaurant Map called
        if (MAP_CONTROL == 0) {

            latitude = Double.parseDouble(order.getRestaurant().getData_map().getLat());
            longitude = Double.parseDouble(order.getRestaurant().getData_map().getLng());

            title = order.getRestaurant().getName();
        }
        // Customer Map called
        else if (MAP_CONTROL == 1) {
            latitude = 41.383150;
            longitude = 2.134603;
            title = order.getShipping_address().getFirst_name() + " " + order.getShipping_address().getLast_name();
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

    private void setComments(View v) {
        progress = new ProgressDialog(getContext());
        progress.show();
        progress.setContentView(R.layout.custom);
        progress.setCanceledOnTouchOutside(false);

        TextView text = (TextView) progress.findViewById(R.id.text);
        ImageView image = (ImageView) progress.findViewById(R.id.zasSpin);

        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.zas_spin);
        image.startAnimation(animation);

        int idOrder = order.getId();

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
    }

    public void loadNextFragment(Fragment fragment, String title){
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.frame, fragment)
                .addToBackStack(null)
                .commit();

        ((MainActivity) getActivity()).setTitle(title);
    }

    @Override
    public void listaRecibida(OrderSearch body) {

        if (body != null) {
            numOfOrdersAccepted = body.getOrders().size();
        }
    }

    @Override
    public void listaRecibida(OrderNoteGet body) {
        try {
            if (body != null) {
                OrderNote clientNote =  setClientNote();
                if(order.getNote() != "" && clientNote != null) {
                    clientNote.setId("0");
                    lstcomments.add(clientNote);
                }

                if (body.getOrder_notes().isEmpty() && clientNote == null) {
                    comCount = 0;
                } else {
                    lstcomments.addAll(body.getOrder_notes());
                    order.setLista_comments(lstcomments);
                    comCount = order.getLista_comments().size();
                }
                commentsCount.setText(String.valueOf(comCount));
            }
        } catch (Exception ex) {
        }
        progress.dismiss();
    }

    @Override
    public void errorRecibido(Object error) {
        progress.dismiss();
    }

    @Override
    public void closedialog() {
        progress.dismiss();
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
            SharedPreferences prefs =
                    getActivity().getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(Constants.PREFERENCES_IS_ORDER_CHANGED,false);
            editor.commit();

            progress = new ProgressDialog(getContext());
            progress.show();
            progress.setContentView(R.layout.custom);
            progress.setCanceledOnTouchOutside(false);


            TextView text = (TextView) progress.findViewById(R.id.text);
            ImageView image = (ImageView) progress.findViewById(R.id.zasSpin);

            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.zas_spin);
            image.startAnimation(animation);

            setValues();
            progress.dismiss();
        }
    }

    @Override
    public void notMaxTime() {

    }

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
            clientNote.setNote(notetext);
            clientNote.setCreatedAt(order.getCreated_at());
        }
        return  clientNote;
    }

}
