package com.example.test.repository;



import com.example.test.repository.member_billing.listener.RepositoryListener;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Slf4j
public class BDSBaseJPARepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements BDSBaseJPARepository<T, ID> {

    private Map<String, RepositoryListener<?>> listeners = new HashMap<>();

    private final EntityManager entityManager;

    @Autowired
    public BDSBaseJPARepositoryImpl(JpaEntityInformation entityInformation, EntityManager entityManager,Map<String, RepositoryListener<?>> listeners) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
        this.listeners=listeners;
    }



    @Transactional
    @Override
    public <S extends T> List<S> saveAllAndFlushAndCommit(Iterable<S> entities) {
        List<S> savedEntities = super.saveAllAndFlush(entities);
        for (S entity : savedEntities) {
            entityManager.refresh(entity);
        }
        return savedEntities;
    }

    @Transactional
    @Override
    public <S extends T> List<S> saveAllAndFlushAndCommitInBatch(Iterable<S> entities, int batchSize) {
        try (Session session = entityManager.unwrap(Session.class)) {
            session.setJdbcBatchSize(batchSize);
        }
        List<S> savedEntities = super.saveAllAndFlush(entities);
        for (S entity : savedEntities) {
            entityManager.refresh(entity);
        }
        return savedEntities;
    }

    @Transactional
    @Override
    public <S extends T> S saveAndFlushAndCommit(S entity) {
        S savedEntities = super.saveAndFlush(entity);
        entityManager.refresh(entity);
        return savedEntities;
    }

    @Override
    public <S extends T> List<S> saveAllInBatch(Iterable<S> entities, int batchSize) {
        Session session = entityManager.unwrap(Session.class);
        session.setJdbcBatchSize(batchSize);
        List<S> savedEntities = super.saveAllAndFlush(entities);
        return savedEntities;
    }


    @Override
    @Transactional
    public <S extends T> S saveAndSync(S entity) {
        S savedEntities = super.saveAndFlush(entity);
        try {
            if (listeners != null && !listeners.isEmpty()) {
                RepositoryListener listener = listeners.get(entity.getClass().getName());
                if (listener != null) {
                    listener.onSave(savedEntities);
                }
            }
        }catch (Exception e){
            log.error("Exception :{}",e.getMessage(),e);
        }
        return savedEntities;
    }

    @Transactional
    @Override
    public <S extends T> List<S> saveAllAndSync(Iterable<S> entities) {
        List<S> savedEntities = super.saveAllAndFlush(entities);

        try {
            if (listeners != null && !listeners.isEmpty()) {
                S firstEntity = getFirstElement(entities);
                RepositoryListener listener = listeners.get(firstEntity.getClass().getName());
                if (listener != null) {
                    listener.onSaveAll(savedEntities);
                }
            }
        }catch (Exception e){
            log.error("Exception :{}",e.getMessage(),e);
        }

        return savedEntities;
    }

    private <S> S getFirstElement(Iterable<S> iterable) {
        if (iterable == null) {
            return null; // Handle null iterable
        }
        Iterator<S> iterator = iterable.iterator();
        return iterator.hasNext() ? iterator.next() : null; // Return null if empty
    }
}
