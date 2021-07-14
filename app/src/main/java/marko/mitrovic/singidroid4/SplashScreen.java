package marko.mitrovic.singidroid4;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    SharedPreferences prefs = null;
    String TAG = "SpashScreenTAG";
    private final int SPLASH_DISPLAY_LENGTH = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        prefs = getSharedPreferences("SingidroidInitPrefs", MODE_PRIVATE);
        if (prefs.getBoolean("firstrun", true)) { //

            runAppInit(); //On first run we run AppInit that does Student profile setup
            SplashScreen.this.finish();
        }else{
            Log.d(TAG,"Not first time run");
            runMain(); //This is executed if the app is not run for the fist time.
        }
    }


    public void runAppInit(){ //Runs the AppIntro(AppInit class) and makes 1 call to the server

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
