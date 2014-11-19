package com.example.mirnabouchra.cloudolympics;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import android.content.IntentSender.SendIntentException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.firebase.client.Firebase;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class MainActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private TextView username;
    private EditText code;
    private Firebase firebase;

    /* Request code used to invoke sign in user interactions. */
    private static final int RC_SIGN_IN = 0;

    /* Client used to interact with Google APIs. */
    private GoogleApiClient mGoogleApiClient;

    /* A flag indicating that a PendingIntent is in progress and prevents
     * us from starting further intents.
     */
    private boolean mIntentInProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_main);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();

        firebase = new Firebase("https://cloud-olympics.firebaseio.com/");
        username = (TextView) findViewById(R.id.username);
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

    protected void onStart() {
        Log.d(LOG_TAG, "onStart");
        super.onStart();
        //mGoogleApiClient.connect();
    }

    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    public void join(View view) {
        Intent intent = new Intent(this, JoinActivity.class);

        String codeValue = code.getText().toString();

        String playerName;
        String imageUrl;
        if(mGoogleApiClient.isConnected()) {
            // Get data of current signed-in user
            Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
            playerName = currentPerson.getDisplayName();
            imageUrl = currentPerson.getImage().getUrl();
        } else {
            Random rand = new Random();
            playerName = "Anonymous " + rand.nextInt(10);
            imageUrl = "";
        }

        // Register player data in Firebase
        Firebase ref = firebase.child("room/" + codeValue + "/players");
        Firebase pushRef = ref.push();
        Map<String, String> user = new HashMap<String, String>();
        user.put("name", playerName);
        user.put("imageUrl", imageUrl);
        pushRef.setValue(user);
        String key = pushRef.getKey();

        // Add intent data
        intent.putExtra("playerId", key);
        intent.putExtra("playerName", playerName);
        intent.putExtra("code", codeValue);
        // Start the intent
        startActivity(intent);
    }

    public void onConnectionFailed(ConnectionResult result) {
        if (!mIntentInProgress && result.hasResolution()) {
            try {
                mIntentInProgress = true;
                startIntentSenderForResult(result.getResolution().getIntentSender(),
                        RC_SIGN_IN, null, 0, 0, 0);
            } catch (SendIntentException e) {
                // The intent was canceled before it was sent.  Return to the default
                // state and attempt to connect to get an updated ConnectionResult.
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }
    }

    public void onConnected(Bundle connectionHint) {
        Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
        Toast.makeText(this, currentPerson.getDisplayName() + " is connected!",
                Toast.LENGTH_LONG).show();
        username.setText(currentPerson.getDisplayName());
        Log.d(LOG_TAG, currentPerson.getImage().getUrl());
    }

    public void onConnectionSuspended(int cause) {
        mGoogleApiClient.connect();
    }
}
