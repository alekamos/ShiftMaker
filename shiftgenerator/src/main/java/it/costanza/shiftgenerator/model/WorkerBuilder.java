package it.costanza.shiftgenerator.model;

public class WorkerBuilder {
    private String name;
    private String surname;
    private String mail;
    private String nickname;

    public WorkerBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public WorkerBuilder setSurname(String surname) {
        this.surname = surname;
        return this;
    }

    public WorkerBuilder setMail(String mail) {
        this.mail = mail;
        return this;
    }

    public WorkerBuilder setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public Worker createWorker() {
        return new Worker(name, surname, mail, nickname);
    }
}