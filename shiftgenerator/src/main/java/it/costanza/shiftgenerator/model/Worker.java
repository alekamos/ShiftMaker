package it.costanza.shiftgenerator.model;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.*;

public class Worker {

    private UUID id;
    private PersonalData personalData;
    private String nickname;
    private Map<LocalDate,List<Slot>> personalCalendar;



    public void generateId() {
        this.id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }

    public PersonalData getPersonalData() {
        return personalData;
    }

    public void setPersonalData(PersonalData personalData) {
        this.personalData = personalData;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Map<LocalDate, List<Slot>> getPersonalCalendar() {
        return personalCalendar;
    }

    public void setPersonalCalendar(Map<LocalDate, List<Slot>> personalCalendar) {
        this.personalCalendar = personalCalendar;
    }

    Worker(String name, String surname, String mail, String nickname) {
        this.personalData = new PersonalData(name,surname,mail);
        this.nickname = nickname;
        this.personalCalendar = new TreeMap<>();
        generateId();
    }


    @Override
    public String toString() {
        return "Worker{" +
                "id=" + id +
                ", personalData=" + personalData +
                ", nickname='" + nickname + '\'' +
                ", personalCalendar=" + personalCalendar +
                '}';
    }

    public Worker() {
    }

    /**
     * Add a slot in personal calendar,if slot is null
     */
    public void addSlot(Slot slot){
        List<Slot> slotList = this.personalCalendar.get(slot.getDay());
        if(slotList!=null && slotList.size()>0)
            this.personalCalendar.get(slot.getDay()).add(slot);
        else {
            List<Slot> slotDayList = new ArrayList<>();
            if(slot!=null)
                slotDayList.add(slot);
            this.personalCalendar.put(slot.getDay(), slotDayList);
        }
    }


    public void initializeDayOfPersonalCalendar(LocalDate day){
        List<Slot> slotDayList = new ArrayList<>();
        this.personalCalendar.put(day, slotDayList);

    }


}
