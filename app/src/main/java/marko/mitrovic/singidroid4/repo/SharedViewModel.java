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
}
