package marko.mitrovic.singidroid4.fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import marko.mitrovic.singidroid4.R;


public class UniversityBillsFragment extends Fragment{

    private View view;
    private ViewGroup mTableViewGroup;

    public UniversityBillsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_university_bills, container, false);

        mTableViewGroup = view.findViewById(R.id.billTable);

        //This should be pulling data from the API, but eh...
        addToTable("PFB - Poslovna ekonomija", "160-360190-86");
        addToTable("PFB - Anglistika", "160-360180-19");
        addToTable("FTHM - Fakultet za turistički i hotelijerski menadžement", "160-342460-23");
        addToTable("FIR - Informatika i Računarstvo", "265-1780310001126-61");
        addToTable("TF - Tehnički fakultet", "265-1780310001129-52");
        addToTable("FFKMS - Fakultet za fizčku kulturu i menadžement u sportu", "265-1780310001128-55");
        addToTable("Master i doktorske studije", "160-360186-98");
        addToTable("Strane studije", "265-1780310001125-64");
        addToTable("Krems", "265-1780310001128-55");
        addToTable("Prijava ispita i uverenja, Diplomski radovi, izrada diplome, upisni materijal, osiguranje", "265-1780310001130-49");

        TextView text1 = view.findViewById(R.id.text1);

        text1.setText(Html.fromHtml("<b> Molimo Vas da na sve uplate ka Univerzitetu u poziv na broj unosite Vaš broj indeksa u formi godina\n" +
                "upisa zatim broj indeksa, bez razmaka i znakova interpunkcije npr. 2017200079 . </b>"));

        return view;
    }


    private void addToTable(String facultyName, String bankAccountNumber) {
        View tableView = getActivity().getLayoutInflater().inflate(R.layout.uni_bill_table_row, null);

        TextView table_faculty_name = tableView.findViewById(R.id.faculty_name);
        TextView table_bank_number = tableView.findViewById(R.id.bank_number);

        tableView.setTag(bankAccountNumber);

        tableView.setOnLongClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Copied Text", v.getTag().toString());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getContext(), "Copied!", Toast.LENGTH_SHORT).show();
            return true;
        });
        table_faculty_name.setText(facultyName);

        table_bank_number.setText(bankAccountNumber);

        mTableViewGroup.addView(tableView);
    }
}
