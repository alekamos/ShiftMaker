package service;

import it.costanza.dao.TurniGeneratiDao;
import it.costanza.model.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TurniService {







    /**
     * Mi controlla che un candidato stia dentro i limiti imposti
     *
     * @param turniMese
     * @return
     */
    public boolean candidatoQualityCheck(ArrayList<Turno> turniMese, Persona candidato, Turno turnoDaAssegnare) throws IOException {


        int anno = Integer.parseInt(PropertiesServices.getProperties("anno"));
        int mese = Integer.parseInt(PropertiesServices.getProperties("mese"));

        ArrayList<Date> we1 = DateService.getNEsimaSettimanaMensileFeriale(anno, mese, 1);
        ArrayList<Date> we2 = DateService.getNEsimaSettimanaMensileFeriale(anno, mese, 2);
        ArrayList<Date> we3 = DateService.getNEsimaSettimanaMensileFeriale(anno, mese, 3);
        ArrayList<Date> we4 = DateService.getNEsimaSettimanaMensileFeriale(anno, mese, 4);
        ArrayList<Date> we5 = DateService.getNEsimaSettimanaMensileFeriale(anno, mese, 5);
        ArrayList<Date> we6 = DateService.getNEsimaSettimanaMensileFeriale(anno, mese, 6);

        int[] presenzaFeriale = new int[6];


        for (Turno turno : turniMese) {


            //è il primo turno del mese
            if(turno.getPersonaInTurno()==null)
                return true;

            boolean personaInTurnoSameAsPersonaElem = turno.getPersonaInTurno().getNome().equals(candidato.getNome());

            //Controlli sui turni in settimana, ovvero tutte le date feriali ma non i turni di venerdì notte
            if (we1 != null && isTurnoInSettimanaLavorativa(turno, we1) && personaInTurnoSameAsPersonaElem)
                presenzaFeriale[0]++;

            if (we2 != null && isTurnoInSettimanaLavorativa(turno, we2) && personaInTurnoSameAsPersonaElem)
                presenzaFeriale[1]++;

            if (we3 != null && isTurnoInSettimanaLavorativa(turno, we3) && personaInTurnoSameAsPersonaElem)
                presenzaFeriale[2]++;

            if (we4 != null && isTurnoInSettimanaLavorativa(turno, we4) && personaInTurnoSameAsPersonaElem)
                presenzaFeriale[3]++;

            if (we5 != null && isTurnoInSettimanaLavorativa(turno, we5)  && personaInTurnoSameAsPersonaElem)
                presenzaFeriale[4]++;

            if (we6 != null && isTurnoInSettimanaLavorativa(turno, we6)  && personaInTurnoSameAsPersonaElem)
                presenzaFeriale[5]++;

        }



        int indicePresFeriale = DateService.getNumeroSettimanaFeriale(turnoDaAssegnare.getData())-1;
        if (presenzaFeriale[indicePresFeriale] >= Integer.parseInt(PropertiesServices.getProperties(Const.MAX_FERIALE)))
            return false;







        return true;
    }




    public Persona copyTurnoAssegnato(ArrayList<Turno> turniAssegnati, Date data, String tipoTurno, String ruoloTurno) {

        ArrayList<Turno> turnoDelGiorno = getTurniDelGiorno(turniAssegnati, data, tipoTurno, ruoloTurno);

        return turnoDelGiorno.get(0).getPersonaInTurno();

    }


    /**
     * Genera un nuovo array di persone con le statistiche
     * @param turniFinale
     * @param persone
     * @return
     * @throws IOException
     */
    public ArrayList<Persona> generaPersoneConStatistiche(ArrayList<Turno> turniFinale, ArrayList<Persona> persone) throws IOException {



        ArrayList<Persona> personeOut = new ArrayList<>();

        int anno = Integer.parseInt(PropertiesServices.getProperties("anno"));
        int mese = Integer.parseInt(PropertiesServices.getProperties("mese"));

        ArrayList<Date> we1 = DateService.getNEsimaSettimanaMensileFeriale(anno, mese, 1);
        ArrayList<Date> we2 = DateService.getNEsimaSettimanaMensileFeriale(anno, mese, 2);
        ArrayList<Date> we3 = DateService.getNEsimaSettimanaMensileFeriale(anno, mese, 3);
        ArrayList<Date> we4 = DateService.getNEsimaSettimanaMensileFeriale(anno, mese, 4);
        ArrayList<Date> we5 = DateService.getNEsimaSettimanaMensileFeriale(anno, mese, 5);

        ArrayList<Date> wend1 = DateService.getNEsimaSettimanaMensileFestiva(anno, mese, 1);
        ArrayList<Date> wend2 = DateService.getNEsimaSettimanaMensileFestiva(anno, mese, 2);
        ArrayList<Date> wend3 = DateService.getNEsimaSettimanaMensileFestiva(anno, mese, 3);
        ArrayList<Date> wend4 = DateService.getNEsimaSettimanaMensileFestiva(anno, mese, 4);
        ArrayList<Date> wend5 = DateService.getNEsimaSettimanaMensileFestiva(anno, mese, 5);
        ArrayList<Date> wend6 = DateService.getNEsimaSettimanaMensileFestiva(anno, mese, 6);
        long t1 = System.currentTimeMillis();

        for (int i = 0; i < persone.size(); i++) {

            persone.get(i);
            int numeroTurni = 0;
            int numeroTurniGiorno = 0;
            int numeroTurniNotte = 0;
            int numeroTurniWe = 0;
            int counterPresenzaWe = 0;
            int[] presenzaFeriale = new int[5];
            int[] presenzaFestiva = new int[6];

            for (Turno turno : turniFinale) {
                boolean personaInTurnoSameAsPersonaElem = turno.getPersonaInTurno().getNome().equals(persone.get(i).getNome());

                //controllo numero turni
                if (personaInTurnoSameAsPersonaElem)
                    numeroTurni++;

                //controllo numero turni giorni
                if (turno.getTipoTurno().equals(Const.GIORNO))
                    if (personaInTurnoSameAsPersonaElem)
                        numeroTurniGiorno++;

                //controllo numero turni notte
                if (turno.getTipoTurno().equals(Const.NOTTE))
                    if (personaInTurnoSameAsPersonaElem)
                        numeroTurniNotte++;

                //controllo numero turni weekend
                if (DateService.isWeekendDate(turno.getData()))
                    if (personaInTurnoSameAsPersonaElem)
                        numeroTurniWe++;


                //Controlli sui turni in settimana, ovvero tutte le date feriali ma non i turni di venerdì notte
                if(we1!=null && isTurnoInSettimanaLavorativa(turno,we1) && personaInTurnoSameAsPersonaElem)
                    presenzaFeriale[0]++;

                if(we2!=null && isTurnoInSettimanaLavorativa(turno,we2) && personaInTurnoSameAsPersonaElem)
                    presenzaFeriale[1]++;

                if(we3!=null && isTurnoInSettimanaLavorativa(turno,we3) && personaInTurnoSameAsPersonaElem)
                    presenzaFeriale[2]++;

                if(we4!=null && isTurnoInSettimanaLavorativa(turno,we4) && personaInTurnoSameAsPersonaElem)
                    presenzaFeriale[3]++;

                if(we5!=null && isTurnoInSettimanaLavorativa(turno,we5) && personaInTurnoSameAsPersonaElem)
                    presenzaFeriale[4]++;



                //Controlli sui turni nel weekend
                if(wend1!=null && isTurnoInSettimanaLavorativa(turno,wend1) && personaInTurnoSameAsPersonaElem)
                    presenzaFestiva[0]++;

                if(wend2!=null && isTurnoInSettimanaLavorativa(turno,wend2) && personaInTurnoSameAsPersonaElem)
                    presenzaFestiva[1]++;

                if(wend3!=null && isTurnoInSettimanaLavorativa(turno,wend3) && personaInTurnoSameAsPersonaElem)
                    presenzaFestiva[2]++;

                if(wend4!=null && isTurnoInSettimanaLavorativa(turno,wend4) && personaInTurnoSameAsPersonaElem)
                    presenzaFestiva[3]++;

                if(wend5!=null && isTurnoInSettimanaLavorativa(turno,wend5) && personaInTurnoSameAsPersonaElem)
                    presenzaFestiva[4]++;

                if(wend6!=null && isTurnoInSettimanaLavorativa(turno,wend6) && personaInTurnoSameAsPersonaElem)
                    presenzaFestiva[5]++;


            }

            //a questo devo sommare le settimane di ricerca

            personeOut.add(new Persona(persone.get(i).getNome()));
            personeOut.get(i).setNumeroTurni(numeroTurni);
            personeOut.get(i).setNumeroTurniWe(numeroTurniWe);
            personeOut.get(i).setNumeroTurniGiorno(numeroTurniGiorno);
            personeOut.get(i).setNumeroTurniNotte(numeroTurniNotte);
            personeOut.get(i).setPresenzaFeriale(presenzaFeriale);


            for (int j = 0; j < presenzaFestiva.length; j++) {
                if (presenzaFestiva[j]>0)
                    counterPresenzaWe++;

            }
            personeOut.get(i).setPresenzaFestiva(counterPresenzaWe);

        }


        return personeOut;
    }


    /**
     * Controlla se il turno è feriale (non festivo) weekend nei giorni indicati e passati come parametro
     * in pratica controlla se è turni in settimana, ovvero tutte le date feriali ma non i turni di venerdì notte
     * @param turno
     * @param dateSettimana
     * @return
     */
    private boolean isTurnoInSettimanaLavorativa(Turno turno, ArrayList<Date> dateSettimana){

        //se il turno è un venerdì notte è non è da contare
        Calendar cal = Calendar.getInstance();
        cal.setTime(turno.getData());
        if(cal.get(Calendar.DAY_OF_WEEK)==Calendar.FRIDAY && turno.getTipoTurno().equals(Const.NOTTE))
            return false;

        for (Date giorniIndicati : dateSettimana) {
            if(DateService.isSameDay(giorniIndicati,turno.getData()))
                return true;
        }


        return false;





    }




    /**
     * Controlla che il giorno prima non abbia fatto notte
     *
     * @param candidatoTurno
     * @param turno
     * @param turniMese
     * @return
     */
    public boolean checkFattibilitaTurno(Persona candidatoTurno, Turno turno, ArrayList<Turno> turniMese) {

        Date giornoPrima = DateService.aumentaTogliGiorno(turno.getData(), -1);
        ArrayList<Turno> listaTurniDellaGiornataPrecedenteDiurnoONotturno = null;
        boolean turnoLiberoIlGiornoPrima = true;

        //Se giorno controllo che il gg prima non abbia fatto notte
        if (turno.getTipoTurno().equals(Const.GIORNO)) {
            listaTurniDellaGiornataPrecedenteDiurnoONotturno = getTurniDelGiorno(turniMese, giornoPrima, Const.NOTTE);
        }

        if (turno.getTipoTurno().equals(Const.NOTTE)) {
            listaTurniDellaGiornataPrecedenteDiurnoONotturno = getTurniDelGiorno(turniMese, giornoPrima, Const.NOTTE);
        }

        for (Turno turnoDelloStessoGiorno : listaTurniDellaGiornataPrecedenteDiurnoONotturno) {
            if (turnoDelloStessoGiorno.getPersonaInTurno() != null)
                if (turnoDelloStessoGiorno.getPersonaInTurno().getNome().equals(candidatoTurno.getNome())) {
                    turnoLiberoIlGiornoPrima = false;
                }
        }


        return turnoLiberoIlGiornoPrima;
    }

    private boolean checkFattibilitaTurnoDebug(Persona candidatoTurno, Turno turno, ArrayList<Turno> turniMese) {

        Date giornoPrima = DateService.aumentaTogliGiorno(turno.getData(), -1);
        ArrayList<Turno> listaTurniDellaGiornataPrecedenteDiurnoONotturno = null;
        boolean turnoLiberoIlGiornoPrima = true;

        //Se giorno controllo che il gg prima non abbia fatto notte
        if (turno.getTipoTurno().equals(Const.GIORNO)) {
            listaTurniDellaGiornataPrecedenteDiurnoONotturno = getTurniDelGiorno(turniMese, giornoPrima, Const.NOTTE);
        }

        if (turno.getTipoTurno().equals(Const.NOTTE)) {
            listaTurniDellaGiornataPrecedenteDiurnoONotturno = getTurniDelGiorno(turniMese, giornoPrima, Const.NOTTE);
        }

        for (Turno turnoDelloStessoGiorno : listaTurniDellaGiornataPrecedenteDiurnoONotturno) {
            if (turnoDelloStessoGiorno.getPersonaInTurno() != null)
                if (turnoDelloStessoGiorno.getPersonaInTurno().getNome().equals(candidatoTurno.getNome())) {
                    turnoLiberoIlGiornoPrima = false;
                }
        }


        return turnoLiberoIlGiornoPrima;
    }


    /**
     * Controlla che il giorno prima non abbia fatto notte
     *
     * @param candidatoTurno
     * @param turno
     * @param turniMese
     * @return
     */
    public boolean checkFattibilitaTurnoSuccessivo(Persona candidatoTurno, Turno turno, ArrayList<Turno> turniMese, ArrayList<Turno> turniAssegnati) {

        Date gioroDopo = DateService.aumentaTogliGiorno(turno.getData(), 1);
        ArrayList<Turno> listaTurniDellaGiornataSuccessivaGiorno = null;
        ArrayList<Turno> listaTurniDellaGiornataSuccessivaNotte = null;
        ArrayList<Turno> listaTurniDellaGiornata = new ArrayList<>();
        boolean turnoLiberoIlGiornoDopo = true;


        if (turno.getTipoTurno().equals(Const.NOTTE)) {
            listaTurniDellaGiornataSuccessivaGiorno = getTurniDelGiorno(turniAssegnati, gioroDopo, Const.GIORNO);
            listaTurniDellaGiornataSuccessivaNotte = getTurniDelGiorno(turniAssegnati, gioroDopo, Const.NOTTE);
        }

        //faccio merge
        if (listaTurniDellaGiornataSuccessivaGiorno != null)
            listaTurniDellaGiornata.addAll(listaTurniDellaGiornataSuccessivaGiorno);
        if (listaTurniDellaGiornataSuccessivaNotte != null)
            listaTurniDellaGiornata.addAll(listaTurniDellaGiornataSuccessivaNotte);

        for (Turno turnoDelGiornoDopo : listaTurniDellaGiornata) {
            if (turnoDelGiornoDopo.getPersonaInTurno() != null)
                if (turnoDelGiornoDopo.getPersonaInTurno().getNome().equals(candidatoTurno.getNome())) {
                    turnoLiberoIlGiornoDopo = false;
                }
        }
        return turnoLiberoIlGiornoDopo;
    }


    /**
     * Qui si controlla che:
     * Lo stesso giorno non hai già fatto un turno, così se fai notte non puoi fare giorno o sei fai giorno non sei sia urgentista che reparto
     *
     * @param candidatoTurno
     * @param turno
     * @param turniMese
     * @return
     */
    public boolean checkIsNotGiaInTurno(Persona candidatoTurno, Turno turno, ArrayList<Turno> turniMese) {

        ArrayList<Turno> listaTurniDellaGiornata = getTurniDelGiorno(turniMese, turno.getData());
        boolean nonInTurno = true;

        for (Turno turnoDelloStessoGiorno : listaTurniDellaGiornata) {
            if (turnoDelloStessoGiorno.getPersonaInTurno() != null)
                if (turnoDelloStessoGiorno.getPersonaInTurno().getNome().equals(candidatoTurno.getNome()))
                    nonInTurno = false;
        }


        return nonInTurno;
    }


    /**
     * Qui si controlla che:
     * Lo stesso giorno non hai già fatto un turno, così se fai notte non puoi fare giorno o sei fai giorno non sei sia urgentista che reparto
     *
     * @param candidatoTurno
     * @param turno
     * @param turniAssegnati
     * @return
     */
    public boolean checkIsNotGiaInTurnoTraIPrenotati(Persona candidatoTurno, Turno turno, ArrayList<Turno> turniAssegnati) {

        ArrayList<Turno> listaTurniDellaGiornata = getTurniDelGiorno(turniAssegnati, turno.getData());
        boolean nonInTurno = true;

        for (Turno turnoDelloStessoGiorno : listaTurniDellaGiornata) {
            if (turnoDelloStessoGiorno.getPersonaInTurno() != null)
                if (turnoDelloStessoGiorno.getPersonaInTurno().getNome().equals(candidatoTurno.getNome()))
                    nonInTurno = false;
        }


        return nonInTurno;
    }


    /**
     * Mi fa la lista di tutti i turni del giorno giorno o notte non importa
     *
     * @param giorno
     * @return
     */
    private ArrayList<Turno> getTurniDelGiorno(ArrayList<Turno> turniMese, Date giorno) {

        //prendo la lista dei turni giorno o notte del turno che si sta facendo
        ArrayList<Turno> turniStessoGiorno = new ArrayList<>();
        //per tutti i turni del mese
        for (Turno turno : turniMese) {
            //se tutti i turni dello stesso giorno, e dello stesso tipo.. es: il 12 giorno
            if (DateService.isSameDay(turno.getData(), giorno)) {
                turniStessoGiorno.add(turno);

            }
        }
        return turniStessoGiorno;
    }

    /**
     * Mi fa la lista di tutti i turni del giorno specificando giorno o notte
     *
     * @param giorno
     * @return
     */
    private ArrayList<Turno> getTurniDelGiorno(ArrayList<Turno> turniMese, Date giorno, String tipoTurno) {

        //prendo la lista dei turni giorno o notte del turno che si sta facendo
        ArrayList<Turno> turniStessoGiorno = new ArrayList<>();
        //per tutti i turni del mese
        for (Turno turno : turniMese) {
            //se tutti i turni dello stesso giorno, e dello stesso tipo.. es: il 12 giorno
            if (DateService.isSameDay(turno.getData(), giorno) && turno.getTipoTurno().equals(tipoTurno)) {
                turniStessoGiorno.add(turno);

            }
        }
        return turniStessoGiorno;
    }

    /**
     * Mi fa la lista di tutti i turni del giorno specificando giorno o notte e ruolo
     *
     * @param giorno
     * @return
     */
    private ArrayList<Turno> getTurniDelGiorno(ArrayList<Turno> turniMese, Date giorno, String tipoTurno, String tipoRuolo) {

        //prendo la lista dei turni giorno o notte del turno che si sta facendo
        ArrayList<Turno> turniStessoGiorno = new ArrayList<>();
        //per tutti i turni del mese
        for (Turno turno : turniMese) {
            //se tutti i turni dello stesso giorno, e dello stesso tipo.. es: il 12 giorno
            if (DateService.isSameDay(turno.getData(), giorno) && turno.getTipoTurno().equals(tipoTurno) && turno.getRuoloTurno().equals(tipoRuolo)) {
                turniStessoGiorno.add(turno);

            }
        }
        return turniStessoGiorno;
    }


    /**
     * Controlla che la persona sia disponibile nel turno che dovrebbe fare
     *
     * @param randomPersona il candidato
     * @param turno         il turno che dovrebbe fare
     * @return
     */
    public boolean checkDisponibilita(Persona randomPersona, Turno turno) {


        boolean result;
        ArrayList<Turno> turniIndisponibilita = randomPersona.getIndisponibilitaList();

        if (turniIndisponibilita != null && turniIndisponibilita.contains(turno))
            result = false;
        else
            result = true;


        return result;


    }








    /**
     * Carica il pattern dei turni del mese
     *
     * @return
     */
    public static ArrayList<Turno> caricaPatternTurniMese() throws IOException {

        ArrayList<Turno> turni = new ArrayList<>();

        Calendar cal = Calendar.getInstance();

        int anno = Integer.parseInt(PropertiesServices.getProperties("anno"));
        int mese = Integer.parseInt(PropertiesServices.getProperties("mese"));

        ArrayList<Date> datesOfMonth = DateService.getDatesOfMonth(anno, mese);
        for (Date data : datesOfMonth) {
            cal.setTime(data);

            //Se il turno non è del weekend ci vuole anche quello di ricerca
            boolean weekendDate = DateService.isWeekendDate(data);
            if (!weekendDate) {
                turni.add(new Turno(data, Const.GIORNO, Const.RUOLO_RICERCA));
                turni.add(new Turno(data, Const.GIORNO, Const.RUOLO_URGENTISTA));
            }


            turni.add(new Turno(data, Const.GIORNO, Const.RUOLO_REPARTO_1));
            turni.add(new Turno(data, Const.GIORNO, Const.RUOLO_REPARTO_2));


            turni.add(new Turno(data, Const.NOTTE, Const.RUOLO_REPARTO_1));
            turni.add(new Turno(data, Const.NOTTE, Const.RUOLO_REPARTO_2));
        }

        return turni;
    }


    public Persona getRandomPersona(List<Persona> list) {


        return list.get(RandomSingleton.getInstance().nextInt(list.size()));


    }


    /**
     * Se ad esempio stiamo facendo un turno del giorno x giorno reparto 1 ed è già assegnato allo torna false, true se il turno è libero
     *
     * @param turniPreAssegnati
     * @param candidatoTurno
     * @return
     */
    public boolean checkTurnoLiberoTurnoAssegnato(ArrayList<Turno> turniPreAssegnati, Turno candidatoTurno) {


        ArrayList<Turno> turniDelGiorno = getTurniDelGiorno(turniPreAssegnati, candidatoTurno.getData(), candidatoTurno.getTipoTurno(), candidatoTurno.getRuoloTurno());
        if (turniDelGiorno.size() > 0) {
            return false;
        } else {
            return true;
        }


    }


    /**
     * Carica le persone dal file di properties con le loro indisponibilita
     *
     * @return
     * @throws IOException
     */
    public ArrayList<Persona> caricaPersone() throws IOException {


        FileInputStream file = new FileInputStream("turniMaker/src/main/resources/dati.xlsx");


        Workbook workbook = new XSSFWorkbook(file);
        Sheet persone = workbook.getSheetAt(0);
        Iterator<Row> iterator = persone.iterator();

        ArrayList<Persona> personeList = new ArrayList<>();

        //ciclo tutte le righe
        while (iterator.hasNext()) {
            Row nextRow = iterator.next();

            //considero solo le righe maggiori di 1 perchè la 0 sono le date
            if (nextRow.getRowNum() >= 1) {
                Iterator<Cell> cellIterator = nextRow.cellIterator();
                //qui devo iterare
                Persona rigaPersona = new Persona();
                ArrayList<Turno> indispPersona = new ArrayList<>();
                //ciclo tutte le celle, le colonne
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();

                    if (cell.getColumnIndex() == 0)
                        rigaPersona.setNome(cell.getStringCellValue());

                    if ("G".equals(cell.getStringCellValue())) {
                        Turno indispElem = new Turno();
                        //Vado a prendere la rispettiva cella della prima riga
                        indispElem.setData(persone.getRow(0).getCell(cell.getColumnIndex()).getDateCellValue());
                        indispElem.setTipoTurno(Const.GIORNO);
                        indispPersona.add(indispElem);
                    }

                    if ("N".equals(cell.getStringCellValue())) {
                        Turno indispElem = new Turno();
                        //Vado a prendere la rispettiva cella della prima riga
                        indispElem.setData(persone.getRow(0).getCell(cell.getColumnIndex()).getDateCellValue());
                        indispElem.setTipoTurno(Const.NOTTE);
                        indispPersona.add(indispElem);
                    }

                    if ("GN".equals(cell.getStringCellValue()) || "NG".equals(cell.getStringCellValue())) {
                        Turno indispElem = new Turno();
                        //Vado a prendere la rispettiva cella della prima riga
                        indispElem.setData(persone.getRow(0).getCell(cell.getColumnIndex()).getDateCellValue());
                        indispElem.setTipoTurno(Const.GIORNO);
                        indispPersona.add(indispElem);

                        Turno indispElemNotte = new Turno();
                        //Vado a prendere la rispettiva cella della prima riga
                        indispElemNotte.setData(persone.getRow(0).getCell(cell.getColumnIndex()).getDateCellValue());
                        indispElemNotte.setTipoTurno(Const.NOTTE);
                        indispPersona.add(indispElemNotte);

                    }


                }
                rigaPersona.setIndisponibilitaList(indispPersona);
                personeList.add(rigaPersona);
            }
        }

        return personeList;
    }

    /**
     * Carica i turni già schedulati dal file excel
     * @return
     * @throws IOException
     */
    public ArrayList<Turno> caricaTurniSchedulati() throws IOException {

        ArrayList<Turno> turniSchedulatiOut = new ArrayList<>();

        FileInputStream file = new FileInputStream("turniMaker/src/main/resources/dati.xlsx");




        Workbook workbook = new XSSFWorkbook(file);
        Sheet turniAssegnati = workbook.getSheetAt(1);
        Iterator<Row> turniAssIter = turniAssegnati.iterator();

        while (turniAssIter.hasNext()) {

            Row nextRow = turniAssIter.next();
            //da qui iniziano i dati
            if(nextRow.getRowNum()>=2) {
                //qui vado secco sulla posizione
                int i = 1;
                //GIORNO RICERCA
                if(nextRow.getCell(i)!=null && !"".equals(nextRow.getCell(i).getStringCellValue())) {
                    Turno turno = new Turno();
                    turno.setTipoTurno(Const.GIORNO);
                    turno.setRuoloTurno(Const.RUOLO_RICERCA);
                    turno.setData(nextRow.getCell(0).getDateCellValue());
                    turno.setPersonaInTurno(new Persona(nextRow.getCell(i).getStringCellValue()));
                    turniSchedulatiOut.add(turno);
                }
                i++;
                //GIORNO reparto 1
                if(nextRow.getCell(i)!=null && !"".equals(nextRow.getCell(i).getStringCellValue())) {
                    Turno turno = new Turno();
                    turno.setTipoTurno(Const.GIORNO);
                    turno.setRuoloTurno(Const.RUOLO_REPARTO_1);
                    turno.setData(nextRow.getCell(0).getDateCellValue());
                    turno.setPersonaInTurno(new Persona(nextRow.getCell(i).getStringCellValue()));
                    turniSchedulatiOut.add(turno);
                }
                i++;
                //GIORNO reparto 1
                if(nextRow.getCell(i)!=null && !"".equals(nextRow.getCell(i).getStringCellValue())) {
                    Turno turno = new Turno();
                    turno.setTipoTurno(Const.GIORNO);
                    turno.setRuoloTurno(Const.RUOLO_REPARTO_2);
                    turno.setData(nextRow.getCell(0).getDateCellValue());
                    turno.setPersonaInTurno(new Persona(nextRow.getCell(i).getStringCellValue()));
                    turniSchedulatiOut.add(turno);
                }
                i++;
                //GIORNO urgentista
                if(nextRow.getCell(i)!=null && !"".equals(nextRow.getCell(i).getStringCellValue())) {
                    Turno turno = new Turno();
                    turno.setTipoTurno(Const.GIORNO);
                    turno.setRuoloTurno(Const.RUOLO_URGENTISTA);
                    turno.setData(nextRow.getCell(0).getDateCellValue());
                    turno.setPersonaInTurno(new Persona(nextRow.getCell(i).getStringCellValue()));
                    turniSchedulatiOut.add(turno);
                }
                i++;
                //NOTTE reparto 1
                if(nextRow.getCell(i)!=null && !"".equals(nextRow.getCell(i).getStringCellValue())) {
                    Turno turno = new Turno();
                    turno.setTipoTurno(Const.NOTTE);
                    turno.setRuoloTurno(Const.RUOLO_REPARTO_1);
                    turno.setData(nextRow.getCell(0).getDateCellValue());
                    turno.setPersonaInTurno(new Persona(nextRow.getCell(i).getStringCellValue()));
                    turniSchedulatiOut.add(turno);
                }

                i++;
                //NOTTE reparto 1
                if(nextRow.getCell(i)!=null && !"".equals(nextRow.getCell(i).getStringCellValue())) {
                    Turno turno = new Turno();
                    turno.setTipoTurno(Const.NOTTE);
                    turno.setRuoloTurno(Const.RUOLO_REPARTO_2);
                    turno.setData(nextRow.getCell(0).getDateCellValue());
                    turno.setPersonaInTurno(new Persona(nextRow.getCell(i).getStringCellValue()));
                    turniSchedulatiOut.add(turno);
                }







            }
        }

        workbook.close();
        file.close();

        return turniSchedulatiOut;

    }




    /**
     * Mi cerca un turno specifico dentro l'array che passo
     * @param turni
     * @param data
     * @param tipoTurno
     * @param ruolo
     * @return
     */
    public Turno getTurnoSpecificoFromList(ArrayList<Turno> turni,Date data,String tipoTurno, String ruolo){

        for (Turno turno : turni) {
            if(turno.equals(new Turno(data,tipoTurno,ruolo)))
                return turno;

        }
        return null;
    }


    /**
     * Mi controlla se il turno è un feriale o un turno festivo (sab dom o venerdì notte)
     * @param turno
     * @return
     */
    public static String isTurnoFerialeFestivo(Turno turno){
        Date date = DateService.removeTime(turno.getData());
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        if(cal.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY ||  cal.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK)==Calendar.FRIDAY && turno.getTipoTurno().equals("NOTTE"))
            return Const.FESTIVO;
        else
            return Const.FERIALE;
    }

    public Turno deepCopyTurno(Turno turno){
        Turno trn = new Turno();
        trn.setPersonaInTurno(turno.getPersonaInTurno());
        trn.setData(turno.getData());
        trn.setTipoTurno(turno.getTipoTurno());
        trn.setRuoloTurno(turno.getRuoloTurno());
        return trn;
    }


    public void salvaTurni(long idCalTurni, ArrayList<Turno> turniGenerati) {
        TurniGeneratiDao dao = new TurniGeneratiDao();


        dao.salvaTurniMultipli(Assemblers.mappingTurni(idCalTurni,turniGenerati));


    }


    public void salvaTurno(Turno attempt) {
        TurniGeneratiDao dao = new TurniGeneratiDao();
        dao.salva(Assemblers.mappingTurni(attempt));
    }

    /**
     *
     * @return
     */
    public static ArrayList<Turno> getTurniWeekendMese() throws IOException {
        int numeroTurniFestivi = 0;

        ArrayList<Turno> turniFestivi = new ArrayList<>();
        ArrayList<Turno> turniMese = caricaPatternTurniMese();
        for (Turno turno : turniMese) {

            if(isTurnoFerialeFestivo(turno).equals(Const.FESTIVO))
                turniFestivi.add(turno);
        }
        return turniFestivi;
    }


    /**
     * Estrae gli altri turni al quale appartiene il turno passato come parametro, si suppone che il turno passato come parametro sia già un turno festivo del weekend
     * @param turnoDaAssegnare
     * @return
     */
    @Deprecated
    public ArrayList<Turno> getTurniWeekendDelTurno(Turno turnoDaAssegnare) {


        //TODO qua in fase di generazione turni si potrebbe già etichettare quali sono feriali e quali sono festivi
        //PORCATA incredibile

        ArrayList<Turno> output = new ArrayList<>();


        Calendar cal = Calendar.getInstance();
        cal.setTime(turnoDaAssegnare.getData());
        int meseCorrente = cal.get(Calendar.MONTH);
        ArrayList<Date> nEsimaSettimanaMensileFestiva = DateService.getNEsimaSettimanaMensileFestiva(Const.CURRENT_ANNO, Const.CURRENT_MESE, DateService.getWeekendNumberOfDay(turnoDaAssegnare.getData()));




        //potrebbe essere un venerdì
        cal.setTime(nEsimaSettimanaMensileFestiva.get(0));
        if(cal.get(Calendar.DAY_OF_WEEK)==Calendar.FRIDAY) {
            output.add(new Turno(nEsimaSettimanaMensileFestiva.get(0), Const.NOTTE, Const.RUOLO_REPARTO_1));
            output.add(new Turno(nEsimaSettimanaMensileFestiva.get(0), Const.NOTTE, Const.RUOLO_REPARTO_2));


            cal.setTime(DateService.aumentaTogliGiorno(nEsimaSettimanaMensileFestiva.get(0), 1));
            //devo verificare che non sia sforato il mese
            if (cal.get(Calendar.MONTH) == meseCorrente) {
                output.add(new Turno(nEsimaSettimanaMensileFestiva.get(1), Const.GIORNO, Const.RUOLO_REPARTO_1));
                output.add(new Turno(nEsimaSettimanaMensileFestiva.get(1), Const.GIORNO, Const.RUOLO_REPARTO_2));
                output.add(new Turno(nEsimaSettimanaMensileFestiva.get(1), Const.NOTTE, Const.RUOLO_REPARTO_1));
                output.add(new Turno(nEsimaSettimanaMensileFestiva.get(1), Const.NOTTE, Const.RUOLO_REPARTO_2));
            }

            cal.setTime(DateService.aumentaTogliGiorno(nEsimaSettimanaMensileFestiva.get(0), 2));
            //devo verificare che non sia sforato il mese
            if (cal.get(Calendar.MONTH) == meseCorrente) {
                output.add(new Turno(nEsimaSettimanaMensileFestiva.get(2), Const.GIORNO, Const.RUOLO_REPARTO_1));
                output.add(new Turno(nEsimaSettimanaMensileFestiva.get(2), Const.GIORNO, Const.RUOLO_REPARTO_2));
                output.add(new Turno(nEsimaSettimanaMensileFestiva.get(2), Const.NOTTE, Const.RUOLO_REPARTO_1));
                output.add(new Turno(nEsimaSettimanaMensileFestiva.get(2), Const.NOTTE, Const.RUOLO_REPARTO_2));
            }

            return output;
        }else if (cal.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY) {
            output.add(new Turno(nEsimaSettimanaMensileFestiva.get(0), Const.GIORNO, Const.RUOLO_REPARTO_1));
            output.add(new Turno(nEsimaSettimanaMensileFestiva.get(0), Const.GIORNO, Const.RUOLO_REPARTO_2));
            output.add(new Turno(nEsimaSettimanaMensileFestiva.get(0), Const.NOTTE, Const.RUOLO_REPARTO_1));
            output.add(new Turno(nEsimaSettimanaMensileFestiva.get(0), Const.NOTTE, Const.RUOLO_REPARTO_2));

            //domenica
            cal.setTime(DateService.aumentaTogliGiorno(nEsimaSettimanaMensileFestiva.get(0), 1));
            //devo verificare che non sia sforato il mese
            if (cal.get(Calendar.MONTH) == meseCorrente) {
                output.add(new Turno(nEsimaSettimanaMensileFestiva.get(1), Const.GIORNO, Const.RUOLO_REPARTO_1));
                output.add(new Turno(nEsimaSettimanaMensileFestiva.get(1), Const.GIORNO, Const.RUOLO_REPARTO_2));
                output.add(new Turno(nEsimaSettimanaMensileFestiva.get(1), Const.NOTTE, Const.RUOLO_REPARTO_1));
                output.add(new Turno(nEsimaSettimanaMensileFestiva.get(1), Const.NOTTE, Const.RUOLO_REPARTO_2));
            }

            //venerdì
            cal.setTime(DateService.aumentaTogliGiorno(nEsimaSettimanaMensileFestiva.get(0), -1));
            //devo verificare che non sia sforato il mese
            if (cal.get(Calendar.MONTH) == meseCorrente) {
                output.add(new Turno(DateService.aumentaTogliGiorno(nEsimaSettimanaMensileFestiva.get(0), -1), Const.NOTTE, Const.RUOLO_REPARTO_1));
                output.add(new Turno(DateService.aumentaTogliGiorno(nEsimaSettimanaMensileFestiva.get(0), -1), Const.NOTTE, Const.RUOLO_REPARTO_2));
            }
            return output;
        }else if (cal.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY) {
            //la domenica è per forza di un giorno soltanto
            output.add(new Turno(nEsimaSettimanaMensileFestiva.get(0), Const.GIORNO, Const.RUOLO_REPARTO_1));
            output.add(new Turno(nEsimaSettimanaMensileFestiva.get(0), Const.GIORNO, Const.RUOLO_REPARTO_2));
            output.add(new Turno(nEsimaSettimanaMensileFestiva.get(0), Const.NOTTE, Const.RUOLO_REPARTO_1));
            output.add(new Turno(nEsimaSettimanaMensileFestiva.get(0), Const.NOTTE, Const.RUOLO_REPARTO_2));
            return output;


        }



        return output;



    }










}



