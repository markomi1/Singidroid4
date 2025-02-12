package marko.mitrovic.singidroid4;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.github.paolorotolo.appintro.AppIntro2;
import marko.mitrovic.singidroid4.repo.SharedViewModel;
import marko.mitrovic.singidroid4.slides.InitSlide1;
import marko.mitrovic.singidroid4.slides.InitSlide2;
import marko.mitrovic.singidroid4.slides.InitSlide3;

public class AppInit extends AppIntro2 {
    SharedPreferences prefs = null;
    private SharedViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addSlide(InitSlide1.newInstance(R.layout.user_pref_init)); //Init slide 1
        addSlide(InitSlide2.newInstance(R.layout.user_pref_init2));//Init slide 2
        addSlide(InitSlide3.newInstance(R.layout.user_pref_init3));//Init slide 3
        viewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        prefs = getSharedPreferences("SingidroidInitPrefs", MODE_PRIVATE); //Init sharedprefs
    }

    @Override
    public void onSaveInstanceState(Bundle outState) //This was some sort of test, i think?
    {
        Log.d("CHATFRAG", "onSaveInstanceState: been called ");
        super.onSaveInstanceState(outState);
        outState.putString("textView", "test");
        //Save the fragment's state here
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        //Called when user wants to exit/skip the AppInit,
        // creates dialog that asks the user to confirm the action.
        super.onSkipPressed(currentFragment);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //Setting message manually and performing action on button click
        builder.setMessage(R.string.appinit_init_skip)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id) {
                        prefs.edit().putBoolean("firstrun", false).apply(); //Set the flag to false so that init is complete.
                        //Sets the StudentProf to "default" values only if he skips the AppInit part.
                        saveToSharedPref("default", "0", "default", true);
                        Intent mainIntent = new Intent(AppInit.this, SplashScreen.class);
                        AppInit.this.startActivity(mainIntent);
                        AppInit.this.finish();
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel(); //Cancels the AlertDialog and let's the user resume AppInit
                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle(getString(R.string.appinit_skip));
        alert.show();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.appinit_confirm_done_text)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id) {
                        prefs.edit().putBoolean("firstrun", false).apply(); //Set the flag to false so that init is complete,

                        saveToSharedPref(viewModel.getSelectedFaculty().getValue(),
                                viewModel.getSelectedYear().getValue(), viewModel.getSelectedCourse().getValue(), false); //Gets all the values and sends them to be stored

                        Intent mainIntent = new Intent(AppInit.this, SplashScreen.class);
                        AppInit.this.startActivity(mainIntent);
                        AppInit.this.finish();
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel(); //Cancels the AlertDialog and let's the user resume AppInit
                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle(getString(R.string.appinit_title));
        alert.show();

    }

    public void saveToSharedPref(String faks, String year, String courseid, boolean skipped) {
        SharedPreferences studentPerfs = getSharedPreferences("StudentPrefs", Context.MODE_PRIVATE); //Saves student faculty/year/course choice
        studentPerfs.edit().putBoolean("SkippedInit", skipped).apply();
        studentPerfs.edit().putString("FacultyChoice", faks).apply();
        studentPerfs.edit().putString("YearChoice", year).apply();
        studentPerfs.edit().putString("CourseChoice", courseid).apply();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }


}
