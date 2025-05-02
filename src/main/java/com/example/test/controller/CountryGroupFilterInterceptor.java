package com.example.test.controller;

import com.example.test.model.entity.oac.MemberBillingSummaryEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;


@Component
public class CountryGroupFilterInterceptor implements HandlerInterceptor {

    @PersistenceContext(unitName = "oacMember")
    private EntityManager entityManager;

    @Override
    @Transactional
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Session session = entityManager.unwrap(Session.class);
        if (session.getEnabledFilter("countryGroup") == null) {
            session.enableFilter("countryGroup")
                    .setParameter("country", "USA");
        }
        return true;
    }
}