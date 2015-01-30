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
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.example.mirnabouchra.cloudolympics.GameActivity;
import com.example.mirnabouchra.cloudolympics.R;


/**
 * Created by steren
 */
public class ShakingGameActivity extends GameActivity implements SensorEventListener {

    private static final String LOG_TAG = ShakingGameActivity.class.getSimpleName();

    private static final double ACCELERATION_THRESHOLD = 20;

    private SensorManager mSensorManager;
    private Sensor mSensor;

    private int shakeCount;

    /** Here is stored previous values */
    private float prevAx;
    private float prevAy;
    private float prevAz;

    // Called when the activity is first created.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        state = GameState.GAME;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.shaking_game_activity);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

        // init tap count to 0
        shakeCount = 0;

        prevAx = 0;
        prevAy = 0;
        prevAz = 0;
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

    @Override
    public void onSensorChanged(SensorEvent event) {
        // compute acceleration amplitude
        float ax = event.values[0];
        float ay = event.values[1];
        float az = event.values[2];
        double a = Math.sqrt(ax * ax + ay * ay + az * az);


        if( a > ACCELERATION_THRESHOLD ) {
            // scalar product between the previous and the current vector should be negative to count a shake
            if((prevAx * ax + prevAy * ay + prevAz * az) < 0 ) {
                prevAx = 0;
                prevAy = 0;
                prevAz = 0;

                shakeCount++;

                ((Vibrator) getSystemService(Context.VIBRATOR_SERVICE)).vibrate(100);
                Log.d(LOG_TAG, "SHAKE! " + shakeCount);
                gameDataRef.setValue(shakeCount);
            }

            // if previous recorded acceleration hasn't been set already
            if(Math.sqrt(prevAx * prevAx + prevAy * prevAy + prevAz * prevAz) < ACCELERATION_THRESHOLD) {
                prevAx = ax;
                prevAy = ay;
                prevAz = az;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }


}


