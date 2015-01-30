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

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mirnabouchra.cloudolympics.GameActivity;
import com.example.mirnabouchra.cloudolympics.R;


public class MathGameActivity extends GameActivity {
    private static final String LOG_TAG = MathGameActivity.class.getSimpleName();

    private EditText resultInput;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        state = GameState.GAME;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.math_game_activity);

        resultInput = (EditText) findViewById(R.id.result);
        resultInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(resultInput.getWindowToken(), 0);

                resultInput.setEnabled(false);
                resultInput.setFocusable(false);
                resultInput.setClickable(false);

                String codeValue = resultInput.getText().toString();
                gameDataRef.setValue(codeValue);
                return true;
            }
        });
        resultInput.requestFocus();

    }
}
