package com.goosebyte.containment;

public class GameArea {

    float left;
    float top;
    float right;
    float bottom;

    public float width() {
        return right-left;
    }

    public float height() {
        return bottom - top;
    }


    public GameArea(float left, float top, float right, float bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

}
