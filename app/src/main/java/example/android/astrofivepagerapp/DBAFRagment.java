package example.android.astrofivepagerapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
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


import static android.content.Context.LOCATION_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DBAFRagment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DBAFRagment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DBAFRagment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    WebView panchangam;
    // TODO: Rename and change types of parameters
    double tz;
    String date;


    private static final int REQUEST_LOCATION_TURN_ON = 2000;
    boolean markerClicked;
    TextView tvLocInfo;
    Button submitBtn;
    private static final int PERMISSION_REQUEST_CODE = 200;

    ProgressBar pb;
    private LocationManager locationManager;
    private String provider;
    private OnFragmentInteractionListener mListener;
    public CustomProgressDialog progressDialog;
    SharedPreferences.Editor editor;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public DBAFRagment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DBAFRagment.
     */
    // TODO: Rename and change types and number of parameters
    public static DBAFRagment newInstance(String param1, String param2) {
        DBAFRagment fragment = new DBAFRagment();
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
        View v = inflater.inflate(R.layout.fragment_advt_fragment, container, false);
        panchangam = (WebView) v.findViewById(R.id.panchangam);
        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        pb = (ProgressBar) v.findViewById(R.id.progressBar);
        // progressDialog = CustomProgressDialog.getInstance();
        // Define the criteria how to select the locatioin provider -> use
        // default
        //Calendar cal = Calendar.getInstance();


        //  Log.e("tz", String.valueOf(tz));

    /*    SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        editor.putString("date", date); // Storing string
        editor.putString("timezone", String.valueOf(tz)); // Storing string
        editor.putString("latitude", String.valueOf(latitude)); // Storing string
        editor.putString("longitude", String.valueOf(longitude)); // Storing string
editor.commit();*/
        /* TimeZone.getTimeZone("UTC").getOffset();*/

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
        String longitude = pref.getString("longitude", "");
        String Placename = pref.getString("cityname", "");
        String queryurl ="lat=" + latitude + "&Lon=" + longitude + "&tz=" + tz + "&Mydate=" + date + "&mychartMethod=" + chartid + "&myayanmsha=" + ayanmshaid + "&isBhavaMadhya=" + bhavaMadhyaid + "&lang=" +langid+"&Placename="+Placename+"&Nodetype="+nodeid;
        String queryurl1="lat=" + latitude + "&Lon=" + longitude + "&tz=" + tz + "&Mydate=" + date + "&mychartMethod=" + "0" + "&myayanmsha=" + "0" + "&isBhavaMadhya=" + "1" + "&lang=" +"En"+"&Placename="+Placename+"&Nodetype="+"0";

        Log.d("url",queryurl);
        Log.d("url1",queryurl1);
        WebView mywebview = (WebView) v.findViewById(R.id.panchangam);
        //WebView mywebview1 = (WebView) v.findViewById(R.id.adver);
        mywebview.setWebViewClient(new CustomWebViewClient());
        WebSettings webSetting = mywebview.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setDisplayZoomControls(true);
        if(chartid.equals("") && bhavaMadhyaid.equals("") && ayanmshaid.equals("")&& langid.equals("") && nodeid.equals(""))
        {            if (!latitude.equals("0.0") && !longitude.equals("0.0") && !latitude.equals("") && !longitude.equals("")) {
                //  mywebview.loadUrl("https://kpaf.in/astroapp/mobileapp/panchangam.aspx?lat=" + String.valueOf(latitude) + "&Lon=" + String.valueOf(longitude) + "&tz=" + String.valueOf(tz) + "&Mydate=" + date + "&charttype=N");
                mywebview.loadUrl("https://astrouser.com/astroapp/mobileapp/dba.aspx?" + queryurl1);
            } else {
                pb.setVisibility(View.GONE);
            }
        }else {
            mywebview.loadUrl("https://astrouser.com/astroapp/mobileapp/dba.aspx?" + queryurl);

        }
        Log.e("url", "https://astrouser.com/astroapp/mobileapp/dba.aspx?lat=" + String.valueOf(latitude) + "&Lon=" + String.valueOf(longitude) + "&tz=" + String.valueOf(tz) + "&Mydate=" + date + "&charttype=N"+"&Placename="+Placename+"&Nodetype="+GlobalDeclaration.node);
        // mywebview1.loadUrl("https://kpaf.in/astroapp/mobileapp/adv.html");
        //   progressDialog.showProgress(getActivity());
        return v;
    }


    private class CustomWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
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

            //progressDialog.hideProgress();
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
