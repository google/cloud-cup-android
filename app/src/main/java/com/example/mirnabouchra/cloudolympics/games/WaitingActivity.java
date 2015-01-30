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
