package it.costanza.dao;

public interface Crud<R> {

    long salva(R entity);

    long update(R entity);

    Object getById(Long id);


}
