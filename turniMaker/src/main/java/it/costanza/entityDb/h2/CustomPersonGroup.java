package it.costanza.entityDb.h2;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CustomPersonGroup {



    @Id
    private String persona;

    private Integer hit;
    private Integer total;

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

    @Basic
    @Column(name = "TOTAL", nullable = false)
    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
