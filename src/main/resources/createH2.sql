

create table RUN
(
    ID_RUN bigint(10) auto_increment
        primary key,
    ANNOMESE varchar(6) null,
    DATA_INIZIO_RUN datetime null,
    DATA_FINE_RUN datetime null,
    TIPO_RUN varchar(100) null
);


create index RUN_ID_RUN_index
    on RUN (ID_RUN);


create table TURNI_GENERATI_MONITOR
(
    ID_TURNO bigint(10) auto_increment primary key,
    ID_RUN   bigint(10)  null,
    STATO    varchar(50) null,
    constraint TURNI_GENERATI_MONITOR_pk
        unique (ID_TURNO),
    constraint TURNI_GENERATI_MONITOR_RUN_ID_RUN_fk
        foreign key (ID_RUN) references RUN (ID_RUN)
            on delete cascade
);

create index TURNI_GENERATI_MONITOR_ID_RUN_index
    on TURNI_GENERATI_MONITOR (ID_RUN);

create index TURNI_GENERATI_MONITOR_ID_TURNO_index
    on TURNI_GENERATI_MONITOR (ID_TURNO);


create table TURNI_GENERATI
(
    ID_TURNO bigint(10)
        primary key,
    DATA_TURNO date null,
    TIPO_TURNO varchar(100) null,
    RUOLO_TURNO varchar(100) null,
    PERSONA_TURNO varchar(100) null,
    constraint TURNI_GENERATI_ID_TURNO_fk
        foreign key (ID_TURNO) references TURNI_GENERATI_MONITOR (ID_TURNO) on delete cascade
);




create table TURNI_GENERATI_STATS
(
    ID_TURNO bigint(10) primary key,
    MEDIA_TURNI_TOT DECIMAL(5,5) null,
    SDEV_TURNI_TOT DECIMAL(5,5) null,
    MEDIA_PRES_FEST DECIMAL(5,5) null,
    SDEV_PRES_FEST DECIMAL(5,5) null,
    MEDIA_GIORNI_FEST DECIMAL(5,5) null,
    SDEV_GIORNI_FEST DECIMAL(5,5) null,
    MEDIA_GIORNI_FER DECIMAL(5,5) null,
    SDEV_GIORNI_FER DECIMAL(5,5) null,
    MEDIA_NOTTI DECIMAL(5,5) null,
    SDEV_NOTTI DECIMAL(5,5) null,
    SDEV_1_SETTIMANA DECIMAL(5,5) null,
    SDEV_2_SETTIMANA DECIMAL(5,5) null,
    SDEV_3_SETTIMANA DECIMAL(5,5) null,
    SDEV_4_SETTIMANA DECIMAL(5,5) null,
    SDEV_5_SETTIMANA DECIMAL(5,5) null,
    SCORE DECIMAL(5,5) null,
    SCORE_FORMULA varchar(200) null,
    DATA_INSERIMENTO datetime null,
    DATA_AGGIORNAMENTO datetime null,
        constraint TURNI_GENERATI_STATS_ID_TURNO_fk
        foreign key (ID_TURNO) references TURNI_GENERATI_MONITOR (ID_TURNO)
) ;

create index TURNI_GENERATI_STATS_ID_TURNO_index
    on TURNI_GENERATI_STATS (ID_TURNO);




create index TURNI_GENERATI_ID_TURNO_index
    on TURNI_GENERATI (ID_TURNO);


