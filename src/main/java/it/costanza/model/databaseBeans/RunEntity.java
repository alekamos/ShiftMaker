package it.costanza.model.databaseBeans;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "RUN", schema = "EUROPE", catalog = "")
public class RunEntity {
    private long idRun;
    private Timestamp dataInizioRun;
    private Timestamp dataFineRun;
    private String tipoRun;

    @Id
    @Column(name = "ID_RUN")
    public long getIdRun() {
        return idRun;
    }

    public void setIdRun(long idRun) {
        this.idRun = idRun;
    }

    @Basic
    @Column(name = "DATA_INIZIO_RUN")
    public Timestamp getDataInizioRun() {
        return dataInizioRun;
    }

    public void setDataInizioRun(Timestamp dataInizioRun) {
        this.dataInizioRun = dataInizioRun;
    }

    @Basic
    @Column(name = "DATA_FINE_RUN")
    public Timestamp getDataFineRun() {
        return dataFineRun;
    }

    public void setDataFineRun(Timestamp dataFineRun) {
        this.dataFineRun = dataFineRun;
    }

    @Basic
    @Column(name = "TIPO_RUN")
    public String getTipoRun() {
        return tipoRun;
    }

    public void setTipoRun(String tipoRun) {
        this.tipoRun = tipoRun;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RunEntity runEntity = (RunEntity) o;

        if (idRun != runEntity.idRun) return false;
        if (dataInizioRun != null ? !dataInizioRun.equals(runEntity.dataInizioRun) : runEntity.dataInizioRun != null)
            return false;
        if (dataFineRun != null ? !dataFineRun.equals(runEntity.dataFineRun) : runEntity.dataFineRun != null)
            return false;
        if (tipoRun != null ? !tipoRun.equals(runEntity.tipoRun) : runEntity.tipoRun != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (idRun ^ (idRun >>> 32));
        result = 31 * result + (dataInizioRun != null ? dataInizioRun.hashCode() : 0);
        result = 31 * result + (dataFineRun != null ? dataFineRun.hashCode() : 0);
        result = 31 * result + (tipoRun != null ? tipoRun.hashCode() : 0);
        return result;
    }
}
