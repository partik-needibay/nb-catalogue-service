package com.needibay.cart.repository.address;

import com.needibay.cart.entity.address.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepo extends JpaRepository<Address, Long> {

    @Query(value = "select * from nb_addresses where model_id = :customerId and model = 'Customer' and address_type_id = 2", nativeQuery = true)
    List<Address> findShippingAddressByCustomerId(Integer customerId);

    @Query(value = "select * from nb_addresses where model_id = :customerId and model = 'Customer' and address_type_id = 1", nativeQuery = true)
    List<Address> findBillingAddressByCustomerId(Integer customerId);

}
