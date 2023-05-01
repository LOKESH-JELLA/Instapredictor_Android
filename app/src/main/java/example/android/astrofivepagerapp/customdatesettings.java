package example.android.astrofivepagerapp;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link customdatesettings.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link customdatesettings#newInstance} factory method to
 * create an instance of this fragment.
 */
public class customdatesettings extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String date;
    Button bt_time, bt_date;
    TextView currentdatetime;
    EditText set_date, set_time;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private LocationManager locationManager;
    private String provider;
    Fragment selectedFragment = null;
    String fragmentTag = null;
    Toolbar toolbar;
    private int mYear, mMonth, mDay, mHour, mMinute, mseconds;
    double latitude;
    double tz;
    //String date;
    double longitude;

    Location mLastLocation;
    private static final int REQUEST_LOCATION_TURN_ON = 2000;

    private static final int PERMISSION_REQUEST_CODE = 200;
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

    public customdatesettings() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment customdatesettings.
     */
    // TODO: Rename and change types and number of parameters
    public static customdatesettings newInstance(String param1, String param2) {
        customdatesettings fragment = new customdatesettings();
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
        View v= inflater.inflate(R.layout.datetimepicker5pager, container, false);

        final String[] date_data = new String[1];
        final String[] dd = new String[1];
        final String[] tt = new String[1];
       Button ok = (Button) v.findViewById(R.id.ok);
        Button cancel = (Button) v.findViewById(R.id.btn_cancel);
       /* cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.dismiss();
            }
        });
*/
       /* bt_date = (Button) v.findViewById(R.id.se);
        bt_time = (Button) v.findViewById(R.id.selecttime);*/
        set_date = (EditText) v.findViewById(R.id.set_date);
        set_time = (EditText) v.findViewById(R.id.set_time);
        currentdatetime = (TextView) v.findViewById(R.id.currentdate_time);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
        String strDate = mdformat.format(calendar.getTime());

        set_time.setText(strDate);
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

        set_date.setText(mDay + ":" + (mMonth + 1) + ":" + mYear);


        //currentdatetime.setVisibility(View.VISIBLE);
       /* change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bt_date.setVisibility(View.VISIBLE);
                bt_time.setVisibility(View.VISIBLE);
                currentdatetime.setVisibility(View.GONE);
                currentdatetime.setText("");
            }
        });*/

        set_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);
                mseconds = c.get(Calendar.SECOND);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
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

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                set_date.setText(dayOfMonth + ":" + (monthOfYear + 1) + ":" + year);
                                dd[0] = (monthOfYear + 1) + ":" + dayOfMonth + ":" + year;


                            }

                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                datePickerDialog.show();

            }
        });


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
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalDeclaration.date = currentdatetime.getText().toString();
                //GlobalDeclaration.time = df.format(calobj.getTime());


                editor.putString("date", date); // Storing string
                editor.commit();
                GlobalDeclaration.flag = true;
                Intent intent = new Intent(getActivity(),MainActivity.class);
                startActivity(intent);



            }
        });
        set_time.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                date = dd[0] + ":" + set_time.getText().toString().trim();

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
                date = dd[0] + ":" + set_time.getText().toString().trim();

            }
        });
       /* date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // Show Date dialog
                showDialog(Date_id);
            }
        });
        time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // Show time dialog
                showDialog(Time_id);
            }
        });*/
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
