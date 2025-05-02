package com.example.test.model.entity.oac;


import com.example.test.model.entity.member_billing.BDSBaseEntity;
import com.example.test.model.entity.member_billing.MemberBillingSummaryAttributesEntity;
import com.example.test.model.entity.member_billing.MemberBillingSummaryAttributesExtensionEntity;
import jakarta.persistence.*;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;


@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Table(schema = "oac_member", catalog = "oac_member",name = "bills")
@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "bills-with-billAttributes-extension",
                attributeNodes = {
                        @NamedAttributeNode("mbrBillSummaryAttributes"),
                        @NamedAttributeNode("mbrBillingSummaryAttributesExt")
                }
        ),
        @NamedEntityGraph(
                name = "bills-with-billDetails",
                attributeNodes = {
                        @NamedAttributeNode("billDetails"),
                        @NamedAttributeNode("mbrBillSummaryAttributes")
                }
        )
})
@FilterDef(
        name = "countryGroup",
        parameters = @ParamDef(name = "country", type = String.class)  // Keep 'type' as 'string'
)
@Filter(name = "countryGroup", condition = "operating_cntry_cd =:country")
public class MemberBillingSummaryEntity extends BDSBaseEntity {

    private static final long serialVersionUID = -4013941058632332146L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bill_num")
    private String billNumber;

    @Column(name = "bill_id")
    private Long mbrBillSmryId;

    @Column(name = "entity_id")
    private String buyerNumber;

    @Column(name = "bill_pri_ref_id")
    private String priRefId;

    @Column(name = "bill_pri_ref_type")
    private String priRefType;

    @Column(name = "bill_sec_ref_id")
    private String secRefId;

    @Column(name = "bill_sec_ref_type")
    private String secRefType;

    @Column(name = "bill_ter_ref_id")
    private String terRefId;

    @Column(name = "bill_ter_ref_type")
    private String terRefType;

//    @Column(name = "bill_quat_ref_id")
//    private String qatRefId;



    @Column(name = "bill_quat_ref_type")
    private String qatRefType;

    @Column(name = "bill_quin_ref_id")
    private String quinRefId;

    @Column(name = "bill_quin_ref_type")
    private String quinRefType;

    @Column(name = "facility_id")
    private Long facilityId;

    @TenantId
    @Column(name = "operating_cntry_cd")
    private String operatingCountryCode;



    @Column(name = "tot_bill_amt")
    private BigDecimal amountDue;
    @Column(name = "tot_chrg_amt")
    private BigDecimal totalChargeAmount;
    @Column(name = "tot_tax_amt")
    private BigDecimal totalTaxAmount;
    @Column(name = "due_amt")
    private BigDecimal balanceDue;
    @Column(name = "tot_chrg_adj_amt")
    private BigDecimal totalChargeAdjustedAmount;
    @Column(name = "tot_tax_adj_amt")
    private BigDecimal totalTaxAdjustedAmount;

    @Column(name = "paid_amt")
    private BigDecimal paidAmount;

    @Column(name = "currency_cd")
    private String currencyCode;

    @Column(name = "curr_bill_ver")
    private Integer billVersion;

    @Column(name = "bill_cat_cd")
    private String invoiceCategory;

    @Column(name = "bill_desc")
    private String invoiceDescription;

    @Column(name = "bill_status_cd")
    private String billStatusCd;
    
    @Column(name = "lot_num")
    private Long refLotNumber;

    @Column(name = "bill_type_cd")
    private String billTypeCode;

    @Column(name = "ref_bill_num")
    private String refBillNumber;

    @Column(name = "bill_status_rsn")
    private String billStatusReason;

    @OneToOne(mappedBy = "bill",fetch = FetchType.LAZY)
    private MemberBillingSummaryAttributesEntity mbrBillSummaryAttributes;

    @OneToOne(mappedBy = "bill",fetch = FetchType.LAZY)
    private MemberBillingSummaryAttributesExtensionEntity mbrBillingSummaryAttributesExt;

//    @Formula("(case when (bill_quat_ref_type = 'SOURCE' && bill_quat_ref_id = 'CAS') = true then true else false end)")
//    private boolean migratedBill;

    @OneToMany()
    @JoinColumn(name = "bill_num",referencedColumnName = "bill_num")
    @OrderBy("seqNumber")
    @Fetch(FetchMode.SELECT)
    @BatchSize(size = 500)
    private List<BillDetailsEntity> billDetails;


    public MemberBillingSummaryAttributesEntity createMbrBillSummaryAttributesIfNull()
    {
        if (Objects.isNull(mbrBillSummaryAttributes))
        {
            mbrBillSummaryAttributes = new MemberBillingSummaryAttributesEntity();
            mbrBillSummaryAttributes.setBillNumber(this.billNumber);
        }
        return mbrBillSummaryAttributes;
    }

}
