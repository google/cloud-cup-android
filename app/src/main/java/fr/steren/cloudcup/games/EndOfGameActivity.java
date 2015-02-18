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

package fr.steren.cloudcup.games;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import fr.steren.cloudcup.BitmapUtils;
import fr.steren.cloudcup.Consts;
import fr.steren.cloudcup.GameActivity;
import fr.steren.cloudcup.R;
import info.evelio.drawable.RoundedAvatarDrawable;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.IOException;

public class EndOfGameActivity extends GameActivity {
    private static final String LOG_TAG = EndOfGameActivity.class.getSimpleName();

    private Firebase playersRef;
    private ImageView winnerImage;
    private TextView winnerNameView;
    private Firebase stateRef;
    private Firebase currentGameRef;

    class DownloadImageAsyncTask extends AsyncTask<Uri, Void, Bitmap> {
        private Uri uri;

        @Override
        protected Bitmap doInBackground(Uri... params) {
            uri = params[0];
            // URI scheme must be 'https' (for downloaded images), 'content' (for shared images), or
            // 'file' (for camera images).
            if (!(uri.getScheme().equals("https") || uri.getScheme().equals("content")
                    || uri.getScheme().equals("file"))) {
                return null;
            }
            try {
                return BitmapUtils.decodeBitmapBounded(
                        BitmapUtils.getInputStream(EndOfGameActivity.this, uri), 90, 90);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error reading bitmap", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                BitmapDrawable bitmap = new BitmapDrawable(result);
                winnerImage.setImageDrawable(new RoundedAvatarDrawable(result));
            }
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        state = GameState.DONE;
        Log.d(LOG_TAG, "End of game!!");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.end_of_game);

        winnerNameView = (TextView) findViewById(R.id.winner_name);
        winnerImage = (ImageView) findViewById(R.id.winner_image);

        playersRef = new Firebase(Consts.FIREBASE_URL + "/room/" + code + "/players");
        playersRef.addValueEventListener(new ValueEventListener() {
            int maxScore = 0;
            String winnerName = "";
            String winnerImageUrl = "";
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(LOG_TAG, dataSnapshot.getValue().toString());
                for (final DataSnapshot player: dataSnapshot.getChildren()) {
                    int score = Integer.parseInt(player.child("score").getValue().toString());
                    if (score > maxScore) {
                        maxScore = score;
                        winnerName = player.child("name").getValue().toString();
                        winnerImageUrl = player.child("imageUrl").getValue().toString();
                    }
                }
                Log.d(LOG_TAG, "winner is " + winnerName + "with score " + maxScore);
                winnerNameView.setText(winnerName + " won!");
                if (!winnerImageUrl.isEmpty()) {
                    new DownloadImageAsyncTask().execute(Uri.parse(winnerImageUrl));
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });


        stateRef = new Firebase(Consts.FIREBASE_URL + "/room/" + code + "/state");
        currentGameRef = new Firebase(Consts.FIREBASE_URL + "/room/" + code + "/currentGame");


        final ImageButton button = (ImageButton) findViewById(R.id.startButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                stateRef.setValue("restarted");
            }
        });

    }
}
