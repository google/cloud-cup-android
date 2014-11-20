package com.example.mirnabouchra.cloudolympics.games;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.util.Log;

import com.example.mirnabouchra.cloudolympics.GameActivity;
import com.example.mirnabouchra.cloudolympics.R;


/**
 * Created by mirnabouchra on 11/17/14.
 */
public class TappingGameActivity extends GameActivity {

    private static final String LOG_TAG = TappingGameActivity.class.getSimpleName();
    private GestureDetector mDetector;

    private int tapCount;

    class TapListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent event) {
            tapCount++;
            Log.d(LOG_TAG, "tap count: " + tapCount);

            gameDataRef.setValue(tapCount);
            return true;
        }
    }

    // Called when the activity is first created.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tapping_game_activity);

        // init tap count to 0
        tapCount = 0;

        mDetector = new GestureDetector(this, new TapListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

}


