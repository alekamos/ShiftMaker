package model;

import java.util.List;
import java.util.UUID;

public class Worker {

    private UUID id;
    private PersonalData personalData;
    private String nickname;
    private List<Task> personalCalendar;



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

    public List<Task> getPersonalCalendar() {
        return personalCalendar;
    }

    public void setPersonalCalendar(List<Task> personalCalendar) {
        this.personalCalendar = personalCalendar;
    }


    Worker(String name, String surname, String mail, String nickname) {
        this.personalData = new PersonalData(name,surname,mail);
        this.nickname = nickname;
        generateId();
    }

    public Worker() {
    }
}
