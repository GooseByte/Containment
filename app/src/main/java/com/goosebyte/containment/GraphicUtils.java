package com.goosebyte.containment;

import android.content.res.Resources;
import android.util.TypedValue;

public class GraphicUtils {

    public static double calculateDistanceBetweenPoints(
            double x1,
            double y1,
            double x2,
            double y2) {
        return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
    }

    public static boolean twoCirclesColliding(float c1x,float c1y, float c1radius, float c2x, float c2y, float c2radius) {
        if(GraphicUtils.calculateDistanceBetweenPoints(c1x, c1y, c2x, c2y) < c1radius + c2radius){
            return true;
        }
        return false;
    }

    public static boolean pointInsideRectangle(float pointx, float pointy, float rectLeft, float recRight, float recTop, float recBottom) {
        return (pointx >= rectLeft && pointx <= recRight && pointy >= recTop && pointy <= recBottom);
    }

    public static int dipToPx (float dps){
               return Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 14,Resources.getSystem().getDisplayMetrics()));
    }


}
