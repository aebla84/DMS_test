package dms.deideas.zas.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import dms.deideas.zas.Activities.MainActivity;
import dms.deideas.zas.Adapters.OrderNoteAdapter;
import dms.deideas.zas.Constants;
import dms.deideas.zas.Globals;
import dms.deideas.zas.Model.Order;
import dms.deideas.zas.Model.OrderNote;
import dms.deideas.zas.R;
import dms.deideas.zas.Services.OrderNoteGet;
import dms.deideas.zas.Services.Retrofit.RetrofitDelegateHelper;

/**
 * Created by bnavarro on 12/07/2016.
 */
public class OrderNoteFragment extends Fragment implements RetrofitDelegateHelper.AlRecibirListaCommentsDelegate, RetrofitDelegateHelper.response {

    private static Order orderSelected;
    private RetrofitDelegateHelper restHelper;
    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private String title;
    private int id_order = 0;
    private View view;
    private ImageButton FAB;
    private ImageButton btnSaveComment;
    private EditText et_addcomment;
    private LinearLayout linearLayout_editcomment;
    private OrderNote order_note;
    private TextView tv_nocomments;
    private Boolean bResult = false;
    private ProgressDialog progress;
    private Boolean isOrderChanged = false;
    private SharedPreferences prefs;

    // Inicializar Comentarios
    private List<OrderNote> lstcomments = new ArrayList<OrderNote>();
    // Inicializar el layout manager
    private LinearLayoutManager layout;

    public OrderNoteFragment() {
        // Required empty public constructor
        order_note = new OrderNote();
    }

    public static OrderNoteFragment newInstance(String title, Order order) {
        OrderNoteFragment fragment = new OrderNoteFragment();
        Bundle b = new Bundle();
        b.putString("title", title);
        b.putInt("idOrder", order.getId());
        orderSelected = order;
        fragment.setArguments(b);

        return fragment;
    }

    private void readPreferences() {
        prefs = getActivity().getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        readPreferences();
        if (getArguments() != null) {
            id_order = getArguments().getInt("idOrder");
            title = getArguments().getString("title");
        }
        if(orderSelected.getLista_comments() != null)
            lstcomments.addAll(orderSelected.getLista_comments());
        adapter = new OrderNoteAdapter(lstcomments);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.comment_orders_fragment, container, false);
        FAB = (ImageButton) view.findViewById(R.id.fab_addcomment);
        linearLayout_editcomment = (LinearLayout) view.findViewById(R.id.send_message);
        et_addcomment = (EditText) view.findViewById(R.id.write_comment);
        btnSaveComment = (ImageButton) view.findViewById(R.id.send_comment);

        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                linearLayout_editcomment.setVisibility(View.VISIBLE);
            }
        });
        btnSaveComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order_note.setNote(et_addcomment.getText().toString());
                saveOrderNote();

                et_addcomment.clearFocus();
                linearLayout_editcomment.setVisibility(View.INVISIBLE);
                //Hide Keyboard
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et_addcomment.getWindowToken(), 0);
            }
        });

        ((MainActivity) getActivity()).setTitle(title);

        Globals g = Globals.getInstance();
        g.setIdFragment(Constants.COMMONFRAGMENT_CODE);

        recycler = (RecyclerView) view.findViewById(R.id.recycle2);
        recycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recycler.setAdapter(adapter);
        //tv_nocomments.setVisibility(View.INVISIBLE);

        return view;
    }


    @Override
    public void listaRecibida(OrderNoteGet body) {
        try {
            if (body != null) {

                if (body.getOrder_notes().isEmpty()) {
                    //tv_nocomments.setVisibility(View.VISIBLE);
                } else {
                    List<OrderNote> lstAux = new ArrayList<OrderNote>();
                    lstAux.addAll(lstcomments);
                    lstcomments.clear();
                    lstcomments.addAll(body.getOrder_notes());
                    for (OrderNote comment : lstAux) {
                        if (comment.getId().equals("0")) {
                            lstcomments.add(comment);}
                    }
                    adapter = new OrderNoteAdapter(lstcomments);
                    recycler = (RecyclerView) view.findViewById(R.id.recycle2);
                    recycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
                    recycler.setAdapter(adapter);
                    //tv_nocomments.setVisibility(View.INVISIBLE);

                    if(progress!= null)  progress.dismiss();
                }

            }
        } catch (Exception ex) {

        }
    }

    @Override
    public void errorRecibido(Object error) {

    }

    public void loadNotes() {
        // Indicates the service call
        Globals g = Globals.getInstance();
        g.setServiceCode(Constants.SERVICE_CODE_order_notes);
        try {
            restHelper = new RetrofitDelegateHelper(id_order, 0);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        restHelper.getOrderNotesByIdOrder(this);

    }

    public void saveOrderNote() {
        /*progress = ProgressDialog.show(getContext(), null, "Cargando", false, true);
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
        Boolean bResult = false;
        Globals g = Globals.getInstance();
        try {
            g.setServiceCode(Constants.SERVICE_CODE_notes_byuser);
            restHelper = new RetrofitDelegateHelper(id_order, 0, null, order_note);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        restHelper.addOrderNote(this);

    }

    @Override
    public void hascreatecomment(Boolean iscreate)
    {
        if(iscreate)
        {
            loadNotes();

            SharedPreferences prefs =
                    getActivity().getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(Constants.PREFERENCES_IS_ORDER_CHANGED,true);
            editor.commit();
        }
    }
}