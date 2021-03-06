package it.costanza.entityDb.h2;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "TURNI_GENERATI")
public class TurniLocalEntity {

    private Long idTurno;
    private Date dataTurno;
    private String tipoTurno;
    private String ruoloTurno;
    private String personaTurno;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_TURNO", nullable = false)
    public Long getIdTurno() {
        return idTurno;
    }

    public void setIdTurno(Long idTurno) {
        this.idTurno = idTurno;
    }



    @Basic
    @Column(name = "DATA_TURNO", nullable = true)
    public Date getDataTurno() {
        return dataTurno;
    }

    public void setDataTurno(Date dataTurno) {
        this.dataTurno = dataTurno;
    }

    @Basic
    @Column(name = "TIPO_TURNO", nullable = true, length = 100)
    public String getTipoTurno() {
        return tipoTurno;
    }

    public void setTipoTurno(String tipoTurno) {
        this.tipoTurno = tipoTurno;
    }

    @Basic
    @Column(name = "RUOLO_TURNO", nullable = true, length = 100)
    public String getRuoloTurno() {
        return ruoloTurno;
    }

    public void setRuoloTurno(String ruoloTurno) {
        this.ruoloTurno = ruoloTurno;
    }

    @Basic
    @Column(name = "PERSONA_TURNO", nullable = true, length = 100)
    public String getPersonaTurno() {
        return personaTurno;
    }

    public void setPersonaTurno(String personaTurno) {
        this.personaTurno = personaTurno;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TurniLocalEntity that = (TurniLocalEntity) o;

        if (dataTurno != null ? !dataTurno.equals(that.dataTurno) : that.dataTurno != null) return false;
        if (tipoTurno != null ? !tipoTurno.equals(that.tipoTurno) : that.tipoTurno != null) return false;
        if (ruoloTurno != null ? !ruoloTurno.equals(that.ruoloTurno) : that.ruoloTurno != null) return false;
        if (personaTurno != null ? !personaTurno.equals(that.personaTurno) : that.personaTurno != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = dataTurno != null ? dataTurno.hashCode() : 0;
        result = 31 * result + (dataTurno != null ? dataTurno.hashCode() : 0);
        result = 31 * result + (tipoTurno != null ? tipoTurno.hashCode() : 0);
        result = 31 * result + (ruoloTurno != null ? ruoloTurno.hashCode() : 0);
        result = 31 * result + (personaTurno != null ? personaTurno.hashCode() : 0);
        return result;
    }


}
