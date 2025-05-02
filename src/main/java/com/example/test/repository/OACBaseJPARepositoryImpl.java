package com.example.test.repository;


import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.lang.Nullable;


import java.io.Serializable;
import java.util.List;

/**
 * Custom implementation api's for repositories.
 * @author yekota
 * @param <T>
 * @param <ID>
 */
public class OACBaseJPARepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements OACBaseJPARepository<T, ID> {

    private final EntityManager entityManager;

    public OACBaseJPARepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);
        this.entityManager = entityManager;
    }

    public OACBaseJPARepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    /**
     * This custom implementation do pagination and sorting without querying for 'total elements count'.
     *
     * @param spec
     * @param pageable
     * @return
     */
    public List<T> findAllWithoutCount(@Nullable Specification<T> spec, Pageable pageable) {
        TypedQuery<T> query = getQuery(spec, pageable.getSort());

        if (pageable.getOffset() < 0) {
            throw new IllegalArgumentException("Offset must not be less than zero!");
        }
        if (pageable.getPageSize() < 1) {
            throw new IllegalArgumentException("Max results must not be less than one!");
        }

        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        return query.getResultList();
    }
}