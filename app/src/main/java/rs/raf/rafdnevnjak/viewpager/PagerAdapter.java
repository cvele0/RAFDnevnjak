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
//    private Fragment mFragmentAtPos0;
//    private CalendarPageListener calendarPageListener = new CalendarPageListener();

//    private final class CalendarPageListener implements PagerAdapter.CalendarPageFragmentListener {
//        @Override
//        public void onSwitchToNextFragment(LocalDate date) {
//            fragmentManager.beginTransaction().remove(mFragmentAtPos0)
//                    .commit();
//            mFragmentAtPos0 = new DailyPlanFragment(date);
//            notifyDataSetChanged();
//        }
//    }

    public PagerAdapter(@NonNull FragmentManager fm, ArrayList<Fragment> mFragments, MainActivity mainActivity) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.fragmentManager = fm;
        this.mFragments = mFragments;
        this.mainActivity = mainActivity;
    }

//    @Override
//    public int getItemPosition(@NonNull Object object) {
//        if (object instanceof DailyPlanFragment)
//            return POSITION_NONE;
//        return POSITION_UNCHANGED;
//    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
//        if (position == 0)
//            return new CalendarFragment();
//        if (position == 1) { // Position where you want to replace fragments
//            if (mFragmentAtPos0 == null) {
//                mFragmentAtPos0 = FirstFragment.newInstance(listener);
//            }
//            return mFragmentAtPos0;
//        }
//        if (position == 2)
//            return Clasificacion.newInstance();
//        if (position == 3)
//            return Informacion.newInstance();
//
//        return null;
        return switch (position) {
            case FRAGMENT_1 -> new CalendarFragment();
            case FRAGMENT_2 -> new DailyPlanFragment(LocalDate.now());
            default -> new ProfileFragment();
        };
    }

    @Override
    public int getCount() {
        return ITEM_COUNT;
    }

    public void replaceFragment(int position, Fragment fragment) {
//        // Get the old fragment at the specified position
//        Fragment oldFragment = getItem(position);
////
////        // Replace the old fragment with the new one
//        FragmentTransaction transaction = mainActivity.getSupportFragmentManager().beginTransaction();
////        transaction.remove(oldFragment);
////        Toast.makeText(mainActivity.getApplicationContext(), "hey", Toast.LENGTH_LONG).show();
//        transaction.replace();
//////        transaction.addToBackStack(null);
//        transaction.commit();
//
//        // Update the internal list of fragments in the adapter
//        mFragments.set(position, fragment);
        mFragments.set(position, fragment);
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

    public interface CalendarPageFragmentListener {
        void onSwitchToNextFragment(LocalDate date);
    }
}
