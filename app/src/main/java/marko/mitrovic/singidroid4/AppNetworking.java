package marko.mitrovic.singidroid4;

import android.content.Context;
import android.util.Log;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import org.json.JSONArray;

public class AppNetworking {


    public void ApiCall(Context context,String moduleName, String methodName,String additionalParam){

        String url = "https://api.singidunum.rs/key:SD03-A1K8-1033-0001-3355/module"+moduleName+"/method:"+methodName+additionalParam;

        AndroidNetworking.initialize(context.getApplicationContext());
        AndroidNetworking.get("https://api.singidunum.rs/key:SD03-A1K8-1033-0001-3355/module:posts/method:getCategoryPosts/categories:3,4/page:0/count:15/")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("Response", String.valueOf(response));

                    }
                    @Override
                    public void onError(ANError error) {
                        Log.i("Error", String.valueOf(error));
                    }
                });
    }

}
