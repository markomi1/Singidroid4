package marko.mitrovic.singidroid4;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
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
    final static String TAG = "Article Activity";
    private ImageListAdapter imageListAdapter;
    private GestureDetectorCompat mDetector;
    private RecyclerView recyclerView;
    private ImageView postImage, articleImageRecycleView, shareIconImage, openBrowserIconImage;
    private String browserUrl = "https://singidunum.ac.rs";


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
        shareIconImage = findViewById(R.id.article_share);
        openBrowserIconImage = findViewById(R.id.article_open_web);

        shareIconImage.setOnClickListener(v -> {
            Toast.makeText(this, "Sharing", Toast.LENGTH_SHORT).show();
            ShareCompat.IntentBuilder.from(this)
                    .setType("text/plain")
                    .setText(browserUrl)
                    .startChooser();
        });
        //Open the post URL in the browser
        openBrowserIconImage.setOnClickListener(v -> {
            Toast.makeText(this, "Opening in browser", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(browserUrl));
            startActivity(i);
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        ArticleImageViewClickListener listener = (view, imageList, position) -> {
            Intent intent = new Intent(this, ImageSliderActivity.class);
            intent.putExtra("imageUrl", imageList.get(position));
            startActivity(intent);
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        };
        imageListAdapter = new ImageListAdapter(this, listener);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(imageListAdapter);
    }

    @Override
    protected void onResume() {
        Intent intent = getIntent();
        title.setText(intent.getStringExtra("title"));
        date.setText(intent.getStringExtra("date"));
        String url = intent.getStringExtra("imageurl");
        List<String> imageList = intent.getStringArrayListExtra("imageList");
        browserUrl = intent.getStringExtra("permalink");
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
