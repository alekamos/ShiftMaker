package it.costanza.entityDb.h2;

import it.costanza.model.Run;

public class PersonGroup implements Comparable<PersonGroup>{

    private Integer hit;
    private String persona;

    public Integer getHit() {
        return hit;
    }

    public void setHit(Integer hit) {
        this.hit = hit;
    }

    public String getPersona() {
        return persona;
    }

    public void setPersona(String persona) {
        this.persona = persona;
    }

    @Override
    public int compareTo(PersonGroup o) {

        return hit.compareTo(o.getHit());
    }
}
