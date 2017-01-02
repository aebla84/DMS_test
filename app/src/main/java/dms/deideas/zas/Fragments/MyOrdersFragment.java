package dms.deideas.zas.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Comparator;

import dms.deideas.zas.Activities.MainActivity;
import dms.deideas.zas.Adapters.OrderAdapter;
import dms.deideas.zas.Constants;
import dms.deideas.zas.Globals;
import dms.deideas.zas.Model.Order;
import dms.deideas.zas.Model.Reparto;
import dms.deideas.zas.R;
import dms.deideas.zas.Services.OrderSearch;
import dms.deideas.zas.Services.OrderUpdate;
import dms.deideas.zas.Services.Retrofit.RetrofitDelegateHelper;


public class MyOrdersFragment extends Fragment implements View.OnClickListener, RetrofitDelegateHelper.AlRecibirListaDelegate, OrderAdapter.OrderListener {

    private String title,originPage="MyOrders";
    private String motodriver;
    private RecyclerView recycler;
    private OrderAdapter adapter;
    private TextView countOrders;
    private Integer numTotalOrders = 0;
    private ProgressDialog progress;
    private Integer idUser;
    private SharedPreferences prefs;
    private Integer int_numOrders = 0;
    private Integer int_numMyOrders = 0;
    private Integer inumMyOrdersWithouProblems = 0;
    private String areadelivery = "";
    private Integer countListOrder = 0;
    private String timeMax = "";

    // Inicializar el layout manager
    private LinearLayoutManager layout;

    private RetrofitDelegateHelper restHelperOrders;
    private RetrofitDelegateHelper restHelperProblems;

    private Boolean isOrderChanged;

    private View view;

    public MyOrdersFragment() {
    }

    public static MyOrdersFragment newInstance(String title, String motodriver) {

        Bundle b = new Bundle();
        b.putString("title", title);
        b.putString("motodriver", motodriver);

        MyOrdersFragment fragment = new MyOrdersFragment();
        fragment.setArguments(b);
        return fragment;

    }

    private void readPreferences(){
        prefs = getActivity().getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE);
        idUser = prefs.getInt(Constants.PREFERENCES_USER_ID, 0);
        int_numOrders = prefs.getInt(Constants.PREFERENCES_NUMBERS_ORDERS, 0);
        int_numMyOrders = prefs.getInt(Constants.PREFERENCES_NUMBERS_ORDERS_ACCEPTED, 0);
        inumMyOrdersWithouProblems = prefs.getInt(Constants.PREFERENCES_NUMBERS_ORDERS_ACCEPTEDBYDRIVER, 0);
        areadelivery = prefs.getString(Constants.PREFERENCES_AREA_DELIVERY, "");
        isOrderChanged = prefs.getBoolean(Constants.PREFERENCES_IS_ORDER_CHANGED, false);
        timeMax = prefs.getString(Constants.PREFERENCES_MAXTIME_ORDERS_CHANGE_MAXPRIORITY, "0");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        readPreferences();
        title = getArguments().getString("title");
        motodriver = getArguments().getString("motodriver");

        adapter = new OrderAdapter(this,timeMax);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.my_orders_fragment, container, false);
        ((MainActivity) getActivity()).setTitle(getArguments().getString("title"));

        countOrders = (TextView) view.findViewById(R.id.numOrders);
        if (countOrders != null) {
            countOrders.setText(String.valueOf(adapter.getOrdersCount()));
        }
        numTotalOrders = 0;
        countListOrder = 0;

        loadlistOrders();

        recycler = (RecyclerView) view.findViewById(R.id.recycle);
        recycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recycler.setAdapter(adapter);
        recycler.getAdapter().notifyDataSetChanged();
        recycler.setHasFixedSize(true);

        Globals g = Globals.getInstance();
        g.setIdFragment(Constants.HOMEFRAGMENT_CODE);

        return view;
    }


    @Override
    public void onClick(View v) {
    }
    @Override
    public void onStart() {
        super.onResume();
    }

    public void loadlistOrders() {

        progress = new ProgressDialog(getContext());
        progress.show();
        progress.setContentView(R.layout.custom);
        progress.setCanceledOnTouchOutside(false);


        TextView text = (TextView) progress.findViewById(R.id.text);
        ImageView image = (ImageView) progress.findViewById(R.id.zasSpin);

        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.zas_spin);
        image.startAnimation(animation);

        adapter = new OrderAdapter(this,timeMax);
        adapter.setComparador(new Comparator<Order>() {
            @Override
            public int compare(Order lhs, Order rhs) {

                String statusL = lhs.getOrderstatus();
                String statusR = rhs.getOrderstatus();

                if (statusL.equalsIgnoreCase(statusR)) {
                    return 0;
                }
                if (statusL.equalsIgnoreCase(Constants.ORDER_STATUS_problem)) {
                    return -1;
                }
                if (statusR.equalsIgnoreCase(Constants.ORDER_STATUS_problem)) {
                    return 1;
                }

                return 0;
            }
        });


        // Indicates the service call
        Globals g = Globals.getInstance();

        g.setServiceCode(Constants.SERVICE_CODE_order_problem_byuser);
        try {
            delegateHelper(g.getServiceCode());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Get orders accepted by idUser
                //g.setServiceCode(Constants.SERVICE_CODE_order_accepted_byuser);
                Globals g = Globals.getInstance();
                g.setServiceCode(Constants.SERVICE_CODE_order_accepted_byuser_byareadelivery);
                try

                {
                    delegateHelper(g.getServiceCode());
                } catch (
                        UnsupportedEncodingException e
                        ) {
                    e.printStackTrace();
                } catch (
                        NoSuchAlgorithmException e
                        ) {
                    e.printStackTrace();
                } catch (
                        InvalidKeyException e
                        ) {
                    e.printStackTrace();
                }


            }
        }, Constants.RESPONSE_TIME);
    }

    private void delegateHelper(int id) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        switch (id) {
            case Constants.SERVICE_CODE_order_accepted_byuser://rest_has_accepted
                restHelperOrders = new RetrofitDelegateHelper(0,Integer.parseInt(motodriver),areadelivery);
                restHelperOrders.getOrdersByUser(this);
                break;
            case Constants.SERVICE_CODE_order_problem_byuser://incidencias
                restHelperProblems = new RetrofitDelegateHelper(0,Integer.parseInt(motodriver),areadelivery);
                restHelperProblems.getOrdersProblemByIdUser(this);
                break;
            case Constants.SERVICE_CODE_order_accepted_byuser_byareadelivery://rest_has_accepted
                restHelperOrders = new RetrofitDelegateHelper(0,Integer.parseInt(motodriver),areadelivery);
                restHelperOrders.getOrdersByUserAndArea(this);
                break;
            default:
                break;
        }

    }

    @Override
    public void listaRecibida(OrderSearch body) {

        if (body != null) {
            adapter.add(body.getOrders());
            adapter.notifyDataSetChanged();
            recycler.getAdapter().notifyDataSetChanged();
        }
        numTotalOrders =numTotalOrders+ body.getCountOrders();
        countOrders.setText(String.valueOf(numTotalOrders));
    }

    @Override
    public void errorRecibido(Object error) {
        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void closedialog() {
        countListOrder++;
        if ( countListOrder == 2)
            if (progress != null && countListOrder == 2)
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
    public void orderReceived(OrderUpdate order) {

    }

    @Override
    public void notMaxTime() {

    }


    @Override
    public void onOrderClicked(View card, Order order) {

        String title = getResources().getString(R.string.order_id_item) + " : " + String.valueOf(order.getId());

        DetailMyOrderFragment fragment = DetailMyOrderFragment.newInstance(title, order,originPage);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.frame, fragment)
                .addToBackStack("detailmyorders")
                .commit();


        //getActivity().setTitle(title);
        ((MainActivity) getActivity()).setTitle(title);

    }

}