package com.example.mirnabouchra.cloudolympics.games;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.mirnabouchra.cloudolympics.GameActivity;
import com.example.mirnabouchra.cloudolympics.R;

/**
 * Created by mirnabouchra on 11/21/14.
 */
public class SequenceGameActivity extends GameActivity {
    private static final String LOG_TAG = SequenceGameActivity.class.getSimpleName();
    private Button blueButton;
    private Button redButton;
    private Button greenButton;
    private Button yellowButton;
    private String sequence = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        state = GameState.GAME;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.sequence_game);

        blueButton = (Button) findViewById(R.id.blue_button);
        redButton = (Button) findViewById(R.id.red_button);
        greenButton = (Button) findViewById(R.id.green_button);
        yellowButton = (Button) findViewById(R.id.yellow_button);

        blueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sequence.length() < 4) {
                    sequence += "b";
                }
                sendSequence();
            }
        });
        redButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sequence.length() < 4) {
                    sequence += "r";
                }
                sendSequence();
            }
        });
        greenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sequence.length() < 4) {
                    sequence += "g";
                }
                sendSequence();
            }
        });
        yellowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sequence.length() < 4) {
                    sequence += "y";
                }
                sendSequence();
            }
        });
    }

    private void sendSequence() {
        if (sequence.length() == 4) {
            gameDataRef.setValue(sequence);
            Log.d(LOG_TAG, "sequence is ----"  + sequence);
        }
    }
}
