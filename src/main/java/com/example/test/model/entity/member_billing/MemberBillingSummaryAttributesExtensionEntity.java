package com.example.test.model.entity.member_billing;


import com.example.test.model.entity.oac.MemberBillingSummaryEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;



@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Table(schema = "member_billing", catalog = "member_billing", name = "mbr_bill_attributes_extension")
@Where(clause = "status = 'A'")
public class MemberBillingSummaryAttributesExtensionEntity extends BDSBaseEntity
{
    private static final long serialVersionUID = 8363130549566668825L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "mbr_bill_attr_ext_id")
    private Long mbrBillAttExtId;

    @Column(name = "bill_num")
    private String billNumber;



    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_num", referencedColumnName = "bill_num", insertable = false, updatable = false, nullable = false)
    private MemberBillingSummaryEntity bill;

}
