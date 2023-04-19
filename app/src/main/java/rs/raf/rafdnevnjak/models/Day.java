package rs.raf.rafdnevnjak.models;

import java.time.LocalDate;
import java.util.Objects;

public class Day {
    private LocalDate date;

    public Day(LocalDate date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Day day = (Day) o;
        return Objects.equals(date, day.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date);
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDayOfMonth() {
        return String.valueOf(date.getDayOfMonth());
    }
}

