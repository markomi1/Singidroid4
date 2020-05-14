package marko.mitrovic.singidroid4.fragments.news_tabs;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import marko.mitrovic.singidroid4.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class newsTab extends Fragment {


    public static newsTab newInstance(int index) {
        newsTab fragment = new newsTab();
        Bundle bundle = new Bundle();
        bundle.putInt("section_number", index);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news_tab, container, false);
    }

}
