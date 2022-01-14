package it.costanza.shiftgenerator.model;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Slot is a thing to do in calendar. Could be a single turn, could be a rest.
 * id is a UUID, name could be: REST,RIA_1 etc
 */
public class Slot implements Comparable<Slot>{

    private UUID id;
    private LocalDate day;
    private LocalDate startTime;
    private LocalDate endTime;
    private String name;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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



    public Slot() {
        this.id = UUID.randomUUID();
    }

    public Slot(LocalDate day, String name, String type) {
        this.day = day;
        this.name = name;
        this.type = type;
        this.id = UUID.randomUUID();
    }

    @Override
    public String toString() {
        return "Slot{" +
                "id=" + id +
                ", day=" + day +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    @Override
    public int compareTo(Slot ohterDay) {
        return this.day.compareTo(ohterDay.getDay());
    }
}
