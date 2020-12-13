package it.costanza.controllers;

import it.costanza.entityDb.mysql.TurniGeneratiMonitorEntity;
import it.costanza.service.StatService;

import java.util.List;

public class Test {

    public static void main(String[] args) {

        StatService stat = new StatService();
        List<TurniGeneratiMonitorEntity> listTurniDaElaborare = stat.getListTurniDaElaborare(1l);

        System.out.println(listTurniDaElaborare);
    }

}
