package marko.mitrovic.singidroid4;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.github.paolorotolo.appintro.AppIntro2;
import marko.mitrovic.singidroid4.slides.InitSlide1;
import marko.mitrovic.singidroid4.slides.InitSlide2;
import marko.mitrovic.singidroid4.slides.InitSlide3;

public class AppInit extends AppIntro2 {
    SharedPreferences prefs = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addSlide(InitSlide1.newInstance(R.layout.user_pref_init)); //Init slide 1
        addSlide(InitSlide2.newInstance(R.layout.user_pref_init2));//Init slide 2
        addSlide(InitSlide3.newInstance(R.layout.user_pref_init3));//Init slide 3
        prefs = getSharedPreferences("SingidroidInitPrefs", MODE_PRIVATE); //Init sharedprefs

    }
    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        Log.d("CHATFRAG", "onSaveInstanceState: been called ");
        super.onSaveInstanceState(outState);
        outState.putString("textView","test");
        //Save the fragment's state here
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) { //Callsed when user wants to exit/skip the AppInit, creates dialog that asks the user to confirm the action.
        super.onSkipPressed(currentFragment); //TODO add localization for the strings

        AlertDialog.Builder builder = new AlertDialog.Builder(this);


        //Setting message manually and performing action on button click
        builder.setMessage("Do you want to skip App Init ? Clicking yes will resume the app with default settings")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        prefs.edit().putBoolean("firstrun", false).apply(); //Set the flag to false so that init is complete,

                        //TODO add default values after firstrun=false if onSkipPressed is called

                        Intent mainIntent = new Intent(AppInit.this,SplashScreen.class);
                        AppInit.this.startActivity(mainIntent);
                        AppInit.this.finish();
                        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel(); //Cancels the AlertDialog and let's the user resume AppInit
                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Skip?");
        alert.show();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        prefs.edit().putBoolean("firstrun", false).apply();

        //TODO grab all data and save it to sharedpref for the rest of the app to use

        Intent mainIntent = new Intent(AppInit.this,SplashScreen.class);
        AppInit.this.startActivity(mainIntent);
        AppInit.this.finish();

        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
    }

  /*  @Override
    public void onBackPressed() { //Intercept back pressed, so user can't go back or so i can handle it well etc
        Toast.makeText(getApplicationContext(),"Back pressed",
                Toast.LENGTH_SHORT).show();

    }*/

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }


}
