package rs.raf.rafdnevnjak.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

import rs.raf.rafdnevnjak.R;
import rs.raf.rafdnevnjak.ShowObligationActivity;
import rs.raf.rafdnevnjak.models.Day;
import rs.raf.rafdnevnjak.models.Obligation;
import rs.raf.rafdnevnjak.models.Priority;
import rs.raf.rafdnevnjak.modelviews.RecyclerViewModel;
import rs.raf.rafdnevnjak.recycler.DailyPlanAdapter;
import rs.raf.rafdnevnjak.recycler.ObligationDiffItemCallback;

public class DailyPlanFragment extends Fragment implements DailyPlanAdapter.ClickListener {
    private TextView dateText;
    private CheckBox checkBox;
    private SearchView searchView;
    private FloatingActionButton floatingActionButton;
    private RecyclerView dailyPlanRecyclerView;
    private DailyPlanAdapter dailyPlanAdapter;
    private RecyclerViewModel recyclerViewModel;
    private TextView lowTab;
    private TextView midTab;
    private TextView highTab;
    public static LocalDate selectedDate;
    private LinearLayout linearLayout;
    public DailyPlanFragment(LocalDate localDate) {
        super(R.layout.fragment_daily_plan);
        selectedDate = localDate;
    }

    ActivityResultLauncher<Intent> obligationActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // There are no request codes
                    Intent data = result.getData();
                    ArrayList<Obligation> list = (ArrayList<Obligation>) data.getSerializableExtra(ShowObligationActivity.SHOW_OBLIGATION_RETURN_KEY);
                    recyclerViewModel.setObligations(new Day(selectedDate.minusDays(1)), list);
                } else {
                    Toast.makeText(getContext(), "Error happened", Toast.LENGTH_LONG).show();
                }
            });

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recyclerViewModel = new ViewModelProvider(requireActivity()).get(RecyclerViewModel.class);
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
        dailyPlanRecyclerView = view.findViewById(R.id.dailyPlanRecyclerView);
        floatingActionButton = view.findViewById(R.id.dailyPlanFloatingButton);
        lowTab = view.findViewById(R.id.lowTab);
        midTab = view.findViewById(R.id.midTab);
        highTab = view.findViewById(R.id.highTab);
        linearLayout = view.findViewById(R.id.dailyPlanFragmentLayout);

        checkBox.setChecked(true);
        setView();
    }

    private void setView() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd. yyyy.");
        dateText.setText(selectedDate.format(formatter));

        recyclerViewModel.reserveSpace(new Day(selectedDate));
        dailyPlanAdapter = new DailyPlanAdapter(
                new Day(selectedDate),
                getContext(),
                new ObligationDiffItemCallback(),
                Objects.requireNonNull(recyclerViewModel.getObligations().getValue())
                        .get(new Day(selectedDate))
                , this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext().getApplicationContext());
        dailyPlanRecyclerView.setLayoutManager(layoutManager);
        dailyPlanRecyclerView.setAdapter(dailyPlanAdapter);

        //TODO obrisi
//        dodajBezveze(new Day(selectedDate.minusDays(1)));
        dodajBezveze(new Day(LocalDate.of(2023, 4, 19).minusDays(1)));
        dodajBezveze2(new Day(LocalDate.of(2023, 4, 14).minusDays(1)));
    }

    private void dodajBezveze(Day day) {
        Obligation obligation1 = new Obligation("bol",
                Priority.MID, LocalTime.of(14, 30),
                LocalTime.of(14, 45),
                "Nesto protiv bolova");
        Obligation obligation2 = new Obligation("Civ2",
                Priority.LOW, LocalTime.of(11, 30),
                LocalTime.of(11, 45),
                "Civas");
        Obligation obligation3 = new Obligation("Dij",
                Priority.MID, LocalTime.of(20, 30),
                LocalTime.of(20, 45),
                "dijabolik");
        Obligation obligation4 = new Obligation("Bla",
                Priority.LOW, LocalTime.of(18, 30),
                LocalTime.of(18, 45),
                "blah blah");
        Obligation obligation5 = new Obligation("MK",
                Priority.HIGH, LocalTime.of(4, 0),
                LocalTime.of(4, 30),
                "mile bog");
        recyclerViewModel.addObligation(day, obligation1);
        recyclerViewModel.addObligation(day, obligation2);
        recyclerViewModel.addObligation(day, obligation3);
        recyclerViewModel.addObligation(day, obligation4);
        recyclerViewModel.addObligation(day, obligation5);
    }

    private void dodajBezveze2(Day day) {
        Obligation obligation1 = new Obligation("bol",
                Priority.MID, LocalTime.of(14, 30),
                LocalTime.of(14, 45),
                "Nesto protiv bolova");
        Obligation obligation2 = new Obligation("Civ2",
                Priority.LOW, LocalTime.of(11, 30),
                LocalTime.of(11, 45),
                "Civas");
        Obligation obligation3 = new Obligation("Dij",
                Priority.MID, LocalTime.of(20, 30),
                LocalTime.of(20, 45),
                "dijabolik");
        Obligation obligation4 = new Obligation("Bla",
                Priority.LOW, LocalTime.of(18, 30),
                LocalTime.of(18, 45),
                "blah blah");
//        Obligation obligation5 = new Obligation("MK",
//                Priority.HIGH, LocalTime.of(4, 0),
//                LocalTime.of(4, 30),
//                "mile bog");
        recyclerViewModel.addObligation(day, obligation1);
        recyclerViewModel.addObligation(day, obligation2);
        recyclerViewModel.addObligation(day, obligation3);
        recyclerViewModel.addObligation(day, obligation4);
//        recyclerViewModel.addObligation(day, obligation5);
    }

    private void initListeners() {
        floatingActionButton.setOnClickListener(e -> {
            Day day = new Day(selectedDate.minusDays(1));
//            dodajBezveze(day);
            //TODO raditi
        });

        recyclerViewModel.getObligations().observe(requireActivity(), obligations -> {
            Day day = new Day(selectedDate.minusDays(1));
            dailyPlanAdapter.submitList(obligations.get(day));
        });

        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                recyclerViewModel.showAllObligations(new Day(selectedDate.minusDays(1)));
            } else {
                recyclerViewModel.showActiveObligations(new Day(selectedDate.minusDays(1)));
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Day day = new Day(selectedDate.minusDays(1));
                recyclerViewModel.filterObligations(day, newText);
                return true;
            }
        });

        lowTab.setOnClickListener(e -> {
            recyclerViewModel.sortByPriority(new Day(selectedDate.minusDays(1)), Priority.LOW);
        });

        midTab.setOnClickListener(e -> {
            recyclerViewModel.sortByPriority(new Day(selectedDate.minusDays(1)), Priority.MID);
        });

        highTab.setOnClickListener(e -> {
            recyclerViewModel.sortByPriority(new Day(selectedDate.minusDays(1)), Priority.HIGH);
        });
    }

    @Override
    public void onObligationClick(int position, Day day) {
        Day dayBefore = new Day(day.getDate().minusDays(1));
        Intent intent = new Intent(requireActivity(), ShowObligationActivity.class);
        intent.putExtra(ShowObligationActivity.DAY_KEY, day);
        intent.putExtra(ShowObligationActivity.OBLIGATION_KEY,
                recyclerViewModel.getObligations().getValue().get(dayBefore));
        intent.putExtra(ShowObligationActivity.SELECTED_OBLIGATION_KEY, position);
        obligationActivityResultLauncher.launch(intent);
    }

    @Override
    public void onEditClick(String name, Day day) {
        //TODO implement
        Toast.makeText(getContext(), "edit clicked", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDeleteClick(String name, Day day) {
        Snackbar snackbar = Snackbar.make(linearLayout, "Item deleted", Snackbar.LENGTH_LONG);
        Day dayBefore = new Day(day.getDate().minusDays(1));
        Optional<Obligation> deleted = recyclerViewModel.
                getObligations().getValue().get(dayBefore)
                .stream()
                .filter(ob -> ob.getName().equals(name))
                .findFirst();
        if (!deleted.isPresent()) return;
        Obligation deletedObligation = recyclerViewModel.removeObligation(dayBefore, deleted.get());
        snackbar.setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (deletedObligation == null) return;
                recyclerViewModel.addObligation(dayBefore, deletedObligation);
            }
        });
        snackbar.show();
    }
}
