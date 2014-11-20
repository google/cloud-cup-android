package com.example.mirnabouchra.cloudolympics.games;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mirnabouchra.cloudolympics.Consts;
import com.example.mirnabouchra.cloudolympics.GameActivity;
import com.example.mirnabouchra.cloudolympics.R;
import com.firebase.client.Firebase;

/**
 * A default Activity that extends GameActivity which decided which game should be started.
 * Created by mirnabouchra on 11/19/14.
 */
public class BlankGameActivity extends GameActivity {
    private static final String LOG_TAG = BlankGameActivity.class.getSimpleName();

    private Firebase stateRef;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blank_game_activity);

        Firebase.setAndroidContext(this);
        stateRef = new Firebase(Consts.FIREBASE_URL + "/room/" + code + "/state");

        final Button button = (Button) findViewById(R.id.startButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // When clicking on Join, set the state of the room from "not-started" to "waiting".
                stateRef.setValue("waiting");
            }
        });
    }
}
