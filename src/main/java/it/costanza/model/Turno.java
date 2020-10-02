package it.costanza.model;

import java.util.Date;

public class Turno {


    private Date data;
    private String tipoTurno;
    private String ruoloTurno;
    private Persona personaInTurno;

    public Turno(Date data, String tipoTurno, String ruoloTurno) {
        this.data = data;
        this.tipoTurno = tipoTurno;
        this.ruoloTurno = ruoloTurno;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getTipoTurno() {
        return tipoTurno;
    }

    public void setTipoTurno(String tipoTurno) {
        this.tipoTurno = tipoTurno;
    }

    public Persona getPersonaInTurno() {
        return personaInTurno;
    }

    public void setPersonaInTurno(Persona personaInTurno) {
        this.personaInTurno = personaInTurno;
    }

    public String getRuoloTurno() {
        return ruoloTurno;
    }

    public void setRuoloTurno(String ruoloTurno) {
        this.ruoloTurno = ruoloTurno;
    }
}
