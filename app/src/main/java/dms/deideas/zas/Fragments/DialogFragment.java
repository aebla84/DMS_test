package dms.deideas.zas.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import dms.deideas.zas.Activities.MainActivity;
import dms.deideas.zas.Constants;
import dms.deideas.zas.Globals;
import dms.deideas.zas.Model.Order;
import dms.deideas.zas.R;
import dms.deideas.zas.Services.Retrofit.RetrofitDelegateHelper;

/**
 * Created by dmadmin on 29/06/2016.
 */
public class DialogFragment extends android.support.v4.app.DialogFragment {

    private RetrofitDelegateHelper restHelper;

    private Globals g = Globals.getInstance();

    private Order order;
    private static Integer idUser = 0;

    private String toastMessage = "",originPage="";


    public static DialogFragment newInstance(String title, Order order, Integer intIdUser,String originPage) {
        Bundle b = new Bundle();
        idUser = intIdUser;
        b.putString("title", title);
        b.putString("originPage", originPage);
        b.putSerializable("order", order);
        DialogFragment fragment = new DialogFragment();
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        String title = getArguments().getString("title");
        order = (Order) getArguments().getSerializable("order");

        return new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.ic_access_time_black_24dp)
                .setTitle(title)
                .setPositiveButton(getResources().getString(R.string.accept), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String status = orderState(order.getOrderstatus());
                        Integer idUserDriver = 0;
                        //Toast.makeText(getContext(), toastMessage, Toast.LENGTH_SHORT).show();


                        // Making Post
                        if(g.getScreenCode() == Constants.screenCode_detailOrders){
                            //Toast.makeText(getContext(), " POST 7", Toast.LENGTH_SHORT).show();
                            g.setServiceCode(Constants.SERVICE_CODE_order_edit_acceptbymotodriver);
                        }
                        else if(g.getScreenCode() == Constants.screenCode_detailMyOrders){
                            idUserDriver = idUser;
                            //Toast.makeText(getContext(), " POST 0", Toast.LENGTH_SHORT).show();
                            g.setServiceCode(Constants.SERVICE_CODE_order_edit);
                        }
                        else if(g.getScreenCode() == Constants.screenCode_detailMyOrders_disallocate){
                            g.setServiceCode(Constants.SERVICE_CODE_order_edit_cancelbymotodriver);
                        }
                        else{
                            idUserDriver = 0; //Vamos a quitar el usuario de la orden.
                            g.setServiceCode(Constants.SERVICE_CODE_order_edit);
                        }


                        try {
                            restHelper = new RetrofitDelegateHelper(order.getId(),idUser);
                            SharedPreferences prefs = getActivity().getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE);
                            restHelper.updateStatus(order.getOrderstatus(),idUserDriver, prefs);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        } catch (InvalidKeyException e) {
                            e.printStackTrace();
                        }

                        if (g.getScreenCode() == Constants.screenCode_detailOrders || g.getScreenCode() == Constants.screenCode_detailMyOrders_disallocate) {

                            // return to Orders Fragment
                            String title = getResources().getString(R.string.orders_sb_title);
                            OrdersFragment fragment = OrdersFragment.newInstance(title);
                            FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager
                                    .beginTransaction()
                                    .replace(R.id.frame, fragment)
                                    .addToBackStack(null)
                                    .commit();

                            ((MainActivity) getActivity()).setTitle(title);


                        } else if (g.getScreenCode() == Constants.screenCode_detailMyOrders || g.getScreenCode() == Constants.screenCode_detailMyOrders_disallocateProblem) {
                            // return to My Orders Fragment
                            String title = getResources().getString(R.string.myorders_sb_title);
                            MyOrdersFragment fragment = MyOrdersFragment.newInstance(title, idUser.toString());
                            FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager
                                    .beginTransaction()
                                    .replace(R.id.frame, fragment)
                                    .addToBackStack(null)
                                    .commit();

                            ((MainActivity) getActivity()).setTitle(title);

                        }


                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (g.getScreenCode() == 0) {

                            // return to Detail Order Fragment
                            String title = getResources().getString(R.string.order_id_item) + " : " + String.valueOf(order.getId());
                            DetailOrderFragment fragment = DetailOrderFragment.newInstance(title, order,originPage);
                            FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager
                                    .beginTransaction()
                                    .replace(R.id.frame, fragment)
                                    .addToBackStack(null)
                                    .commit();
                            ((MainActivity) getActivity()).setTitle(title);

                        } else if (g.getScreenCode() == 1) {
                            // return to Detail Order Fragment
                            String title = getResources().getString(R.string.order_id_item) + " : " + String.valueOf(order.getId());
                            DetailMyOrderFragment fragment = DetailMyOrderFragment.newInstance(title, order,originPage);
                            FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager
                                    .beginTransaction()
                                    .replace(R.id.frame, fragment)
                                    .addToBackStack(null)
                                    .commit();
                            ((MainActivity) getActivity()).setTitle(title);
                        }
                    }
                })
                .create();
    }

    private String orderState(String orderstatus) {

        switch (orderstatus) {
            case Constants.ORDER_STATUS_rest_has_accepted:
                toastMessage = getResources().getString(R.string.toast_order_accepted);
                break;
            case Constants.ORDER_STATUS_driver_has_accepted:
                toastMessage = getResources().getString(R.string.toast_in_rest);
                break;
            case Constants.ORDER_STATUS_driver_in_rest:
                toastMessage = getResources().getString(R.string.toast_collected);
                break;
            case Constants.ORDER_STATUS_driver_on_road:
                toastMessage = getResources().getString(R.string.toast_finished);
                break;
            default:
                orderstatus = orderstatus;
                break;

        }
        return toastMessage;
    }
}
