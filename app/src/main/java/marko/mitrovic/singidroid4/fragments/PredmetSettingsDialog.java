package marko.mitrovic.singidroid4.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.lifecycle.ViewModelProvider;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import marko.mitrovic.singidroid4.R;
import marko.mitrovic.singidroid4.api.ApiCalls;
import marko.mitrovic.singidroid4.api.AppNetworking;
import marko.mitrovic.singidroid4.repo.SharedViewModel;
import marko.mitrovic.singidroid4.util.PredmetiSpinnerAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PredmetSettingsDialog extends AppCompatDialogFragment{
    private final String TAG = "PredmetSettingsDialog";
    private View view;
    private ApiCalls api;
    private SharedViewModel viewModel; //Shared repo
    private String selectedFaculty = "", selectedYear = "", selectedCourse = "";
    private Spinner facultyView, yearView, courseView;
    private SharedPreferences studentPerfs;
    private Gson gson;
    private String preselectedFaculty, preselectedYear, preselectedCourse;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        viewModel = new ViewModelProvider(getActivity()).get(SharedViewModel.class); //get repo
        api = AppNetworking.getClient(getContext()).create(ApiCalls.class);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.predmeti_settings_dialog, null);
        facultyView = view.findViewById(R.id.predmeti_faculty);
        yearView = view.findViewById(R.id.predmeti_year);
        courseView = view.findViewById(R.id.predmeti_course);
        gson = new Gson();
        studentPerfs = this.getActivity().getSharedPreferences("StudentPrefs", Context.MODE_PRIVATE);
        preselectedFaculty = studentPerfs.getString("FacultyChoice", "");
        preselectedYear = studentPerfs.getString("YearChoice", "");
        preselectedCourse = studentPerfs.getString("CourseChoice", "");


        api.getFaculties().enqueue(new Callback<JsonArray>(){
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.body() != null) {
                    setFacultiesSpinner(response.body());
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.e("getFaculties_Predmeti", "Failed, dumping stack trace:");
                t.printStackTrace();
            }
        });

        api.getYears().enqueue(new Callback<JsonArray>(){
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.body() != null) {
                    setYearsSpinner(response.body());
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.e("Call_predmet_getYears", "Failed, dumping stack trace:");
                t.printStackTrace();
                Toast.makeText(view.getContext(), R.string.loading_content_error, Toast.LENGTH_LONG).show();
            }
        });


        //Making of the popup dialog
        builder.setView(view).setTitle(R.string.subject_change_title).setPositiveButton(R.string.apply, (dialog, which) -> {
            if (!selectedCourse.equals("")) { //Sanity check, just in case somehow it's ""
                viewModel.setPredmetDialog(selectedCourse); //If it's not "" then we set the viewModal var while will notify an observer located in Predmet.java
                dialog.dismiss(); //Dismissing the dialog
            } else {
                Toast.makeText(getActivity(), R.string.empty_fields_somehow, Toast.LENGTH_SHORT).show();
            }

        }).setNegativeButton(R.string.cancel, (dialog, which) -> {
            //Do nothing
        }).setNeutralButton(R.string.predmet_dialog_neutral, (dialog, which) -> { //IF the Save As Default is selected then we do the next:
            if (!selectedFaculty.equals("") && !selectedYear.equals("") && !selectedCourse.equals("")) { //Sanity check to see if everything is set, which it should be
                studentPerfs.edit().putBoolean("SkippedInit", false).apply(); //Set the Skip Init to false
                studentPerfs.edit().putString("FacultyChoice", selectedFaculty).apply(); //Set the FacultyChoice to the selected SelectedFaculty
                studentPerfs.edit().putString("YearChoice", selectedYear).apply(); //Set the YearChoice to the selected SelectedYear
                studentPerfs.edit().putString("CourseChoice", selectedCourse).apply(); //Set the CourseChoice to the selected SelectedCourse
                viewModel.setPredmetDialog(selectedCourse); //Notifying the observer in Predmet.java that's watching for changes in this var
                dialog.dismiss(); //Dialog dismissing
            } else {
                Toast.makeText(getActivity(), R.string.empty_fields_somehow, Toast.LENGTH_SHORT).show();
            }

        });

        Dialog dialog = builder.create(); //We make a dialog out of builder so we can down below attach custom fadein/fadeout animations
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Fancy fade in animation
        return dialog;
    }


    public void setFacultiesSpinner(JsonArray arr) { //Used to set the Faculties spinner(list)
        Log.d(TAG, "Faculties recieved"); //Debug log
        Log.d(TAG, arr.toString());//Debug log
        PredmetiSpinnerAdapter aa = new PredmetiSpinnerAdapter(getActivity(), arr); //Custom adapter that i stole from some website
        facultyView.setAdapter(aa); //Setting the adapter to the list
        facultyView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getContext(), "Selected: " + facultyView.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                //When stuff is selected this is triggered
                JsonObject obj = gson.fromJson(facultyView.getSelectedItem().toString(), JsonObject.class); //Getting the selected item, which is a JSON, and extracting the ID from it
                selectedFaculty = obj.get("id").getAsString(); //We're getting the ID field and setting the selectedFaculty with it
                getCourses();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (!preselectedFaculty.equals("")) {
            for (int i = 0; i < arr.size(); i++) {
                if (arr.get(i).getAsJsonObject().get("id").getAsString().equals(preselectedFaculty)) {
                    facultyView.setSelection(i); //If SharedPreferences is set, aka it has faculty set then we use that to find it in the JSON array and set the spinner to the needed number
                }
            }
        }


        yearView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getContext(), "Selected: " + yearView.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                //When stuff is selected this is triggered
                JsonObject obj = gson.fromJson(yearView.getSelectedItem().toString(), JsonObject.class); //Same as above
                selectedYear = obj.get("id").getAsString(); //Same as above
                getCourses();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        courseView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getContext(), "Selected: " + courseView.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                JsonObject obj = gson.fromJson(courseView.getSelectedItem().toString(), JsonObject.class);
                selectedCourse = obj.get("courses_id").getAsString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void setYearsSpinner(JsonArray arr) {
        Log.d(TAG, "Years recieved");
        Log.d(TAG, arr.toString());
        PredmetiSpinnerAdapter aa = new PredmetiSpinnerAdapter(getActivity(), arr);
        yearView.setAdapter(aa);
        if (!preselectedYear.equals("")) {
            for (int i = 0; i < arr.size(); i++) {
                if (arr.get(i).getAsJsonObject().get("id").getAsString().equals(preselectedYear)) {
                    yearView.setSelection(i); //If SharedPreferences is set, aka it has years set then we use that to find it in the JSON array and set the spinner to the needed number
                }
            }
        }

    }


    public void getCourses() {

        if (!selectedFaculty.isEmpty() && !selectedYear.isEmpty()) {
            api.getCourse(selectedFaculty, selectedYear).enqueue(new Callback<JsonArray>(){
                @Override
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                    if (response.body() != null) {
                        JsonArray res = response.body();
                        Log.d(TAG, "Courses recieved");
                        Log.d(TAG, res.toString());
                        PredmetiSpinnerAdapter aa = new PredmetiSpinnerAdapter(getActivity(), res);
                        courseView.setAdapter(aa);

                        if (!preselectedCourse.equals("")) {
                            for (int i = 0; i < res.size(); i++) {
                                if (res.get(i).getAsJsonObject().get("courses_id").getAsString().equals(preselectedCourse)) {
                                    courseView.setSelection(i); //If SharedPreferences is set, aka it has years set then we use that to find it in the JSON array and set the spinner to the needed number
                                }
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonArray> call, Throwable t) {
                    Log.e("Call_predmet_getCourse", "Failed, dumping stack trace:");
                    t.printStackTrace();
                    Toast.makeText(view.getContext(), R.string.loading_content_error, Toast.LENGTH_LONG).show();
                }
            });
        }

    }

}
