package com.example.test.repository.oac_member;


import com.example.test.model.entity.oac.MemberBillingSummaryEntity;
import com.example.test.repository.OACBaseJPARepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * Repository interface for {@link MemberBillingSummaryEntity} entity.
 * <p>
 * This repository uses a Named Entity Graph to optimize queries and avoid the N+1 query problem.
 * </p>
 * <p>
 * The Named Entity Graph "bills-with-billAttributes-extension" fetches the associated {@link MemberBillingSummaryAttributesEntity}, {@link MemberBillingSummaryAttributesExtensionEntity},
 * and {@code payments} entities in a single query.
 * </p>
 *
 * <p>Usage example:</p>
 * <pre>
 * {@code
 *     List<Billing> billings = MemberBillingSummaryRepository.findByBillNumber(customerId);
 *     // The above method will fetch Billing entities along with their associated attributes, extensions.
 * }
 * </pre>
 *
 * @see MemberBillingSummaryRepository
 */
@Repository
@Transactional(propagation = Propagation.REQUIRES_NEW,readOnly = true)
public interface MemberBillingSummaryRepository extends OACBaseJPARepository<MemberBillingSummaryEntity, String>,
        JpaSpecificationExecutor<MemberBillingSummaryEntity>
{
    @EntityGraph(value = "bills-with-billAttributes-extension",type = EntityGraph.EntityGraphType.LOAD)
    MemberBillingSummaryEntity findByRefLotNumber(Long mbrBillSmryId);


    @EntityGraph(value = "bills-with-billAttributes-extension",type = EntityGraph.EntityGraphType.LOAD)
    @Query("FROM MemberBillingSummaryEntity where refLotNumber =:ref")
    MemberBillingSummaryEntity findByRefLotNumbedwdwr(Long ref);


}
