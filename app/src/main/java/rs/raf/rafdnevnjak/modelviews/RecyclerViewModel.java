package rs.raf.rafdnevnjak.modelviews;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import rs.raf.rafdnevnjak.models.Day;
import rs.raf.rafdnevnjak.models.Obligation;

public class RecyclerViewModel extends ViewModel {
    private final MutableLiveData<HashMap<Day, ArrayList<Obligation>>> obligations = new MutableLiveData<>();
    private HashMap<Day, ArrayList<Obligation>> obligationsMap = new HashMap<>();

    public RecyclerViewModel() {
        HashMap<Day, ArrayList<Obligation>> mapToSubmit = new HashMap<>(obligationsMap);
        obligations.setValue(mapToSubmit);
    }

    public LiveData<HashMap<Day, ArrayList<Obligation>>> getObligations() {
        return obligations;
    }

    public void filterObligations(Day day, String filter) {
        if (obligationsMap.get(day) == null) return;
        List<Obligation> list = obligationsMap.get(day)
                .stream()
                .filter(ob -> ob.getName().toLowerCase().startsWith(filter.toLowerCase()))
                .collect(Collectors.toList());
        ArrayList<Obligation> filtered = new ArrayList<>(list);
        HashMap<Day, ArrayList<Obligation>> filteredMap = new HashMap<>(obligationsMap);
        filteredMap.put(day, filtered);
        obligations.setValue(filteredMap);
    }

    @SuppressWarnings("ConstantConditions")
    public void showAllObligations(Day day) {
        if (obligationsMap.get(day) == null) return;
        ArrayList<Obligation> filtered = new ArrayList<>(obligationsMap.get(day));
        HashMap<Day, ArrayList<Obligation>> filteredMap = new HashMap<>(obligationsMap);
        filteredMap.put(day, filtered);
        obligations.setValue(filteredMap);
    }

    @SuppressWarnings("ConstantConditions")
    public void showActiveObligations(Day day) {
        if (obligationsMap.get(day) == null) return;
        List<Obligation> list = obligationsMap.get(day)
                .stream()
                .filter(ob -> {
                    LocalTime obg = ob.getStartTime();
                    LocalTime now = LocalTime.now();
                    LocalTime timeObg = LocalTime.of(obg.getHour(), obg.getMinute());
                    LocalTime timeNow = LocalTime.of(now.getHour(), now.getMinute());
                    return timeObg.isAfter(timeNow);
                })
                .collect(Collectors.toList());
        ArrayList<Obligation> filtered = new ArrayList<>(list);
        HashMap<Day, ArrayList<Obligation>> filteredMap = new HashMap<>(obligationsMap);
        filteredMap.put(day, filtered);
        obligations.setValue(filteredMap);
    }

    public void reserveSpace(Day day) {
        obligationsMap.putIfAbsent(day, new ArrayList<>());
        HashMap<Day, ArrayList<Obligation>> mapToSubmit = new HashMap<>(obligationsMap);
        obligations.setValue(mapToSubmit);
    }

    @SuppressWarnings("ConstantConditions")
    public void addObligation(Day day, Obligation obligation) {
        obligationsMap.putIfAbsent(day, new ArrayList<>());
        obligationsMap.get(day).add(obligation);
        obligationsMap.get(day).sort(Comparator.comparing(Obligation::getStartTime));
        HashMap<Day, ArrayList<Obligation>> mapToSubmit = new HashMap<>(obligationsMap);
        obligations.setValue(mapToSubmit);
    }

    public void removeObligation(Day day, Obligation obligation) {
        Objects.requireNonNull(obligationsMap.get(day)).remove(obligation);
        HashMap<Day, ArrayList<Obligation>> mapToSubmit = new HashMap<>(obligationsMap);
        obligations.setValue(mapToSubmit);
    }
}
