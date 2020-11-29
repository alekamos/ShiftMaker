package it.costanza.model;





import java.io.IOException;
import it.costanza.service.PropertiesServices;


public class Const {

    public static final String NOTTE = "NOTTE";
    public static final String GIORNO = "GIORNO";


    public static final String RUOLO_REPARTO_1 = "REPARTO_1";
    public static final String RUOLO_REPARTO_2 = "REPARTO_2";
    public static final String RUOLO_URGENTISTA = "URGENTISTA_1";
    public static final String RUOLO_RICERCA = "RICERCA";


    public static final String PERSONE_ARRAY = "PERSONE_ARRAY";
    public static final String PREFIX_INDISPONIBILITA = "INDISPONIBILITA_";
    public static final String LIST_SEPARATOR = ";";


    public static final String PESO_TURNI = "PESO_TURNI";
    public static final String PESO_WE = "PESO_WE";
    public static final String PESO_GIORNO = "PESO_GIORNO";
    public static final String PESO_NOTTE = "PESO_NOTTE";
    public static final String PESO_PRES_FES = "PESO_PRES_FESTIVI";
    public static final String PESO_SD_FES = "PESO_SD_FESTIVI";
    public static final String PESO_SD_PRES_FER ="PESO_PRES_SET";
    public static final String SEZIONE_STAMPA = "###########################";
    public static final String SEZIONE_STAMPA_MAIN = "===========================";

    public static final String QUALITY_CHECK = "QUALITY_CHECK";
    public static final String QC_DIFF_PRESENZ_FERIALE= "QC_DIFF_PRESENZ_FERIALE";


    public static final String MIN_NOTTI = "QC_MIN_NOTTI";
    public static final String MAX_NOTTI = "QC_MAX_NOTTI";
    public static final String MAX_FERIALE = "QC_MAX_PRESENZ_FERIALE";
    public static final String ECCEZIONI_TURNI_NOTTE = "ECCEZIONE_NOTTE";


    public static final String MAKING = "MAKING";
    public static final String GENERATED = "CREATED";
    public static int CURRENT_ANNO = 0;
    public static int CURRENT_MESE = 0;

    static {
        try {
            CURRENT_ANNO = Integer.parseInt(PropertiesServices.getProperties("anno"));
            CURRENT_MESE = Integer.parseInt(PropertiesServices.getProperties("mese"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
