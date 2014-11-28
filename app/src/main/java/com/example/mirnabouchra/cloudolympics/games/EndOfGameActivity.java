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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        state = GameState.WAITING;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.end_of_game);

        final ImageButton button = (ImageButton) findViewById(R.id.startButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

    }
}
