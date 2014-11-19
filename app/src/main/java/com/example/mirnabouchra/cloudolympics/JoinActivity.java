package com.example.mirnabouchra.cloudolympics;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.mirnabouchra.cloudolympics.games.ShakingGameActivity;
import com.example.mirnabouchra.cloudolympics.games.TappingGameActivity;
import com.firebase.client.Firebase;

/**
 * Created by mirnabouchra on 11/17/14.
 */
public class JoinActivity extends Activity {

    private static final String LOG_TAG = JoinActivity.class.getSimpleName();
    private Firebase firebaseRef;
    private String codeValue;
    private String playerId;
    private String playerName;
    private TextView roomText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.join_activity);
        firebaseRef = new Firebase("https://cloud-olympics.firebaseio.com/");
        playerName = getIntent().getStringExtra("playerName");
        codeValue = getIntent().getStringExtra("code");
        playerId = getIntent().getStringExtra("playerId");

        roomText = (TextView) findViewById(R.id.room_text);
        roomText.setText("You joined room " + codeValue);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startGame(View view) {
        Intent intent = new Intent(this, ShakingGameActivity.class);
        intent.putExtra("playerId", playerId);
        intent.putExtra("code", codeValue);
        startActivity(intent);
    }
}
