package marko.mitrovic.singidroid4.repo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import org.json.JSONArray;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<JSONArray> facultiesArray = new MutableLiveData<>(); //Used for storing faculties JSON
    private MutableLiveData<JSONArray> yearsArray = new MutableLiveData<>(); //Used for storing years Array
    private MutableLiveData<String> selectedFaculty = new MutableLiveData<>(); //Stores selected faculty ID
    private MutableLiveData<String> selectedYear = new MutableLiveData<>(); //Stores selected year ID
    private MutableLiveData<String> selectedCourse = new MutableLiveData<>(); //Stores selected Course ID
    private MutableLiveData<JSONArray> newsFaculties = new MutableLiveData<>(); //Stores list of available news sources
    private MutableLiveData<String> toolbarColor = new MutableLiveData<>(); //Used to pass color data from NewsFragmentSettingsDialog class to MainActivity
    private MutableLiveData<String> radioButtonSelected = new MutableLiveData<>(); //Used to pass radioButton tag  data from within NewsFragmentSettingsDialog class

    public void setFacultiesArray(JSONArray input) {
        facultiesArray.postValue(input);
    }

    public LiveData<JSONArray> getFacultiesArray() {
        return facultiesArray;
    }

    public void setYearsArray(JSONArray input) {
        yearsArray.postValue(input);
    }

    public LiveData<JSONArray> getYearsArray() {
        return yearsArray;
    }

    public void setSelectedFaculty(String input) {
        selectedFaculty.postValue(input);
    }

    public LiveData<String> getSelectedFaculty() {
        return selectedFaculty;
    }

    public void setSelectedYear(String input) {
        selectedYear.postValue(input);
    }

    public LiveData<String> getSelectedYear() {
        return selectedYear;
    }

    public void setSelectedCourse(String input) {

        selectedCourse.postValue(input);
    }

    public LiveData<String> getSelectedCourse() {

        return selectedCourse;
    }

    public LiveData<JSONArray> getNewsFaculties() {
        return newsFaculties;
    }

    public void setNewsFaculties(JSONArray input) {
        newsFaculties.postValue(input);
    }


    public LiveData<String> getToolBarColor() {
        return toolbarColor;
    }

    public void setToolbarColor(String input) {
        toolbarColor.postValue(input);
    }

    public LiveData<String> getRadioButtonSeleted() {
        return radioButtonSelected;
    }

    public void setRadioButtonSelected(String input) {
        radioButtonSelected.postValue(input);
    }


}
