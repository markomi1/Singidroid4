package marko.mitrovic.singidroid4.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import marko.mitrovic.singidroid4.MainActivity;
import marko.mitrovic.singidroid4.R;
import marko.mitrovic.singidroid4.tabsPageAdapter;

public class NewsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private View view;
    SwipeRefreshLayout swipeLayout;
    @SuppressLint("ResourceAsColor")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_news, container, false);

        ((MainActivity) getActivity()).setStatusBarColor(0, 0, "#A8011D");

        //Sets up tabs for news, aka, Singidunum,Fir, etc.
        tabsPageAdapter sectionsPagerAdapter = new tabsPageAdapter(getContext(), getActivity().getSupportFragmentManager());
        ViewPager viewPager = view.findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = view.findViewById(R.id.tabsLayout);
        tabs.setupWithViewPager(viewPager);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        swipeLayout = view.findViewById(R.id.swiperefresh);
        swipeLayout.setOnRefreshListener(this);
        //Implement so color of the spinner is the same color as Toolbar!
        swipeLayout.setColorScheme(android.R.color.holo_red_dark);

    }

    @Override
    public void onRefresh() {

        Log.i("REFRESH","REEEEEEEEEE");
        Toast.makeText(view.getContext(),"TEST",Toast.LENGTH_LONG).show();
        //swipeLayout.setRefreshing(false);

    }
}
