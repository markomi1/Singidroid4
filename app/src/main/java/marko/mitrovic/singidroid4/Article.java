package marko.mitrovic.singidroid4;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import marko.mitrovic.singidroid4.repo.SharedViewModel;
import marko.mitrovic.singidroid4.util.ArticleImageViewClickListener;
import marko.mitrovic.singidroid4.util.GestureLogic;
import marko.mitrovic.singidroid4.util.ImageListAdapter;

import java.util.List;


public class Article extends AppCompatActivity{

    private SharedViewModel viewModel;
    private TextView title, date, postContent;
    private ImageView postImage, articleImageRecycleView;
    private ImageListAdapter imageListAdapter;
    private GestureDetectorCompat mDetector;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article);
        viewModel = new ViewModelProvider(this).get(SharedViewModel.class);


        mDetector = new GestureDetectorCompat(this, new GestureLogic());

        title = findViewById(R.id.postTitle);
        date = findViewById(R.id.post_date);
        postImage = findViewById(R.id.postImage);
        postContent = findViewById(R.id.postContent);
        recyclerView = findViewById(R.id.horizontal_image_recycle_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        ArticleImageViewClickListener listener = (view, imageList, position) -> {

            Log.d("Bbb", "Clicked on: " + imageList.get(position) + " Position: " + position);
            Intent intent = new Intent(this, ImageSliderActivity.class);
            intent.putExtra("imageUrl", imageList.get(position));
            startActivity(intent);
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);


        };


        imageListAdapter = new ImageListAdapter(this, listener);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(imageListAdapter);


        //articleImageRecycleView = findViewById(R.id.article_images);


    }



    @Override
    protected void onResume() {
        Intent intent = getIntent();

        title.setText(intent.getStringExtra("title"));
        date.setText(intent.getStringExtra("date"));
        String url = intent.getStringExtra("imageurl");
        List<String> imageList = intent.getStringArrayListExtra("imageList");

        Glide.with(this).load(url).apply(RequestOptions.centerCropTransform()).fitCenter().into(postImage);
        postContent.setText(intent.getStringExtra("content"));
        if (imageListAdapter.getItemCount() == 0) {
            if (!imageList.isEmpty()) {
                imageListAdapter.addAll(imageList); //Add all the new stuff in
                imageListAdapter.notifyDataSetChanged();
            } else {
                recyclerView.setVisibility(View.GONE);
            }
        }
        super.onResume();
    }

}
