package example.android.astrofivepagerapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link goPremiumFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link goPremiumFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class goPremiumFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
WebView gopremiumwebview;
    ProgressBar pb;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public goPremiumFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment goPremiumFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static goPremiumFragment newInstance(String param1, String param2) {
        goPremiumFragment fragment = new goPremiumFragment();
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
        View v= inflater.inflate(R.layout.fragment_go_premium, container, false);
        gopremiumwebview = (WebView)v.findViewById(R.id.webview_timechart);

        pb = (ProgressBar) v.findViewById(R.id.progressBar);
        pb.setVisibility(View.VISIBLE);

        //String timechart_url = "http://products.astrouser.com";
        String timechart_url = "https://astrouser.com/astro-products/";
        gopremiumwebview.setWebViewClient(new goPremiumFragment.CustomWebViewClient());
        WebSettings webSetting = gopremiumwebview.getSettings();
//        WebSettings webSetting_p = panchangam.getSettings();
        //  WebSettings webSetting_r = rulingplanet.getSettings();
        webSetting.setJavaScriptEnabled(true);
        gopremiumwebview.clearHistory();
        gopremiumwebview.clearFormData();
        gopremiumwebview.clearCache(true);
        webSetting.setDisplayZoomControls(true);
        gopremiumwebview.loadUrl(timechart_url);
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
