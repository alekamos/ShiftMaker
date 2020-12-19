package it.costanza.model;

import service.DateService;

import java.util.Date;
import java.util.Objects;

public class Turno {


    private Date data;
    private String tipoTurno;
    private String ruoloTurno;
    private Persona personaInTurno;
    private boolean isFestivo;

    public Turno(Date data, String tipoTurno, String ruoloTurno) {
        this.data = data;
        this.tipoTurno = tipoTurno;
        this.ruoloTurno = ruoloTurno;
    }


    public Turno(Date data, String tipoTurno, String ruoloTurno, boolean isFestivo) {
        this.data = data;
        this.tipoTurno = tipoTurno;
        this.ruoloTurno = ruoloTurno;
        this.isFestivo = isFestivo;
    }

    public Turno(Date data, String tipoTurno, String ruoloTurno,Persona personaInTurno) {
        this.data = data;
        this.tipoTurno = tipoTurno;
        this.ruoloTurno = ruoloTurno;
        this.personaInTurno = personaInTurno;
    }

    public boolean isFestivo() {
        return isFestivo;
    }

    public void setFestivo(boolean festivo) {
        isFestivo = festivo;
    }

    public Turno() {

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

    @Override
    public String toString() {
        return "Turno{" +
                "data=" + data +
                ", tipoTurno='" + tipoTurno + '\'' +
                ", ruoloTurno='" + ruoloTurno + '\'' +
                ", personaInTurno=" + personaInTurno +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Turno)) return false;
        Turno turno = (Turno) o;
        return DateService.isSameDay(turno.getData(),getData()) &&
                Objects.equals(getTipoTurno(), turno.getTipoTurno()) &&
                Objects.equals(getRuoloTurno(), turno.getRuoloTurno());
    }


    @Override
    public int hashCode() {
        return Objects.hash(DateService.removeTime(getData()), getTipoTurno(), getRuoloTurno());
    }
}
