package dms.deideas.zas.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import dms.deideas.zas.Activities.MainActivity;
import dms.deideas.zas.Constants;
import dms.deideas.zas.Globals;
import dms.deideas.zas.Model.Incidencia;
import dms.deideas.zas.Model.Order;
import dms.deideas.zas.Model.OrderNote;
import dms.deideas.zas.R;
import dms.deideas.zas.Services.Retrofit.RetrofitDelegateHelper;


public class PhoneFragment extends Fragment implements View.OnClickListener, RetrofitDelegateHelper.response {


    private View view;
    private String title, phone,prevpage_title,originPage;
    private EditText newPhone;
    private ImageButton accept, cancel;
    private Order order;
    private RetrofitDelegateHelper restHelper;


    public PhoneFragment() {
        // Required empty public constructor
    }


    public static PhoneFragment newInstance(String title, String phone, Order order, String prevpage_title,String originPage) {
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("phone", phone);
        args.putSerializable("order", order);
        args.putString("prevpage_title", prevpage_title);
        args.putString("originPage", originPage);
        PhoneFragment fragment = new PhoneFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString("title");
            phone = getArguments().getString("phone");
            order = (Order)getArguments().getSerializable("order");
            prevpage_title = getArguments().getString("prevpage_title");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_phone, container, false);
        ((MainActivity) getActivity()).setTitle(getArguments().getString("title"));
        newPhone = (EditText)view.findViewById(R.id.newPhone);
        accept = (ImageButton)view.findViewById(R.id.accept);
        cancel = (ImageButton)view.findViewById(R.id.cancel);

        accept.setOnClickListener(this);
        cancel.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        if (v == accept) {
            addcomment_phone();
        } else if (v == cancel) {
            DetailMyOrderFragment fragment = DetailMyOrderFragment.newInstance(prevpage_title,order,originPage);
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
    public void onStart() {
        super.onResume();
    }

    public void addcomment_phone(){
        OrderNote orderNote = new OrderNote();
        //TODO
        String strphone = String.valueOf(newPhone.getText());
        if( strphone.length() == 0  || strphone.equals(getContext().getResources().getString(R.string.phone)))
            newPhone.setError( getContext().getResources().getString(R.string.phone_error) );
        else{   orderNote.setNote(getContext().getResources().getString(R.string.comment_phone) + strphone + ".");

            // Indicates the service call
            Globals g = Globals.getInstance();
            try {
                g.setServiceCode(Constants.SERVICE_CODE_notes_byuser);
                restHelper = new RetrofitDelegateHelper(order.getId(), 0, null, orderNote);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            }
            restHelper.addOrderNote(this);
        }

    }
    @Override
    public void hascreatecomment(Boolean bResult) {
        if(bResult)
        {
            SharedPreferences prefs = getActivity().getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(Constants.PREFERENCES_IS_ORDER_CHANGED,true);
            editor.commit();


            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(newPhone.getWindowToken(), 0);

            Toast.makeText(getContext(), getContext().getResources().getString(R.string.toast_phone), Toast.LENGTH_SHORT).show();
            DetailMyOrderFragment fragment = DetailMyOrderFragment.newInstance(prevpage_title,order,originPage);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frame, fragment)
                    .addToBackStack(null)
                    .commit();

            ((MainActivity) getActivity()).setTitle(title);

        }
    }
}
