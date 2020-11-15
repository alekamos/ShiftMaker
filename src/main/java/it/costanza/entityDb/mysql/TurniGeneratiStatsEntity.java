package it.costanza.entityDb.mysql;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "TURNI_GENERATI_STATS", schema = "EUROPE", catalog = "")
public class TurniGeneratiStatsEntity {
    private Long idTurno;
    private Double mediaTurniTot;
    private Double sdevTurniTot;
    private Double mediaPresFest;
    private Double sdevPresFest;
    private Double mediaGiorniFest;
    private Double sdevGiorniFest;
    private Double mediaGiorniFer;
    private Double sdevGiorniFer;
    private Double mediaNotti;
    private Double sdevNotti;
    private Double sdev1Settimana;
    private Double sdev2Settimana;
    private Double sdev3Settimana;
    private Double sdev4Settimana;
    private Double sdev5Settimana;
    private Double score;
    private String scoreFormula;
    private Timestamp dataInserimento;
    private Timestamp dataAggiornamento;
    private TurniGeneratiMonitorEntity turniGeneratiMonitorByIdTurno;

    @Id
    @Column(name = "ID_TURNO", nullable = false)
    public Long getIdTurno() {
        return idTurno;
    }

    public void setIdTurno(Long idTurno) {
        this.idTurno = idTurno;
    }

    @Basic
    @Column(name = "MEDIA_TURNI_TOT", nullable = true, precision = 5)
    public Double getMediaTurniTot() {
        return mediaTurniTot;
    }

    public void setMediaTurniTot(Double mediaTurniTot) {
        this.mediaTurniTot = mediaTurniTot;
    }

    @Basic
    @Column(name = "SDEV_TURNI_TOT", nullable = true, precision = 5)
    public Double getSdevTurniTot() {
        return sdevTurniTot;
    }

    public void setSdevTurniTot(Double sdevTurniTot) {
        this.sdevTurniTot = sdevTurniTot;
    }

    @Basic
    @Column(name = "MEDIA_PRES_FEST", nullable = true, precision = 5)
    public Double getMediaPresFest() {
        return mediaPresFest;
    }

    public void setMediaPresFest(Double mediaPresFest) {
        this.mediaPresFest = mediaPresFest;
    }

    @Basic
    @Column(name = "SDEV_PRES_FEST", nullable = true, precision = 5)
    public Double getSdevPresFest() {
        return sdevPresFest;
    }

    public void setSdevPresFest(Double sdevPresFest) {
        this.sdevPresFest = sdevPresFest;
    }

    @Basic
    @Column(name = "MEDIA_GIORNI_FEST", nullable = true, precision = 5)
    public Double getMediaGiorniFest() {
        return mediaGiorniFest;
    }

    public void setMediaGiorniFest(Double mediaGiorniFest) {
        this.mediaGiorniFest = mediaGiorniFest;
    }

    @Basic
    @Column(name = "SDEV_GIORNI_FEST", nullable = true, precision = 5)
    public Double getSdevGiorniFest() {
        return sdevGiorniFest;
    }

    public void setSdevGiorniFest(Double sdevGiorniFest) {
        this.sdevGiorniFest = sdevGiorniFest;
    }

    @Basic
    @Column(name = "MEDIA_GIORNI_FER", nullable = true, precision = 5)
    public Double getMediaGiorniFer() {
        return mediaGiorniFer;
    }

    public void setMediaGiorniFer(Double mediaGiorniFer) {
        this.mediaGiorniFer = mediaGiorniFer;
    }

    @Basic
    @Column(name = "SDEV_GIORNI_FER", nullable = true, precision = 5)
    public Double getSdevGiorniFer() {
        return sdevGiorniFer;
    }

    public void setSdevGiorniFer(Double sdevGiorniFer) {
        this.sdevGiorniFer = sdevGiorniFer;
    }

    @Basic
    @Column(name = "MEDIA_NOTTI", nullable = true, precision = 5)
    public Double getMediaNotti() {
        return mediaNotti;
    }

    public void setMediaNotti(Double mediaNotti) {
        this.mediaNotti = mediaNotti;
    }

    @Basic
    @Column(name = "SDEV_NOTTI", nullable = true, precision = 5)
    public Double getSdevNotti() {
        return sdevNotti;
    }

    public void setSdevNotti(Double sdevNotti) {
        this.sdevNotti = sdevNotti;
    }

    @Basic
    @Column(name = "SDEV_1_SETTIMANA", nullable = true, precision = 5)
    public Double getSdev1Settimana() {
        return sdev1Settimana;
    }

    public void setSdev1Settimana(Double sdev1Settimana) {
        this.sdev1Settimana = sdev1Settimana;
    }

    @Basic
    @Column(name = "SDEV_2_SETTIMANA", nullable = true, precision = 5)
    public Double getSdev2Settimana() {
        return sdev2Settimana;
    }

    public void setSdev2Settimana(Double sdev2Settimana) {
        this.sdev2Settimana = sdev2Settimana;
    }

    @Basic
    @Column(name = "SDEV_3_SETTIMANA", nullable = true, precision = 5)
    public Double getSdev3Settimana() {
        return sdev3Settimana;
    }

    public void setSdev3Settimana(Double sdev3Settimana) {
        this.sdev3Settimana = sdev3Settimana;
    }

    @Basic
    @Column(name = "SDEV_4_SETTIMANA", nullable = true, precision = 5)
    public Double getSdev4Settimana() {
        return sdev4Settimana;
    }

    public void setSdev4Settimana(Double sdev4Settimana) {
        this.sdev4Settimana = sdev4Settimana;
    }

    @Basic
    @Column(name = "SDEV_5_SETTIMANA", nullable = true, precision = 5)
    public Double getSdev5Settimana() {
        return sdev5Settimana;
    }

    public void setSdev5Settimana(Double sdev5Settimana) {
        this.sdev5Settimana = sdev5Settimana;
    }

    @Basic
    @Column(name = "SCORE", nullable = true, precision = 5)
    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    @Basic
    @Column(name = "SCORE_FORMULA", nullable = true, length = 200)
    public String getScoreFormula() {
        return scoreFormula;
    }

    public void setScoreFormula(String scoreFormula) {
        this.scoreFormula = scoreFormula;
    }

    @Basic
    @Column(name = "DATA_INSERIMENTO", nullable = true)
    public Timestamp getDataInserimento() {
        return dataInserimento;
    }

    public void setDataInserimento(Timestamp dataInserimento) {
        this.dataInserimento = dataInserimento;
    }

    @Basic
    @Column(name = "DATA_AGGIORNAMENTO", nullable = true)
    public Timestamp getDataAggiornamento() {
        return dataAggiornamento;
    }

    public void setDataAggiornamento(Timestamp dataAggiornamento) {
        this.dataAggiornamento = dataAggiornamento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TurniGeneratiStatsEntity that = (TurniGeneratiStatsEntity) o;

        if (idTurno != null ? !idTurno.equals(that.idTurno) : that.idTurno != null) return false;
        if (mediaTurniTot != null ? !mediaTurniTot.equals(that.mediaTurniTot) : that.mediaTurniTot != null)
            return false;
        if (sdevTurniTot != null ? !sdevTurniTot.equals(that.sdevTurniTot) : that.sdevTurniTot != null) return false;
        if (mediaPresFest != null ? !mediaPresFest.equals(that.mediaPresFest) : that.mediaPresFest != null)
            return false;
        if (sdevPresFest != null ? !sdevPresFest.equals(that.sdevPresFest) : that.sdevPresFest != null) return false;
        if (mediaGiorniFest != null ? !mediaGiorniFest.equals(that.mediaGiorniFest) : that.mediaGiorniFest != null)
            return false;
        if (sdevGiorniFest != null ? !sdevGiorniFest.equals(that.sdevGiorniFest) : that.sdevGiorniFest != null)
            return false;
        if (mediaGiorniFer != null ? !mediaGiorniFer.equals(that.mediaGiorniFer) : that.mediaGiorniFer != null)
            return false;
        if (sdevGiorniFer != null ? !sdevGiorniFer.equals(that.sdevGiorniFer) : that.sdevGiorniFer != null)
            return false;
        if (mediaNotti != null ? !mediaNotti.equals(that.mediaNotti) : that.mediaNotti != null) return false;
        if (sdevNotti != null ? !sdevNotti.equals(that.sdevNotti) : that.sdevNotti != null) return false;
        if (sdev1Settimana != null ? !sdev1Settimana.equals(that.sdev1Settimana) : that.sdev1Settimana != null)
            return false;
        if (sdev2Settimana != null ? !sdev2Settimana.equals(that.sdev2Settimana) : that.sdev2Settimana != null)
            return false;
        if (sdev3Settimana != null ? !sdev3Settimana.equals(that.sdev3Settimana) : that.sdev3Settimana != null)
            return false;
        if (sdev4Settimana != null ? !sdev4Settimana.equals(that.sdev4Settimana) : that.sdev4Settimana != null)
            return false;
        if (sdev5Settimana != null ? !sdev5Settimana.equals(that.sdev5Settimana) : that.sdev5Settimana != null)
            return false;
        if (score != null ? !score.equals(that.score) : that.score != null) return false;
        if (scoreFormula != null ? !scoreFormula.equals(that.scoreFormula) : that.scoreFormula != null) return false;
        if (dataInserimento != null ? !dataInserimento.equals(that.dataInserimento) : that.dataInserimento != null)
            return false;
        if (dataAggiornamento != null ? !dataAggiornamento.equals(that.dataAggiornamento) : that.dataAggiornamento != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = idTurno != null ? idTurno.hashCode() : 0;
        result = 31 * result + (mediaTurniTot != null ? mediaTurniTot.hashCode() : 0);
        result = 31 * result + (sdevTurniTot != null ? sdevTurniTot.hashCode() : 0);
        result = 31 * result + (mediaPresFest != null ? mediaPresFest.hashCode() : 0);
        result = 31 * result + (sdevPresFest != null ? sdevPresFest.hashCode() : 0);
        result = 31 * result + (mediaGiorniFest != null ? mediaGiorniFest.hashCode() : 0);
        result = 31 * result + (sdevGiorniFest != null ? sdevGiorniFest.hashCode() : 0);
        result = 31 * result + (mediaGiorniFer != null ? mediaGiorniFer.hashCode() : 0);
        result = 31 * result + (sdevGiorniFer != null ? sdevGiorniFer.hashCode() : 0);
        result = 31 * result + (mediaNotti != null ? mediaNotti.hashCode() : 0);
        result = 31 * result + (sdevNotti != null ? sdevNotti.hashCode() : 0);
        result = 31 * result + (sdev1Settimana != null ? sdev1Settimana.hashCode() : 0);
        result = 31 * result + (sdev2Settimana != null ? sdev2Settimana.hashCode() : 0);
        result = 31 * result + (sdev3Settimana != null ? sdev3Settimana.hashCode() : 0);
        result = 31 * result + (sdev4Settimana != null ? sdev4Settimana.hashCode() : 0);
        result = 31 * result + (sdev5Settimana != null ? sdev5Settimana.hashCode() : 0);
        result = 31 * result + (score != null ? score.hashCode() : 0);
        result = 31 * result + (scoreFormula != null ? scoreFormula.hashCode() : 0);
        result = 31 * result + (dataInserimento != null ? dataInserimento.hashCode() : 0);
        result = 31 * result + (dataAggiornamento != null ? dataAggiornamento.hashCode() : 0);
        return result;
    }

    @OneToOne
    @JoinColumn(name = "ID_TURNO", referencedColumnName = "ID_TURNO", nullable = false)
    public TurniGeneratiMonitorEntity getTurniGeneratiMonitorByIdTurno() {
        return turniGeneratiMonitorByIdTurno;
    }

    public void setTurniGeneratiMonitorByIdTurno(TurniGeneratiMonitorEntity turniGeneratiMonitorByIdTurno) {
        this.turniGeneratiMonitorByIdTurno = turniGeneratiMonitorByIdTurno;
    }
}
