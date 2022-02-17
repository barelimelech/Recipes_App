package com.example.recipes_app.model;

import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Model {
    public static final Model instance = new Model();

    public Executor executor = Executors.newFixedThreadPool(1);
    public Handler mainThread = HandlerCompat.createAsync(Looper.getMainLooper());
    private ModelFirebase modelFirebase = new ModelFirebase();

    public boolean isSignedIn() {
        return modelFirebase.isSignedIn();
    }

    private Model() {}

}
