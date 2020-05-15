package marko.mitrovic.singidroid4.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
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
        studentPerfs = getActivity().getSharedPreferences("StudentPrefs", Context.MODE_PRIVATE); //Prepare shared preferences


        LayoutInflater inflater = getActivity().getLayoutInflater();

        view = inflater.inflate(R.layout.news_settings_dialog, null);

        final JSONArray newsFaculties = viewModel.getNewsFaculties().getValue(); //We get the value from the repo and put it in local var for later use

        if (studentPerfs.getString("NewsSource", "").equals("")) {
            //If the app is firsttime run then the shared pref NewsSource would be null, so it returns an empty string, and we check for that, if true then we set ----
            //the radio button to US - Singidunum faculty(default) and we also write it down in shared pref so next time this check is skipped and goes to the one below
            try {
                addRadioButtons(newsFaculties, "US");
                studentPerfs.edit().putString("NewsSource", "US").apply();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            try {
                //It'll return US if it doesn't find anything, which it shouldn't as we checked for that above, but if it does find that it'll pass that
                addRadioButtons(newsFaculties, studentPerfs.getString("NewsSource", "US"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        builder.setView(view).setTitle("Change News Source").setPositiveButton("Apply", new DialogInterface.OnClickListener() { //Making of the popup dialog
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (lin.getCheckedRadioButtonId() != -1) { //Checks to see if any button is selected, which it should be, if it isn't somehow it's skipped


                    String selected = viewModel.getRadioButtonSeleted().getValue(); //We get the selected radio button from the repo
                    for (int i = 0; i < newsFaculties.length(); i++) { // we lope over the JSON file that we received
                        try {
                            if (newsFaculties.getJSONObject(i).getString("faculty_short").equals(selected)) {
                                //Here we check to see if the selected radio button acronym matches the one in the JSON
                                String color = "#" + newsFaculties.getJSONObject(i).getString("color"); //If it does we store the color of it
                                String newsSource = newsFaculties.getJSONObject(i).getString("faculty_short");// And it's acronym
                                viewModel.setToolbarColor(color); //We notify the function that's observing this variable to change the color of the toolbar
                                studentPerfs.edit().putString("Color", color).apply(); //We save the color used in the shared preferences
                                studentPerfs.edit().putString("NewsSource", newsSource).apply(); //We also save the News Source in the shared preferences
                                break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Do nothing
            }
        });

        Dialog dialog = builder.create(); //We make a dialog out of builder so we can down below attach custom fadein/fadeout animations
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        return dialog;
    }


    public void addRadioButtons(JSONArray toSet, String previousSelection) throws JSONException {
        if (toSet == null) {
            return;
        }
        lin = (RadioGroup) view.findViewById(R.id.news_sources_radio_group);//We get the Radio Group so we can get the button access it's members
        lin.removeAllViews(); // remove old members
        for (int i = 0; i <= toSet.length(); i++) {
            RadioButton rdbtn = new RadioButton(getActivity()); //Make a radio button
            String textToSet = (String) toSet.getJSONObject(i).getString("faculty_short") + " - " + toSet.getJSONObject(i).getString("faculty_title");
            // ^ Makes it look like  example: US - Singidunum
            String temp = toSet.getJSONObject(i).getString("faculty_short");
            if (temp.equals(previousSelection)) { //Check to see what to check by default.
                rdbtn.setChecked(true);
            }

            rdbtn.setText(textToSet); //Give it title
            rdbtn.setTag(toSet.getJSONObject(i).getString("faculty_short")); //Give it a tag as well
            rdbtn.setId(ViewCompat.generateViewId()); //Generate random ID
            rdbtn.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.ic_news_radio_buttons)); //Set background to custom button layout

            rdbtn.setTextColor(Color.parseColor("#000000")); //Set text color


            rdbtn.setOnClickListener(new View.OnClickListener() { //Set onclick listener for all radio buttons
                @Override
                public void onClick(View v) {
                    viewModel.setRadioButtonSelected(v.getTag().toString()); //When they're clicked they set their tagID in the repo which is later used above
                }
            });
            lin.addView(rdbtn); //We add the radio buttons to the view
            RadioGroup.LayoutParams layoutParams = (RadioGroup.LayoutParams) view.findViewById(rdbtn.getId()).getLayoutParams();
            layoutParams.setMargins(10, 50, 0, 20);// Bit of padding


        }

    }


}