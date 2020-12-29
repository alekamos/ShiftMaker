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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class TurniService {






    public Persona copyTurnoAssegnato(ArrayList<Turno> turniAssegnati, Date data, String tipoTurno, String ruoloTurno) {

        ArrayList<Turno> turnoDelGiorno = getTurniDelGiorno(turniAssegnati, data, tipoTurno, ruoloTurno);

        return turnoDelGiorno.get(0).getPersonaInTurno();

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


        boolean result = true;
        ArrayList<Turno> turniIndisponibilita = randomPersona.getIndisponibilitaList();

        for (Turno singIndisp : turniIndisponibilita) {
            if(DateService.isSameDay(singIndisp.getData(),turno.getData()) &&
                    singIndisp.getTipoTurno().equals(turno.getTipoTurno()))
                result = false;
        }



        return result;


    }








    /**
     * Carica il pattern dei turni del mese
     *
     * @return
     */
    public static ArrayList<Turno> caricaPatternTurniMese() throws IOException {

        ArrayList<Turno> turni = new ArrayList<>();





        ArrayList<Date> datesOfMonth = DateService.getDatesOfMonth(Const.CURRENT_ANNO, Const.CURRENT_MESE);
        for (Date data : datesOfMonth) {


            turni.addAll(generaSkeletonTurniGiorno(data));


        }

        return turni;
    }




    public static ArrayList<Turno> generaSkeletonTurniGiorno(Date data) {

        ArrayList<Turno> turni = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);

        //Se il turno non è del weekend ci vuole anche quello di ricerca
        boolean weekendDate = DateService.isWeekendDate(data);
        if (!weekendDate) {
            turni.add(new Turno(data, Const.GIORNO, Const.RUOLO_RICERCA,false));
            turni.add(new Turno(data, Const.GIORNO, Const.RUOLO_URGENTISTA,false));

            turni.add(new Turno(data, Const.GIORNO, Const.RUOLO_REPARTO_1,false));
            turni.add(new Turno(data, Const.GIORNO, Const.RUOLO_REPARTO_2,false));

            //in questo caso dobbiamo distinguere i venerdì notte (festivi) dagli altri giorni
            if(cal.get(Calendar.DAY_OF_WEEK)==Calendar.FRIDAY) {
                turni.add(new Turno(data, Const.NOTTE, Const.RUOLO_REPARTO_1, true));
                turni.add(new Turno(data, Const.NOTTE, Const.RUOLO_REPARTO_2, true));
            }else {
                turni.add(new Turno(data, Const.NOTTE, Const.RUOLO_REPARTO_1, false));
                turni.add(new Turno(data, Const.NOTTE, Const.RUOLO_REPARTO_2, false));
            }



        }else{
            turni.add(new Turno(data, Const.GIORNO, Const.RUOLO_REPARTO_1,true));
            turni.add(new Turno(data, Const.GIORNO, Const.RUOLO_REPARTO_2,true));
            turni.add(new Turno(data, Const.NOTTE, Const.RUOLO_REPARTO_1,true));
            turni.add(new Turno(data, Const.NOTTE, Const.RUOLO_REPARTO_2,true));
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

        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        System.out.println("Current relative path is: " + s);
        FileInputStream file = new FileInputStream("commonFiles/dati.xlsx");


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
     * Carica i turni già schedulati dal file excel assegna anche se il turno è feriale o festivo in base al giorno
     * @return
     * @throws IOException
     */
    public ArrayList<Turno> caricaTurniSchedulati() throws IOException {

        ArrayList<Turno> turniSchedulatiOut = new ArrayList<>();

        FileInputStream file = new FileInputStream("commonFiles/dati.xlsx");
        Calendar cal = Calendar.getInstance();




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

                    cal.setTime(turno.getData());
                    if(cal.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY)
                        turno.setFestivo(true);
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

                    cal.setTime(turno.getData());
                    if(cal.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY)
                        turno.setFestivo(true);
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

                    cal.setTime(turno.getData());
                    if(cal.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY || cal.get(Calendar.DAY_OF_WEEK)==Calendar.FRIDAY)
                        turno.setFestivo(true);
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

                    cal.setTime(turno.getData());
                    if(cal.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY || cal.get(Calendar.DAY_OF_WEEK)==Calendar.FRIDAY)
                        turno.setFestivo(true);
                    turniSchedulatiOut.add(turno);
                }







            }
        }

        workbook.close();
        file.close();

        return turniSchedulatiOut;

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

            if(turno.isFestivo())
                turniFestivi.add(turno);
        }
        return turniFestivi;
    }


    /**
     * Estrae gli altri turni al quale appartiene il turno passato come parametro, si suppone che il turno passato come parametro sia già un turno festivo del weekend
     * @param turnoDaAssegnare
     * @return
     */
    public ArrayList<Turno> getTurniWeekendDelTurno(Turno turnoDaAssegnare) {


        //TODO qua in fase di generazione turni si potrebbe già etichettare quali sono feriali e quali sono festivi
        //PORCATA incredibile

        ArrayList<Turno> output = new ArrayList<>();


        Calendar cal = Calendar.getInstance();
        cal.setTime(turnoDaAssegnare.getData());
        int meseCorrente = cal.get(Calendar.MONTH);
        int weekendNumberOfDay = DateService.getWeekendNumberOfDay(turnoDaAssegnare.getData());
        ArrayList<Date> nEsimaSettimanaMensileFestiva = DateService.getNEsimaSettimanaMensileFestiva(Const.CURRENT_ANNO, Const.CURRENT_MESE, weekendNumberOfDay);




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


    /**
     * Questo metodo serve ad ottimizzare la generazione dei turni mettendo prima i festivi (che sono quelli con più vincoli, poi i feriali)
     * Mette all'inizio i giorni più diffiicli ovvero quelli con più indisponibilità
     * @param skeletonTurni
     * @return
     */
    public ArrayList<Turno> ordinaOttimizzaTurniSenzaIndisponibilita(ArrayList<Turno> skeletonTurni,ArrayList<Turno> turniSchedulati) throws IOException {


        ArrayList<Turno> turniFeriali = new ArrayList<>();
        ArrayList<Turno> turniFestivi = new ArrayList<>();


        //prima i turni già schedulati così sa già prima cosa deve fare nei calcoli
        for (Turno turno : turniSchedulati) {
            if(turno.isFestivo() && !turniFestivi.contains(turno))
                turniFestivi.add(turno);
            else if (!turno.isFestivo() && !turniFeriali.contains(turno))
                turniFeriali.add(turno);
        }


        /**
         * Qui devo mettere solo i turni rimanenti ovvero i turni che non sono ancora stati inseriti
         */
        for (Turno turno : skeletonTurni) {
            if(turno.isFestivo() && !turniFestivi.contains(turno))
                turniFestivi.add(turno);
            else if (!turno.isFestivo() && !turniFeriali.contains(turno))
                turniFeriali.add(turno);
        }

        turniFestivi.addAll(turniFeriali);

        return turniFestivi;






    }


    /**
     * Questo metodo serve ad ottimizzare la generazione dei turni mettendo prima i festivi (che sono quelli con più vincoli, poi i feriali)
     * Mette all'inizio i giorni più diffiicli ovvero quelli con più indisponibilità
     * @param skeletonTurni
     * @return
     */
    public ArrayList<Turno> ordinaOttimizzaTurni(ArrayList<Turno> skeletonTurni,ArrayList<Turno> turniSchedulati) throws IOException {


        ArrayList<Turno> turniFeriali = new ArrayList<>();
        ArrayList<Turno> turniFestivi = new ArrayList<>();


        ArrayList<GiornoExcel> turniIndispCount = contaIndisponibPerDay();
        Collections.sort(turniIndispCount);

        //prendo l'ultimo elemento
        int minIndisp = turniIndispCount.get(turniIndispCount.size()-1).getCountIndisp();

        //prima i turni già schedulati così sa già prima cosa deve fare nei calcoli
        for (Turno turno : turniSchedulati) {
            if(turno.isFestivo() && !turniFestivi.contains(turno))
                turniFestivi.add(turno);
            else if (!turno.isFestivo() && !turniFeriali.contains(turno))
                turniFeriali.add(turno);
        }


        //prima i turni critici, fino che ci sono indisponibilità superiori alla norma (ultimo elemento dell'array ordinato lui piazza)
        for (int i = 0; i < turniIndispCount.size() && turniIndispCount.get(i).getCountIndisp()>=minIndisp+2; i++) {

            for (Turno turno : skeletonTurni) {
                if(DateService.isSameDay(turniIndispCount.get(i).getDate(),turno.getData())) {
                    //se è un festivo e non è già stato aggiunto prima tra i turni schedulati
                    if (turno.isFestivo() && !turniFestivi.contains(turno))
                        turniFestivi.add(turno);
                }
            }
        }


        /**
         * Qui devo mettere solo i turni rimanenti ovvero i turni che non sono ancora stati inseriti
         */
        for (Turno turno : skeletonTurni) {
            if(turno.isFestivo() && !turniFestivi.contains(turno))
                turniFestivi.add(turno);
            else if (!turno.isFestivo() && !turniFeriali.contains(turno))
                turniFeriali.add(turno);
        }

        turniFestivi.addAll(turniFeriali);

        return turniFestivi;






    }

    /**
     * Conta il numero di indisponibilita per giornata
     * @return
     * @throws IOException
     */
    private ArrayList<GiornoExcel> contaIndisponibPerDay() throws IOException {

        FileInputStream file = new FileInputStream("commonFiles/dati.xlsx");


        Workbook workbook = new XSSFWorkbook(file);
        Sheet riga = workbook.getSheetAt(0);


        ArrayList<GiornoExcel> lista = new ArrayList<>();
        //conto quando finiscono le celle
        int maxCell=  riga.getRow(0).getLastCellNum();

        //ciclo dalla prima all'ultioma cella per singola riga
        for (int dayIndex = 1; dayIndex < maxCell; dayIndex++) {

            int tmpCount = 0;
            //mi creo l'iteretor che resetterò iogni volta
            Iterator<Row> iterator = riga.iterator();
            //ciclo tutte le righe
            while (iterator.hasNext()) {
                Row nextRow = iterator.next();

                //se la cella non è la prima riga, è diversa da null, e non ha la stringa vuota (quindi contiene un indisponibilita) aumento il contatore
                if(nextRow.getRowNum()>0 && nextRow.getCell(dayIndex)!=null && !"".equals(nextRow.getCell(dayIndex).getStringCellValue()))
                    tmpCount++;

            }

            GiornoExcel data = new GiornoExcel();
            data.setDate(riga.getRow(0).getCell(dayIndex).getDateCellValue());
            data.setCountIndisp(tmpCount);
            lista.add(data);

        }

        return lista;

    }







    public ArrayList<Turno> getTurniOppostiWe(ArrayList<Turno> turniCurrWeekend,Turno turnoDaAssegnare) {

        ArrayList<Turno> output = new ArrayList<>();

        for (Turno turno : turniCurrWeekend) {
            if(!turno.getTipoTurno().equals(turnoDaAssegnare.getTipoTurno()))
                output.add(turno);
        }

        return output;

    }

    /**
     * Serve per la generazione del cadidato, mi esclude i turni in cui non deve essere presente il candidato
     * Es: se sto scegliendo giorno X giorno, devo escludere gli altri turni del giorno corrente, la notte precedente
     * Sono i controlli che si fanno post generazione del candidato in pratica
     * @param turnoDaAssegnare
     * @return
     */
    public ArrayList<Turno> getTurniDaEscludere(Turno turnoDaAssegnare) {

        ArrayList<Turno> output = new ArrayList<>();


        //Gli altri turni del giorno
        //Non è un problema tenere il turno stesso tanto non è assegnato, questo mi garantisce che una persona ci sia una sola volta al giorno
        Turno t1 = new Turno();
        Turno t2 = new Turno();
        t1.setData(turnoDaAssegnare.getData());
        t2.setData(turnoDaAssegnare.getData());
        t1.setTipoTurno(Const.GIORNO);
        t2.setTipoTurno(Const.NOTTE);

        output.add(t1);
        output.add(t2);


        //se è giorno il giorno dopo può fare sia giorno che notte
        //Se notte il giorno dopo non può fare ne notte ne giorno
        //se notte il giorno prima non può fare notte(è su uno smonto)
        if(turnoDaAssegnare.getTipoTurno().equals(Const.NOTTE)) {
            Turno t3 = new Turno();
            Turno t4 = new Turno();
            Turno t5 = new Turno();
            t3.setData(DateService.aumentaTogliGiorno(turnoDaAssegnare.getData(),1));
            t4.setData(DateService.aumentaTogliGiorno(turnoDaAssegnare.getData(),1));
            t3.setTipoTurno(Const.GIORNO);
            t4.setTipoTurno(Const.NOTTE);

            t5.setData(DateService.aumentaTogliGiorno(turnoDaAssegnare.getData(),-1));
            t5.setTipoTurno(Const.NOTTE);

            output.add(t3);
            output.add(t4);
            output.add(t5);
        }

        //se giorno il giorno prima non può fare notte
        if(turnoDaAssegnare.getTipoTurno().equals(Const.GIORNO)) {
            Turno t6 = new Turno();
            t6.setData(DateService.aumentaTogliGiorno(turnoDaAssegnare.getData(),-1));
            t6.setTipoTurno(Const.NOTTE);

            output.add(t6);

        }

        return output;


    }
}



