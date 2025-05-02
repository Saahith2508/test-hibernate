package com.example.test.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.List;

@NoRepositoryBean
public interface OACBaseJPARepository<T,ID extends Serializable> extends JpaRepository<T,ID>
{
    List<T> findAllWithoutCount(@Nullable Specification<T> spec, Pageable pageable);
}
