package com.example.test.controller;

import com.example.test.repository.oac_member.MemberBillingSummaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RequestMapping("/test")
@RestController
public class Testcontroller {

    @Autowired
    private MemberBillingSummaryRepository memberBillingSummaryRepository;

    @GetMapping(value = "/test")
    public void getBillingBatchConfig()
    {
       memberBillingSummaryRepository.findByRefLotNumbedwdwr(123l);
    }
}
