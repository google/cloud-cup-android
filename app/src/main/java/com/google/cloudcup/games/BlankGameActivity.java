/*
Copyright 2014 Google Inc. All rights reserved.

        Licensed under the Apache License, Version 2.0 (the "License");
        you may not use this file except in compliance with the License.
        You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

        Unless required by applicable law or agreed to in writing, software
        distributed under the License is distributed on an "AS IS" BASIS,
        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
        See the License for the specific language governing permissions and
        limitations under the License.
*/

package com.google.cloudcup.games;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;

import com.google.cloudcup.Consts;
import com.google.cloudcup.GameActivity;
import com.google.cloudcup.ImageAdapter;
import com.google.cloudcup.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A default Activity that extends GameActivity which decided which game should be started.
 * Created by mirnabouchra on 11/19/14.
 */
public class BlankGameActivity extends GameActivity {
    private static final String LOG_TAG = BlankGameActivity.class.getSimpleName();

    private Firebase stateRef;
    private Firebase playersRef;
    private ImageAdapter adapter;
    GridView gridView;
    private List<String> playerImageUrls = new ArrayList<String>();
    private List<String> playerNames = new ArrayList<String>();

    /**
     * Fragment that displays a grid of players joining the game.
     */
    /*public static class PlayersListFragment extends Fragment {
        @Override
        public View onCreateView(
                LayoutInflater inflater,
                ViewGroup container,
                Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_players_list, container, false);
        }
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        state = GameState.GAME;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.blank_game_activity);

        gridView = (GridView) findViewById(R.id.gridview);
        adapter = new ImageAdapter(this);
        gridView.setAdapter(adapter);

        Firebase.setAndroidContext(this);
        stateRef = new Firebase(Consts.FIREBASE_URL + "/room/" + code + "/state");

        final ImageButton button = (ImageButton) findViewById(R.id.startButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // When clicking on Join, set the state of the room from "not-started" to "waiting".
                stateRef.setValue("waiting");
            }
        });

        playersRef = new Firebase(Consts.FIREBASE_URL + "/room/" + code + "/players");
        playersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    return;
                }
                Log.d(LOG_TAG, dataSnapshot.getValue().toString());
                for (final DataSnapshot child : dataSnapshot.getChildren()) {
                    final Firebase playerName = child.getRef().child("name");

                    playerName.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(final DataSnapshot name) {
                            final Firebase playerImageUrl = child.getRef().child("imageUrl");
                            playerImageUrl.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot imageUrl) {
                                    Log.d(LOG_TAG, "name --" + name.getValue().toString());
                                    Log.d(LOG_TAG, "imageUrl --" + imageUrl.getValue().toString());
                                    if (!playerImageUrls.contains(imageUrl.getValue().toString())) {
                                        playerImageUrls.add(imageUrl.getValue().toString());
                                        Log.d(LOG_TAG, "players count" +  playerImageUrls.size());
                                        playerNames.add(name.getValue().toString());
                                        adapter.setImageUrls(playerImageUrls);
                                        adapter.setNames(playerNames);
                                        adapter.notifyDataSetChanged();
                                    }

                                }

                                @Override
                                public void onCancelled(FirebaseError firebaseError) {
                                }
                            });
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
