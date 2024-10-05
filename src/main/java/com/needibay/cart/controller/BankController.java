package com.needibay.cart.controller;


import com.needibay.cart.entity.Bank;
import com.needibay.cart.response.Response;
import com.needibay.cart.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/")
public class BankController {

    @Autowired
    BankService bankService;

    @PostMapping("/bank")
    public ResponseEntity<Response> createContact(@RequestBody Bank theBank, HttpServletRequest req)
    {
        
        bankService.save(theBank);
        return new ResponseEntity<Response>(new Response.Build().setSuccess(true).setMessage("Contact has been saved!").build(), HttpStatus.OK);

    }

    @GetMapping("/bank")
    public ResponseEntity<Response> findAll(HttpServletRequest req)
    {
        
        return new ResponseEntity<Response>(new Response.Build().setSuccess(true).setMessage("Permission Fetched!").setData(bankService.findAll()).build(), HttpStatus.OK);

    }

    @GetMapping("/bank/{id}")
    public ResponseEntity<Response> findById(int id, HttpServletRequest req)
    {
        
        return new ResponseEntity<Response>(new Response.Build().setSuccess(true).setMessage("Permission Fetched!").setData(bankService.findById(id)).build(), HttpStatus.OK);

    }

    @DeleteMapping("/bank/{id}")
    public ResponseEntity<Response> DeleteById(int id, HttpServletRequest req)
    {
        
        bankService.delete(id);
        return new ResponseEntity<Response>(new Response.Build().setSuccess(true).setMessage("Permission Fetched!").build(), HttpStatus.OK);

    }

    
}
