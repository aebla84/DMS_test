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
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import dms.deideas.zas.Activities.MainActivity;
import dms.deideas.zas.Adapters.OrderAdapter;
import dms.deideas.zas.Adapters.OrderListIncidenciasAdapter;
import dms.deideas.zas.Adapters.OrderNoteAdapter;
import dms.deideas.zas.Constants;
import dms.deideas.zas.Globals;
import dms.deideas.zas.Model.Incidencia;
import dms.deideas.zas.Model.Order;
import dms.deideas.zas.Model.OrderNote;
import dms.deideas.zas.Model.Reparto;
import dms.deideas.zas.R;
import dms.deideas.zas.Services.OrderSearch;
import dms.deideas.zas.Services.OrderUpdate;
import dms.deideas.zas.Services.Retrofit.RetrofitDelegateHelper;

/**
 * Created by bnavarro on 12/07/2016.
 */
public class OrderIncidenciasFragment extends Fragment implements RetrofitDelegateHelper.AlRecibirListaDelegate,RetrofitDelegateHelper.response {

    private RetrofitDelegateHelper restHelper;
    private String title;

    private int id_order = 0;
    private Order   order;
    private String typeOfProblem;
    private Spinner sp_typeOfProblem;
    private String strTypeOfProblem;
    private View view;
    private View view2;
    private ImageButton FAB;
    private ImageButton btnSaveComment;
    private ImageButton btnCancelComment;
    private EditText et_addcomment;
    private LinearLayout linearLayout_editcomment;
    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private Incidencia problem_details;
    private TextView tv_nocomments;
    private ProgressDialog progress;
    private String oldstatus = "";
    private String originPage ="";

    private Integer idUser;
    private SharedPreferences prefs;

    private Boolean isOrderChanged = false;

    public OrderIncidenciasFragment() {
        // Required empty public constructor
    }

    // Inicializar Incidencias
    private ArrayList<Incidencia> lstincidencias = new ArrayList<Incidencia>();
    private ArrayList<Incidencia> lstincidenciasAux;

    // Inicializar el layout manager
    private LinearLayoutManager layout;


    public static OrderIncidenciasFragment newInstance(String title, Order order,String originPage) {
        OrderIncidenciasFragment fragment = new OrderIncidenciasFragment();

        Bundle b = new Bundle();
        b.putString("title", title);
        b.putString("originPage", originPage);
        b.putInt("idOrder", order.getId());
        ArrayList<Incidencia> lstIncidencias = (ArrayList<Incidencia>) order.getLista_incidencias();
        b.putSerializable("lstincidencias", lstIncidencias);
        b.putSerializable("order", order);

        fragment.setArguments(b);
        return fragment;
    }

    private void readPreferences() {
        final SharedPreferences prefs = getActivity().getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE);
        idUser = prefs.getInt(Constants.PREFERENCES_USER_ID, 0);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id_order = getArguments().getInt("idOrder");
            title = getArguments().getString("title");
            lstincidencias = (ArrayList<Incidencia>) getArguments().getSerializable("lstincidencias");
            typeOfProblem = getArguments().getString("typeOfProblem");
            order = (Order)getArguments().getSerializable("order");
            oldstatus = order.getOrderstatus();
        }
        adapter = new OrderListIncidenciasAdapter(lstincidencias);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        readPreferences();

        view = inflater.inflate(R.layout.incidencias_orders_fragment, container, false);
        //tv_nocomments = (TextView) view.findViewById(R.id.tv_nocomments);
        ((MainActivity) getActivity()).setTitle(title);

        Globals g = Globals.getInstance();
        g.setIdFragment(Constants.COMMONFRAGMENT_CODE);

        FAB = (ImageButton) view.findViewById(R.id.fab_addcomment);
        linearLayout_editcomment = (LinearLayout) view.findViewById(R.id.send_message);
        et_addcomment = (EditText) view.findViewById(R.id.write_comment);
        btnSaveComment = (ImageButton) view.findViewById(R.id.send_comment);
        sp_typeOfProblem = (Spinner) view.getRootView().findViewById(R.id.problems_spinner);
        strTypeOfProblem = String.valueOf(sp_typeOfProblem.getSelectedItem());
        btnCancelComment = (ImageButton) view.findViewById(R.id.cancel_comment);


        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(view.getContext(),
                R.array.problems_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        sp_typeOfProblem.setAdapter(adapter2);

        loadIncidencias(lstincidencias);

        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                linearLayout_editcomment.setVisibility(View.VISIBLE);
            }
        });
        btnSaveComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                strTypeOfProblem = String.valueOf(sp_typeOfProblem.getSelectedItem());
                if(validate()) {
                    lstincidenciasAux = new ArrayList<Incidencia>();
                    Incidencia incidenciaObj = new Incidencia();
                    //incidenciaObj.setPosition("0");
                    List<String> lstnew = new ArrayList<String>();
                    if(et_addcomment.getText().toString().equals("")){
                        lstnew.add(" ");
                    } else {
                        lstnew.add(et_addcomment.getText().toString());
                    }
                    incidenciaObj.setLstdescription(lstnew);
                    incidenciaObj.setTypeofincidencia(convertTypeOfProblem(strTypeOfProblem));
                    addIncidencia(incidenciaObj);

                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(et_addcomment.getWindowToken(), 0);

                    linearLayout_editcomment.setVisibility(View.INVISIBLE);

                }
            }
        });
        btnCancelComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp_typeOfProblem.setSelection(0);
                et_addcomment.setText("");
                et_addcomment.clearFocus();
                linearLayout_editcomment.setVisibility(View.INVISIBLE);
            }
        });

        return view;
    }


    @Override
    public void listaRecibida(OrderSearch body) {

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


    public void loadIncidencias(ArrayList<Incidencia> lstincidencias) {

        if(lstincidencias!=null){

            adapter = new OrderListIncidenciasAdapter(lstincidencias);
            adapter.notifyDataSetChanged();
            recycler = (RecyclerView) view.findViewById(R.id.recycle2);
            recycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
            recycler.setAdapter(adapter);
            //tv_nocomments.setVisibility(View.INVISIBLE);
            if(progress!= null)  progress.dismiss();
        }
        else
        {
           // tv_nocomments.setVisibility(View.VISIBLE);
        }

    }

    public void addIncidencia(Incidencia incidenciaObj) {
        /*progress = ProgressDialog.show(getContext(), null, "Cargando", false, false);
        progress.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));*/

        progress = new ProgressDialog(getContext());
        progress.show();
        progress.setContentView(R.layout.custom);
        progress.setCanceledOnTouchOutside(false);


        TextView text = (TextView) progress.findViewById(R.id.text);
        ImageView image = (ImageView) progress.findViewById(R.id.zasSpin);

        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.zas_spin);
        image.startAnimation(animation);

        // Indicates the service call
        Globals g = Globals.getInstance();
        try {
            g.setServiceCode(Constants.SERVICE_CODE_problem_add_completed);
            restHelper = new RetrofitDelegateHelper(id_order, 0, incidenciaObj);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        restHelper.addIncidencia(true,this);
    }

    public Boolean validate()
    {
        Boolean bResult = false;
   /* bResult = (et_addcomment.getText().length()>0)?true:false;
    if(et_addcomment.getText().length()<=0)
    {
        bResult = false;
        et_addcomment.setError(getResources().getString(R.string.alert_edittext_problem));
    }
    else
    { bResult = true;}*/
        if(strTypeOfProblem.equals("Elige el tipo de incidencia"))
        {
            bResult = false;
            TextView errorText = (TextView)sp_typeOfProblem.getSelectedView();
            errorText.setText(getResources().getString(R.string.alert_spinner_problem));//changes the selected item text to this
        }
        else
        { bResult = true;}
        return bResult;
    }

    public String convertTypeOfProblem(String strTypeOfProblem) {
        String strResult = "";
        switch (strTypeOfProblem) {
            case Constants.PROBLEM_es_drop_food_str:
                strResult = Constants.PROBLEM_drop_food_str;
                break;
            case Constants.PROBLEM_es_wrong_plate_str:
                strResult = Constants.PROBLEM_wrong_plate_str;
                break;
            case Constants.PROBLEM_es_wrong_order_str:
                strResult = Constants.PROBLEM_wrong_order_str;
                break;
            case Constants.PROBLEM_es_forget_plate_str:
                strResult = Constants.PROBLEM_forget_plate_str;
                break;
            case Constants.PROBLEM_es_drop_drink_str:
                strResult = Constants.PROBLEM_drop_drink_str;
                break;
            case Constants.PROBLEM_es_wrong_drink_str:
                strResult = Constants.PROBLEM_wrong_drink_str;
                break;
            case Constants.PROBLEM_es_forget_drink_str:
                strResult = Constants.PROBLEM_forget_drink_str;
                break;
            case Constants.PROBLEM_es_other_str:
                strResult = Constants.PROBLEM_other_str;
                break;
            default:
                break;

        }
        return strResult;
    }


    @Override
    public void hascreatecomment(Boolean bResult) {
        if(bResult)
        {
            Toast.makeText(getContext(), "Incidencia creada correctamente", Toast.LENGTH_SHORT).show();
            title = getResources().getString(R.string.order_id_item) + " : " + String.valueOf(id_order);


            DetailMyOrderFragment fragment = DetailMyOrderFragment.newInstance(title, order,originPage);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager
                .beginTransaction()
                .replace(R.id.frame, fragment)
                .addToBackStack("detailmyorders")
                .commit();

           /*     if(oldstatus.equals(Constants.ORDER_STATUS_problem))
            { }
            else
            {
                 String title = getResources().getString(R.string.myorders_sb_title);
                MyOrdersFragment fragment = MyOrdersFragment.newInstance(title, idUser.toString());
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.frame, fragment)
                        .addToBackStack("myorders")
                        .commit();
           /* }*/


            getActivity().setTitle(title);

            //Save isOrderChange
            SharedPreferences prefs =
                    getActivity().getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(Constants.PREFERENCES_IS_ORDER_CHANGED,true);
            editor.commit();

            if(progress!= null)  progress.dismiss();
        }
    }
}