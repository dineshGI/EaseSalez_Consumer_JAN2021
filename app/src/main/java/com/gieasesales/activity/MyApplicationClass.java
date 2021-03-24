package com.gieasesales.activity;

import android.app.Application;

import com.google.firebase.FirebaseApp;

public class MyApplicationClass extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }
}
