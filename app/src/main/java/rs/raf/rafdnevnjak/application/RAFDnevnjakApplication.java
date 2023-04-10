package rs.raf.rafdnevnjak.application;

import android.app.Application;

import timber.log.Timber;

public class RAFDnevnjakApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
        createPassword();
    }

    private void createPassword() {

    }
}
