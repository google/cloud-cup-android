package com.example.mirnabouchra.cloudolympics;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;
import android.view.inputmethod.EditorInfo;

import com.example.mirnabouchra.cloudolympics.games.BlankGameActivity;
import com.google.android.gms.common.ConnectionResult;
import android.content.IntentSender.SendIntentException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.firebase.client.Firebase;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import java.io.InputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class MainActivity extends ActionBarActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();


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
                        BitmapUtils.getInputStream(MainActivity.this, uri), 90, 90);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error reading bitmap", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                userImage.setImageBitmap(result);
            }
        }
    }

    private ImageView userImage;
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
        userImage = (ImageView) findViewById(R.id.user_image);
        code = (EditText) findViewById(R.id.code);
        code.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                join();
                return true;
            }
        });
        code.requestFocus();
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
        mGoogleApiClient.connect();
    }

    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    public void join() {
        Intent intent = new Intent(this, BlankGameActivity.class);

        String codeValue = code.getText().toString();

        String playerName;
        String imageUrl;
        if(mGoogleApiClient.isConnected()) {
            // Get data of current signed-in user
            Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
            playerName = currentPerson.getName().getGivenName();
            imageUrl = getUserImageUrl(currentPerson);

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
        user.put("score", "0");
        pushRef.setValue(user);
        String key = pushRef.getKey();

        // Add intent data
        intent.putExtra("playerId", key);
        intent.putExtra("playerName", playerName);
        intent.putExtra("code", codeValue);

        // Start the intent
        startActivity(intent);
    }

    private String getUserImageUrl(Person person) {
        String imageUrl = person.getImage().getUrl();
        // re-create the image URL with a larger size
        String sizeSplit = "sz=";
        String[] parts = imageUrl.split(sizeSplit);
        imageUrl = parts[0] + sizeSplit + "500";
        return imageUrl;
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
        Toast.makeText(this, "Welcome, " + currentPerson.getName().getGivenName() + "!",
                Toast.LENGTH_LONG).show();
        username.setText(currentPerson.getName().getGivenName());
        new DownloadImageAsyncTask().execute(Uri.parse(getUserImageUrl(currentPerson)));

        code.requestFocus();
    }

    public void onConnectionSuspended(int cause) {
        mGoogleApiClient.connect();
    }
}
