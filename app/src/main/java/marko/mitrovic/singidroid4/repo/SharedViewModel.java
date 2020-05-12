package marko.mitrovic.singidroid4.repo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import org.json.JSONArray;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<String> text = new MutableLiveData<>(); //test shit, will maybe, probably not be removed
    private MutableLiveData<JSONArray> jArray = new MutableLiveData<>(); //test shit, will maybe, probably not be removed
    private MutableLiveData<JSONArray> facultiesArray = new MutableLiveData<>(); //Used for storing faculties JSON
    public void setText(String input) {
        text.postValue(input);
    }

    public LiveData<String> getText() {
        return text;
    }

    public void setjArray(JSONArray input){
        jArray.postValue(input);
    }
    public LiveData<JSONArray> getjArray() {
        return jArray;
    }

    public void setFaculties(JSONArray input){
        facultiesArray.postValue(input);
    }
    public LiveData<JSONArray> getFaculties(){
        return facultiesArray;
    }
}
