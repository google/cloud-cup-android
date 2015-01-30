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

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;

import com.google.cloudcup.GameActivity;
import com.google.cloudcup.R;


/**
 * Created by steren
 */
public class TurnGameActivity extends GameActivity implements SensorEventListener {

    private static final String LOG_TAG = TurnGameActivity.class.getSimpleName();

    private static final double ANGLE_SENSIBILITY = Math.PI / 6;

    private SensorManager mSensorManager;
    private Sensor mSensor;

    private float originalAngle = 0;
    private int demiTurns = 0;
    private boolean halfTurn = false;
    /** */
    private int fullturn;

    private final float[] mRotationMatrix = new float[16];
    private final float[] mOrientation = new float[3];



    // Called when the activity is first created.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        state = GameState.GAME;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.turn_game_activity);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);


    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener( (SensorEventListener) this, mSensor, SensorManager.SENSOR_DELAY_FASTEST );
    }

    @Override
    protected void onStop() {
        mSensorManager.unregisterListener((SensorEventListener) this);
        super.onStop();
    }

    /*  return value un range -PI or PI */
    private double backtoRange(double angle) {
       while(angle > Math.PI) {
           angle = angle - 2 * Math.PI;
       }
       while (angle < - Math.PI) {
         angle = angle + Math.PI;
       }
       return angle;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        SensorManager.getRotationMatrixFromVector(mRotationMatrix , event.values);
        SensorManager.getOrientation(mRotationMatrix, mOrientation);

        float zAngle = mOrientation[0];

        //Log.d(LOG_TAG, "Turn z: " + zAngle);

        if(originalAngle == 0) {
            originalAngle = zAngle;
        }

        if( !halfTurn && Math.abs(zAngle - backtoRange(originalAngle + Math.PI)) < ANGLE_SENSIBILITY ) {
            halfTurn = true;
            demiTurns++;
            sendTurnValues();
        }

        if( halfTurn && Math.abs(zAngle - originalAngle) < ANGLE_SENSIBILITY) {
            halfTurn = false;
            demiTurns++;
            ((Vibrator) getSystemService(Context.VIBRATOR_SERVICE)).vibrate(100);
            sendTurnValues();
        }

    }

    private void sendTurnValues() {
        Log.d(LOG_TAG, "Turn value: " + demiTurns);
        gameDataRef.setValue(demiTurns);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }


}


