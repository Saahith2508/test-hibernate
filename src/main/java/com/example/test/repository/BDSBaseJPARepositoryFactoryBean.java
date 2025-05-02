package com.example.test.repository;


import com.example.test.repository.member_billing.listener.ListenTo;
import com.example.test.repository.member_billing.listener.RepositoryListener;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;


import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BDSBaseJPARepositoryFactoryBean<T extends JpaRepository<S, ID>, S, ID extends Serializable> extends JpaRepositoryFactoryBean<T, S, ID> {

    private final Map<String, RepositoryListener<?>> listeners = new HashMap<>();


    public BDSBaseJPARepositoryFactoryBean(Class repositoryInterface, List<? extends RepositoryListener> repositoryListeners) {
        super(repositoryInterface);

        repositoryListeners.forEach(listener->{
            ListenTo listenTo = listener.getClass().getAnnotation(ListenTo.class);
            if (listenTo != null) {
                listeners.put(listenTo.entityClass().getName(), listener);
            }
        });
    }

    @Override
    protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
        return new BDSBaseJPARepositoryFactory(entityManager, listeners);
    }
}
