package com.needibay.cart.repository.cart;

import com.needibay.cart.entity.cart.EavEntityStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EavEntityStoreRepo extends JpaRepository<EavEntityStore, Long> {

    @Query(value = "SELECT `eav_entity_store`.`increment_last_id` FROM `eav_entity_store` where `eav_entity_store`.`id` = 1", nativeQuery = true)
    public Long findLastOrderIncrementId();

    @Modifying
    @Query(value = "update eav_entity_store set increment_last_id = :incrementLastId where id = 1", nativeQuery = true)
    public void updateLastOrderIncrementId(Long incrementLastId);

    @Query(value = "SELECT `eav_entity_store`.`increment_last_id` FROM `eav_entity_store` where `eav_entity_store`.`id` = 2", nativeQuery = true)
    public Long findLastInvoiceIncrementId();

    @Modifying
    @Query(value = "update eav_entity_store set increment_last_id = :incrementLastId where id = 2", nativeQuery = true)
    public void updateLastInvoiceIncrementId(Long incrementLastId);

    @Query(value = "SELECT `eav_entity_store`.`increment_last_id` FROM `eav_entity_store` where `eav_entity_store`.`id` = 3", nativeQuery = true)
    public Long findLastPurchaseOrderId();

    @Modifying
    @Query(value = "update eav_entity_store set increment_last_id = :incrementLastId where id = 3", nativeQuery = true)
    public void updateLastPurchaseOrderIncrementId(Long incrementLastId);

    @Query(value = "SELECT `eav_entity_store`.`increment_last_id` FROM `eav_entity_store` where `eav_entity_store`.`id` = 4", nativeQuery = true)
    public Long findLastQuotationId();

    @Modifying
    @Query(value = "update eav_entity_store set increment_last_id = :incrementLastId where id = 4", nativeQuery = true)
    public void updateLastQuotationId(Long incrementLastId);

}
