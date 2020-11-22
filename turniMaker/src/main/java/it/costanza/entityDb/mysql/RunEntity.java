package it.costanza.entityDb.mysql;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "RUN", schema = "EUROPE", catalog = "")
public class RunEntity {
    private Long idRun;
    private String annomese;
    private Timestamp dataInizioRun;
    private Timestamp dataFineRun;
    private String tipoRun;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_RUN", nullable = false)
    public Long getIdRun() {
        return idRun;
    }

    public void setIdRun(Long idRun) {
        this.idRun = idRun;
    }

    @Basic
    @Column(name = "ANNOMESE", nullable = true, length = 6)
    public String getAnnomese() {
        return annomese;
    }

    public void setAnnomese(String annomese) {
        this.annomese = annomese;
    }

    @Basic
    @Column(name = "DATA_INIZIO_RUN", nullable = true)
    public Timestamp getDataInizioRun() {
        return dataInizioRun;
    }

    public void setDataInizioRun(Timestamp dataInizioRun) {
        this.dataInizioRun = dataInizioRun;
    }

    @Basic
    @Column(name = "DATA_FINE_RUN", nullable = true)
    public Timestamp getDataFineRun() {
        return dataFineRun;
    }

    public void setDataFineRun(Timestamp dataFineRun) {
        this.dataFineRun = dataFineRun;
    }

    @Basic
    @Column(name = "TIPO_RUN", nullable = true, length = 100)
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

        if (idRun != null ? !idRun.equals(runEntity.idRun) : runEntity.idRun != null) return false;
        if (annomese != null ? !annomese.equals(runEntity.annomese) : runEntity.annomese != null) return false;
        if (dataInizioRun != null ? !dataInizioRun.equals(runEntity.dataInizioRun) : runEntity.dataInizioRun != null)
            return false;
        if (dataFineRun != null ? !dataFineRun.equals(runEntity.dataFineRun) : runEntity.dataFineRun != null)
            return false;
        if (tipoRun != null ? !tipoRun.equals(runEntity.tipoRun) : runEntity.tipoRun != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = idRun != null ? idRun.hashCode() : 0;
        result = 31 * result + (annomese != null ? annomese.hashCode() : 0);
        result = 31 * result + (dataInizioRun != null ? dataInizioRun.hashCode() : 0);
        result = 31 * result + (dataFineRun != null ? dataFineRun.hashCode() : 0);
        result = 31 * result + (tipoRun != null ? tipoRun.hashCode() : 0);
        return result;
    }
}
