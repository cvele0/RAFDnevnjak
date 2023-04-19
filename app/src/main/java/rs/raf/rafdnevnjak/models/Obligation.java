package rs.raf.rafdnevnjak.models;

import java.time.LocalTime;
import java.util.Objects;

public class Obligation {
    private String name;
    private Priority priority;
    private LocalTime startTime;
    private LocalTime endTime;
    private String text;

    public Obligation(String name, Priority priority, LocalTime startTime, LocalTime endTime, String text) {
        this.name = name;
        this.priority = priority;
        this.startTime = startTime;
        this.endTime = endTime;
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Obligation that = (Obligation) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Obligation{" +
                "name='" + name + '\'' +
                ", priority=" + priority +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", text='" + text + '\'' +
                '}';
    }
    public String getName() {
        return name;
    }

    public Priority getPriority() {
        return priority;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public String getText() {
        return text;
    }
}
