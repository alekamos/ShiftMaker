package it.costanza.entityDb.mysql;

import javax.persistence.*;

@Entity
@Table(name = "TURNI_GENERATI_MONITOR", schema = "EUROPE", catalog = "")
public class TurniGeneratiMonitorEntity {
    private Long idCalTurni;
    private String stato;
    private RunEntity runByIdRun;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CAL_TURNI", nullable = false)
    public Long getIdCalTurni() {
        return idCalTurni;
    }

    public void setIdCalTurni(Long idCalTurni) {
        this.idCalTurni = idCalTurni;
    }

    @Basic
    @Column(name = "STATO", nullable = true, length = 50)
    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TurniGeneratiMonitorEntity that = (TurniGeneratiMonitorEntity) o;

        if (idCalTurni != null ? !idCalTurni.equals(that.idCalTurni) : that.idCalTurni != null) return false;
        if (stato != null ? !stato.equals(that.stato) : that.stato != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = idCalTurni != null ? idCalTurni.hashCode() : 0;
        result = 31 * result + (stato != null ? stato.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "ID_RUN", referencedColumnName = "ID_RUN")
    public RunEntity getRunByIdRun() {
        return runByIdRun;
    }

    public void setRunByIdRun(RunEntity runByIdRun) {
        this.runByIdRun = runByIdRun;
    }
}
