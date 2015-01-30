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

package com.google.cloudcup;

/**
 * Created by mirnabouchra on 11/21/14.
 */
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class TutorialView extends View {


    public static final float VIEW_ALPHA = 0.0f;
    public static final String VIEW_COLOR = "#ffffff";
    public static final float LINE_WIDTH = 15.0f;
    public static final String ARROW_COLOR = "#000000";
    public static final float DRAW_ANIMATION_SPEED = 50.0f;
    public static final float TIP_FRAME_PADDING = 15.0f;

    private static Paint paint;
    private Path pathAnimated;
    private Path pathNoAnimation;
    private Context context;
    float[] dashes = { 0.0f, Float.MAX_VALUE };
    private ArrayList<Arrow> arrows;

    public TutorialView(Context context) {
        super(context);

        this.context=context;
        this.setBackgroundColor(Color.TRANSPARENT);
        //if(Build.VERSION.SDK_INT >= 11) this.setAlpha(VIEW_ALPHA);
        this.arrows = new ArrayList<Arrow>();
        paint = new Paint();
        paint.setColor(Color.parseColor(ARROW_COLOR));
        paint.setStrokeWidth(LINE_WIDTH);
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStyle(Paint.Style.STROKE);
    }

    private Point getClosestPointInRectToPoint(Rect rect, Point point){
        float xMin = rect.left;
        float yMin = rect.top;
        float xMax = rect.right;
        float yMax = rect.bottom;

        float closeX;
        float closeY;

        if(point.x < xMin) closeX = xMin;
        else if (point.x > xMax) closeX = xMax;
        else closeX = point.x;

        if(point.y < yMin) closeY = yMin;
        else if (point.y > yMax) closeY = yMax;
        else closeY = point.y;

        return new Point((int)closeX,(int)closeY);
    }

    public void addArrow(Arrow arrow){
        this.arrows.add(arrow);
        generatePath();
    }
    private void generatePath(){
        Path pAnimated = new Path();
        Path pNoAnimated = new Path();
        for (Arrow a : this.arrows)
            if(a.getAnimated()) pAnimated.addPath(a.getPath());
            else pNoAnimated.addPath(a.getPath());

        this.pathAnimated = pAnimated;
        this.pathNoAnimation = pNoAnimated;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(this.arrows.isEmpty()){
            Log.i("TutorialView","No Arrows to Draw!");
            return;
        }

            /*
             * Animated Arrows
             */

        if(!pathAnimated.isEmpty()){
            dashes[0]+= DRAW_ANIMATION_SPEED;
            paint.setPathEffect(new DashPathEffect(dashes, 0));
            canvas.drawPath(pathAnimated, paint);
            invalidate();
        }

            /*
             * Non-Animated Arrows
             */

        if(!pathNoAnimation.isEmpty()){
            paint.setPathEffect(null);
            canvas.drawPath(pathNoAnimation,paint);
        }
    }

    public void dismissView(Boolean animated){
        this.setVisibility(View.GONE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.dismissView(true);
        return true;
    }
}
