package com.mobdeve.cactus.mobdevemp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import java.util.HashMap;

public class GameView extends SurfaceView implements Runnable {

    private Thread gameThread;
    private SurfaceHolder ourHolder;
    private volatile boolean playing;
    private Canvas canvas;
    private Bitmap bitmapRunningMan, bitmapBackground;
    private boolean isMoving;
    private float runSpeedPerSecond = 10;
    private float manXPos = 0, manYPos = 50;
    private int frameWidth = 150, frameHeight = 150;
    private int frameCount = 6;
    private int currentFrame = 0;
    private long fps;
    private long timeThisFrame;
    private long lastFrameChangeTime = 0;
    private int frameLengthInMillisecond = 50;

    private Rect frameToDraw = new Rect(0, 0, frameWidth, frameHeight);

    private RectF whereToDraw = new RectF(manXPos, manYPos, manXPos + frameWidth, frameHeight);

    public GameView(Context context) {
        super(context);
        ourHolder = getHolder();
        bitmapRunningMan = BitmapFactory.decodeResource(getResources(), R.drawable.walking_man);
        bitmapRunningMan = Bitmap.createScaledBitmap(bitmapRunningMan, frameWidth * frameCount, frameHeight, false);
    }

    @Override
    public void run() {
        while (playing) {
            long startFrameTime = System.currentTimeMillis();
            update();
            draw();

            timeThisFrame = System.currentTimeMillis() - startFrameTime;

            if (timeThisFrame >= 1) {
                fps = 500 / timeThisFrame;
            }
        }
    }

    public void update() {
        if (isMoving) {
            manXPos = manXPos + runSpeedPerSecond / fps;

            if (manXPos > getWidth()) {
                manXPos = 0;
            }
        }
    }

    public void manageCurrentFrame() {
        long time = System.currentTimeMillis();

        if (isMoving) {
            if (time > lastFrameChangeTime + frameLengthInMillisecond) {
                lastFrameChangeTime = time;
                currentFrame++;

                if (currentFrame >= frameCount) {
                    currentFrame = 0;
                }
            }
        }

        frameToDraw.left = currentFrame * frameWidth;
        frameToDraw.right = frameToDraw.left + frameWidth;
    }

    public void draw() {
        if (ourHolder.getSurface().isValid()) {
            canvas = ourHolder.lockCanvas();
            canvas.drawColor(ResourcesCompat.getColor(getResources(), R.color.ewanblue, null));
            whereToDraw.set((int) manXPos, (int) manYPos, (int) manXPos + frameWidth, (int) manYPos + frameHeight);
            manageCurrentFrame();
            canvas.drawBitmap(bitmapRunningMan, frameToDraw, whereToDraw, null);
            ourHolder.unlockCanvasAndPost(canvas);
        }
    }

    public void pause() {
        playing = false;
        isMoving = !isMoving;
        try {
            gameThread.join();
        } catch(InterruptedException e) {
            Log.e("ERR", "Joining Thread");
        }
    }

    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
        isMoving = !isMoving;
    }
}