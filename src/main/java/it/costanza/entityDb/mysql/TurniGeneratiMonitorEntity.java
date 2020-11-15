package it.costanza.entityDb.mysql;

import javax.persistence.*;

@Entity
@Table(name = "TURNI_GENERATI_MONITOR", schema = "EUROPE")
public class TurniGeneratiMonitorEntity {
    private Long idTurno;
    private String stato;
    private TurniGeneratiEntity turniGeneratiByIdTurno;
    private RunEntity runByIdRun;
    private TurniGeneratiStatsEntity turniGeneratiStatsByIdTurno;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_TURNO", nullable = false)
    public Long getIdTurno() {
        return idTurno;
    }

    public void setIdTurno(Long idTurno) {
        this.idTurno = idTurno;
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

        if (idTurno != null ? !idTurno.equals(that.idTurno) : that.idTurno != null) return false;
        if (stato != null ? !stato.equals(that.stato) : that.stato != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = idTurno != null ? idTurno.hashCode() : 0;
        result = 31 * result + (stato != null ? stato.hashCode() : 0);
        return result;
    }

    @OneToOne(mappedBy = "turniGeneratiMonitorByIdTurno")
    public TurniGeneratiEntity getTurniGeneratiByIdTurno() {
        return turniGeneratiByIdTurno;
    }

    public void setTurniGeneratiByIdTurno(TurniGeneratiEntity turniGeneratiByIdTurno) {
        this.turniGeneratiByIdTurno = turniGeneratiByIdTurno;
    }

    @ManyToOne
    @JoinColumn(name = "ID_RUN", referencedColumnName = "ID_RUN")
    public RunEntity getRunByIdRun() {
        return runByIdRun;
    }

    public void setRunByIdRun(RunEntity runByIdRun) {
        this.runByIdRun = runByIdRun;
    }

    @OneToOne(mappedBy = "turniGeneratiMonitorByIdTurno")
    public TurniGeneratiStatsEntity getTurniGeneratiStatsByIdTurno() {
        return turniGeneratiStatsByIdTurno;
    }

    public void setTurniGeneratiStatsByIdTurno(TurniGeneratiStatsEntity turniGeneratiStatsByIdTurno) {
        this.turniGeneratiStatsByIdTurno = turniGeneratiStatsByIdTurno;
    }
}
