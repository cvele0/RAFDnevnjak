package rs.raf.rafdnevnjak.fragments;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import rs.raf.rafdnevnjak.R;

public class DailyPlanFragment extends Fragment {
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

    }
    private void initListeners() {

    }
}