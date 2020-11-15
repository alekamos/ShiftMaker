package it.costanza.controllers;

import it.costanza.dao.TurnoDao;

public class
testH2 {

    public static void main(String[] args) {
        TurnoDao dao = new TurnoDao();
        dao.svuotaLocal();
    }
}
