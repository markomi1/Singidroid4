package marko.mitrovic.singidroid4.util;

import android.view.View;

import java.util.List;

public interface ArticleImageViewClickListener{
    void imageClick(View view, List<String> listOfImages, int ClickedImagePosition);
}
