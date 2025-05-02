package com.example.test.model.entity.oac;


import com.example.test.model.entity.member_billing.BDSBaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "oac_member", catalog = "oac_member",name = "bill_details")
@SQLRestriction( "status = 'A'")
public class BillDetailsEntity extends BDSBaseEntity
{
    private static final long serialVersionUID = 1L;

    @Column(name = "bill_dtl_id")
    @Id
    private Long billDtlId;

    @Column(name = "bill_num")
    private String billNumber;

    @Column(name = "chrg_cmnt")
    private String chargeComments;

    @Column(name = "chrg_pri_ref_id")
    private String chargePriRefId;

    @Column(name = "chrg_pri_ref_type")
    private String chargePriRefType;

    @Column(name = "chrg_sec_ref_id")
    private String chargeSecRefId;

    @Column(name = "chrg_sec_ref_type")
    private String chargeSecRefType;
    
    @Column(name = "chrg_ter_ref_id")
    private String chargeTerRefId;

    @Column(name = "chrg_ter_ref_type")
    private String chargeTerRefType;

    @Column(name = "chrg_status")
    private String chargeStatus;

    @Column(name = "currency_cd")
    private String currencyCode;

    @Column(name = "item_qty")
    private Integer quantity;

    @Column(name = "chrg_type_cd")
    private String chargeTypeCode;
    
    @Column(name = "chrg_cat_cd")
    private String chargeCategory;
    
    @Column(name = "rvsl_flg")
    private Boolean reversalFlag;
    
    @Column(name = "ref_chrg_id")
    private Long refChargeId;
    
    @Column(name = "seq_num")
    private Integer seqNumber;
    
    @Column(name = "tot_amt")
    private BigDecimal totalAmount;
    
    @Column(name = "tot_chrg_amt")
    private BigDecimal totalChargeAmount;
    
    @Column(name = "tot_tax_amt")
    private BigDecimal totalTaxAmount;



}