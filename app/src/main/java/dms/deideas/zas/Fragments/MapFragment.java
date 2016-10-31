package dms.deideas.zas.Fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.gms.maps.SupportMapFragment;

import dms.deideas.zas.Activities.MainActivity;
import dms.deideas.zas.Constants;
import dms.deideas.zas.Globals;
import dms.deideas.zas.Model.Order;
import dms.deideas.zas.R;


public class MapFragment extends SupportMapFragment {

    private Order order;
    public MapFragment() {
    }

    public static MapFragment newInstance(String title) {

        Bundle b = new Bundle();
        b.putString("title", title);

        MapFragment fragment = new MapFragment();

        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = super.onCreateView(inflater, container, savedInstanceState);

        Globals g = Globals.getInstance();
        g.setIdFragment(Constants.COMMONFRAGMENT_CODE);

        ((MainActivity) getActivity()).setTitle(getArguments().getString("title"));

        return root;
    }
}
