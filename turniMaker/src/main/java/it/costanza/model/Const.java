package it.costanza.model;



import service.DateService;
import service.PropertiesServices;
import service.TurniService;

import java.io.IOException;
import java.util.ArrayList;

import static service.PropertiesServices.getProperties;

public class Const {

    public static final String NOTTE = "NOTTE";
    public static final String GIORNO = "GIORNO";

    public static final String FESTIVO = "FESTIVO";
    public static final String FERIALE = "FERIALE";


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




    public static final String MAKING = "MAKING";
    public static final String GENERATED = "CREATED";


    public static int CURRENT_ANNO = 0;
    public static int CURRENT_MESE = 0;
    public static int NUMERO_TURNI_FESTIVI = 0;
    public static ArrayList<Turno> SKELETON_TURNI;
    public static ArrayList<Turno> TURNI_FESTIVI_WEEKEND;
    //cose da calcolare all'inizio
    static {
        try {
            CURRENT_ANNO = Integer.parseInt(PropertiesServices.getProperties("anno"));
            CURRENT_MESE = Integer.parseInt(PropertiesServices.getProperties("mese"));
            NUMERO_TURNI_FESTIVI = TurniService.getTurniWeekendMese().size();
            TURNI_FESTIVI_WEEKEND = TurniService.getTurniWeekendMese();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
