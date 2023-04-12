package rs.raf.rafdnevnjak.application;

import android.app.Application;
import android.content.Context;

import java.io.FileOutputStream;

import timber.log.Timber;

public class RAFDnevnjakApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
    }
}
