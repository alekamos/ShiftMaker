package it.costanza.dao;

public interface Crud<R> {

    long salva(R entity);

    Object getById(Long id);


}
