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

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

import rs.raf.rafdnevnjak.activities.EditObligationActivity;
import rs.raf.rafdnevnjak.activities.MainActivity;
import rs.raf.rafdnevnjak.R;
import rs.raf.rafdnevnjak.activities.ShowObligationActivity;
import rs.raf.rafdnevnjak.models.Day;
import rs.raf.rafdnevnjak.models.Obligation;
import rs.raf.rafdnevnjak.models.Priority;
import rs.raf.rafdnevnjak.modelviews.RecyclerViewModel;
import rs.raf.rafdnevnjak.recycler.DailyPlanAdapter;
import rs.raf.rafdnevnjak.recycler.ObligationDiffItemCallback;
import rs.raf.rafdnevnjak.viewpager.PagerAdapter;

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
                    Toast.makeText(getContext(), "Show - Error happened", Toast.LENGTH_LONG).show();
                }
            });

    ActivityResultLauncher<Intent> editObligationActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // There are no request codes
                    Intent data = result.getData();
                    Obligation obligation = (Obligation) data.getSerializableExtra(EditObligationActivity.EDIT_OBLIGATION_RETURN_KEY);
                    int returnPosition = data.getIntExtra(EditObligationActivity.RETURN_POSITION_RETURN_KEY, -1);
                    if (returnPosition == -1) {
                        recyclerViewModel.addObligation(new Day(selectedDate.minusDays(1)), obligation);
                    } else {
                        recyclerViewModel.modifyObligation(new Day(selectedDate.minusDays(1)), obligation, returnPosition);
                    }
                    Toast.makeText(getContext(), R.string.data_saved, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), R.string.data_discarded, Toast.LENGTH_LONG).show();
                }
            });

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerViewModel = new ViewModelProvider(requireActivity()).get(RecyclerViewModel.class);
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
    }

    private void dodajBezveze(Day day) {
        Random random = new Random();
        int rnd = random.nextInt(5);
        if (rnd == 0) {
            Obligation obligation1 = new Obligation("bol",
                    Priority.MID, LocalTime.of(14, 30),
                    LocalTime.of(14, 45),
                    "Nesto protiv bolova");
            recyclerViewModel.addObligation(day, obligation1);
        } else if (rnd == 1) {
            Obligation obligation2 = new Obligation("Civ2",
                    Priority.LOW, LocalTime.of(11, 30),
                    LocalTime.of(11, 45),
                    "Civas");
            recyclerViewModel.addObligation(day, obligation2);
        } else if (rnd == 2) {
            Obligation obligation3 = new Obligation("Dij",
                    Priority.MID, LocalTime.of(20, 30),
                    LocalTime.of(20, 45),
                    "dijabolik");
            recyclerViewModel.addObligation(day, obligation3);
        } else if (rnd == 3) {
            Obligation obligation4 = new Obligation("Bla",
                    Priority.LOW, LocalTime.of(18, 30),
                    LocalTime.of(18, 45),
                    "blah blah");
            recyclerViewModel.addObligation(day, obligation4);
        } else if (rnd == 4) {
            Obligation obligation5 = new Obligation("MK",
                    Priority.HIGH, LocalTime.of(4, 0),
                    LocalTime.of(4, 30),
                    "mile bog");
            recyclerViewModel.addObligation(day, obligation5);
        }
    }

    private void refreshPager() {
        PagerAdapter adapter = ((MainActivity) requireActivity()).getAdapter();
        adapter.notifyDataSetChanged();
    }

    private void initListeners() {
        floatingActionButton.setOnClickListener(e -> {
//            Day day = new Day(selectedDate.minusDays(1));
            Day day = new Day(selectedDate);
            Intent intent = new Intent(requireActivity(), EditObligationActivity.class);
            intent.putExtra(EditObligationActivity.EDIT_OBLIGATION_DAY_KEY, day);
//            intent.putExtra(EditObligationActivity.EDIT_OBLIGATION_OBLIGATION_KEY, null);
            intent.putExtra(EditObligationActivity.IS_EDIT_KEY, false);
//            intent.putExtra(EditObligationActivity.POSITION_KEY, -1);
            editObligationActivityResultLauncher.launch(intent);
            refreshPager();
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
        Day dayBefore = new Day(day.getDate().minusDays(1));
        Intent intent = new Intent(requireActivity(), EditObligationActivity.class);
        int pos = -1;
        Obligation obligation = null;
        for (int i = 0; i < recyclerViewModel.getObligations().getValue().get(dayBefore).size(); i++) {
            if (recyclerViewModel.getObligations().getValue().get(dayBefore).get(i).getName().equals(name)) {
                pos = i;
                obligation = recyclerViewModel.getObligations().getValue().get(dayBefore).get(i);
                break;
            }
        }
        if (pos == -1) return;
        intent.putExtra(EditObligationActivity.EDIT_OBLIGATION_DAY_KEY, day);
        intent.putExtra(EditObligationActivity.EDIT_OBLIGATION_OBLIGATION_KEY, obligation);
        intent.putExtra(EditObligationActivity.IS_EDIT_KEY, true);
        intent.putExtra(EditObligationActivity.POSITION_KEY, pos);
        editObligationActivityResultLauncher.launch(intent);
        refreshPager();
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
        refreshPager();
        snackbar.setAction("UNDO", view -> {
            if (deletedObligation == null) return;
            recyclerViewModel.addObligation(dayBefore, deletedObligation);
            refreshPager();
        });
        snackbar.show();
    }
}
