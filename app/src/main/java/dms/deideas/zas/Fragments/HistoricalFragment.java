package dms.deideas.zas.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import dms.deideas.zas.Adapters.HistoricalAdapter;
import dms.deideas.zas.Constants;
import dms.deideas.zas.Globals;
import dms.deideas.zas.Model.Order;
import dms.deideas.zas.Model.Reparto;
import dms.deideas.zas.R;
import dms.deideas.zas.Services.OrderSearch;
import dms.deideas.zas.Services.OrderUpdate;
import dms.deideas.zas.Services.Retrofit.RetrofitDelegateHelper;

/**
 * Created by dmadmin on 31/05/2016.
 */


public class HistoricalFragment extends Fragment implements  RetrofitDelegateHelper.AlRecibirListaDelegate, HistoricalAdapter.HistoricalListener{

    //region Declare variables
    private String title,originPage="History";
    private String motodriver;
    private RecyclerView recycler;
    private HistoricalAdapter adapter;
    // Inicializar el layout manager
    private LinearLayoutManager layout;
    private RetrofitDelegateHelper restHelper;
    private View view;
    private ProgressDialog progress;
    //endregion

    public HistoricalFragment() {

    }

    public static HistoricalFragment newInstance(String title, String motodriver) {

        Bundle b = new Bundle();
        b.putString(Constants.ARGUMENT_TITLE, title);
        b.putString(Constants.ARGUMENT_MOTODRIVER, motodriver);

        HistoricalFragment fragment = new HistoricalFragment();
        fragment.setArguments(b);
        return fragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.historical_fragment, container, false);

        ((MainActivity) getActivity()).setTitle(getArguments().getString(Constants.ARGUMENT_TITLE));
        recycler = (RecyclerView) view.findViewById(R.id.recycle);
        recycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recycler.setAdapter(adapter);
        recycler.setHasFixedSize(true);

        Globals g = Globals.getInstance();
        g.setIdFragment(Constants.HOMEFRAGMENT_CODE);
        g.setIdFragmentHistorical(Constants.HISTORICALFRAGMENT_CODE);

        return view;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = getArguments().getString(Constants.ARGUMENT_TITLE);
        motodriver = getArguments().getString(Constants.ARGUMENT_MOTODRIVER);
        adapter = new HistoricalAdapter(this);

        //region Order History Orders
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
        //endregion

        final SharedPreferences prefs
                = getActivity().getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE);
        int idUser = prefs.getInt(Constants.PREFERENCES_USER_ID, 0);

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

        //region Indicates the service call -- getOrderHistorical
        Globals g = Globals.getInstance();
        g.setServiceCode(Constants.SERVICE_CODE_history);
        try {
            restHelper = new RetrofitDelegateHelper(0, idUser);
            restHelper.getOrderHistorical(this);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        //endregion
    }

    @Override
    public void onStart() {
        super.onResume();
    }

    @Override
    public void listaRecibida(OrderSearch body) {

        if (body!=null && body.getOrders() != null) {
            adapter.add(body.getOrders());
        }
        else
        {
            int lengthLong = Toast.LENGTH_LONG;
            Toast.makeText(view.getContext(), R.string.toast_No_orders_ended, lengthLong);
        }
    }

    @Override
    public void errorRecibido(Object error) {
        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
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
                .addToBackStack(Constants.DETAILMYORDERSFRAGMENT)
                .commit();

        ((MainActivity) getActivity()).setTitle(title);

    }
}
