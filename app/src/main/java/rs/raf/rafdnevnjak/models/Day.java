package rs.raf.rafdnevnjak.models;

public class Day {
    private int id;
    private String day;

    public Day(int id, String day) {
        this.id = id;
        this.day = day;
    }

    public int getId() {
        return id;
    }

    public String getDay() {
        return day;
    }
}
