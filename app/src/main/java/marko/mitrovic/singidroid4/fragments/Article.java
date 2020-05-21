package marko.mitrovic.singidroid4.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import marko.mitrovic.singidroid4.R;
import marko.mitrovic.singidroid4.repo.SharedViewModel;


public class Article extends AppCompatActivity{

    private View view;
    private SharedViewModel viewModel;
    private TextView title, date, postContent;
    private ImageView postImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_article);
        viewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        Intent intent = getIntent();

        title = findViewById(R.id.postTitle);
        date = findViewById(R.id.postDate);
        postImage = findViewById(R.id.postImage);
        postContent = findViewById(R.id.postContent);


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
