package rs.raf.rafdnevnjak.modelviews;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import rs.raf.rafdnevnjak.models.Day;

public class RecyclerViewModel extends ViewModel {

    public static int counter = 101;

    private final MutableLiveData<List<Day>> days = new MutableLiveData<>();
    private ArrayList<Day> dayList = new ArrayList<>();

    public RecyclerViewModel() {
//        for (int i = 0; i <= 100; i++) {
//            Day day = new Day("Day" + i);
//            dayList.add(day);
//        }
        // We are doing this because cars.setValue in the background is first checking if the reference on the object is same
        // and if it is it will not do notifyAll. By creating a new list, we get the new reference everytime
        ArrayList<Day> listToSubmit = new ArrayList<>(dayList);
        days.setValue(listToSubmit);
    }

    public LiveData<List<Day>> getDays() {
        return days;
    }

//    public void filterDays(String filter) {
//        List<Day> filteredList = dayList.stream().filter(car -> car.getManufacturer().toLowerCase().startsWith(filter.toLowerCase())).collect(Collectors.toList());
//        days.setValue(filteredList);
//    }

    public int addDay(String pictureUrl, String manufacturer, String model) {
        int id = counter++;
//        Day day = new Day("day" + id);
//        dayList.add(day);
        ArrayList<Day> listToSubmit = new ArrayList<>(dayList);
        days.setValue(listToSubmit);
        return id;
    }

    public void removeDay(int id) {
        Optional<Day> carObject = dayList.stream().filter(car -> car.getId() == id).findFirst();
        if (carObject.isPresent()) {
            dayList.remove(carObject.get());
            ArrayList<Day> listToSubmit = new ArrayList<>(dayList);
            days.setValue(listToSubmit);
        }
    }
}
