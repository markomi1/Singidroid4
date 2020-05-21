package marko.mitrovic.singidroid4.newsPager;

import android.view.View;

public interface RecyclerViewClickListener{

    void onClick(View view, String title, String date, String imageUrl, String content);
}