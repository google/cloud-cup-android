package com.example.mirnabouchra.cloudolympics;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.mirnabouchra.cloudolympics.games.ShakingGameActivity;
import com.example.mirnabouchra.cloudolympics.games.TappingGameActivity;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.DataSnapshot;
import com.firebase.client.ValueEventListener;


/**
 * Created by steren
 */
public class GameActivity extends Activity {
    private static final String LOG_TAG = GameActivity.class.getSimpleName();
    protected Firebase gameDataRef;
    protected String code;
    protected String playerID;
    protected Intent currentIntent;
    protected String gameType = "";
    protected String currentGame;
    private Firebase gameTypeRef;

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

        currentGame = getIntent().getStringExtra("number") != null ?
                getIntent().getStringExtra("number") : "-1";

        currentIntent = getIntent();

        Firebase.setAndroidContext(this);
        gameDataRef = new Firebase(Consts.FIREBASE_URL + "/room/" + code + "/games/" +
                currentGame + "/data/" + playerID);

        Firebase currentGameRef = new Firebase(Consts.FIREBASE_URL + "/room/" + code);
        currentGameRef.child("currentGame").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.getValue() != null && !snapshot.getValue().toString().equals("-1") &&
                        !currentGame.equals(snapshot.getValue().toString())) {
                    currentGame = snapshot.getValue().toString();
                    gameTypeRef = new Firebase(Consts.FIREBASE_URL + "/room/" + code + "/games/" +
                            currentGame);
                    gameTypeRef.child("type").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null &&
                                    !dataSnapshot.getValue().toString().isEmpty()) {
                                Log.d(LOG_TAG, "gameType is now " + dataSnapshot.getValue());
                                gameType = (String) dataSnapshot.getValue();
                                startGame();
                            }
                        }
                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                        }
                    });
                    startGame();
                }
            }
            @Override public void onCancelled(FirebaseError error) {
            }
        });
    }

    private void startGame() {
        if (gameType == null || gameType.isEmpty()) return;
        if (currentGame.equals("-1")) return;
        if (currentIntent != null && currentIntent.getStringExtra("number") != null &&
                currentIntent.getStringExtra("number").equals(currentGame)) return;
        if (gameType.equals("tap")) {
            Log.d(LOG_TAG, "tap!");
            Intent intent = new Intent(this, TappingGameActivity.class);
            intent.putExtra("playerId", playerID);
            intent.putExtra("code", code);
            intent.putExtra("number", currentGame);
            currentIntent = intent;
            startActivity(intent);
            finish();
        } else if (gameType.equals("shake")) {
            Log.d(LOG_TAG, "Shake!");
            // start shake activity
            Intent intent = new Intent(this, ShakingGameActivity.class);
            intent.putExtra("playerId", playerID);
            intent.putExtra("code", code);
            intent.putExtra("number", currentGame);
            currentIntent = intent;
            startActivity(intent);
            finish();
        }
    }
}