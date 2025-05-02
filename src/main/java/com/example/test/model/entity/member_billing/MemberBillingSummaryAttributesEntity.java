package com.example.test.model.entity.member_billing;


import com.example.test.model.entity.oac.MemberBillingSummaryEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;


import java.time.ZonedDateTime;

/**
 * Very important to exclude custom serializers and
 * deserializers in hashing and equals as this will create an update in jpa
 * when loading json based columns
 */
@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Table(schema = "member_billing", catalog = "member_billing", name = "mbr_bill_attributes")
@Where(clause = "status = 'A'")
@DynamicUpdate
public class MemberBillingSummaryAttributesEntity extends BDSBaseEntity
{
    private static final long serialVersionUID = 8363130549566668825L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mbr_bill_attr_id")
    private Long mbrBillAttrId;

    @Column(name = "bill_num")
    private String billNumber;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_num", referencedColumnName = "bill_num", insertable = false, updatable = false, nullable = false)
    private MemberBillingSummaryEntity bill;



}
