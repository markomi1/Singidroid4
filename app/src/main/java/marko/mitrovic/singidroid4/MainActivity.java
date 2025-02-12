package marko.mitrovic.singidroid4;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.JsonArray;
import marko.mitrovic.singidroid4.api.ApiCalls;
import marko.mitrovic.singidroid4.api.AppNetworking;
import marko.mitrovic.singidroid4.fragments.*;
import marko.mitrovic.singidroid4.repo.SharedViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivityTag";
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;
    private SharedViewModel viewModel; //Shared repo
    private NewsFragmentSettingsDialog newsSettings;
    private PredmetSettingsDialog predmetSettings;
    private SharedPreferences studentPerfs;
    private ApiCalls api;
    private int currentSelectedItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);



        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.draw_layout);

        studentPerfs = getSharedPreferences("StudentPrefs", Context.MODE_PRIVATE);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        newsSettings = new NewsFragmentSettingsDialog(); //Setting newsSetting var
        predmetSettings = new PredmetSettingsDialog();


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        toggle.syncState();

        //navigationView.getMenu().getItem(0).setActionView(R.layout.news_settings);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NewsFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_vesti);
        }


        //Sets the toolbar color to desired color

        viewModel = new ViewModelProvider(this).get(SharedViewModel.class); //get repo

        viewModel.getToolBarColor().observe(this, s -> { //This is only called when the getToolbarColor variable is updated from NewsFragmentSettingsDialog class

            setStatusBarColor(0, 0.9f, s, false); //sets the color of the toolbar to the given one from NewsFragmentSettingsDialog class

        });

        setStatusBarColor(0, 0.9f, studentPerfs.getString("Color", "#A8011D"), false);
        //Sets it to the default color if nothing is found, if it does find something it sets it to that color
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {

        return super.onCreateView(name, context, attrs);
    }

    @Override
    public void onBackPressed() {

        if (currentSelectedItem == R.id.nav_stranice_predmeta) {


            FragmentManager fm = getSupportFragmentManager();

            Predmet fragment = (Predmet) fm.findFragmentById(R.id.fragment_container);
            int modalNumber = fragment.modalNumber;
            if (modalNumber > 0) {
                fragment.closeModal();
                return;
            }
        }

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);

            if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
                super.onBackPressed();
                finish();
                return;
            }
            else {
                Toast.makeText(getBaseContext(), R.string.back_notification, Toast.LENGTH_SHORT).show();
            }

            mBackPressed = System.currentTimeMillis();

        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        currentSelectedItem = item.getItemId();
        switch (item.getItemId()) {
            case R.id.nav_vesti:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NewsFragment()).commit();
                break;
            case R.id.nav_o_aplikaciji:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AboutFragment()).commit();
                break;
            case R.id.nav_stranice_predmeta:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Predmet()).commit();
                break;
            case R.id.nav_student_stanje:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new StudentBillState()).commit();
                break;
            case R.id.nav_lokacija:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LocationFragment()).commit();
                break;
            case R.id.nav_kontakt:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new contactFragment()).commit();
                break;
            case R.id.nav_tekuci_racun:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new UniversityBillsFragment()).commit();
                break;
        }


        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void setStatusBarColor(int Alpha, float darkness, String hex, boolean darkMode) { //Change status Bar and Toolbar color to given HEX value, automatically darkens
        int color;
        int darkerColor;
        int dakrToolbar;

        if (Alpha == 0) {
            Alpha = 200;
        }

        if (darkness == 0) {
            darkness = 0.9f;
        }

        float[] hsv = new float[3]; //Make HSV float


        color = Color.parseColor(hex); //Parse normal color


        Color.colorToHSV(color, hsv);

        hsv[2] *= darkness; // value component

        darkerColor = Color.HSVToColor(Alpha, hsv);
        if (darkMode) { //Makes shit darker, magic, don't touch
            float[] hsvToolbar = new float[3];
            float sub = darkness - 0.2f;

            Color.colorToHSV(color, hsvToolbar);
            hsvToolbar[2] *= sub;
            dakrToolbar = Color.HSVToColor(hsvToolbar);

            hsv[2] *= darkness - 0.7f; // value component

            darkerColor = Color.HSVToColor(Alpha, hsv);


            toolbar.setBackgroundColor(dakrToolbar);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            //window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);

            window.getDecorView().setFitsSystemWindows(true);
            if (!darkMode) {
                toolbar.setBackgroundColor(color);
            }

            NavigationView navView = window.findViewById(R.id.nav_view);//Getting the nav_view
            navView.getHeaderView(0).setBackgroundColor(color); //Getting the header and changing it's color
            window.setStatusBarColor(darkerColor);

        }
    }


    public void showNewsSourceSettings(View view) {


        if (viewModel.getNewsFaculties().getValue() == null) { //Will trigger only once, next time it'll load from the var in ELSE
            api = AppNetworking.getClient(this).create(ApiCalls.class); //Call the API
            api.getNewsSources().enqueue(new Callback<JsonArray>(){
                @Override
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                    viewModel.setNewsFaculties(response.body()); //Set the var with the newly received JSON
                    newsSettings.show(getSupportFragmentManager(), "newsSource");
                }

                @Override
                public void onFailure(Call<JsonArray> call, Throwable t) {
                    Log.e("Call_getNewsSources", "Failed, dumping stack trace:");
                    t.printStackTrace();
                    Toast.makeText(view.getContext(), R.string.loading_content_error, Toast.LENGTH_LONG).show();
                }
            });
        } else {
            newsSettings.show(getSupportFragmentManager(), "newsSource");
        }


    }

    public void showPredmetSettings(View view) {
        predmetSettings.show(getSupportFragmentManager(), "predmetSettings");
    }
}
