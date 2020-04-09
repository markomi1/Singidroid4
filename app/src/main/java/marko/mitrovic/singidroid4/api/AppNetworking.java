package marko.mitrovic.singidroid4.api;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.ANResponse;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import marko.mitrovic.singidroid4.R;
import marko.mitrovic.singidroid4.SplashScreen;
import okhttp3.OkHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.concurrent.TimeUnit;

public class AppNetworking{


    public JSONArray ApiCall(Context context, String moduleName, String methodName, String additionalParam){



        String url = "https://api.singidunum.rs/key:SD03-A1K8-1033-0001-3355/module:" + moduleName + "/method:" + methodName + additionalParam;
        final JSONArray[] toReturn = new JSONArray[1];
        AndroidNetworking.initialize(context.getApplicationContext());
        AndroidNetworking.get(url)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("Response", String.valueOf(response));
                        toReturn[0] = response;
                    }
                    @Override
                    public void onError(ANError error) {
                        Log.i("Error ApiCall", String.valueOf(error));
                    }
                });
        return toReturn[0];
    }

    public void ApiCallTest(Context context){



        String url = "http://192.168.4.110:8443";
        final JSONArray[] jsonObject = {new JSONArray()};
        AndroidNetworking.initialize(context.getApplicationContext());

        AndroidNetworking.post(url)
                .addHeaders("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .addHeaders("Content-Type", "application/x-www-form-urlencoded")
                .addBodyParameter("elementid", "null")
                .addBodyParameter("id", "733")
                .addBodyParameter("type", "20")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("Response", String.valueOf(response));
                        jsonObject[0] = response;
                    }
                    @Override
                    public void onError(ANError error) {
                        Log.i("Error ApiCall", String.valueOf(error));
                    }
                });

    }



    public JSONArray SyncApiCall(Context context, String moduleName, String methodName, String additionalParam){
        JSONArray users = null;
        //String url = "https://api.singidunum.rs/key:SD03-A1K8-1033-0001-3355/module:"+moduleName+"/method:"+methodName+additionalParam;
        String url = "http://192.168.4.110:8443/getFaculties";
        AndroidNetworking.initialize(context.getApplicationContext());
        ANRequest request = AndroidNetworking.get(url)
                .setPriority(Priority.HIGH)
                .getResponseOnlyIfCached()
                .setMaxAgeCacheControl(60, TimeUnit.SECONDS) //TODO Implement this call in my own API with caching enabled as it seems it's disabled on their server
                .build();
        ANResponse<JSONArray> response = request.executeForJSONArray();

        if (response.isSuccess()) {
             users = response.getResult();
        } else {

            ANError error = response.getError();
            Log.e("Networking Error", String.valueOf(error));
        }

        return users;

    }

}
