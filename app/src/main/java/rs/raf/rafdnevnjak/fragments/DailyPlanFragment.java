package rs.raf.rafdnevnjak.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import rs.raf.rafdnevnjak.R;

public class DailyPlanFragment extends Fragment {
    private TextView dateText;
    private CheckBox checkBox;
    private SearchView searchView;
    private RecyclerView recyclerView;
    public DailyPlanFragment() {
        super(R.layout.fragment_daily_plan);
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
        dateText = view.findViewById(R.id.dailyPlanDateText);
        checkBox = view.findViewById(R.id.dailyPlanCheckBox);
        searchView = view.findViewById(R.id.dailyPlanSearchView);
        recyclerView = view.findViewById(R.id.dailyPlanRecyclerView);
    }
    private void initListeners() {

    }
}
