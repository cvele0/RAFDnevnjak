package rs.raf.rafdnevnjak.viewpager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.time.LocalDate;
import java.util.ArrayList;

import rs.raf.rafdnevnjak.MainActivity;
import rs.raf.rafdnevnjak.fragments.CalendarFragment;
import rs.raf.rafdnevnjak.fragments.DailyPlanFragment;
import rs.raf.rafdnevnjak.fragments.ProfileFragment;

public class PagerAdapter extends FragmentPagerAdapter {

    private final int ITEM_COUNT = 3;
    public static final int FRAGMENT_1 = 0;
    public static final int FRAGMENT_2 = 1;
    public static final int FRAGMENT_3 = 2;
    private FragmentManager fragmentManager;
    private ArrayList<Fragment> mFragments;
    private MainActivity mainActivity;
    private LocalDate date;
    private boolean changed;

    public PagerAdapter(@NonNull FragmentManager fm, ArrayList<Fragment> mFragments, MainActivity mainActivity, LocalDate date) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.fragmentManager = fm;
        this.mFragments = mFragments;
        this.mainActivity = mainActivity;
        this.date = date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
        changed = true;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
//        if (object instanceof DailyPlanFragment && changed) {
//            changed = false;
//            return POSITION_NONE;
//        }
//        return POSITION_UNCHANGED;
        return POSITION_NONE;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return switch (position) {
            case FRAGMENT_1 -> new CalendarFragment();
            case FRAGMENT_2 -> new DailyPlanFragment(date);
            default -> new ProfileFragment();
        };
    }

    @Override
    public int getCount() {
        return ITEM_COUNT;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return switch (position) {
            case FRAGMENT_1 -> "1";
            case FRAGMENT_2 -> "2";
            default -> "3";
        };
    }
}
