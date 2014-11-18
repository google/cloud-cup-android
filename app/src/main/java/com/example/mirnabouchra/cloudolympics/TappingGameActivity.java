package com.example.mirnabouchra.cloudolympics;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.GestureDetector;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Response.Listener;
import com.android.volley.Response.ErrorListener;
import com.firebase.client.Firebase;
import com.firebase.client.*;


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

            firebaseRef.setValue(tapCount);
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


