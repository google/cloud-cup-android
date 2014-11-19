package com.example.mirnabouchra.cloudolympics.games;

import android.os.Bundle;

import com.example.mirnabouchra.cloudolympics.GameActivity;
import com.example.mirnabouchra.cloudolympics.R;

/**
 * A default Activity that extends GameActivity which decided which game should be started.
 * Created by mirnabouchra on 11/19/14.
 */
public class BlankGameActivity extends GameActivity {
    private static final String LOG_TAG = BlankGameActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blank_game_activity);
    }
}
