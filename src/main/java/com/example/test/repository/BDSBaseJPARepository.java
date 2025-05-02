package com.example.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

@NoRepositoryBean
public interface BDSBaseJPARepository<T,ID extends Serializable> extends JpaRepository<T, ID>
{
    //Use this to refresh hibernate cache, this will make new entity visible to other services
    public <S extends T> List<S> saveAllAndFlushAndCommit(Iterable<S> entities);

    public <S extends T> S saveAndFlushAndCommit(S entity);

    public <S extends T> List<S> saveAllInBatch(Iterable<S> entities, int batchSize);

    public <S extends T> S saveAndSync(S entity);

    public <S extends T> List<S> saveAllAndSync(Iterable<S> entities);
    public <S extends T> List<S> saveAllAndFlushAndCommitInBatch(Iterable<S> entities, int batchSize);


}
