package com.goosebyte.containment;

import com.parse.Parse;
import android.app.Application;
import static com.goosebyte.containment.GobalValues.PARSE_APPLICATIONID;
import static com.goosebyte.containment.GobalValues.PARSE_CLIENTKEY;
import static com.goosebyte.containment.GobalValues.PARSE_SERVER;

public class Containment extends Application {
    // Initializes Parse SDK as soon as the application is created
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(PARSE_APPLICATIONID)
                .clientKey(PARSE_CLIENTKEY)
                .server(PARSE_SERVER)
                .build()
        );
    }
}

