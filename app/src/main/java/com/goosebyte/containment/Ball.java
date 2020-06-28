package com.goosebyte.containment;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.List;

public class Ball {
    private int id;
    private float x, y;
    private int r,g,b;
    private float radius;
    private float xVelocity;
    private float yVelocity;
    private boolean pendingRadiusIncrease;
    private float minX, minY, maxX, maxY;

    public Ball(GameArea gameArea, int id, float x, float y, float radius, float velocityX, float velocityY, int red, int green, int blue) {
        this.id = id;
        this.x = x;
        this.y= y;
        this.radius = radius;
        this.xVelocity = velocityX;
        this.yVelocity = velocityY;
        this.r = red;
        this.g = green;
        this.b = blue;
        this.minX = gameArea.left + radius;
        this.maxX = gameArea.right - radius;
        this.minY = gameArea.top + radius;
        this.maxY = gameArea.bottom - radius;
        this.pendingRadiusIncrease = false;
    }

    public void draw(Canvas canvas) {
        //TODO: Fix : performance
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.rgb(r,g,b));

        canvas.drawCircle(x,y,radius,paint);
    }

    private boolean collissionWithBall(Ball b) {
        if(GraphicUtils.twoCirclesColliding(this.x,this.y,this.radius,b.x,b.y,b.radius)) {
            return true;
        }
        return false;
    }

    public void update(List<Ball> ballCollection) {
            boolean collission = false;
            for (Ball b : ballCollection) {
                if (this.id == b.id) {
                    continue;
                }

                if (collissionWithBall(b)) {
                    float tempX = b.xVelocity;
                    float tempY = b.yVelocity;
                    b.xVelocity = xVelocity;
                    b.yVelocity = yVelocity;
                    xVelocity= tempX;
                    yVelocity = tempY;

                    do {
                        x += xVelocity;
                        y += yVelocity;

                    } while (collissionWithBall(b));
                    this.pendingRadiusIncrease = true;
                    b.pendingRadiusIncrease = true;
                    collission = true;
                    break;
                }
            }

            if (collission) {
                return;
            }

            if (pendingRadiusIncrease) {
                //radius+=1;
                pendingRadiusIncrease = false;
            }

            x += xVelocity;
            y += yVelocity;

        if (x<=minX) {
            x =minX;
            xVelocity *= -1;
        }

        if (x>=maxX){
            x = maxX;
            xVelocity *= -1;
        }

        if (y<=minY){
            y=minY;
            yVelocity *= -1;
        }

        if (y>=maxY) {
            y=maxY;
            yVelocity *= -1;
            }
    }
}
