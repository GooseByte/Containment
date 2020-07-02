package com.goosebyte.containment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.SharedPreferences;
import androidx.appcompat.app.AlertDialog;

import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.goosebyte.containment.GobalValues.USER_PASSWORD_SUFFIX;
import static com.goosebyte.containment.Utils.generateRandomNumber;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private MainThread thread;

    private List<Ball> ballCollection = new ArrayList<>();
    RectangleShape upRectangleShape = null;
    RectangleShape downRectangleShape = null;
    RectangleShape gameSingleBorderShape = null;

    RectangleShape buttonsArea = null;

    GameArea gameArea = null;

    Context context = null;

    private static final int RADIUS = 10;

    public GameView(Context context) {
        super(context);
        this.context = context;

        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);
        setFocusable(true); 
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunning(true);
        thread.start();

        if (!SharedPrefs.sharedPrefsExist(context)) {
          //Create User on Parse
            ParseUtils.createParseUserDialog(context);
        } else {
            //TODO : Load existing username, login
            String password = "MetalMilitia" + USER_PASSWORD_SUFFIX;

            ParseUtils.loginUser("MetalMilitia",password);
            ParseUtils.updateScoreRecord(25,2000);

            List<ParseObject> scoreBoard = ParseUtils.getScores(20,0);
            for (ParseObject result : scoreBoard) {
                System.out.println("Year " + result.get("year").toString() + " Population " + result.get("population"));
            }

        }


        buttonsArea = new RectangleShape(1,Resources.getSystem().getDisplayMetrics().heightPixels - 270,Resources.getSystem().getDisplayMetrics().widthPixels -1,Resources.getSystem().getDisplayMetrics().heightPixels -1  , Paint.Style.STROKE);




        /*
        upRectangleShape = new RectangleShape(1,1,100,(Resources.getSystem().getDisplayMetrics().heightPixels /2) - 30, Paint.Style.FILL);
        downRectangleShape = new RectangleShape(1,(Resources.getSystem().getDisplayMetrics().heightPixels /2) + 30,100,Resources.getSystem().getDisplayMetrics().heightPixels, Paint.Style.FILL);
        gameSingleBorderShape = new RectangleShape(101,1,Resources.getSystem().getDisplayMetrics().widthPixels-102,Resources.getSystem().getDisplayMetrics().heightPixels-3, Paint.Style.STROKE);
        gameArea = new GameArea(102,2, Resources.getSystem().getDisplayMetrics().widthPixels-103,Resources.getSystem().getDisplayMetrics().heightPixels-3);
        generateBalls(2);
*/
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    public void update() {
        buttonsArea.update();

/*
        upRectangleShape.update();
        downRectangleShape.update();

        for (Ball b : ballCollection) {
            b.update(ballCollection);
        }
*/
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {
            canvas.drawColor(Color.BLACK);

            buttonsArea.draw(canvas);

/*
            upRectangleShape.draw(canvas);
            downRectangleShape.draw(canvas);
            gameSingleBorderShape.draw(canvas);

            for (Ball b : ballCollection) {
                b.draw(canvas);
            }
*/
        }
    }

    private void generateBalls(int nrOfBalls) {
        Random rand = new Random(System.currentTimeMillis());

        for (int c = 0; c < nrOfBalls; c++) {
            float x = generateRandomNumber(rand,(int) gameArea.left + RADIUS, (int) gameArea.right - RADIUS);
            float y =  generateRandomNumber(rand,(int) gameArea.top + RADIUS, (int) gameArea.bottom - RADIUS);

            float xVelocity;
            do
                xVelocity = generateRandomNumber(rand,-10,10);
            while ( Math.abs(xVelocity) <3);

            float yVelocity;
            do
                yVelocity = generateRandomNumber(rand,-10,10);
            while (Math.abs(yVelocity) <3);

            float[] colors = {(float)Math.random() * 360,255,127};
            int rgb = Color.HSVToColor(colors);
            int r = Color.red(rgb);
            int g = Color.green(rgb);
            int b = Color.blue(rgb);

            Ball ball = new Ball(gameArea,c,x,y,RADIUS,xVelocity,yVelocity,r,g,b);
            ballCollection.add(ball);

        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //generateBalls(5);

        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN :
            case MotionEvent.ACTION_POINTER_DOWN:
                if (pointInsideButton(x,y, upRectangleShape)) {
                    generateBalls(1);
                    upRectangleShape.touched = true;
                }
                if (pointInsideButton(x,y, downRectangleShape)) {
                    ballCollection.remove(ballCollection.size()-1);
                    downRectangleShape.touched = true;
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if (pointInsideButton(x,y, upRectangleShape)) {
                    upRectangleShape.touched = false;
                }
                if (pointInsideButton(x,y, downRectangleShape)) {
                    downRectangleShape.touched = false;
                }
                break;


            default:
                break;
        }

        return true;
    }

    private boolean pointInsideButton (float pointx, float pointy, RectangleShape button) {
        return (GraphicUtils.pointInsideRectangle(pointx,pointy,button.left,button.right,button.top,button.bottom));
        }

}
