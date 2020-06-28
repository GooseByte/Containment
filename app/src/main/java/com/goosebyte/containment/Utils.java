package com.goosebyte.containment;

import java.util.Random;

public class Utils {

    public static int generateRandomNumber(Random rand, int min, int max) {
        return rand.nextInt((max - min) + 1) + min;
    }

}
