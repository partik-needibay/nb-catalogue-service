package com.needibay.cart.controller;


import com.needibay.cart.entity.Contact;
import com.needibay.cart.response.Response;
import com.needibay.cart.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/")
public class ContactController {

    @Autowired
    ContactService contactService;

    @PostMapping("/contact")
    public ResponseEntity<Response> createContact(@RequestBody Contact theContact, HttpServletRequest req)
    {
        
        contactService.save(theContact);
        return new ResponseEntity<Response>(new Response.Build().setSuccess(true).setMessage("Contact has been saved!").build(), HttpStatus.OK);

    }
    @GetMapping("/contact")
    public ResponseEntity<Response> findAll(HttpServletRequest req)
    {
        return new ResponseEntity<Response>(new Response.Build().setSuccess(true).setMessage("Permission Fetched!").setData(contactService.findAll()).build(), HttpStatus.OK);

    }

    @GetMapping("/contact/{id}")
    public ResponseEntity<Response> findById(@PathVariable Integer id, HttpServletRequest req)
    {
        
        return new ResponseEntity<Response>(new Response.Build().setSuccess(true).setMessage("Permission Fetched!").setData(contactService.findById(id)).build(), HttpStatus.OK);

    }

    @DeleteMapping("/contact/{id}")
    public ResponseEntity<Response> DeleteById(@PathVariable Integer id, HttpServletRequest req)
    {
        
        contactService.delete(id);
        return new ResponseEntity<Response>(new Response.Build().setSuccess(true).setMessage("Permission Fetched!").build(), HttpStatus.OK);

    }

    
}
