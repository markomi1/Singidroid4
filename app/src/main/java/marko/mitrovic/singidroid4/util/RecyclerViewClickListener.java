package marko.mitrovic.singidroid4.util;

import android.view.View;

import java.util.List;

public interface RecyclerViewClickListener{

    void onClick(View view, String title, String permaLink, String date, String imageUrl, String content, List<String> imageList);

}
