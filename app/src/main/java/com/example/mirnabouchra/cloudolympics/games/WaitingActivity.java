package com.example.mirnabouchra.cloudolympics.games;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.mirnabouchra.cloudolympics.Consts;
import com.example.mirnabouchra.cloudolympics.GameActivity;
import com.example.mirnabouchra.cloudolympics.R;
import com.firebase.client.Firebase;

public class WaitingActivity extends GameActivity {
    private static final String LOG_TAG = WaitingActivity.class.getSimpleName();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        state = GameState.WAITING;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.waiting_activity);

    }
}
