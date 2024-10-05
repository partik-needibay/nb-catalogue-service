package com.needibay.cart.controller;


import com.needibay.cart.entity.EventService;
import com.needibay.cart.entity.EventServiceCategory;
import com.needibay.cart.response.Response;
import com.needibay.cart.service.EventServiceCategoryService;
import com.needibay.cart.service.EventServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController()
@RequestMapping("/")
public class EventServiceController {

    @Autowired
    EventServiceService eventService;

    @Autowired
    EventServiceCategoryService eventServiceCategoryService;

    @PostMapping("/service")
    public ResponseEntity<Response> createContact(@RequestBody EventService theEventService, HttpServletRequest req)
    {
        
        
        System.out.print(theEventService);
        eventService.save(theEventService);
        return new ResponseEntity<Response>(new Response.Build().setSuccess(true).setMessage("Service has been saved!!").build(), HttpStatus.OK);

    }

    @GetMapping("/service")
    public ResponseEntity<Response> findAll(HttpServletRequest req)
    {
        
        return new ResponseEntity<Response>(new Response.Build().setSuccess(true).setMessage("Permission Fetched!").setData(eventService.findAll()).build(), HttpStatus.OK);

    }

    @GetMapping("/service/{id}")
    public ResponseEntity<Response> findById(Integer id, HttpServletRequest req)
    {
        return new ResponseEntity<Response>(new Response.Build().setSuccess(true).setMessage("Permission Fetched!").setData(eventService.findById(id)).build(), HttpStatus.OK);

    }

    @DeleteMapping("/service/{id}")
    public ResponseEntity<Response> DeleteById(int id, HttpServletRequest req)
    {
        
        eventService.delete(id);
        return new ResponseEntity<Response>(new Response.Build().setSuccess(true).setMessage("Permission Fetched!").build(), HttpStatus.OK);

    }

    

    @PostMapping("/service/category")
    public ResponseEntity<Response> createServiceCategory(@RequestBody EventServiceCategory theEventService, HttpServletRequest req)
    {
        
        
        System.out.print(theEventService);
        eventServiceCategoryService.save(theEventService);
        return new ResponseEntity<Response>(new Response.Build().setSuccess(true).setMessage("Service has been saved!!").build(), HttpStatus.OK);

    }

    @GetMapping("/service/category")
    public ResponseEntity<Response> findAllServiceCategory(HttpServletRequest req)
    {
        
        return new ResponseEntity<Response>(new Response.Build().setSuccess(true).setMessage("Permission Fetched!").setData(eventServiceCategoryService.findAll()).build(), HttpStatus.OK);

    }

    @GetMapping("/service/category/{id}")
    public ResponseEntity<Response> findServiceCategoryById(@PathVariable Integer id, HttpServletRequest req)
    {
        
        return new ResponseEntity<Response>(new Response.Build().setSuccess(true).setMessage("Permission Fetched!").setData(eventServiceCategoryService.findById(id)).build(), HttpStatus.OK);

    }

    @DeleteMapping("/service/category/{id}")
    public ResponseEntity<Response> DeleteServiceCategoryById(@PathVariable Integer id, HttpServletRequest req)
    {
        
        eventServiceCategoryService.delete(id);
        return new ResponseEntity<Response>(new Response.Build().setSuccess(true).setMessage("Permission Fetched!").build(), HttpStatus.OK);

    }


}
