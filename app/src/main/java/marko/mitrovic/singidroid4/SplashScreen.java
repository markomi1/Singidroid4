package marko.mitrovic.singidroid4;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {
    private ProgressBar progressBar;

    private final int SPLASH_DISPLAY_LENGTH = 5000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        progressBar = (ProgressBar)findViewById(R.id.progressBar_cyclic);
        //progressBar.getProgressDrawable().setColorFilter(Color.RED,android.graphics.PorterDuff.Mode.SRC_IN);




        runMain();
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
