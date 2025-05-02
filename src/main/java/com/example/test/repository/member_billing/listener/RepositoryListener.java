package com.example.test.repository.member_billing.listener;

public interface RepositoryListener<T> {

    public  void onSaveAll(Iterable<T> entities);


    public  void onSave(T entity);


}
