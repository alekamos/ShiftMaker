package it.costanza.entityDb.h2;

import it.costanza.model.Run;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class PersonGroup{

    private Integer hit;
    @Id
    private String persona;

    @Basic
    @Column(name = "HIT", nullable = false)
    public Integer getHit() {
        return hit;
    }

    public void setHit(Integer hit) {
        this.hit = hit;
    }

    @Basic
    @Column(name = "PERSONA", nullable = false)
    public String getPersona() {
        return persona;
    }

    public void setPersona(String persona) {
        this.persona = persona;
    }


}
