package it.costanza.shiftgenerator.model;

import java.util.List;

public class PersonalData {

    private String name;
    private String surname;
    private String email;


    public PersonalData(String name, String surname, String email) {
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "PersonalData{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email +
                '}';
    }
}
