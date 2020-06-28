package com.goosebyte.containment;

import com.parse.Parse;
import android.app.Application;

public class Containment extends Application {
    // Initializes Parse SDK as soon as the application is created
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("hncHgZodRWQKZK6CqqZcMz9bfJ3LwYQEuJ9FJsbt")
                .clientKey("EUcYezYblvME11J924kWHaaqAOkY0a133YD83fiN")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}

