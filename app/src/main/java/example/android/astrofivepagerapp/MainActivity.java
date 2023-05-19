package example.android.astrofivepagerapp;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;

import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, HomeFragment.OnFragmentInteractionListener, infoFragment.OnFragmentInteractionListener, goPremiumFragment.OnFragmentInteractionListener, PrivacyPolicyFragment.OnFragmentInteractionListener, OTMapFragment.OnFragmentInteractionListener, fragment_chart_tab.OnFragmentInteractionListener, TimeChartFragment.OnFragmentInteractionListener, Panchangamfragment.OnFragmentInteractionListener, LagnaFragment.OnFragmentInteractionListener, RulingPlanetFragment.OnFragmentInteractionListener, HoraFragment.OnFragmentInteractionListener, DBAFRagment.OnFragmentInteractionListener, SubmitTecketFragment.OnFragmentInteractionListener {
    ImageView refresh, settings, caltime;
    SharedPreferences pref1;

    ProgressDialog pd;
    SharedPreferences.Editor editor;
    Spinner sp_chart, sp_ayanmsha, sp_BhavaMadhya, sp_Lang, sp_node;
    Button btn_submit, btn_cancel;
    Button bt_time, bt_date;
    TextView currentdatetime;
    Button tv_currentdatetime;
    EditText set_date, set_time, set_date_data;
    ArrayList<String> chart_list = new ArrayList<>();
    ArrayList<String> ayanmsha_list = new ArrayList<>();
    ArrayList<String> bhavaMadhya_list = new ArrayList<>();
    ArrayList<String> lang_list = new ArrayList<>();

    private String nameFrg;

    ArrayList<String> chartid_list = new ArrayList<>();
    ArrayList<String> ayanmshaid_list = new ArrayList<>();
    ArrayList<String> bhavaMadhyaid_list = new ArrayList<>();
    ArrayList<String> langid_list = new ArrayList<>();
    private String chartid, ayanmshaid, bhavaMadhyaid, langid, nodeid;
    String pos;
    ArrayList<String> Nodelist = new ArrayList<>();
    ArrayList<String> Nodelistid = new ArrayList<>();
    private int mYear, mMonth, mDay, mHour, mMinute, mseconds;
    String date;
    String bhavaMadhyaid_set;
    int locationInterval = 1000;
    private static final int REQUEST_LOCATION_TURN_ON = 2000;
    LocationRequest locationRequest;
    FusedLocationProviderClient fusedLocationProviderClient;
    private ActivityResultLauncher<IntentSenderRequest> resolutionForResult;
    private double latitude=0.0;
    private double longitude=0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        refresh = (ImageView) findViewById(R.id.refresh);
        settings = (ImageView) findViewById(R.id.settings);
        caltime = (ImageView) findViewById(R.id.caltime);
        pref1 = getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref1.edit();
        pos = pref1.getString("pos", "");
        if (!CheckGooglePlayServices()) {
            Toast.makeText(MainActivity.this, R.string.google_play_services, Toast.LENGTH_SHORT).show();
            finish();
        }
        try {
            //calling location request for peroid of time
            locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, locationInterval)
                    .setWaitForAccurateLocation(false)
                    .setMinUpdateIntervalMillis(locationInterval)
                    .setMaxUpdateDelayMillis(locationInterval)
                    .build();
            getCurrentLocation();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        resolutionForResult = registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(), result -> {
            if(result.getResultCode() == RESULT_OK){
                getCurrentLocation();
            }else {
                turnOnGPS();
            }
        });




        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment4 = new HomeFragment();
                moveToFragment(fragment4);
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
                String strDate = mdformat.format(calendar.getTime());

                mYear = calendar.get(Calendar.YEAR);
                mMonth = calendar.get(Calendar.MONTH);
                mDay = calendar.get(Calendar.DAY_OF_MONTH);
                GlobalDeclaration.date1 = mDay + "/" + mMonth + "/" + mYear;
                GlobalDeclaration.time1 = strDate;
                date = (mMonth + 1) + ":" + mDay + ":" + mYear + ":" + strDate;
                editor.putString("date", date); // Storing string
                editor.commit();
                refresh.setVisibility(View.VISIBLE);


            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                settings.setVisibility(View.VISIBLE);
                initpopup_settings();

            }
        });
        caltime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                caltime.setVisibility(View.VISIBLE);
                initpopup();


            }
        });
       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private boolean CheckGooglePlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                Objects.requireNonNull(googleAPI.getErrorDialog(this, result, 0)).show();
            }
            return false;
        }
        return true;

    }
    private void getCurrentLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                if (isGPSEnabled()) {

                    LocationServices.getFusedLocationProviderClient(MainActivity.this)
                            .requestLocationUpdates(locationRequest, new LocationCallback() {
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    super.onLocationResult(locationResult);

                                    LocationServices.getFusedLocationProviderClient(MainActivity.this)
                                            .removeLocationUpdates(this);

                                    if (locationResult.getLocations().size() > 0) {
                                        int index = locationResult.getLocations().size() - 1;
                                         latitude = locationResult.getLocations().get(index).getLatitude();
                                         longitude = locationResult.getLocations().get(index).getLongitude() * -1;
                                        if (latitude != 0.0 && longitude != 0.0) {

                                            editor.putString("latitude", String.valueOf(latitude)); // Storing string
                                            editor.putString("longitude", String.valueOf(longitude)); // Storing string
                                            editor.commit();

                                                Fragment fragment4 = new HomeFragment();
                                                moveToFragment(fragment4);
                                                Calendar calendar = Calendar.getInstance();
                                                SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
                                                String strDate = mdformat.format(calendar.getTime());

                                                mYear = calendar.get(Calendar.YEAR);
                                                mMonth = calendar.get(Calendar.MONTH);
                                                mDay = calendar.get(Calendar.DAY_OF_MONTH);
                                                GlobalDeclaration.date1 = mDay + "/" + mMonth + "/" + mYear;
                                                GlobalDeclaration.time1 = strDate;
                                                date = (mMonth + 1) + ":" + mDay + ":" + mYear + ":" + strDate;
                                                editor.putString("date", date); // Storing string
                                                editor.commit();

                                        }

                                    }
                                }
                            }, Looper.getMainLooper());


                } else {
                    turnOnGPS();
                }

            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (isGPSEnabled()) {
                    getCurrentLocation();
                } else {
                    turnOnGPS();
                }
            } else {
                getCurrentLocation();
            }
        } else {
            getCurrentLocation();
        }


    }

    private void turnOnGPS() {


        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(task -> {

            try {
                LocationSettingsResponse response = task.getResult(ApiException.class);
                Log.e("LOCATION response", String.valueOf(response));

            } catch (ApiException e) {

                switch (e.getStatusCode()) {
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:


                        ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                        try {
                            resolvableApiException.startResolutionForResult(MainActivity.this, REQUEST_LOCATION_TURN_ON);
                        } catch (IntentSender.SendIntentException ex) {
                            throw new RuntimeException(ex);
                        }
                        IntentSenderRequest intentSenderRequest = new IntentSenderRequest.Builder((resolvableApiException).getResolution()).build();
                        resolutionForResult.launch(intentSenderRequest);

                        break;

                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        //Device does not have location
                        break;
                }
            }
        });

    }

    private boolean isGPSEnabled() {
        boolean isEnabled;
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isEnabled;

    }
    //broadcast receiver for receiving the location off state when disabled from settings or notificationbar
    private final BroadcastReceiver gpsreceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (intent != null && intent.getAction() != null && intent.getAction().matches(LocationManager.PROVIDERS_CHANGED_ACTION)) {
                    LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                    boolean isGpsEnabled = false;
                    if (locationManager != null) {
                        isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                    }
                    if (locationManager != null) {
                        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                        Log.e("error", String.valueOf(isNetworkEnabled));
                    }
                    if (!isGpsEnabled) {
                        getCurrentLocation();
                    }
                }
            } catch (Exception e) {
                Log.e("error", e.toString());
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(gpsreceiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(gpsreceiver);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (nameFrg.equalsIgnoreCase(HomeFragment.class.getSimpleName())) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                builder1.setMessage("Do you want to exit ?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    finishAffinity();
                                }
                            }
                        });

                builder1.setNegativeButton(
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
//                                Intent i = new Intent(MainActivity.this, MainActivity.class);
//                                startActivity(i);
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            } /*else {
                Fragment fragment1 = new HomeFragment();
                moveToFragment(fragment1);
            }*/
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }
*/
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_info) {
            Fragment fragment1 = new infoFragment();
            moveToFragment(fragment1);


            // Handle the camera action
        } else if (id == R.id.nav_policy) {
            Fragment fragment2 = new PrivacyPolicyFragment();
            moveToFragment(fragment2);
        } else if (id == R.id.nv_premium) {
            Fragment fragment3 = new goPremiumFragment();
            moveToFragment(fragment3);

        } else if (id == R.id.nav_home) {
            Fragment fragment4 = new HomeFragment();
            moveToFragment(fragment4);

        } else if (id == R.id.nav_submit_ticket) {
            Fragment fragment4 = new SubmitTecketFragment();
            moveToFragment(fragment4);
        } else if (id == R.id.nav_exit) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAndRemoveTask();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void moveToFragment(Fragment fragment) {

        nameFrg = fragment.getClass().getSimpleName();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame, fragment, nameFrg).addToBackStack(null).commit();

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void initpopup() {
        //  Logger.e("row", ((EditText) paramView_main.getChildAt(5)).getText().toString());

        final Dialog layout = new Dialog(MainActivity.this, R.style.Theme_AppCompat_Dialog);
        layout.requestWindowFeature(Window.FEATURE_NO_TITLE);
        layout.setContentView(R.layout.datetimepicker5pager);

        layout.setCanceledOnTouchOutside(false);

        final String[] date_data = new String[1];
        final String[] dd = new String[1];
        final String[] tt = new String[1];
        Button ok = (Button) layout.findViewById(R.id.ok);
        Button cancel = (Button) layout.findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.dismiss();
            }
        });

       /* bt_date = (Button) layout.findViewById(R.id.selectdate);
        bt_time = (Button) layout.findViewById(R.id.selecttime);*/
        set_date = (EditText) layout.findViewById(R.id.set_date);
        set_date_data = (EditText) layout.findViewById(R.id.set_date_data);
        set_time = (EditText) layout.findViewById(R.id.set_time);
        currentdatetime = (TextView) layout.findViewById(R.id.currentdate_time);
        tv_currentdatetime = (Button) layout.findViewById(R.id.tv_currentdatetime);
    /*  String date_new = pref1.getString("date_new", "");
        String time_new = pref1.getString("time_new", "");*/
        tv_currentdatetime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
                String strDate = mdformat.format(calendar.getTime());


                mYear = calendar.get(Calendar.YEAR);
                mMonth = calendar.get(Calendar.MONTH);
                mDay = calendar.get(Calendar.DAY_OF_MONTH);
                // date = (mMonth + 1) + ":" + mDay + ":" + mYear + ":" + strDate;

                set_date.setText(mDay + "/" + (mMonth + 1) + "/" + mYear);
                set_time.setText(strDate);
            }
        });
        if (GlobalDeclaration.date1 != null && GlobalDeclaration.time1 != null) {
            set_date.setText(GlobalDeclaration.date1);
            set_time.setText(GlobalDeclaration.time1);
             /* String[] parts = dd[0].split(":");
        String string1 = parts[0];
        String string2 = parts[1];
        String string3 = parts[2];

        Log.e("string", string2 + "/" + string1 + "/" + string3);*/

        } else {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
            String strDate = mdformat.format(calendar.getTime());

            set_time.setText(strDate);
            mYear = calendar.get(Calendar.YEAR);
            mMonth = calendar.get(Calendar.MONTH);
            mDay = calendar.get(Calendar.DAY_OF_MONTH);

            set_date.setText(mDay + "/" + (mMonth + 1) + "/" + mYear);
//            dd[0] = (mMonth + 1) + ":" + mDay + ":" + mYear;
        }


        set_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);
                mseconds = c.get(Calendar.SECOND);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                set_time.setText(hourOfDay + ":" + minute + ":" + mseconds);
                                tt[0] = hourOfDay + ":" + minute + ":" + mseconds;
                            }
                        }, mHour, mMinute, false);

                timePickerDialog.show();
            }
        });
        set_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                // dd[0] = (monthOfYear + 1) + ":" + dayOfMonth + ":" + year;
                                set_date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                               /* String[] arrOfStr = dd[0].split(":", -2);

                                for (String a : arrOfStr)
                                    System.out.println(a);*/


                            }

                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                datePickerDialog.show();

            }
        });

       /* String[] parts = dd[0].split(":");
        String string1 = parts[0];
        String string2 = parts[1];
        String string3 = parts[2];

        Log.e("string", string2 + "/" + string1 + "/" + string3);*/
        DateFormat df = new SimpleDateFormat("dd:MM:yy:HH:mm:ss");
        Date dateobj = new Date();
        System.out.println(df.format(dateobj));
        //  set_date.setText(df.format(dateobj.getDate()));

        /*getting current date time using calendar class
         * An Alternative of above*/
        DateFormat dateFormat = new SimpleDateFormat("MM:dd:yyyy:HH:mm:ss");
        Date date1 = new Date();
        date = dateFormat.format(date1);
//        System.out.println(dateFormat.format(date));
        currentdatetime.setText(date);

        // String parts[] = date.split(" ");

        //final String currentdate = parts[0];
        //   final String currenttime = parts[1];


        set_time.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //   date = dd[0] + ":" + set_time.getText().toString().trim();

            }
        });
        set_date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //date = dd[0] + ":" + set_time.getText().toString().trim();

            }
        });


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalDeclaration.date = currentdatetime.getText().toString();
                //GlobalDeclaration.time = df.format(calobj.getTime());

                GlobalDeclaration.date1 = set_date.getText().toString();
                GlobalDeclaration.time1 = set_time.getText().toString();

                String new_date = set_date.getText().toString();

                String[] parts = new_date.split("/");
                String string1 = parts[0];
                String string2 = parts[1];
                String string3 = parts[2];

                Log.e("string", string2 + ":" + string1 + ":" + string3);
                String data_date = string2 + ":" + string1 + ":" + string3;
                date = data_date + ":" + set_time.getText().toString().trim();
                editor.putString("date", date); // Storing string
                editor.commit();
                GlobalDeclaration.flag = true;
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);

                layout.dismiss();


            }
        });
        layout.show();

    }

    public void initpopup_settings() {
        //  Logger.e("row", ((EditText) paramView_main.getChildAt(5)).getText().toString());

        final Dialog layout = new Dialog(MainActivity.this, R.style.Theme_AppCompat_Dialog);
        layout.requestWindowFeature(Window.FEATURE_NO_TITLE);
        layout.setContentView(R.layout.settings_5tab);

        layout.setCanceledOnTouchOutside(false);


        sp_chart = layout.findViewById(R.id.sp_chart);
        final LinearLayout ly_bhava = layout.findViewById(R.id.Ly_BhavaMadhya);
        sp_ayanmsha = layout.findViewById(R.id.sp_ayanmsha);
        sp_BhavaMadhya = layout.findViewById(R.id.sp_BhavaMadhya);
        sp_Lang = layout.findViewById(R.id.sp_Lang);
        btn_submit = layout.findViewById(R.id.btn_submit);
        btn_cancel = layout.findViewById(R.id.btn_cancel);

     /*   chart_list.clear();
        chartid_list.clear();
        bhavaMadhya_list.clear();
        bhavaMadhyaid_list.clear();
        ayanmsha_list.clear();
        ayanmshaid_list.clear();
        Nodelist.clear();
        lang_list.clear();
        langid_list.clear();*/


        //  chart_list.add("--Select--");
        chart_list.clear();
        chart_list.add("KP");
        chart_list.add("Vedic");

          /*  ayanmsha_KP_list.add("--Select--");
        ayanmsha_KP_list.add("Kpold");
        ayanmsha_KP_list.add("Kpnew");
        ayanmsha_KP_list.add("kpstline");

        ayanmsha_Vedic_list.add("--Select--");
        ayanmsha_Vedic_list.add("Lahiri");
        ayanmsha_Vedic_list.add("Raman");*/
        //  chartid_list.add("-1");
        chartid_list.clear();
        chartid_list.add("0");
        chartid_list.add("1");

        bhavaMadhya_list.clear();

        // bhavaMadhya_list.add("--Select--");
        bhavaMadhya_list.add("Bhavamadhya");
        bhavaMadhya_list.add("Bhavarambha");

        // bhavaMadhyaid_list.add("-1");
        bhavaMadhyaid_list.clear();
        bhavaMadhyaid_list.add("1");
        bhavaMadhyaid_list.add("0");

        //lang_list.add("--Select--");
        lang_list.clear();
        lang_list.add("English");
      /*  lang_list.add("Telugu");
        lang_list.add("Hindi");
        lang_list.add("Marathi");
        lang_list.add("Gujarathi");
        lang_list.add("Kannada");
        lang_list.add("Tamil");*/

        // langid_list.add("-1");
        langid_list.clear();
        langid_list.add("En");
        /*langid_list.add("Te");
        langid_list.add("Hi");
        langid_list.add("Ma");
        langid_list.add("Gu");
        langid_list.add("Ka");
        langid_list.add("Ta");*/
        Nodelist.clear();
        Nodelist.add("Mean");
        Nodelist.add("True");

        Nodelistid.clear();
        Nodelistid.add("0");
        Nodelistid.add("1");

        sp_node = layout.findViewById(R.id.sp_Node);

        ArrayAdapter Node_adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_spinner_item, Nodelist);
        Node_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_node.setAdapter(Node_adapter);

        ArrayAdapter chart_adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_spinner_item, chart_list);
        chart_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_chart.setAdapter(chart_adapter);

        ArrayAdapter bhavaMadhya_adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_spinner_item, bhavaMadhya_list);
        bhavaMadhya_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_BhavaMadhya.setAdapter(bhavaMadhya_adapter);

        final ArrayAdapter lang_adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_spinner_item, lang_list);
        lang_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_Lang.setAdapter(lang_adapter);
        chartid = pref1.getString("chartid", "");
        bhavaMadhyaid = pref1.getString("bhavaMadhyaid", "");
        ayanmshaid = pref1.getString("ayanmshaid", "");
        langid = pref1.getString("Lang", "");
        nodeid = pref1.getString("node", "");
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* if (!validateFields())*/
                Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                editor.putString("chartid", chartid); // Storing string
                editor.putString("bhavaMadhyaid", bhavaMadhyaid);
                editor.putString("ayanmshaid", ayanmshaid);
                editor.putString("Lang", langid);
                editor.putString("node", nodeid);
                editor.commit();
               /* GlobalDeclaration.mychartMethod = chartid;
                GlobalDeclaration.isBhavaMadhya = bhavaMadhyaid;
                GlobalDeclaration.myayanmsha = ayanmshaid;
                GlobalDeclaration.Lang = langid;
                GlobalDeclaration.node = nodeid;*/
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
                GlobalDeclaration.Flag_settings = true;

                layout.dismiss();


            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.dismiss();
            }
        });
        if (GlobalDeclaration.Flag_settings) {
            sp_chart.setSelection(Integer.parseInt(chartid), true);
            sp_node.setSelection(Integer.parseInt(nodeid), true);
            ayanmsha_list.clear();
            if (chartid.equalsIgnoreCase("0")) {
                //  ayanmsha_list.add("--Select--");
                ly_bhava.setVisibility(View.GONE);
                ayanmsha_list.add("KP-Original ");
                ayanmsha_list.add("KP-New");
                ayanmsha_list.add("KP-St.Line");

                //  ayanmshaid_list.add("-1");
                ayanmshaid_list.add("0");
                ayanmshaid_list.add("1");
                ayanmshaid_list.add("2");

            } else {
                //  ayanmsha_list.add("--Select--");
                ly_bhava.setVisibility(View.VISIBLE);
                ayanmsha_list.add("Lahiri");
                ayanmsha_list.add("Raman");

                //  ayanmshaid_list.add("-1");
                ayanmshaid_list.add("3");
                ayanmshaid_list.add("4");

            }

            ArrayAdapter adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_spinner_item, ayanmsha_list);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_ayanmsha.setAdapter(adapter);

            if (ayanmshaid.equalsIgnoreCase("3")) {
                sp_ayanmsha.setSelection(0, true);
            } else if (ayanmshaid.equalsIgnoreCase("4")) {
                sp_ayanmsha.setSelection(1, true);
            } else {
                sp_ayanmsha.setSelection(Integer.parseInt(ayanmshaid), true);
            }
            if (bhavaMadhyaid.equalsIgnoreCase("0")) {
                sp_BhavaMadhya.setSelection(1, true);
            } else {
                sp_BhavaMadhya.setSelection(0, true);
            }


        }

        sp_chart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                chartid = String.valueOf(position);

                ayanmsha_list.clear();

                switch (chartid) {
                    case "0":

                        //  ayanmsha_list.add("--Select--");
                        ly_bhava.setVisibility(View.GONE);
                        ayanmsha_list.add("KP-Original ");
                        ayanmsha_list.add("KP-New");
                        ayanmsha_list.add("KP-St.Line");

                        //  ayanmshaid_list.add("-1");
                        ayanmshaid_list.add("0");
                        ayanmshaid_list.add("1");
                        ayanmshaid_list.add("2");


                        break;
                    case "1":
                        //  ayanmsha_list.add("--Select--");
                        ly_bhava.setVisibility(View.VISIBLE);
                        ayanmsha_list.add("Lahiri");
                        ayanmsha_list.add("Raman");

                        //  ayanmshaid_list.add("-1");
                        ayanmshaid_list.add("3");
                        ayanmshaid_list.add("4");

                        break;
                    default:
                        break;
                }
                ArrayAdapter adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_spinner_item, ayanmsha_list);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp_ayanmsha.setAdapter(adapter);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_BhavaMadhya.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // bhavaMadhyaid_set=bhavaMadhyaid_list.get(position);
                if (position == 0) {
                    bhavaMadhyaid = "1";
                } else {
                    bhavaMadhyaid = "0";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_ayanmsha.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sp_ayanmsha.getSelectedItem().equals("Lahiri")) {
                    ayanmshaid = "3";
                } else if (sp_ayanmsha.getSelectedItem().equals("Raman")) {
                    ayanmshaid = "4";
                } else {
                    ayanmshaid = String.valueOf(position);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_Lang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                langid = langid_list.get(position);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_node.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                nodeid = Nodelistid.get(position);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        layout.show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Fragment fragment4 = new HomeFragment();
        moveToFragment(fragment4);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
        String strDate = mdformat.format(calendar.getTime());


        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        GlobalDeclaration.date1 = mDay + "/" + mMonth + "/" + mYear;
        GlobalDeclaration.time1 = strDate;
        date = (mMonth + 1) + ":" + mDay + ":" + mYear + ":" + strDate;
        editor.putString("date", date); // Storing string
        editor.commit();
    }
}
