package marko.mitrovic.singidroid4;

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

public class NewsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private View view;
    SwipeRefreshLayout swipeLayout;
    @SuppressLint("ResourceAsColor")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_news,container,false);

        ((MainActivity)getActivity()).setStatusBarColor(0,0,"#A8011D");



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
