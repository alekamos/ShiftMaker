drop table if exists RUN;

drop table if exists TURNI_GENERATI;

drop table if exists TURNI_GENERATI_STATS;

drop table if exists TURNI_GENERATI_MONITOR;

create table RUN
(
    ID_RUN bigint(10) auto_increment
        primary key,
    ANNOMESE varchar(6) null,
    DATA_INIZIO_RUN datetime null,
    DATA_FINE_RUN datetime null,
    TIPO_RUN varchar(100) null
)
    comment 'tabella che logga i run';

create index RUN_ID_RUN_index
    on RUN (ID_RUN);


create table TURNI_GENERATI_MONITOR
(
    ID_CAL_TURNI bigint(10) auto_increment primary key,
    ID_RUN   bigint(10)  null,
    STATO    varchar(50) null,
    constraint TURNI_GENERATI_MONITOR_pk
        unique (ID_CAL_TURNI),
    constraint TURNI_GENERATI_MONITOR_RUN_ID_RUN_fk
        foreign key (ID_RUN) references RUN (ID_RUN)
            on delete cascade
)
    comment 'tabella di raccordo tra turni generati e run';

create index TURNI_GENERATI_MONITOR_ID_RUN_index
    on TURNI_GENERATI_MONITOR (ID_RUN);

create index TURNI_GENERATI_MONITOR_ID_CAL_TURNI_index
    on TURNI_GENERATI_MONITOR (ID_CAL_TURNI);


create table TURNI_GENERATI
(
    ID_SING_TURNO bigint(10) auto_increment primary key,
    ID_CAL_TURNI bigint(10),
    DATA_TURNO date null,
    TIPO_TURNO varchar(100) null,
    RUOLO_TURNO varchar(100) null,
    PERSONA_TURNO varchar(100) null,
    constraint TURNI_GENERATI_ID_CAL_TURNI_fk
        foreign key (ID_CAL_TURNI) references TURNI_GENERATI_MONITOR (ID_CAL_TURNI) on delete cascade
)
    comment 'Tabella contenente i turni generati dal sw';




create table TURNI_GENERATI_STATS
(
    ID_CAL_TURNI bigint(10) primary key,
    MEDIA_TURNI_TOT double(10,5) null,
    SDEV_TURNI_TOT double(10,5) null,
    MEDIA_PRES_FEST double(10,5) null,
    SDEV_PRES_FEST double(10,5) null,
    MEDIA_GIORNI_FEST double(10,5) null,
    SDEV_GIORNI_FEST double(10,5) null,
    MEDIA_GIORNI_FER double(10,5) null,
    SDEV_GIORNI_FER double(10,5) null,
    MEDIA_NOTTI double(10,5) null,
    SDEV_NOTTI double(10,5) null,
    SDEV_1_SETTIMANA double(10,5) null,
    SDEV_2_SETTIMANA double(10,5) null,
    SDEV_3_SETTIMANA double(10,5) null,
    SDEV_4_SETTIMANA double(10,5) null,
    SDEV_5_SETTIMANA double(10,5) null,
    SCORE double(10,5) null,
    SCORE_FORMULA varchar(200) null,
    DATA_INSERIMENTO datetime null,
    DATA_AGGIORNAMENTO datetime null,
    constraint TURNI_GENERATI_STATS_ID_CAL_TURNI_fk
        foreign key (ID_CAL_TURNI) references TURNI_GENERATI_MONITOR (ID_CAL_TURNI)
)
    comment 'statistiche collegate al turno generato';

create index TURNI_GENERATI_STATS_ID_CAL_TURNI_index
    on TURNI_GENERATI_STATS (ID_CAL_TURNI);




create index TURNI_GENERATI_ID_CAL_TURNI_index
    on TURNI_GENERATI (ID_CAL_TURNI);



