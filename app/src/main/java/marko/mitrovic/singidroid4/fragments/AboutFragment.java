package marko.mitrovic.singidroid4.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity;
import marko.mitrovic.singidroid4.R;

public class AboutFragment extends Fragment {
    private View view;
    @SuppressLint("ResourceAsColor")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_about, container, false);

        TextView licenseInfo = view.findViewById(R.id.ossLicense);

        licenseInfo.setOnClickListener(v -> {
            startActivity(new Intent(this.getActivity(), OssLicensesMenuActivity.class));
        });

        return view;
    }
}
