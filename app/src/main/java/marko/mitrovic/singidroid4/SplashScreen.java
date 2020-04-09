package marko.mitrovic.singidroid4;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import marko.mitrovic.singidroid4.api.AppNetworking;
import org.json.JSONArray;

public class SplashScreen extends AppCompatActivity {

    private ProgressBar progressBar;

    SharedPreferences prefs = null;
    String TAG = "SpashScreenTAG";
    private final int SPLASH_DISPLAY_LENGTH = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        prefs = getSharedPreferences("SingidroidInitPrefs", MODE_PRIVATE);
        progressBar = (ProgressBar)findViewById(R.id.progressBar_cyclic);

        //progressBar.getProgressDrawable().setColorFilter(Color.RED,android.graphics.PorterDuff.Mode.SRC_IN);

        if (prefs.getBoolean("firstrun", true)) { //

            runAppInit(); //On first run we run AppInit that does Student profile setup
            SplashScreen.this.finish();
        }else{
            Log.d(TAG,"Not first time run");
            runMain(); //This is executed if the app is not run for the fist time.
        }


    }

    public void runAppInit(){ //Runs the AppIntro(AppInit class) and makes 1 call to the server
        AppNetworking net = new AppNetworking();
        JSONArray response = null;




        Log.i("Response", String.valueOf(response));

        Intent intent = new Intent(this,AppInit.class);
        startActivity(intent);
        // Put this in the last slide, not here!

    }

    public void runMain(){
        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */

                Intent mainIntent = new Intent(SplashScreen.this,MainActivity.class);
                SplashScreen.this.startActivity(mainIntent);
                SplashScreen.this.finish();

                overridePendingTransition(R.anim.fadein,R.anim.fadeout);

            }
        }, SPLASH_DISPLAY_LENGTH);

    }



}
