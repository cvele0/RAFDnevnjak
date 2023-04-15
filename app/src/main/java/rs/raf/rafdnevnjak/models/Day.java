package rs.raf.rafdnevnjak.models;

import java.time.LocalDate;

public class Day {
    private static int counter = 0;
    private int id;
    private LocalDate date;

    public Day(LocalDate date) {
        this.id = ++counter; // TODO resiti preko fajla ili baze
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getDay() {
        return String.valueOf(date.getDayOfMonth());
    }
}
