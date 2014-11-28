package com.example.mirnabouchra.cloudolympics.games;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.mirnabouchra.cloudolympics.Consts;
import com.example.mirnabouchra.cloudolympics.GameActivity;
import com.example.mirnabouchra.cloudolympics.R;
import com.firebase.client.Firebase;

public class EndOfGameActivity extends GameActivity {
    private static final String LOG_TAG = EndOfGameActivity.class.getSimpleName();

    private Firebase stateRef;
    private Firebase currentGameRef;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        state = GameState.DONE;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.end_of_game);

        stateRef = new Firebase(Consts.FIREBASE_URL + "/room/" + code + "/state");
        currentGameRef = new Firebase(Consts.FIREBASE_URL + "/room/" + code + "/currentGame");


        final ImageButton button = (ImageButton) findViewById(R.id.startButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // we should be at "done
                stateRef.setValue("waiting");
                currentGameRef.setValue(-1);

                // TODO, change the player scores to 0
            }
        });

    }
}
