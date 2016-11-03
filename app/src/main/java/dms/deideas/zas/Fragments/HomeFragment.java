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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import dms.deideas.zas.Activities.MainActivity;
import dms.deideas.zas.Adapters.OrderAdapter;
import dms.deideas.zas.Constants;
import dms.deideas.zas.Globals;
import dms.deideas.zas.Model.Order;
import dms.deideas.zas.Model.Reparto;
import dms.deideas.zas.Push.MyFirebaseInstanceIDService;
import dms.deideas.zas.R;
import dms.deideas.zas.Services.MotodriverGet;
import dms.deideas.zas.Services.MotodriverService;
import dms.deideas.zas.Services.OrderCount;
import dms.deideas.zas.Services.OrderSearch;
import dms.deideas.zas.Services.OrderService;
import dms.deideas.zas.Services.OrderUpdate;
import dms.deideas.zas.Services.Retrofit.RetrofitDelegateHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by dmadmin on 31/05/2016.
 */

/**
 * Fragmento Home
 */
public class HomeFragment extends Fragment implements View.OnClickListener, RetrofitDelegateHelper.numOrders, RetrofitDelegateHelper.AlRecibirListaDelegate {

    //region Declare variables
    private ImageButton order;
    private ImageButton myorder;
    private TextView numOrders;
    private TextView numMyOrders;
    private Integer int_numOrders = 0;
    private Integer int_numMyOrders = 0;
    private RetrofitDelegateHelper restHelper;
    private ProgressDialog progress;
    private Integer idUser;
    private String areadelivery;
    private SharedPreferences prefs;
    //endregion

    public static HomeFragment newInstance(String title) {
        HomeFragment fragment = new HomeFragment();
        Bundle b = new Bundle();
        b.putString(Constants.ARGUMENT_TITLE, title);
        fragment.setArguments(b);
        return fragment;
    }

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);

        ((MainActivity) getActivity()).setTitle(getArguments().getString(Constants.ARGUMENT_TITLE));

        readPreferences();
        setUpView(view);

        Globals g = Globals.getInstance();
        g.setIdFragment(Constants.HOMEFRAGMENT_CODE);
        g.setIdFragmentHistorical(0);

        return view;
    }

    private void readPreferences() {
        prefs = getActivity().getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE);
        idUser = prefs.getInt(Constants.PREFERENCES_USER_ID, 0);
        int_numOrders = prefs.getInt(Constants.PREFERENCES_NUMBERS_ORDERS, 0);
        int_numMyOrders = prefs.getInt(Constants.PREFERENCES_NUMBERS_ORDERS_ACCEPTED, 0);
        areadelivery =  prefs.getString(Constants.PREFERENCES_AREA_DELIVERY, "");
    }

    private void setUpView(View view) {

        order = (ImageButton) view.findViewById(R.id.order);
        myorder = (ImageButton) view.findViewById(R.id.myorder);
        numOrders = (TextView) view.findViewById(R.id.numOrders);
        numMyOrders = (TextView) view.findViewById(R.id.numMyOrders);

        order.setOnClickListener(this);
        myorder.setOnClickListener(this);

        //Get number of orders
        getNumOrders();
    }

    @Override
    public void onClick(View v) {

        if (v == order) {

            String title = getResources().getString(R.string.orders_sb_title);
            OrdersFragment fragment = OrdersFragment.newInstance(title);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frame, fragment)
                    .addToBackStack(Constants.ORDERSFRAGMENT)
                    .commit();
            ((MainActivity) getActivity()).setTitle(title);
        } else {
            String title = getResources().getString(R.string.myorders_sb_title);

            String motodriver = Integer.toString(idUser);
            MyOrdersFragment fragment = MyOrdersFragment.newInstance(title, motodriver);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frame, fragment)
                    .addToBackStack(Constants.MYORDERSFRAGMENT)
                    .commit();
            ((MainActivity) getActivity()).setTitle(title);

        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void getNumOrders() {

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

        //region Call getOrdersCountByAreaDelivery and getOrdersCountByUserAreaDelivery
        Retrofit retrofit;
        Globals g = Globals.getInstance();

        try {
            g.setServiceCode(Constants.SERVICE_CODE_order_count_byareadelivery);
            restHelper = new RetrofitDelegateHelper(0, 0,areadelivery);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        //restHelper.getOrdersCount(this);
        restHelper.getOrdersCountByAreaDelivery(this);

        try {
            g.setServiceCode(Constants.SERVICE_CODE_order_count_byuser_byareadelivery);
            restHelper = new RetrofitDelegateHelper(0, idUser,areadelivery);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        //restHelper.getOrdersCountByIdUser(this);
        restHelper.getOrdersCountByUserAreaDelivery(this);

        //endregion

        numOfOrdersAcceptedByDriver();
    }

    private void numOfOrdersAcceptedByDriver() {
        int intResult = 0;
        Globals g = Globals.getInstance();
        // Get orders accepted by idUser
        g.setServiceCode(Constants.SERVICE_CODE_order_accepted_byuser_byareadelivery);
        try {
            restHelper = new RetrofitDelegateHelper(0, idUser,areadelivery);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        //restHelper.getOrdersByUser(this);
        restHelper.getOrdersByUserAndArea(this);

    }

    public void setText(OrderCount body, String element) {
        SharedPreferences.Editor editor = prefs.edit();
        switch (element) {
            case "1":
                if(body!= null && body.getCount() > 0)
                {
                    numOrders.setText(String.valueOf(body.getCount()));
                    editor.putInt("numOrders", body.getCount());
                }
                else{

                    numOrders.setText("0");
                    editor.putInt("numOrders", 0);
                }
                editor.commit();
                break;
            case "2":
                if (body != null && body.getCount() > 0) {
                    numMyOrders.setText(String.valueOf(body.getCount()));
                    editor.putInt("numMyOrders", body.getCount());
                }
                else{
                        numMyOrders.setText("0");
                        editor.putInt("numMyOrders",0);
                    }
                editor.commit();
                progress.dismiss();
                break;
            default:
                break;
        }
    }

    @Override
    public void listaRecibida(OrderSearch body) {
        SharedPreferences.Editor editor = prefs.edit();
        if(body!= null){
            editor.putInt("numMyOrdersWithouProblems",body.getOrders().size());
        }
        else{
            editor.putInt("numMyOrdersWithouProblems",0);
        }
        editor.commit();
    }

    @Override
    public void errorRecibido(Object error) {

    }

    @Override
    public void closedialog() {

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
    public void orderReceived(OrderUpdate order) {

    }

    @Override
    public void notMaxTime() {

    }


}
