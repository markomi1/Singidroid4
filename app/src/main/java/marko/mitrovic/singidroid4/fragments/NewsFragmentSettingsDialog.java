package marko.mitrovic.singidroid4.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.ViewModelProvider;
import marko.mitrovic.singidroid4.R;
import marko.mitrovic.singidroid4.repo.SharedViewModel;
import org.json.JSONArray;
import org.json.JSONException;

public class NewsFragmentSettingsDialog extends AppCompatDialogFragment {
    private View view;
    private SharedViewModel viewModel; //Shared repo
    private SharedPreferences studentPerfs;
    private RadioGroup lin;


    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Context context;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        viewModel = new ViewModelProvider(getActivity()).get(SharedViewModel.class); //get repo
        studentPerfs = getActivity().getSharedPreferences("StudentPrefs", Context.MODE_PRIVATE);


        LayoutInflater inflater = getActivity().getLayoutInflater();

        view = inflater.inflate(R.layout.news_settings_dialog, null);

        if (studentPerfs.getString("NewsSource", "").equals("")) {
            try {
                addRadioButtons(viewModel.getNewsFaculties().getValue(), "US");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            try {
                addRadioButtons(viewModel.getNewsFaculties().getValue(), studentPerfs.getString("NewsSource", ""));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        builder.setView(view).setTitle("Change News Source").setPositiveButton("Apply", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (lin.getCheckedRadioButtonId() != -1) {

                    RadioButton btn = (RadioButton) lin.findViewById(lin.getCheckedRadioButtonId());
                    Log.d("Checked", String.valueOf(btn.getTag()));

                    studentPerfs.edit().putString("NewsSource", String.valueOf(btn.getTag())).apply();
                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Do nothing
            }
        });
        return builder.create();
    }


    public void addRadioButtons(JSONArray toSet, String previousSelection) throws JSONException {
        if (toSet == null) {
            return;
        }
        lin = (RadioGroup) view.findViewById(R.id.news_sources_radio_group);
        lin.removeAllViews();
        for (int i = 0; i < toSet.length(); i++) {
            RadioButton rdbtn = new RadioButton(getActivity());
            String textToSet = (String) toSet.getJSONObject(i).getString("faculty_short") + " - " + toSet.getJSONObject(i).getString("faculty_title");
            String temp = toSet.getJSONObject(i).getString("faculty_short");
            if (temp.equals(previousSelection)) { //Check to see what to check by default.
                rdbtn.setChecked(true);
            }
            // ^ Makes it look like US - Singidunum
            rdbtn.setText(textToSet);
            rdbtn.setTag(toSet.getJSONObject(i).getString("faculty_short"));
            rdbtn.setId(ViewCompat.generateViewId());
            rdbtn.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.ic_news_radio_buttons));
            //rdbtn.setHeight(70);
            rdbtn.setTextColor(Color.parseColor("#000000"));
            //rdbtn.setTextSize(15);


            lin.addView(rdbtn);
            RadioGroup.LayoutParams layoutParams = (RadioGroup.LayoutParams) view.findViewById(rdbtn.getId()).getLayoutParams();
            layoutParams.setMargins(10, 50, 0, 20);


        }

    }


}
