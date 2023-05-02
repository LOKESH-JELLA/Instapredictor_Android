package example.android.astrofivepagerapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;



/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TimeChartFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TimeChartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimeChartFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    SharedPreferences.Editor editor;
    double tz;
    String date;
    ProgressBar pb;
    private static final int REQUEST_LOCATION_TURN_ON = 2000;
    boolean markerClicked;
    TextView tvLocInfo;
    Button submitBtn;
    private static final int PERMISSION_REQUEST_CODE = 200;
    WebView rulingplanet;
    private LocationManager locationManager;
    private String provider;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public CustomProgressDialog progressDialog;

    private OnFragmentInteractionListener mListener;
    ProgressDialog pd;
    private TabchartAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public TimeChartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TimeChartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TimeChartFragment newInstance(String param1, String param2) {
        TimeChartFragment fragment = new TimeChartFragment();
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
        View v = inflater.inflate(R.layout.fragment_time_chart, container, false);
        progressDialog = CustomProgressDialog.getInstance();


        pb = (ProgressBar) v.findViewById(R.id.progressBar);
  /*      locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default
        //Calendar cal = Calendar.getInstance();


        //  Log.e("tz", String.valueOf(tz));
        Calendar cal = Calendar.getInstance();
        Date currentTime = cal.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM:dd:yyyy:HH:mm:ss");
        date = dateFormat.format(currentTime);
        Log.e("Currentdatetime", String.valueOf(date));

        TimeZone timezone = TimeZone.getDefault();
        Log.e("tz", String.valueOf(timezone.getRawOffset()));
        tz=(timezone.getRawOffset()/(60.0 * 1000.00) / 60.0);
        Log.e("tz", String.valueOf(tz));



        *//* TimeZone.getTimeZone("UTC").getOffset();*//*

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        try {
            if (!CheckGooglePlayServices()) {
                getActivity().finish();
                Toast.makeText(getActivity(), "Google play services not exist in your device", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


      */
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        String chartid = pref.getString("chartid", "");
        String bhavaMadhyaid = pref.getString("bhavaMadhyaid", "");
        String ayanmshaid = pref.getString("ayanmshaid", "");
        String langid = pref.getString("Lang", "");
        String nodeid = pref.getString("node", "");
        String date = pref.getString("date", ""); // getting String
        String tz = pref.getString("timezone", ""); // getting String
        String latitude = pref.getString("latitude", ""); // getting String
        String longitude = pref.getString("longitude", ""); // getting String
        String Placename = pref.getString("cityname", ""); // getting String

        String queryurl ="lat=" + latitude + "&Lon=" + longitude + "&tz=" + tz + "&Mydate=" + date + "&mychartMethod=" + chartid + "&myayanmsha=" + ayanmshaid + "&isBhavaMadhya=" + bhavaMadhyaid + "&lang=" +langid+"&Placename="+Placename+"&Nodetype="+nodeid;
        String queryurl1="lat=" + latitude + "&Lon=" + longitude + "&tz=" + tz + "&Mydate=" + date + "&mychartMethod=" + "0" + "&myayanmsha=" + "0" + "&isBhavaMadhya=" + "1" + "&lang=" +"En"+"&Placename="+Placename+"&Nodetype="+"0";

        WebView mywebview = (WebView) v.findViewById(R.id.panchangam);
        mywebview.setWebViewClient(new CustomWebViewClient());
        WebSettings webSetting = mywebview.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setDisplayZoomControls(true);
//        progressDialog.showProgress(getActivity());
        if(chartid.equals("") && bhavaMadhyaid.equals("") && ayanmshaid.equals("")&& langid.equals("") && nodeid.equals("")) {
            if (!latitude.equals("0.0") && !longitude.equals("0.0") && !latitude.equals("") && !longitude.equals("")) {
                mywebview.loadUrl("https://astrouser.com/astroapp/mobileapp/Nchart.aspx?" + queryurl1);

                Log.e("urlnchart", "https://astrouser.com/astroapp/mobileapp/Nchart.aspx?"+queryurl1);

            } else
            {
                pb.setVisibility(View.GONE);
            }
        }else {

            mywebview.loadUrl("https://astrouser.com/astroapp/mobileapp/Nchart.aspx?" + queryurl);
            Log.e("urlnchart", "https://astrouser.com/astroapp/mobileapp/Nchart.aspx?"+queryurl);


        }
        return v;
    }

    private class CustomWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            //progressDialog.showProgress(getActivity());

            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            //   progressDialog.showProgress(getActivity());

            pb.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            System.out.println("on finish");
            // progressDialog.hideProgress();
            pb.setVisibility(View.GONE);
        }
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

}
