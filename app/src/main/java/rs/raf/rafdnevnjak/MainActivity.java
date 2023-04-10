package rs.raf.rafdnevnjak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import rs.raf.rafdnevnjak.fragments.LoginFragment;
import rs.raf.rafdnevnjak.modelviews.SplashViewModel;

public class MainActivity extends AppCompatActivity {
    public static final String PREF_LOGIN_KEY = "prefLoginKey";
    public static final String LOGIN_FRAGMENT_TAG = "loginFragmentTag";

    private SplashViewModel splashViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        splashViewModel = new ViewModelProvider(this).get(SplashViewModel.class);
        // Handle the splash screen transition.
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        splashScreen.setKeepOnScreenCondition(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return false;

//            Boolean value = splashViewModel.isLoading().getValue();
//            if (value == null) return false;
//            return value;
        });

        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        String logged = sharedPreferences.getString(PREF_LOGIN_KEY, "false");

        if (logged.equals("false")) {
            setContentView(R.layout.activity_main_fragment);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.mainFragmentFcv, new LoginFragment(), LOGIN_FRAGMENT_TAG);
            transaction.commit();
        } else {
            setContentView(R.layout.activity_main);
        }
    }
}