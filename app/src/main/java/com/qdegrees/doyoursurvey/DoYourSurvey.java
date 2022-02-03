package com.qdegrees.doyoursurvey;

import androidx.multidex.MultiDexApplication;

    public class DoYourSurvey extends MultiDexApplication {
        static DoYourSurvey app;
        synchronized public static DoYourSurvey getApp(){return app;}
        @Override
        public void onCreate() {
            super.onCreate();
            app=this;

        }

}
