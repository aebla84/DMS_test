package dms.deideas.zas.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import dms.deideas.zas.Constants;
import dms.deideas.zas.Fragments.HistoricalFragment;
import dms.deideas.zas.Fragments.HomeFragment;
import dms.deideas.zas.Fragments.MyOrdersFragment;
import dms.deideas.zas.Fragments.OrdersFragment;
import dms.deideas.zas.Fragments.ProfileFragment;
import dms.deideas.zas.Globals;
import dms.deideas.zas.R;
import dms.deideas.zas.Services.MotodriverGet;
import dms.deideas.zas.Services.MotodriverService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<LocationSettingsResult> {

    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest locationRequest;
    int REQUEST_CHECK_SETTINGS = 100;
    private DrawerLayout drawerLayout;
    private String user;
    private View homeButton;
    private TextView drawerTitle;
    private FragmentManager fragmentManager = getSupportFragmentManager();

    private Status status;

    private Retrofit retrofit;
    private MotodriverService motodriverService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set Background
        DrawerLayout rl = (DrawerLayout) findViewById(R.id.drawer);
        rl.setBackgroundResource(R.drawable.ic_background);

        //Set toolbar
        setToolbar(); // Setear Toolbar como action bar

        //Set Navigation
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        drawerTitle = (TextView) findViewById(R.id.title);
        //Set Title
        drawerTitle.setText(getResources().getString(R.string.home_item));
        if (savedInstanceState == null) {
            selectItem(drawerTitle.getText().toString());
        }
        homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(this);


        //LOCATION
        location();


    }

    private void location() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(AppIndex.API).build();
        mGoogleApiClient.connect();
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
    }


    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            // Set icon of drawer toggle
            ab.setHomeAsUpIndicator(R.drawable.ic_view_headline_white);
            ab.setDisplayHomeAsUpEnabled(true);
        }

    }

    private void setupDrawerContent(final NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {

                        // Marcar item presionado
                        item.setChecked(true);
                        // Crear nuevo fragmento
                        String title = item.getTitle().toString();
                        selectItem(title);
                        return true;
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void selectItem(String title) {

        final SharedPreferences prefs = getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE);
        int idUser = prefs.getInt(Constants.PREFERENCES_USER_ID, 0);
        String motodriver = String.valueOf(idUser);
        String goFragment = "";

        //Depends that section you are selected , it shows one fragment or other.
        if (title.equals(getResources().getString(R.string.home_item))) {

            HomeFragment fragment = HomeFragment.newInstance(title);
            goFragment = getResources().getString(R.string.go_fragment_home);

            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frame, fragment)
                    .addToBackStack(goFragment)
                    .commit();

        } else if (title.equals(getResources().getString(R.string.profile_item))) {

            ProfileFragment fragment = ProfileFragment.newInstance(title);
            goFragment = null;

            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frame, fragment)
                    .addToBackStack(goFragment)
                    .commit();
        } else if (title.equals(getResources().getString(R.string.historical_item))) {

            HistoricalFragment fragment = HistoricalFragment.newInstance(title, motodriver);
            goFragment = getResources().getString(R.string.go_fragment_historical);

            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frame, fragment)
                    .addToBackStack(goFragment)
                    .commit();
        } else if (title.equals(getResources().getString(R.string.orders_item))) {

            OrdersFragment fragment = OrdersFragment.newInstance(title);
            goFragment = getResources().getString(R.string.go_fragment_orders);

            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frame, fragment)
                    .addToBackStack(goFragment)
                    .commit();
        } else if (title.equals(getResources().getString(R.string.my_orders_item))) {

            MyOrdersFragment fragment = MyOrdersFragment.newInstance(title, motodriver);
            goFragment = getResources().getString(R.string.go_fragment_myorders);

            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frame, fragment)
                    .addToBackStack(goFragment)
                    .commit();
        } else if (title.equals(getResources().getString(R.string.logout_item))) {

            SharedPreferences preferences = getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE);

            // Change status of motodriver on server
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.URL_ZAS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            motodriverService = retrofit.create(MotodriverService.class);

            String user = preferences.getString(Constants.PREFERENCES_USER, "");
            String password = preferences.getString(Constants.PREFERENCES_USER_PASSWORD, "");

            motodriverService.StringLogout(user, password).enqueue(new Callback<MotodriverGet>() {
                @Override
                public void onResponse(Call<MotodriverGet> call, Response<MotodriverGet> response) {
                    if (response.body() != null) {
                        Log.d("URL_GET_LOGOUT: ", response.raw().request().url().toString());
                        Log.d("CODE_GET_LOGOUT: ", String.valueOf(response.code()));
                    }
                }

                @Override
                public void onFailure(Call<MotodriverGet> call, Throwable t) {
                    Log.d(Constants.STRING_ERROR,Constants.RETROFIT_ONFAILURE_NOT_REPLY);
                }
            });


            // Clear Preferences
            preferences = getSharedPreferences(Constants.PREFERENCES_NAME, 0);
            SharedPreferences.Editor editor = preferences.edit();
            editor = preferences.edit();
            editor.clear();
            editor.commit();

            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
            finish();
        }

        drawerLayout.closeDrawers();

        //setTitle(title);
        drawerTitle.setText(title);

    }

    @Override
    public void onBackPressed() {

        Globals g = Globals.getInstance();
        int id = g.getIdFragment();
        int idDetail = g.getIdFragmentDetail();
        int idHistorical = g.getIdFragmentHistorical();

        // depending on the fragment called must return to home order or myorder

        if (fragmentManager.getBackStackEntryCount() == 1) {
            finish();
        }
        if (idHistorical == Constants.HISTORICALFRAGMENT_CODE) {
            if (id == Constants.HISTORICALFRAGMENT_CODE) {
                fragmentManager.popBackStack(Constants.HOMEFRAGMENT, 0);
                g.setIdFragmentHistorical(0);
                g.setIdFragmentDetail(0);
            } else if (id == Constants.COMMONFRAGMENT_CODE) {
                fragmentManager.popBackStack(Constants.DETAILMYORDERSFRAGMENT, 0);
                g.setIdFragmentHistorical(6);
                g.setIdFragmentDetail(0);
            } else if (idDetail == Constants.DETAILMYORDERSFRAGMENT_CODE) {
                fragmentManager.popBackStack(Constants.HISTORICALFRAGMENT, 0);
                g.setIdFragmentHistorical(0);
                g.setIdFragmentDetail(0);
            }
        } else if (id == Constants.HOMEFRAGMENT_CODE) {
            fragmentManager.popBackStack(Constants.HOMEFRAGMENT, 0);
        } else if (id == Constants.ORDERSFRAGMENT_CODE) {
            fragmentManager.popBackStack(Constants.ORDERSFRAGMENT, 0);
        } else if (id == Constants.MYORDERSFRAGMENT_CODE) {
            fragmentManager.popBackStack(Constants.MYORDERSFRAGMENT, 0);
        } else if (id == Constants.COMMONFRAGMENT_CODE) {
            if (idDetail == Constants.DETAILORDERSFRAGMENT_CODE) {
                fragmentManager.popBackStack(Constants.DETAILORDERSFRAGMENT, 0);
                g.setIdFragmentDetail(0);
            } else if (idDetail == Constants.DETAILMYORDERSFRAGMENT_CODE) {
                fragmentManager.popBackStack(Constants.DETAILMYORDERSFRAGMENT, 0);
                g.setIdFragmentDetail(0);
            }
        } else
            {
                super.onBackPressed();
            }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.homeButton) {
            HomeFragment fragment = HomeFragment.newInstance((getResources().getString(R.string.home_item)));
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frame, fragment)
                    .addToBackStack(Constants.HOMEFRAGMENT)
                    .commit();
        }
    }

    public void setTitle(String title) {
        drawerTitle.setText(title);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        builder.build()
                );
        result.setResultCallback(this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        //final Status status = locationSettingsResult.getStatus();
        status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                // NO need to show the dialog;
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                //  GPS turned off, Show the user a dialog
                //  GPS disabled show the user a dialog to turn it on
                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().
                    status.startResolutionForResult(this, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    //failed to show dialog
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                // Location settings are unavailable so not possible to show any dialog now
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Iniciamos la segunda actividad, y le indicamos que la iniciamos
        // para rellenar el nombre:
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(),R.string.toast_gps_active, Toast.LENGTH_LONG).show();
            } else {
                try {
                    status.startResolutionForResult(this, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

}
