package com.example.mirnabouchra.cloudolympics;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.DataSnapshot;
import com.firebase.client.ValueEventListener;


/**
 * Created by steren
 */
public class GameActivity extends Activity {
    private static final String LOG_TAG = GameActivity.class.getSimpleName();
    protected Firebase firebaseRef;
    protected String code;
    protected String playerID;
    protected Intent currentIntent;
    protected String gameType = "run";
    protected String gameNumber;

    // Called when the activity is first created.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get the room code
        code = getIntent().getStringExtra("code");
        Log.d(LOG_TAG, "Room code is " + code);

        // get the player ID
        playerID = getIntent().getStringExtra("playerId");
        Log.d(LOG_TAG, "Player ID is " + playerID);

        gameNumber = getIntent().getStringExtra("number") != null ?
                getIntent().getStringExtra("number") : "";

        Firebase.setAndroidContext(this);
        firebaseRef = new Firebase(Consts.FIREBASE_URL);
        firebaseRef = firebaseRef.child("room");
        firebaseRef = firebaseRef.child(code);
        firebaseRef = firebaseRef.child("game");
        firebaseRef = firebaseRef.child("data");
        firebaseRef = firebaseRef.child("players");
        firebaseRef = firebaseRef.child(playerID);

        final Firebase gameRef = new Firebase(Consts.FIREBASE_URL + "/room/" + code + "/game");
        gameRef.child("type").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                gameType = (String) dataSnapshot.getValue();
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        gameRef.child("number").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (!gameNumber.equals(snapshot.getValue().toString())) {
                    gameNumber = snapshot.getValue().toString();
                    startGame();
                }
            }
            @Override public void onCancelled(FirebaseError error) {
            }
        });
    }

    private void startGame() {
        if (gameType.equals("run")) {
            Log.d(LOG_TAG, "Run!");
            Intent intent = new Intent(getBaseContext(), TappingGameActivity.class);
            intent.putExtra("playerId", playerID);
            intent.putExtra("code", code);
            intent.putExtra("number", gameNumber);
            currentIntent = intent;
            startActivity(intent);
            finish();
        } else if (gameType.equals("shake")) {
            Log.d(LOG_TAG, "Shake!");
            // start shake activity
            Intent intent = new Intent(getBaseContext(), BlankGameActivity.class);
            intent.putExtra("playerId", playerID);
            intent.putExtra("code", code);
            intent.putExtra("number", gameNumber);
            currentIntent = intent;
            startActivity(intent);
            finish();
        }
    }
}