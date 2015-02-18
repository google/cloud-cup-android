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

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.util.Log;

import fr.steren.cloudcup.GameActivity;
import fr.steren.cloudcup.R;


/**
 * Created by mirnabouchra on 11/17/14.
 */
public class TappingGameActivity extends GameActivity {

    private static final String LOG_TAG = TappingGameActivity.class.getSimpleName();
    private GestureDetector mDetector;

    private int tapCount;

    class TapListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent event) {
            tapCount++;
            Log.d(LOG_TAG, "tap count: " + tapCount);

            gameDataRef.setValue(tapCount);
            return true;
        }
    }

    // Called when the activity is first created.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        state = GameState.GAME;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tapping_game_activity);

        // init tap count to 0
        tapCount = 0;

        mDetector = new GestureDetector(this, new TapListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

}


