package rs.raf.rafdnevnjak.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import rs.raf.rafdnevnjak.R;
import rs.raf.rafdnevnjak.models.Day;
import rs.raf.rafdnevnjak.recycler.CalendarAdapter;
import rs.raf.rafdnevnjak.recycler.DayDiffItemCallback;

public class CalendarFragment extends Fragment implements CalendarAdapter.OnItemListener {
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private TextView cellDayText;
    private LocalDate selectedDate;
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
        cellDayText = view.findViewById(R.id.cellDayText);
        selectedDate = LocalDate.now();
        setMonthView();
    }

    private void setMonthView() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        monthYearText.setText(selectedDate.format(formatter));

        ArrayList<Day> daysInMonth = daysInMonthArray(selectedDate);
        CalendarAdapter calendarAdapter = new CalendarAdapter(getContext(),
                new DayDiffItemCallback(),
                daysInMonth, this);
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

        for (int i = 1; i <= 42; i++) {
            if (i <= dayOfWeek) {
                res.add(new Day(firstOfMonth.minusDays(dayOfWeek - i + 1)));
            } else if (i > daysInMonth + dayOfWeek) {
                res.add(new Day(firstOfMonth.plusDays(i - dayOfWeek - 1)));
            } else {
                res.add(new Day(firstOfMonth.plusDays(i - dayOfWeek - 1)));
            }
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

    }

    @Override
    public void onItemClick(int position, String dayText) {
        if (dayText.equals("")) {
            Toast.makeText(getContext(), "You selected empty cell", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), dayText, Toast.LENGTH_LONG).show();
        }
    }
}
