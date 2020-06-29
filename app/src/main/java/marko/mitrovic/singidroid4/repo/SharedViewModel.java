package marko.mitrovic.singidroid4.repo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.gson.JsonArray;

public class SharedViewModel extends ViewModel{
    private final MutableLiveData<JsonArray> facultiesArray = new MutableLiveData<>(); //Used for storing faculties JSON

    private final MutableLiveData<JsonArray> yearsArray = new MutableLiveData<>(); //Used for storing years Array

    private final MutableLiveData<JsonArray> coursesArray = new MutableLiveData<>(); //Stores courses
    private final MutableLiveData<String> selectedFaculty = new MutableLiveData<>(); //Stores selected faculty ID
    private final MutableLiveData<String> selectedYear = new MutableLiveData<>(); //Stores selected year ID
    private final MutableLiveData<String> selectedCourse = new MutableLiveData<>(); //Stores selected Course ID
    private final MutableLiveData<JsonArray> newsFaculties = new MutableLiveData<>(); //Stores list of available news sources
    private final MutableLiveData<String> toolbarColor = new MutableLiveData<>(); //Used to pass color data from NewsFragmentSettingsDialog class to MainActivity
    private final MutableLiveData<String> radioButtonSelected = new MutableLiveData<>(); //Used to pass radioButton tag  data from within NewsFragmentSettingsDialog class
    private final MutableLiveData<String> selectedNewsSource = new MutableLiveData<>(); //Selected news source ID (example "3,4")
    private final MutableLiveData<NewsModel> selectedArticle = new MutableLiveData<>(); //Selected article
    private final MutableLiveData<Boolean> backStatus = new MutableLiveData<>(); //Used by JS interface
    private final MutableLiveData<String> predmetDialog = new MutableLiveData<>();


    public LiveData<JsonArray> getFacultiesArray() {
        return facultiesArray;
    }

    public void setFacultiesArray(JsonArray input) {
        facultiesArray.postValue(input);
    }

    public LiveData<JsonArray> getYearsArray() {
        return yearsArray;
    }

    public void setYearsArray(JsonArray input) {
        yearsArray.postValue(input);
    }

    public LiveData<JsonArray> getCoursesArray() {
        return coursesArray;
    }

    public void setCoursesArray(JsonArray input) {
        coursesArray.postValue(input);
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

    public LiveData<JsonArray> getNewsFaculties() {
        return newsFaculties;
    }

    public void setNewsFaculties(JsonArray input) {
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

    public LiveData<String> getSelectedNewsSource() {
        return selectedNewsSource;
    }

    public void setSelectedNewsSource(String input) {
        selectedNewsSource.postValue(input);
    }

    public LiveData<NewsModel> getSelectedArticle() {
        return selectedArticle;
    }

    public void setSelectedArticle(NewsModel input) {
        selectedArticle.postValue(input);
    }

    public LiveData<Boolean> getBackStatus() {
        return backStatus;
    }

    public void setBackStatus(Boolean input) {
        backStatus.setValue(input);
    }

    public LiveData<String> getPredmetDialog() {
        return predmetDialog;
    }

    public void setPredmetDialog(String input) {
        predmetDialog.setValue(input);
    }
}
