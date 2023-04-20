package rs.raf.rafdnevnjak.models;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Objects;

public class Obligation implements Serializable {
    private String name;
    private Priority priority;
    private LocalTime startTime;
    private LocalTime endTime;
    private String text;

    public Obligation() {
        this.name = "";
        this.priority = Priority.LOW;
        this.text = "";
        this.startTime = this.endTime = LocalTime.now();
    }

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

    public void setName(String name) {
        this.name = name;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public void setText(String text) {
        this.text = text;
    }
}
