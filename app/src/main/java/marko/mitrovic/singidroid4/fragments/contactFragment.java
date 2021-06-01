package marko.mitrovic.singidroid4.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import marko.mitrovic.singidroid4.R;

import java.util.ArrayList;
import java.util.List;


public class contactFragment extends Fragment implements View.OnClickListener{

    private final List<View> viewGroup = new ArrayList<>();
    private final String TAG = "contactFragment";
    private View view;
    private ViewGroup mLinearLayout;

    public contactFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_contact, container, false);

        mLinearLayout = view.findViewById(R.id.contactFragment); //Getting the container

        add1Number1Email("Osnovne Studije", "Studenski Servis", "Danijelova 32, Beograd", "+381 11 30 94 094", "some@email.com");


        return view;
    }


    private void add1Number1Email(String name, String phone_name, String phone_location, String phone_number, String email) {
        View layout2 = getActivity().getLayoutInflater().inflate(R.layout.contact_list, null);

        TextView contact_name = layout2.findViewById(R.id.contact_name);
        TextView contact_phone_name = layout2.findViewById(R.id.contact_phone_name);
        TextView contact_phone_location = layout2.findViewById(R.id.contact_phone_location);
        TextView contact_phone_number = layout2.findViewById(R.id.contact_phone_number);

        ImageView contact_phone_icon = layout2.findViewById(R.id.phoneNumberIcon);
        ImageView contact_email_icon = layout2.findViewById(R.id.emailIcon);

        contact_phone_icon.setTag(phone_number);
        contact_email_icon.setTag(email);

        contact_phone_icon.setOnClickListener(v -> {
            Log.d(TAG, "onClick Phone: " + v.getTag());
        });

        contact_email_icon.setOnClickListener(v -> {
            Log.d(TAG, "onClick Email: " + v.getTag());
        });

        contact_name.setText(name);
        contact_phone_name.setText(phone_name);
        contact_phone_location.setText(phone_location);

        contact_phone_number.setText(phone_number);

        mLinearLayout.addView(layout2);
    }

    @Override
    public void onClick(View v) {

    }
}
