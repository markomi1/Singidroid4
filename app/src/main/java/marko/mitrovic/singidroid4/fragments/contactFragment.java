package marko.mitrovic.singidroid4.fragments;

import android.content.*;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import marko.mitrovic.singidroid4.R;

import java.util.ArrayList;
import java.util.List;


public class contactFragment extends Fragment{

    private final List<View> viewGroup = new ArrayList<>();
    private final String TAG = "contactFragment";
    private View view;
    private ViewGroup mLinearLayout;

    public contactFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_contact, container, false);

        mLinearLayout = view.findViewById(R.id.contactFragment); //Getting the container


        add2Number1Email("Osnovne Studije",
                "Studenski Servis",
                "Danijelova 32, Beograd",
                "+381 11 30 94 094",

                "Studenski Servis",
                "Kumodraška 261a, Beograd",
                "+381 11 30 93 306",

                "Studenski Servis",
                "studije@singidunum.ac.rs",
                "Pitanje za studentski servis");

        add1Number1Email("Master Studije",
                "Studenska Služba",
                "Danijelova 32, Beograd",
                "+381 11 30 93 210",
                "Studenska Služba",
                "master@singidunum.ac.rs",
                "Pitanje za Studensku Službu");

        add1Number1Email("Doktorske Studije",
                "Studenska Služba",
                "Danijelova 32, Beograd",
                "+381 11 30 94 047",
                "Studenska Služba",
                "poslediplomske@singidunum.ac.rs",
                "Pitanje za Studensku Službu");

        add1Number1Email("Centar Novi Sad",
                "Studenska Služba",
                "Bulevar Mihajla Pupina 4a, Novi Sad",
                "+381 21 66 21 900",
                "Studenski Servis",
                "novisad@singidunum.ac.rs",
                "Pitanje za studentski servis");

        add1Number1Email("Centar Niš",
                "Studenski Servis",
                "Nikole Pašića 28, Niš",
                "+381 18 208 300",
                "Studenski Servis",
                "nis@singidunum.ac.rs",
                "Pitanje za studentski servis");
        add1Number1Email("IMC FH Krems - Singidunum",
                "Studenski Servis",
                "Danijelova 32, Beograd",
                "+381 11 30 93 265",
                "Studenski Servis",
                "krems@singidunum.ac.rs",
                "Pitanje za studentski servis");

        add1Number1Email("DLS Služba",
                "DLS Služba",
                "Danijelova 32, Beograd",
                "+381 11 30 94 059",
                "Studenski Servis",
                "dls@singidunum.ac.rs",
                "Pitanje za DLS službu");

        add1Number1Email("Računovodstvo",
                "Računovodstvo",
                "Danijelova 32, Beograd",
                "+381 11 30 94 059",
                "Računovodstvo",
                "racunovodstvo@singidunum.ac.rs",
                "Pitanje za računovodstvo");

        add2Number1Email("Marketing i PR",
                "Marketing i PR",
                "Danijelova 32, Beograd",
                "+381 11 30 93 229",
                "Predrag Obradović",
                "Marketing i PR Menadžer",
                "+381 65 30 93 229",
                "Predrag Obradović",
                "pobradovic@singidunum.ac.rs",
                "Pitanje za Marketing i PR");

        add2Number1Email("Singimail Podrška",
                "Lokacija Danijelova",
                "Danijelova 32, Beograd",
                "+381 11 30 94 067",
                "Lokacija Kumodraška",
                "Kumodraška 261a, Beograd",
                "+381 11 30 93 367",
                "Singimail Podrška",
                "erc@singimail.ac.rs",
                "Pitanje za Singimail Podršku");

        add2Number1Email("eStudent Podrška",
                "Lokacija Danijelova",
                "Danijelova 32, Beograd",
                "+381 11 30 94 011",
                "Lokacija Kumodraška",
                "Kumodraška 261a, Beograd",
                "+381 11 30 93 367",
                "eStudent Podrška",
                "estudent@singimail.ac.rs",
                "Pitanje za eStudent Podršku");

        add1Number1Email("Biblioteka",
                "Biblioteka Univerziteta Singidunum",
                "Danijelova 32, Beograd",
                "+381 11 30 93 284",
                "Biblioteka Univerziteta Singidunum",
                "biblioteka@singidunum.ac.rs",
                "Pitanje za biblioteku");

        add1Number1Email("Rektorat",
                "Rektorat Univerziteta Singidunum",
                "Danijelova 32, Beograd",
                "+381 11 30 93 220",
                "Rektorat Univerziteta Singidunum",
                "office@singidunum.ac.rs",
                "Pitanje za Univerzitet Singidunum");


        return view;
    }

    private void add2Number1Email(String location_name,
                                  String phone_name_1,
                                  String phone_location_1,
                                  String phone_number_1,
                                  String phone_name_2,
                                  String phone_location_2,
                                  String phone_number_2,
                                  String email_name,
                                  String email_address,
                                  String email_subject) {
        View contactView = getActivity().getLayoutInflater().inflate(R.layout.contact_list_3_fields, null);
        TextView contact_location_name = contactView.findViewById(R.id.contact_location_name);
        //Setting phone 1 details
        TextView contact_phone_name_1 = contactView.findViewById(R.id.contact_phone_name);
        TextView contact_phone_location_1 = contactView.findViewById(R.id.contact_phone_location);
        TextView contact_phone_number_1 = contactView.findViewById(R.id.contact_phone_number);
        //Setting phone 2 detilas
        TextView contact_phone_name_2 = contactView.findViewById(R.id.contact_phone_name2);
        TextView contact_phone_location_2 = contactView.findViewById(R.id.contact_phone_location2);
        TextView contact_phone_number_2 = contactView.findViewById(R.id.contact_phone_number2);

        TextView contact_email_name = contactView.findViewById(R.id.contact_email_name);
        TextView contact_email_address = contactView.findViewById(R.id.contact_email);

        ImageView contact_phone_icon_1 = contactView.findViewById(R.id.phoneNumberIcon);
        ImageView contact_phone_icon_2 = contactView.findViewById(R.id.phoneNumberIcon2);
        ImageView contact_email_icon = contactView.findViewById(R.id.emailIcon);

        //Setting the data that's gonna get passed to the onClickListeners
        contact_phone_icon_1.setTag(phone_number_1);
        contact_phone_icon_2.setTag(phone_number_2);
        contact_email_icon.setTag(R.string.email, email_address);
        contact_email_icon.setTag(R.string.email_subject, email_subject);
        //onClick listener setter for phone contact, sets onClick and onLongClick.
        clickListenersSetter(contact_phone_icon_1);
        clickListenersSetter(contact_phone_icon_2);
        //Setting the emailOnClick event
        setEmailOnClickEvent(contact_email_icon);

        contact_location_name.setText(location_name);

        contact_phone_name_1.setText(phone_name_1);
        contact_phone_location_1.setText(phone_location_1);
        contact_phone_number_1.setText(phone_number_1);


        contact_phone_name_2.setText(phone_name_2);
        contact_phone_location_2.setText(phone_location_2);
        contact_phone_number_2.setText(phone_number_2);

        contact_email_name.setText(email_name);
        contact_email_address.setText(email_address);

        mLinearLayout.addView(contactView);
    }

    private void setEmailOnClickEvent(ImageView imgView) {
        imgView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType(ClipDescription.MIMETYPE_TEXT_PLAIN);
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{v.getTag(R.string.email).toString()});
            intent.putExtra(Intent.EXTRA_SUBJECT, v.getTag(R.string.email_subject).toString());
            startActivity(Intent.createChooser(intent, "Send Email"));
        });
    }

    private void clickListenersSetter(ImageView imgView) {
        imgView.setOnClickListener(v -> {
            Log.d(TAG, "onClick Phone: " + v.getTag());
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + v.getTag()));
            startActivity(intent);
        });
        imgView.setOnLongClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Copied Text", v.getTag().toString());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getContext(), "Copied!", Toast.LENGTH_SHORT).show();
            return true;
        });
    }

    private void add1Number1Email(String location_name,
                                  String phone_name,
                                  String phone_location,
                                  String phone_number,
                                  String email_name,
                                  String email_address,
                                  String email_subject) {
        View contactView = getActivity().getLayoutInflater().inflate(R.layout.contact_list_2_fields, null);

        TextView contact_location_name = contactView.findViewById(R.id.contact_location_name);
        //Setting phone 1 details
        TextView contact_phone_name_1 = contactView.findViewById(R.id.contact_phone_name);
        TextView contact_phone_location_1 = contactView.findViewById(R.id.contact_phone_location);
        TextView contact_phone_number_1 = contactView.findViewById(R.id.contact_phone_number);

        TextView contact_email_name = contactView.findViewById(R.id.contact_email_name);
        TextView contact_email_address = contactView.findViewById(R.id.contact_email);


        ImageView contact_phone_icon = contactView.findViewById(R.id.phoneNumberIcon);
        ImageView contact_email_icon = contactView.findViewById(R.id.emailIcon);

        //Setting the tags, aka, the data that the onClick will retrieve
        contact_phone_icon.setTag(phone_number);

        contact_email_icon.setTag(R.string.email, email_address);
        contact_email_icon.setTag(R.string.email_subject, email_subject);
        //Setting the onClick listeners
        contact_phone_icon.setOnClickListener(v -> {
            Log.d(TAG, "onClick Phone: " + v.getTag());
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + v.getTag()));
            startActivity(intent);
        });

        setEmailOnClickEvent(contact_email_icon);

        contact_location_name.setText(location_name);
        //Setting the phone details fields
        contact_phone_name_1.setText(phone_name);
        contact_phone_location_1.setText(phone_location);
        contact_phone_number_1.setText(phone_number);
        //Setting the email field
        contact_email_name.setText(email_name);
        contact_email_address.setText(email_address);

        //Finally adding it all to the layout
        mLinearLayout.addView(contactView);
    }

}
