package marko.mitrovic.singidroid4.util;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class GestureLogic extends GestureDetector.SimpleOnGestureListener{


    @Override
    public boolean onDown(MotionEvent event) {
        Log.d("Article", "onDown: " + event.toString());
        return true;
    }


    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2,
                           float velocityX, float velocityY) {
        Log.d("Article", "onFling: " + event1.toString() + event2.toString());
        return true;
    }

}
