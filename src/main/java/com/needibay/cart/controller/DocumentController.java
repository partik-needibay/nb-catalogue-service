package com.needibay.cart.controller;

import com.needibay.cart.entity.Document;
import com.needibay.cart.response.Response;
import com.needibay.cart.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class DocumentController {

    @Autowired
    DocumentService documentService;

    @PostMapping("/document")
    public ResponseEntity<Response> createContact(@RequestBody Document theDocument)
    {
        documentService.save(theDocument);
        return new ResponseEntity<Response>(new Response.Build().setSuccess(true).setMessage("Contact has been saved!").build(), HttpStatus.OK);

    }

    @GetMapping("/document")
    public ResponseEntity<Response> findAll()
    {
        return new ResponseEntity<Response>(new Response.Build().setSuccess(true).setMessage("Permission Fetched!").setData(documentService.findAll()).build(), HttpStatus.OK);

    }

    @GetMapping("/document/{id}")
    public ResponseEntity<Response> findById(@PathVariable Integer id)
    {
        return new ResponseEntity<Response>(new Response.Build().setSuccess(true).setMessage("Permission Fetched!").setData(documentService.findById(id)).build(), HttpStatus.OK);

    }

    @DeleteMapping("/document/{id}")
    public ResponseEntity<Response> DeleteById(@PathVariable Integer id)
    {
        documentService.delete(id);
        return new ResponseEntity<Response>(new Response.Build().setSuccess(true).setMessage("Permission Fetched!").build(), HttpStatus.OK);

    }
}
