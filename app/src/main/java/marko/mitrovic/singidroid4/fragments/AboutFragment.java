package marko.mitrovic.singidroid4.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import marko.mitrovic.singidroid4.MainActivity;
import marko.mitrovic.singidroid4.R;

public class AboutFragment extends Fragment {
    private View view;
    @SuppressLint("ResourceAsColor")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_about,container,false);



        ((MainActivity) getActivity()).setStatusBarColor(0, 0, "#008A94", false);



        return view;
    }
}
