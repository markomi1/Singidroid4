package marko.mitrovic.singidroid4;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class ImageSliderActivity extends AppCompatActivity{
    private ImageView image;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Image", "Image launched");


        setContentView(R.layout.activity_image_slider);
        image = findViewById(R.id.image_slider_holder);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // If we've received a touch notification that the user has touched
        // outside the app, finish the activity.
        if (MotionEvent.ACTION_UP == event.getAction()) {
            finish();
            return true;
        }

        // Delegate everything else to Activity.
        return super.onTouchEvent(event);
    }

    @Override
    protected void onResume() {
        Intent intent = getIntent();
        String url = intent.getStringExtra("imageUrl");
        Glide.with(this).load(url).apply(RequestOptions.centerCropTransform()).fitCenter().into(image);

        super.onResume();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}
