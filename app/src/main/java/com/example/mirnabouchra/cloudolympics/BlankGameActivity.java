package com.example.mirnabouchra.cloudolympics;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

/**
 * A default Activity that extends GameActivity which decided which game should be started.
 * Created by mirnabouchra on 11/19/14.
 */
public class BlankGameActivity extends GameActivity {
    private static final String LOG_TAG = BlankGameActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
