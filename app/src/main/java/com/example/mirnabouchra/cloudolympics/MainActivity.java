package com.example.mirnabouchra.cloudolympics;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.firebase.client.Firebase;


public class MainActivity extends Activity {
    private EditText username;
    private EditText code;
    private Firebase firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_main);

        firebase = new Firebase("https://cloud-olympics.firebaseio.com/");
        username = (EditText) findViewById(R.id.username);
        code = (EditText) findViewById(R.id.code);
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

    public void join(View view) {
        Intent intent = new Intent(this, JoinActivity.class);
        String codeValue = code.getText().toString();
        String usernameValue = username.getText().toString();
        intent.putExtra("playerName", usernameValue);
        intent.putExtra("code", codeValue);
        Firebase ref = firebase.child("room/" + codeValue + "/players");
        Firebase pushRef = ref.push();
        pushRef.setValue(usernameValue);
        String key = pushRef.getKey();
        intent.putExtra("playerId", key);
        startActivity(intent);
    }
}
