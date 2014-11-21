package com.example.mirnabouchra.cloudolympics.games;

import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.mirnabouchra.cloudolympics.Arrow;
import com.example.mirnabouchra.cloudolympics.GameActivity;
import com.example.mirnabouchra.cloudolympics.R;
import com.example.mirnabouchra.cloudolympics.TutorialView;

/**
 * Created by mirnabouchra on 11/21/14.
 */
public class SwipeGameActivity extends GameActivity {
    private static final String LOG_TAG = SwipeGameActivity.class.getSimpleName();

    private static final int MIN_THRESOLD =  300;
    private int x1, x2;
    private int y1, y2;
    private int swipeCount;
    TutorialView tutorialView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        state = GameActivity.GameState.GAME;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swipe_game_activity);
        swipeCount = 0;

        tutorialView = new TutorialView(this);
        Arrow a = new Arrow();
        a.setHead(new Point(530,200));
        a.setTail(new Point(530, 1500));
        tutorialView.addArrow(a);
        setContentView(tutorialView);
    }

    // onTouchEvent () method gets called when User performs any touch event on screen
    // Method to handle touch event like left to right swap and right to left swap
    public boolean onTouchEvent(MotionEvent touchevent) {
        switch (touchevent.getAction()) {
            // when user first touches the screen we get x and y coordinate
            case MotionEvent.ACTION_DOWN: {
                x1 = (int) touchevent.getX();
                y1 = (int) touchevent.getY();
                Log.d(LOG_TAG, "x1 " + x1);
                Log.d(LOG_TAG, "y1 " + y1);
                break;
            }
            case MotionEvent.ACTION_UP: {
                x2 = (int) touchevent.getX();
                y2 = (int) touchevent.getY();
                Log.d(LOG_TAG, "x2 " + x2);
                Log.d(LOG_TAG, "y2 " + y2);

                //if Down to UP sweep event on screen
                if (y1 > (y2 + MIN_THRESOLD)) {
                    tutorialView.setVisibility(View.GONE);
                    swipeCount++;
                    Log.d(LOG_TAG, "swipe count: " + swipeCount);
                    gameDataRef.setValue(swipeCount);
                    tutorialView.setVisibility(View.VISIBLE);
                    setContentView(tutorialView);
                }
                break;
            }
        }
        return false;
    }
}