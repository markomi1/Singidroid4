package marko.mitrovic.singidroid4;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
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
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import marko.mitrovic.singidroid4.api.AppNetworking;
import marko.mitrovic.singidroid4.fragments.AboutFragment;
import marko.mitrovic.singidroid4.fragments.NewsFragment;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private Toolbar toolbar;
    private TabLayout tabsLayout;
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setTheme(R.style.);

        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);



        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.draw_layout);


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        toggle.syncState();

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new NewsFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_vesti);
        }


        setStatusBarColor(0, 0, "#A8011D");


        //toolbar.setBackgroundColor(Color.argb(255,44,44,209));
        AppNetworking net = new AppNetworking();
        //net.test(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {

        return super.onCreateView(name, context, attrs);
    }

    @Override
    public void onBackPressed() {
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
                Toast.makeText(getBaseContext(), "Tap back button in order to exit", Toast.LENGTH_SHORT).show();
            }

            mBackPressed = System.currentTimeMillis();

        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_vesti:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new NewsFragment()).commit();
                break;
            case R.id.nav_o_aplikaciji:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new AboutFragment()).commit();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public boolean appInit(){

        return false;
    }

    public void setStatusBarColor(int Alpha,float darkness,String hex){ //Change status Bar and Toolbar color to given HEX value, automatically darkens
        if(Alpha == 0){
            Alpha = 200;
        }
        if(darkness == 0){
            darkness = 0.9f;
        }
        float[] hsv = new float[3];
        int color = Color.parseColor(hex);
        Color.colorToHSV(color, hsv);
        hsv[2] *= darkness; // value component
        int darkerColor = Color.HSVToColor(Alpha,hsv);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            //window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);

            window.getDecorView().setFitsSystemWindows(true);
            toolbar.setBackgroundColor(color);
            window.setStatusBarColor(darkerColor);

        }
    }


}
