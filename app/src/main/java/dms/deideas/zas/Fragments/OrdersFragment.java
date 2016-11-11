package dms.deideas.zas.Fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Region;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import java.util.List;

import dms.deideas.zas.Activities.MainActivity;
import dms.deideas.zas.Adapters.OrderAdapter;
import dms.deideas.zas.Constants;
import dms.deideas.zas.Globals;
import dms.deideas.zas.Model.Elements;
import dms.deideas.zas.Model.Order;
import dms.deideas.zas.Model.Reparto;
import dms.deideas.zas.Model.row;
import dms.deideas.zas.R;
import dms.deideas.zas.Services.DistanceSearch;
import dms.deideas.zas.Services.DistanceService;
import dms.deideas.zas.Services.OrderSearch;
import dms.deideas.zas.Services.OrderUpdate;
import dms.deideas.zas.Services.Retrofit.RetrofitDelegateHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by dmadmin on 01/06/2016.
 */
public class OrdersFragment extends Fragment implements RetrofitDelegateHelper.AlRecibirListaDelegate, OrderAdapter.OrderListener, LocationListener, RetrofitDelegateHelper.listaRecibidaOrdenada {


    //Init parameters
    private String title,originPage="Orders";
    private RecyclerView recycler;
    private OrderAdapter adapter;
    private LocationManager locationManager;
    private String provider;
    private double latitudeUser;
    private double longitudeuser;
    private LinearLayoutManager layout;
    private RetrofitDelegateHelper restHelperOrders;
    private RetrofitDelegateHelper restHelperProblems;
    private View view;
    private Retrofit retrofit;
    private DistanceService distanceService;
    private OrderSearch ordersearchObje;
    private TextView countOrders;
    private Integer numTotalOrders = 0;
    private ProgressDialog progress;
    private Integer idUser;
    private SharedPreferences prefs;
    private Integer int_numOrders = 0;
    private Integer int_numMyOrders = 0;
    private Integer inumMyOrdersWithouProblems = 0;
    private String numberMaxOrdersVisible = "";
    private Integer numMaxOrdersAccepted_BBDD;
    private String areadelivery = "";
    private Integer countListOrder = 0;


    private class ObjOrderDataLocation {
        public int idOrder;
        public String latitud;
        public String longitud;
        public int distance;
        public String duration;
        public int minutesMotoDriverPickupInRestaurant;

        public int getIdOrder() {
            return idOrder;
        }

        public void setIdOrder(int idOrder) {
            this.idOrder = idOrder;
        }

        public int getDistance() {
            return distance;
        }

        public void setDistance(int distance) {
            this.distance = distance;
        }
    }

    public ObjOrderDataLocation obj = new ObjOrderDataLocation();
    public List<ObjOrderDataLocation> lstObjDataLocation = new ArrayList<ObjOrderDataLocation>();
    private List<String[]> lstDestination = new ArrayList<String[]>();

    public OrdersFragment() {
    }

    public static OrdersFragment newInstance(String title) {
        Bundle b = new Bundle();
        b.putString("title", title);
        OrdersFragment fragment = new OrdersFragment();
        fragment.setArguments(b);
        return fragment;
    }
    private void readPreferences(){
        prefs = getActivity().getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE);
        idUser = prefs.getInt(Constants.PREFERENCES_USER_ID, 0);
        int_numOrders = prefs.getInt("numOrders", 0);
        int_numMyOrders = prefs.getInt("numMyOrders", 0);
        inumMyOrdersWithouProblems = prefs.getInt("numMyOrdersWithouProblems", 0);
        areadelivery =  prefs.getString(Constants.PREFERENCES_AREA_DELIVERY, "");
        numberMaxOrdersVisible = prefs.getString(Constants.PREFERENCES_NUMBER_MAX_ORDERS_VISIBLE, "");
        numMaxOrdersAccepted_BBDD = Integer.valueOf(prefs.getString(Constants.PREFERENCES_NUMBER_MAX_ORDERS_ACCEPTED_BYDRIVER, "0"));
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        view = inflater.inflate(R.layout.activity_orders, container, false);

        //getActivity().setTitle(getArguments().getString("title"));
        ((MainActivity) getActivity()).setTitle(getArguments().getString("title"));


        countOrders =(TextView) view.findViewById(R.id.numOrders);
        if(countOrders != null) {
            countOrders.setText(String.valueOf(adapter.getOrdersCount()));
        }

        recycler = (RecyclerView) view.findViewById(R.id.recycle);
        recycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recycler.setAdapter(adapter);
        recycler.setHasFixedSize(true);


        Globals g = Globals.getInstance();
        g.setIdFragment(Constants.HOMEFRAGMENT_CODE);

        return view;
    }


    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        readPreferences();
        //Get Title "Pedidos"
        title = getArguments().getString("title");

        //Get position user - Latitud and longitud in this moment.
        getpositionUser();
        //Init OrderAdapter
        adapter = new OrderAdapter(this);
        //Get List of orders (Orders Accepted by restaurant and Orders with problem - Without motodriver)
        getOrders();
        //if(progress!=null) progress.dismiss();
    }

    @Override
    public void onStart() {
        super.onResume();
    }

    private void delegateHelper(int id) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {

        switch (id) {
            case Constants.SERVICE_CODE_order_accepted://rest_has_accepted
                restHelperOrders = new RetrofitDelegateHelper(0, 0,areadelivery);
                restHelperOrders.getOrdersAccepted(this);
                break;
            case Constants.SERVICE_CODE_order_problem://incidencias
                restHelperProblems = new RetrofitDelegateHelper(0, 0,areadelivery);
                restHelperProblems.getOrdersProblem(this);
                break;
            case Constants.SERVICE_CODE_order_accepted_byareadelivery://rest_has_accepted + AREA
                restHelperOrders = new RetrofitDelegateHelper(0, 0,areadelivery);
                restHelperOrders.getOrdersByAreaDelivery(this);
                break;
            default:
                restHelperOrders = new RetrofitDelegateHelper(0, 0,areadelivery);
                restHelperOrders.getListaPedidos(this);
                break;
        }
    }


    @Override
    public void listaRecibida(OrderSearch body) {
        //If we receive orders correctly
        Integer  countOrders = 0;
        if (body != null && body.getOrders() != null) {
            if (numberMaxOrdersVisible != null && numberMaxOrdersVisible != "" && (Integer.valueOf(numberMaxOrdersVisible) > 0) && body.getOrders().size() >= Integer.valueOf(numberMaxOrdersVisible)) {
                adapter.add(body.getOrders().subList(0, Integer.valueOf(numberMaxOrdersVisible)));
            } else {
                adapter.add(body.getOrders());
            }
            countOrders = body.getCountOrders();
        }

        numTotalOrders =numTotalOrders + countOrders;

    }

    @Override
    public void listaRecibidaOrdenada(OrderSearch body) {


        //If we receive orders correctly
        if (body != null && body.getOrders() != null) {
           // Si son las aceptadas --> vamos a crearnos un objeto objOrderDataLocation que contenga el idOrder y la lat y la long. Y un array con dichos datos.
            for (Order order : body.getOrders()) {
                ObjOrderDataLocation obj1 = new ObjOrderDataLocation();
                obj1.idOrder = order.getId();
                obj1.minutesMotoDriverPickupInRestaurant = order.getMinutesMotoDriverPickupInRestaurant();
                obj1.latitud = order.getRestaurant().getData_map().getLat();
                obj1.longitud = order.getRestaurant().getData_map().getLng();
                String[] strArray = new String[]{obj1.latitud, obj1.longitud};
                lstDestination.add(strArray);
                lstObjDataLocation.add(obj1);

            }
            //Convert lstDestination in a String to send Google.
            String strlstDestination = "";
            if (lstDestination.size() > 0) {
                for (String[] locRestaurant : lstDestination
                        ) {
                    if (lstDestination.indexOf(locRestaurant) == 0) {
                        strlstDestination = locRestaurant[0].toString() + "," + locRestaurant[1].toString();
                    } else {
                        strlstDestination = strlstDestination + "|" + locRestaurant[0].toString() + "," + locRestaurant[1].toString();
                    }
                }
            }

            ordersearchObje = body;
            //Llamamos a calculateDistance pasándole un array de destinos. Y si nos devuelve datos rellenamos objOrderDataLocation con distancia y duracion
            calculateDistance(strlstDestination,ordersearchObje);

            numTotalOrders = numTotalOrders + body.getCountOrders();
        }
        //if(progress!=null) progress.dismiss();

    }


    @Override
    public void errorRecibido(Object error) {
        /*if(progress!=null) progress.dismiss();
        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();*/
    }

    @Override
    public void closedialog() {
        countListOrder++;
        if ( countListOrder == 2)
            if (progress != null && countListOrder == 2)
                progress.dismiss();
        countOrders.setText(String.valueOf(numTotalOrders));
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

        DetailOrderFragment fragment = DetailOrderFragment.newInstance(title, order, originPage);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.frame, fragment)
                .addToBackStack("detailorders")
                .commit();


        ((MainActivity) getActivity()).setTitle(title);

    }


    private void getpositionUser() {

        Location location = null;
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED  && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)  == PackageManager.PERMISSION_GRANTED) {
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            provider = locationManager.getBestProvider(criteria, false);
            location = locationManager.getLastKnownLocation(provider);
            if(location == null)
            {
                location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            }
            onLocationChanged(location);
        } else {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location != null) {
            this.latitudeUser = (double) (location.getLatitude());
            this.longitudeuser = (double) (location.getLongitude());
        }
        else
        {
            Toast.makeText(getContext(),R.string.toast_without_address_correctly, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Location location = null;
        if (permissions.length == 2 &&
                permissions[0] == android.Manifest.permission.ACCESS_FINE_LOCATION &&  permissions[1] == Manifest.permission.ACCESS_COARSE_LOCATION &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                provider = locationManager.getBestProvider(criteria, false);
                 location = locationManager.getLastKnownLocation(provider);
                onLocationChanged(location);
                return;
            }
        } else {
            Toast.makeText(getContext(), R.string.toast_without_address_correctly, Toast.LENGTH_SHORT).show();
        }
    }



    private void calculateDistance(String lstDestination, final OrderSearch osBody) {

        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.URL_MAP_distancematrix)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        distanceService = retrofit.create(DistanceService.class);
        distanceService.getdistance(latitudeUser + "," + longitudeuser, lstDestination, Constants.API_KEY_GOOGLE_MAP).enqueue(new Callback<DistanceSearch>() {
            @Override
            public void onResponse(Call<DistanceSearch> call, Response<DistanceSearch> response) {
                if (response.isSuccessful()) {
                    Log.d("URL GET Distance: ", response.raw().request().url().toString());
                    Log.d("CODE Distance: ", String.valueOf(response.code()));
                    List<String> lstdistancias = new ArrayList<String>();
                    //Rellenamos el objeto ObjDistOrder con la distancia y el tiempo recibido
                    if (response.body() != null && response.body().getRows() != null && response.body().getRows().size() > 0) {
                        row rowelement = response.body().getRows().get(0);
                        for (Elements el : rowelement.getElements()
                                ) {
                            //Obtenemos la distancia y la duracion y la ponemos en el obj

                            ObjOrderDataLocation obj = lstObjDataLocation.get(rowelement.getElements().indexOf(el));
                            obj.duration = el.getDuration().getText();
                            obj.distance = el.getDistance().getValue();
                            lstObjDataLocation.get(rowelement.getElements().indexOf(el)).duration = obj.duration;
                            lstObjDataLocation.get(rowelement.getElements().indexOf(el)).distance = obj.distance;
                        }

                    }

                    addAdapter(osBody);
                } else {

                    Log.d("error: ", String.valueOf(response.errorBody()));

                }
            }

            @Override
            public void onFailure(Call<DistanceSearch> call, Throwable t) {

                Log.d("Error", call.request().url().toString());
            }
        });


    }

    public void getOrders() {

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

        // Get orders problems
        // Indicates the service call
        g.setServiceCode(Constants.SERVICE_CODE_order_problem);
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
                // Indicates the service call
                Globals g = Globals.getInstance();
                // Get orders accepted
                //g.setServiceCode(Constants.SERVICE_CODE_order_accepted);
                g.setServiceCode(Constants.SERVICE_CODE_order_accepted_byareadelivery);
                try {
                    delegateHelper(g.getServiceCode());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                }

            }
        }, Constants.RESPONSE_TIME);


    }

    private void addAdapter(OrderSearch body){
        adapter.setComparador(new Comparator<Order>() {
            @Override
            public int compare(Order lhs, Order rhs) {
                //minutesMotoDriverPickupInRestaurant;

                int timeL = lhs.getMinutesMotoDriverPickupInRestaurant();
                int timeR = rhs.getMinutesMotoDriverPickupInRestaurant();

                int distanceL = 0;
                int distanceR = 0;

                if (timeL == timeR) {
                    int idL = lhs.getId();
                    int idR = rhs.getId();

                    //Toast.makeText(getContext(), "HORAS IGUALES", Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < lstObjDataLocation.size(); i++) {
                        //Toast.makeText(getContext(), "ORDER OBJETO:" + lstObjDataLocation.get(i).getIdOrder(), Toast.LENGTH_SHORT).show();

                        if (lstObjDataLocation.get(i).getIdOrder() == idL) {
                            distanceL = lstObjDataLocation.get(i).getDistance();

                        }
                        if (lstObjDataLocation.get(i).getIdOrder() == idR) {
                            distanceR = lstObjDataLocation.get(i).getDistance();
                        }
                    }

                    //Toast.makeText(getContext(), "distanceL:" + distanceL, Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getContext(), "distanceR:" + distanceR, Toast.LENGTH_SHORT).show();


                    if (distanceL == distanceR) {
                        return 0;
                    }
                    if (distanceL < distanceR) {
                        return -1;
                    }
                    if (distanceL > distanceR) {
                        return 1;
                    }

                    return 0;
                }

                return 0;
            }
        });

        //Las añadimos en el adapter.
        adapter.add(body.getOrders());
    }

}


