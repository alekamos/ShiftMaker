package model;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Task is a thing to do in calendar. Could be a single turn, could be a rest.
 * id is a UUID, name could be: REST,RIA_1 etc
 */
public class Task implements Comparable<Task>{

    private UUID id;
    private LocalDate day;
    private String name;
    private LocalDate startTime;
    private LocalDate endTime;
    private String type;


    public UUID getId() {
        return id;
    }

    public void generateId() {
        this.id = UUID.randomUUID();
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDate startTime) {
        this.startTime = startTime;
    }

    public LocalDate getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDate endTime) {
        this.endTime = endTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Task() {
        this.id = UUID.randomUUID();
    }

    @Override
    public int compareTo(Task ohterDay) {
        return this.day.compareTo(ohterDay.getDay());
    }
}
