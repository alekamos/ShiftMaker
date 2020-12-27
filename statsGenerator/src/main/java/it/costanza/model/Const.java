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


    public static final String PESO_TURNI = "PESO_TURNI";
    public static final String PESO_WE = "PESO_WE";
    public static final String PESO_GIORNO = "PESO_GIORNO";
    public static final String PESO_NOTTE = "PESO_NOTTE";
    public static final String PESO_PRES_FES = "PESO_PRES_FESTIVI";
    public static final String PESO_SD_FES = "PESO_SD_FESTIVI";
    public static final String PESO_SD_PRES_FER ="PESO_PRES_SET";
    public static final String SEZIONE_STAMPA = "###########################";
    public static final String SEZIONE_STAMPA_MAIN = "===========================";


    public static final String GENERATED = "CREATED";
    public static final String STATO_COMPLETE = "STAT_COMPLETE";
    public static final String PATHFILE = "pathFile";
    public static int CURRENT_ANNO = 0;
    public static int CURRENT_MESE = 0;
    public static double  K_TURNI = 1;
    public static double  K_NOTTE = 1;
    public static double  K_WE = 1;
    public static double  K_SD_FER = 1;




    static {
        try {
            CURRENT_ANNO = PropertiesServices.getAnno();
            CURRENT_MESE = PropertiesServices.getMese();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
