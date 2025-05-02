package com.example.test.repository;


import com.example.test.repository.member_billing.listener.RepositoryListener;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.core.RepositoryInformation;


import java.util.HashMap;
import java.util.Map;

public class BDSBaseJPARepositoryFactory extends JpaRepositoryFactory {

    private EntityManager entityManager;
    private Map<String, RepositoryListener<?>> listeners = new HashMap<>();

    public BDSBaseJPARepositoryFactory(EntityManager entityManager, Map<String, RepositoryListener<?>> listeners) {
        super(entityManager);
        this.entityManager = entityManager;
        this.listeners= listeners;

    }

    //****************** Edit ********************
    //starting from spring boot 2.1.0, getTargetRepository(RepositoryInformation information) was made final. So you can't override it anymore. You will need to override getTargetRepository(RepositoryInformation information, EntityManager entityManager)
    //@Override
    //protected Object getTargetRepository(RepositoryInformation information) {
    //    return new RepositoryBaseImpl(getEntityInformation(information.getDomainType()), entityManager, messageLocale);
    //}
    @Override
    protected JpaRepositoryImplementation<?, ?> getTargetRepository(RepositoryInformation information, EntityManager entityManager) {
        return new BDSBaseJPARepositoryImpl<>(getEntityInformation(information.getDomainType()), entityManager,listeners);
    }
}