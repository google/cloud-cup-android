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
