package marko.mitrovic.singidroid4;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;
import androidx.lifecycle.ViewModelProvider;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import marko.mitrovic.singidroid4.repo.SharedViewModel;
import marko.mitrovic.singidroid4.util.GestureLogic;


public class Article extends AppCompatActivity{

    private View view;
    private SharedViewModel viewModel;
    private TextView title, date, postContent;
    private ImageView postImage;

    private GestureDetectorCompat mDetector;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_article);
        viewModel = new ViewModelProvider(this).get(SharedViewModel.class);


        mDetector = new GestureDetectorCompat(this, new GestureLogic());

        title = findViewById(R.id.postTitle);
        date = findViewById(R.id.postDate);
        postImage = findViewById(R.id.postImage);
        postContent = findViewById(R.id.postContent);


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);

    }

    @Override
    protected void onResume() {
        Intent intent = getIntent();

        title.setText(intent.getStringExtra("title"));
        date.setText(intent.getStringExtra("date"));
        String url = intent.getStringExtra("imageurl");
        Glide.with(this).load(url).apply(RequestOptions.centerCropTransform()).into(postImage);
        postContent.setText(intent.getStringExtra("content"));
        super.onResume();
    }

}
