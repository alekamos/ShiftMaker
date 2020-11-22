package it.costanza.entityDb.h2;

import javax.persistence.*;

@Entity
@Table(name = "PERSONE")
public class Persona {


    private String personaTurno;

    @Id
    @Basic
    @Column(name = "PERSONA", nullable = false)
    public String getPersonaTurno() {
        return personaTurno;
    }

    public void setPersonaTurno(String personaTurno) {
        this.personaTurno = personaTurno;
    }
}
