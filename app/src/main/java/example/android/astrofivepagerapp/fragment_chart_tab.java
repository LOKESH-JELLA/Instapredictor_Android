package example.android.astrofivepagerapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link fragment_chart_tab.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link fragment_chart_tab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_chart_tab extends Fragment implements DBAFRagment.OnFragmentInteractionListener,TimeChartFragment.OnFragmentInteractionListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private TabchartAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    SharedPreferences.Editor editor;
    private OnFragmentInteractionListener mListener;

    public fragment_chart_tab() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_chart_tab.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_chart_tab newInstance(String param1, String param2) {
        fragment_chart_tab fragment = new fragment_chart_tab();
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
        View v =inflater.inflate(R.layout.chart_new, container, false);
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        String date = pref.getString("date", ""); // getting String
        String tz = pref.getString("timezone", ""); // getting String
        String latitude = pref.getString("latitude", ""); // getting String
        String longitude = pref.getString("longitude", ""); // getting String

        viewPager = (ViewPager) v.findViewById(R.id.viewPager);
        GlobalDeclaration.date_new=date;
        GlobalDeclaration.lat_new=latitude;
        GlobalDeclaration.lon_new=longitude;
        GlobalDeclaration.tz_new=tz;


        tabLayout = (TabLayout) v.findViewById(R.id.tabLayout);

        adapter = new TabchartAdapter(getChildFragmentManager());
        adapter.addFragment(new TimeChartFragment(), "Time Chart");
        adapter.addFragment(new DBAFRagment(), "DBA");



        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
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
}
