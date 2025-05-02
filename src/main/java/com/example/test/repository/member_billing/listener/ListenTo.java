package com.example.test.repository.member_billing.listener;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ListenTo {

    Class entityClass() ;

}
