package com.example.mirnabouchra.cloudolympics;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

import com.firebase.client.Firebase;


/**
 * Created by steren
 */
public class GameActivity extends Activity {
    private static final String LOG_TAG = GameActivity.class.getSimpleName();
    protected Firebase firebaseRef;
    protected String code;
    protected String playerID;

    // Called when the activity is first created.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get the room code
        String code = getIntent().getStringExtra("code");
        Log.d(LOG_TAG, "Room code is " + code);

        // get the player ID
        String playerID = getIntent().getStringExtra("player");
        Log.d(LOG_TAG, "Player ID is " + playerID);

        // TODO for testing purposes only
        playerID = "player1";

        Firebase.setAndroidContext(this);
        firebaseRef = new Firebase(Consts.FIREBASE_URL);
        firebaseRef = firebaseRef.child("room");
        firebaseRef = firebaseRef.child(code);
        firebaseRef = firebaseRef.child("game");
        firebaseRef = firebaseRef.child("data");
        firebaseRef = firebaseRef.child("players");
        firebaseRef = firebaseRef.child(playerID);

    }

}


