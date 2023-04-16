package rs.raf.rafdnevnjak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import rs.raf.rafdnevnjak.fragments.LoginFragment;
import rs.raf.rafdnevnjak.models.User;
import rs.raf.rafdnevnjak.modelviews.SplashViewModel;
import rs.raf.rafdnevnjak.viewpager.PagerAdapter;

public class MainActivity extends AppCompatActivity {
    public static final String PREF_LOGIN_KEY = "prefLoginKey";
    public static final String LOGIN_FRAGMENT_TAG = "loginFragmentTag";
    public static final String CALENDAR_FRAGMENT_TAG = "calendarFragmentTag";
    private String validPassword = null;
    private ViewPager viewPager;
    private User loggedUser = null;

    private SplashViewModel splashViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

        splashViewModel = new ViewModelProvider(this).get(SplashViewModel.class);
        // Handle the splash screen transition.
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        splashScreen.setKeepOnScreenCondition(() -> {
            try {
                Thread.sleep(500);
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
            loadLoginView();
        } else {
            loadMainView();
        }
    }

    public void loadLoginView() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        setContentView(R.layout.activity_login_fragment);
        transaction.replace(R.id.loginFragment, new LoginFragment(), LOGIN_FRAGMENT_TAG);
        transaction.commit();
    }

    public void loadMainView() {
        setContentView(R.layout.activity_main_fragment);
        initNavigationBar();
    }

    private void init() {
        loadUser();
    }

    private void initNavigationBar() {
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));

        ((BottomNavigationView)findViewById(R.id.bottomNavigation)).setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_1 -> viewPager.setCurrentItem(PagerAdapter.FRAGMENT_1, false);
                case R.id.navigation_2 -> viewPager.setCurrentItem(PagerAdapter.FRAGMENT_2, false);
                case R.id.navigation_3 -> viewPager.setCurrentItem(PagerAdapter.FRAGMENT_3, false);
            }
            return true;
        });
    }

    public void saveUser(User user) {
        try {
            FileOutputStream fileOut = openFileOutput("userData.dat", Context.MODE_PRIVATE);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(user);
            objectOut.close();
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public User loadUser() {
        User user = null;
        try {
            FileInputStream fileIn = openFileInput("userData.dat");
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            user = (User) objectIn.readObject();
            loggedUser = user;
            objectIn.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return user;
    }

    public User getLoggedUser() {
        return loggedUser;
    }
}