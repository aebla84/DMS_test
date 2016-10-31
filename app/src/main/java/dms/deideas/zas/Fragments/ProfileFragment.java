package dms.deideas.zas.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import dms.deideas.zas.Activities.MainActivity;
import dms.deideas.zas.Constants;
import dms.deideas.zas.Globals;
import dms.deideas.zas.R;

/**
 * Created by dmadmin on 31/05/2016.
 */
public class ProfileFragment extends Fragment {

    private String title;

    private TextView state,name,email,areadelivery;


    public ProfileFragment() {
    }

    public static ProfileFragment newInstance(String title) {

        Bundle b = new Bundle();
        b.putString("title", title);
        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        title = getArguments().getString("title");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);

        //getActivity().setTitle(getArguments().getString("title"));
        ((MainActivity) getActivity()).setTitle(getArguments().getString("title"));

        state = (TextView) view.findViewById(R.id.state);
        name = (TextView) view.findViewById(R.id.name);
        email = (TextView) view.findViewById(R.id.email);
        areadelivery = (TextView) view.findViewById(R.id.txtareadelivery);

        final SharedPreferences prefs = getActivity().getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE);
        String names =  prefs.getString(Constants.PREFERENCES_USER_DISPLAYNAME,"");

        Globals g = Globals.getInstance();
        g.setIdFragment(Constants.HOMEFRAGMENT_CODE);

        return view;
    }

    @Override
    public void onStart() {

        super.onResume();

        final SharedPreferences prefs = getActivity().getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE);

        //state.setText();
        name.setText(prefs.getString(Constants.PREFERENCES_USER_DISPLAYNAME, ""));
        email.setText(prefs.getString(Constants.PREFERENCES_USER_EMAIL, ""));
        areadelivery.setText(prefs.getString(Constants.PREFERENCES_AREA_DELIVERY_STRING, ""));
    }
}
