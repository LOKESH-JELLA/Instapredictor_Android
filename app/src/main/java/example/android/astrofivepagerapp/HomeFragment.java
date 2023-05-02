package example.android.astrofivepagerapp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import com.google.android.material.tabs.TabLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static android.content.Context.LOCATION_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements OTMapFragment.OnFragmentInteractionListener, fragment_chart_tab.OnFragmentInteractionListener, TimeChartFragment.OnFragmentInteractionListener, Panchangamfragment.OnFragmentInteractionListener, LagnaFragment.OnFragmentInteractionListener, RulingPlanetFragment.OnFragmentInteractionListener, HoraFragment.OnFragmentInteractionListener, DBAFRagment.OnFragmentInteractionListener
       {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String date;
    Button bt_time, bt_date;
    TextView currentdatetime;
    EditText set_date, set_time;
    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private LocationManager locationManager;
    private String provider;
    Fragment selectedFragment = null;
    String fragmentTag = null;
    Panchangamfragment homeFragment;
    Toolbar toolbar;
    private int mYear, mMonth, mDay, mHour, mMinute, mseconds;

    double tz;
    //String date;


    SharedPreferences pref1;
    SharedPreferences pref;
    ProgressDialog pd;
    SharedPreferences.Editor editor;

    Spinner sp_chart, sp_ayanmsha, sp_BhavaMadhya, sp_Lang, sp_node;
    Button btn_submit, btn_cancel;

    ArrayList<String> chart_list = new ArrayList<>();
    ArrayList<String> ayanmsha_list = new ArrayList<>();
    ArrayList<String> bhavaMadhya_list = new ArrayList<>();
    ArrayList<String> lang_list = new ArrayList<>();


    ArrayList<String> chartid_list = new ArrayList<>();
    ArrayList<String> ayanmshaid_list = new ArrayList<>();
    ArrayList<String> bhavaMadhyaid_list = new ArrayList<>();
    ArrayList<String> langid_list = new ArrayList<>();
    private String chartid, ayanmshaid, bhavaMadhyaid, langid,nodeid;
    String pos;
    ArrayList<String> Nodelist = new ArrayList<>();
    ArrayList<String> Nodelistid = new ArrayList<>();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_home, container, false);


        viewPager = (ViewPager) v.findViewById(R.id.viewPager);

        tabLayout = (TabLayout) v.findViewById(R.id.tabLayout);


        Log.e("itemcount", String.valueOf(viewPager.getCurrentItem()));


        adapter = new TabAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(new fragment_chart_tab(), "Chart");
        //  adapter.addFragment(new DBAFRagment(), "DBA");
        adapter.addFragment(new RulingPlanetFragment(), "RP");
        adapter.addFragment(new Panchangamfragment(), "Panchang");
        adapter.addFragment(new HoraFragment(), "Hora");
        adapter.addFragment(new LagnaFragment(), "Lagna");


        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                GlobalDeclaration.pos = i;
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        pref1 = getActivity().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref1.edit();
        pos = pref1.getString("pos", "");
        try {
            viewPager.setCurrentItem(GlobalDeclaration.pos);

        } catch (Exception e) {
            e.printStackTrace();
        }
       /* locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        Location location = locationManager.getLastKnownLocation(provider);
*/
        // Initialize the location fields
      /*  if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);
        }*/


        Calendar cal = Calendar.getInstance();
        Date currentTime = cal.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM:dd:yyyy:HH:mm:ss");
        date = dateFormat.format(currentTime);
        Log.e("Currentdatetime", String.valueOf(date));
        // date = dateFormat.format(currentTime);

        TimeZone timezone = TimeZone.getDefault();
        Log.e("tz", String.valueOf(timezone.getRawOffset()));
        tz = (timezone.getRawOffset() / (60.0 * 1000.00) / 60.0);
        Log.e("tz", String.valueOf(tz));

        date = dateFormat.format(currentTime);
        tz = (timezone.getRawOffset() / (60.0 * 1000.00) / 60.0);

        pref = getActivity().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        if (!GlobalDeclaration.flag) {
            editor.putString("date", date);
            GlobalDeclaration.flag = true;// Storing string
        }
        editor.putString("timezone", String.valueOf(tz)); // Storing string
        editor.commit();

       /* toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getItemId() == R.id.cal) {

                    initpopup(GlobalDeclaration.pos);
                   *//* SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                    editor = pref.edit();
                   // editor.putString("date", date); // Storing string
                    editor.putString("timezone", String.valueOf(tz)); // Storing string
                    editor.putString("latitude", String.valueOf(latitude)); // Storing string
                    editor.putString("longitude", String.valueOf(longitude)); // Storing string
                    editor.putString("pos", String.valueOf(viewPager.getCurrentItem())); // Storing string
                    editor.commit();*//*
                }
                if (item.getItemId() == R.id.settings) {

                    initpopup_settings();
                   *//* SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                    editor = pref.edit();
                   // editor.putString("date", date); // Storing string
                    editor.putString("timezone", String.valueOf(tz)); // Storing string
                    editor.putString("latitude", String.valueOf(latitude)); // Storing string
                    editor.putString("longitude", String.valueOf(longitude)); // Storing string
                    editor.putString("pos", String.valueOf(viewPager.getCurrentItem())); // Storing string
                    editor.commit();*//*
                }
                return true;
               *//* finish();
                Intent intent = new Intent(Main_new.this, Main_new.class);
                startActivity(intent);
                return false;*//*
            }
        });

*/
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    private BroadcastReceiver mGpsSwitchStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {
                    checkpermision();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public void checkpermision() {
        LocationManager service = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        boolean enabled = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
    }
    /*private void changeTabsFont() {
        Typeface font = Typeface.createFromAsset(Main_new.this.getAssets(), "fonts/"+ Constants.FontStyle);
        ViewGroup vg = (ViewGroup) viewPager.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(font);
                    ((TextView) tabViewChild).setTextSize(15);

                }
            }
        }
    }*/

    /*@Override
    public void onBackPressed() {

        showResponseAlert("Do you want to exit from app?", true);

              *//*  FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.content, selectedFragment, fragmentTag);
                transaction.commitAllowingStateLoss();*//*


    }*/

    private void showResponseAlert(String message, final Boolean Flag) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (Flag) {
                            editor.putString("pos", "0");
                            editor.apply();

                            //  viewPager.setCurrentItem(0);
                           // finish();
                        }
                    }
                });
        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }




  /*  public void initpopup_settings() {
        //  Logger.e("row", ((EditText) paramView_main.getChildAt(5)).getText().toString());

        final Dialog layout = new Dialog(Main_new.this, R.style.Theme_AppCompat_Dialog);
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

        chart_list.clear();
        chartid_list.clear();
        bhavaMadhya_list.clear();
        bhavaMadhyaid_list.clear();
        ayanmsha_list.clear();
        ayanmshaid_list.clear();
        Nodelist.clear();
        lang_list.clear();
        langid_list.clear();


        //  chart_list.add("--Select--");
        chart_list.add("KP");
        chart_list.add("Vedic");

          *//*  ayanmsha_KP_list.add("--Select--");
            ayanmsha_KP_list.add("Kpold");
            ayanmsha_KP_list.add("Kpnew");
            ayanmsha_KP_list.add("kpstline");

            ayanmsha_Vedic_list.add("--Select--");
            ayanmsha_Vedic_list.add("Lahiri");
            ayanmsha_Vedic_list.add("Raman");*//*
        //  chartid_list.add("-1");
        chartid_list.add("0");
        chartid_list.add("1");


        // bhavaMadhya_list.add("--Select--");
        bhavaMadhya_list.add("Bhavamadhya");
        bhavaMadhya_list.add("Bhavarambha");

        // bhavaMadhyaid_list.add("-1");
        bhavaMadhyaid_list.add("1");
        bhavaMadhyaid_list.add("0");

        //lang_list.add("--Select--");
        lang_list.add("English");
        lang_list.add("Telugu");
        lang_list.add("Hindi");
        lang_list.add("Marathi");
        lang_list.add("Gujarathi");
        lang_list.add("Kannada");
        lang_list.add("Tamil");

        // langid_list.add("-1");
        langid_list.add("En");
        langid_list.add("Te");
        langid_list.add("Hi");
        langid_list.add("Ma");
        langid_list.add("Gu");
        langid_list.add("Ka");
        langid_list.add("Ta");
        Nodelist.add("Mean");
        Nodelist.add("True");

        Nodelistid.add("0");
        Nodelistid.add("1");

        sp_node = layout.findViewById(R.id.sp_Node);

        ArrayAdapter Node_adapter = new ArrayAdapter(Main_new.this, android.R.layout.simple_spinner_item, Nodelist);
        Node_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_node.setAdapter(Node_adapter);
        ArrayAdapter chart_adapter = new ArrayAdapter(Main_new.this, android.R.layout.simple_spinner_item, chart_list);
        chart_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_chart.setAdapter(chart_adapter);

        ArrayAdapter bhavaMadhya_adapter = new ArrayAdapter(Main_new.this, android.R.layout.simple_spinner_item, bhavaMadhya_list);
        bhavaMadhya_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_BhavaMadhya.setAdapter(bhavaMadhya_adapter);

        final ArrayAdapter lang_adapter = new ArrayAdapter(Main_new.this, android.R.layout.simple_spinner_item, lang_list);
        lang_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_Lang.setAdapter(lang_adapter);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                *//* if (!validateFields())*//*
                Toast.makeText(Main_new.this, "Success", Toast.LENGTH_SHORT).show();
                GlobalDeclaration.mychartMethod = chartid;
                GlobalDeclaration.isBhavaMadhya = bhavaMadhyaid;
                GlobalDeclaration.myayanmsha = ayanmshaid;
                GlobalDeclaration.Lang = langid;
                GlobalDeclaration.node = nodeid;

                Intent intent = new Intent(Main_new.this, Main_new.class);
                startActivity(intent);

                layout.dismiss();


            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.dismiss();
            }
        });


        sp_chart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                chartid = chartid_list.get(position);

                ayanmsha_list.clear();

                switch (chartid) {
                    case "0":

                        //  ayanmsha_list.add("--Select--");
                        ly_bhava.setVisibility(View.GONE);
                        ayanmsha_list.add("Kpold");
                        ayanmsha_list.add("Kpnew");
                        ayanmsha_list.add("kpstline");

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
                ArrayAdapter adapter = new ArrayAdapter(Main_new.this, android.R.layout.simple_list_item_1, ayanmsha_list);
                adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                sp_ayanmsha.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_BhavaMadhya.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bhavaMadhyaid = bhavaMadhyaid_list.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_ayanmsha.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ayanmshaid = ayanmshaid_list.get(position);

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
                nodeid =Nodelistid.get(position);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        layout.show();

    }*/
}
