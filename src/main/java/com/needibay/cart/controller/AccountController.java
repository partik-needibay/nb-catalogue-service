package com.needibay.cart.controller;

import com.needibay.cart.dto.VendorAccountDTO;
import com.needibay.cart.dto.VendorServiceDTO;
import com.needibay.cart.entity.Account;
import com.needibay.cart.response.Response;
import com.needibay.cart.service.AccountService;
import com.needibay.cart.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/")
public class AccountController {

    @Autowired
    AccountService accountService;

    @Autowired
    VendorService vendorService;

    @Autowired
    public AccountController(AccountService theAccountService) {
        accountService = theAccountService;
    }

    @GetMapping("/account")
    public ResponseEntity<Response> findAll(HttpServletRequest req)
    {
       
        return new ResponseEntity<Response>(new Response.Build()
                .setSuccess(true).setMessage("Permission Fetched!")
                .setData(accountService.findAll()).build(), HttpStatus.OK);

    }

    @GetMapping("/account/{id}")
    public ResponseEntity<Response> findById(@PathVariable Integer id, HttpServletRequest req)
    {
       
        return new ResponseEntity<Response>(new Response.Build().setSuccess(true).setMessage("Permission Fetched!").setData(accountService.findById(id)).build(), HttpStatus.OK);

    }

    @PostMapping("/account/service")
    public ResponseEntity<Response> updateVendorService(@RequestBody VendorServiceDTO theVendorServiceDTO, HttpServletRequest req)
    {
       
        accountService.updateVendorService(theVendorServiceDTO);
        return new ResponseEntity<Response>(new Response.Build().setSuccess(true).setMessage("Permission Fetched!").build(), HttpStatus.OK);

    }

    @DeleteMapping("/account/{id}")
    public ResponseEntity<Response> DeleteById(int id, HttpServletRequest req)
    {
       
        accountService.delete(id);
        return new ResponseEntity<Response>(new Response.Build().setSuccess(true).setMessage("Permission Fetched!").build(), HttpStatus.OK);

    }

}
