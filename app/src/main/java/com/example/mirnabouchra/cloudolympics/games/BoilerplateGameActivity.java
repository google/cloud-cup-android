package com.example.mirnabouchra.cloudolympics.games;

import android.os.Bundle;

import com.example.mirnabouchra.cloudolympics.GameActivity;
import com.example.mirnabouchra.cloudolympics.R;


public class BoilerplateGameActivity extends GameActivity {
    private static final String LOG_TAG = BoilerplateGameActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        state = GameState.GAME;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.boilerplate_game_activity);
    }
}
