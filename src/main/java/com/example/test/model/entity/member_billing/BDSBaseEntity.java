package com.example.test.model.entity.member_billing;


import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

/**
 * BDS Base Entity
 * Handles the generic JSON Type mappers
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class BDSBaseEntity
{
    public static final String G2BDS_USER = "G2BDS_USER";

    @Column(name = "status")
    private String status;

}
