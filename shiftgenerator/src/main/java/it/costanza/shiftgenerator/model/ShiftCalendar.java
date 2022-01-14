package it.costanza.shiftgenerator.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ShiftCalendar {

    private UUID id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private Map<LocalDate, List<Shift>> shiftPlan;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Map<LocalDate, List<Shift>> getShiftPlan() {
        return shiftPlan;
    }

    public void setShiftPlan(Map<LocalDate, List<Shift>> shiftPlan) {
        this.shiftPlan = shiftPlan;
    }

    public ShiftCalendar() {
        this.id = UUID.randomUUID();
    }

    @Override
    public String toString() {
        return "ShiftCalendar{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", shiftPlan=" + shiftPlan.size() +
                '}';
    }
}
