package com.goosebyte.containment;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class RectangleShape {

    float left;
    float top;
    float right;
    float bottom;
    boolean touched;
    Paint.Style style;

    public RectangleShape(float left, float top, float right, float bottom,
                          Paint.Style fillStyle) {
        this.left= left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.style= fillStyle;
    }

    public void update() {


    }

    public void draw(Canvas canvas) {

        Paint paint = new Paint();
        paint.setStyle(this.style);

        if (touched){
            paint.setColor(Color.rgb(0,250,0));
        } else {
            paint.setColor(Color.rgb(0,0,250));
        }

        canvas.drawRect(left, top, right, bottom,paint);
    }

}
