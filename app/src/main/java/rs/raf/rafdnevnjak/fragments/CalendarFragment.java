package rs.raf.rafdnevnjak.fragments;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import rs.raf.rafdnevnjak.activities.MainActivity;
import rs.raf.rafdnevnjak.R;
import rs.raf.rafdnevnjak.models.Day;
import rs.raf.rafdnevnjak.recycler.CalendarAdapter;
import rs.raf.rafdnevnjak.recycler.DayDiffItemCallback;

public class CalendarFragment extends Fragment implements CalendarAdapter.OnItemListener {
    public static final int SWIPE_THRESHOLD = 100;
    public static final int SWIPE_VELOCITY_THRESHOLD = 70;
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private LocalDate selectedDate;

    private Map<Integer, LocalDate> mapDate;

    public CalendarFragment() {
        super(R.layout.fragment_calendar);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    private void init(View view) {
        initView(view);
        initListeners();
    }

    private void initView(View view) {
        calendarRecyclerView = view.findViewById(R.id.calendarRecyclerView);
        monthYearText = view.findViewById(R.id.monthYearText);
        selectedDate = LocalDate.now();
        setMonthView();
    }

    private void setMonthView() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        monthYearText.setText(selectedDate.format(formatter));

        ArrayList<Day> daysInMonth = daysInMonthArray(selectedDate);
        CalendarAdapter calendarAdapter = new CalendarAdapter(getContext(),
                new DayDiffItemCallback(),
                daysInMonth, this, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(requireContext().getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    private ArrayList<Day> daysInMonthArray(LocalDate date) {
        ArrayList<Day> res = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();
        mapDate = new HashMap<>();

        for (int i = 1; i <= 42; i++) {
            LocalDate calcDate;
            if (i <= dayOfWeek) {
                calcDate = firstOfMonth.minusDays(dayOfWeek - i + 1);
            } else if (i > daysInMonth + dayOfWeek) {
                calcDate = firstOfMonth.plusDays(i - dayOfWeek - 1);
            } else {
                calcDate = firstOfMonth.plusDays(i - dayOfWeek - 1);
            }
            res.add(new Day(calcDate));
            mapDate.put(i - 1, calcDate);
        }
        return res;
    }

    public void previousMonthAction() {
        selectedDate = selectedDate.minusMonths(1);
        setMonthView();
    }

    public void nextMonthAction() {
        selectedDate = selectedDate.plusMonths(1);
        setMonthView();
    }

    private void initListeners() {
        GestureDetector gestureDetector = new GestureDetector(getContext(),
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onDown(@NonNull MotionEvent e) {
                        return false;
                    }

                    @Override
                    public boolean onFling(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
                        try {
                            float diffY = e2.getY() - e1.getY();
                            if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                                if (diffY > 0) {
                                    swipeUpAction();
                                } else {
                                    swipeDownAction();
                                }
                                return true;
                            }
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                        return false;
                    }
                });
        calendarRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                return gestureDetector.onTouchEvent(e);
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {}

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}
        });
    }

    private void swipeDownAction() {
        nextMonthAction();
    }

    private void swipeUpAction() {
        previousMonthAction();
    }

    @Override
    public void onItemClick(int position, String dayText) {
        LocalDate date = mapDate.get(position);
        ((MainActivity) requireActivity()).setDailyPlanFocus(date);
    }
}
