package marko.mitrovic.singidroid4.fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import androidx.fragment.app.Fragment;
import marko.mitrovic.singidroid4.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Predmet extends Fragment{
    public WebView mWebView;
    private View view;
    private SharedPreferences studentPerfs;

    public Predmet() {
        // Required empty public constructor
    }


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        studentPerfs = this.getActivity().getSharedPreferences("StudentPrefs", Context.MODE_PRIVATE);

        boolean skipped = studentPerfs.getBoolean("SkippedInit", true);
        if (!skipped) {
            String faculty = studentPerfs.getString("FacultyChoice", "");
            String year = studentPerfs.getString("YearChoice", "");
            String courseId = studentPerfs.getString("CourseChoice", "120");
            view = inflater.inflate(R.layout.fragment_predmet_webview, container, false);

            mWebView = (WebView) view.findViewById(R.id.predmet_webview);
            mWebView.loadUrl("http://192.168.4.110:8080/predmeti?id=" + courseId);
            WebSettings webSettings = mWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);


        } else {
            view = inflater.inflate(R.layout.fragment_predmet, container, false);
        }


        return view;
    }

}
