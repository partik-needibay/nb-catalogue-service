package com.needibay.cart.service;

import com.needibay.cart.dto.EventServiceDTO;
import com.needibay.cart.dto.VendorServiceDTO;
import com.needibay.cart.entity.Account;
import com.needibay.cart.entity.VendorServiceBridge;
import com.needibay.cart.repository.AccountRepo;
import com.needibay.cart.repository.VendorServiceBridgeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    AccountRepo accountRepo;

    @Autowired
    VendorServiceBridgeRepo vendorServiceBridgeRepo;

    @Override
    @Transactional
    public List<Account> findAll() {
        return accountRepo.findAll();
    }

    @Override
    @Transactional
    public Account findById(Integer theId) {
        return accountRepo.findById(theId).orElseThrow();
    }

    @Override
    @Transactional
    public Account save(Account theAccount) {
        Account account = accountRepo.save(theAccount);
        return account;
    }



    @Override
    public void delete(int theId) {

    }

    @Override
    @Transactional
    public void updateVendorService(VendorServiceDTO theVendorServiceDTO) {
        System.out.print(theVendorServiceDTO.getServices());
        for (EventServiceDTO eventServiceDTO: theVendorServiceDTO.getServices()) {
            VendorServiceBridge vendorServiceBridge = new VendorServiceBridge();
            vendorServiceBridge.setVendorId(theVendorServiceDTO.getVendorId());
            vendorServiceBridge.setServiceId(eventServiceDTO.getServiceId());
            vendorServiceBridge.setHrs(eventServiceDTO.getHrs());
            vendorServiceBridge.setPerHrCost(eventServiceDTO.getPerHrCost());
            vendorServiceBridge.setRentalCost(eventServiceDTO.getRentalCost());
            vendorServiceBridgeRepo.save(vendorServiceBridge);

        }
    }
}
