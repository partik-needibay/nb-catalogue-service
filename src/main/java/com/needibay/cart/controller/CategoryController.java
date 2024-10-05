package com.needibay.cart.controller;

import com.needibay.cart.response.Response;
import com.needibay.cart.service.FileStorageService;
import com.needibay.cart.service.ProductCategoryService;
import com.needibay.cart.util.PaginationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/")
public class CategoryController {

    @Autowired
    ProductCategoryService productCategoryService;

    private final FileStorageService fileStorageService;

    final Environment environment;

    public CategoryController(FileStorageService fileStorageService, Environment environment) {
        this.fileStorageService = fileStorageService;
        this.environment = environment;
    }

    //Code incomplete
    @GetMapping("/category")
    public ResponseEntity<Response> findAll(HttpServletRequest req,
                                            @RequestParam(value = "page", required = false) Integer page,
                                            @RequestParam(value = "sort", required = false) String sortBy,
                                            @RequestParam(value = "order", required = false) String sortOrder
    )
    {
        Pageable pageable = new PaginationBuilder.Build().setPageSequence(page == null ? 0 : page).setPageSize(10).build();
        return new ResponseEntity<Response>(new Response.Build()
                .setSuccess(true)
                .setMessage("Permission Fetched!")
                .setData(productCategoryService.findAll(Pageable.unpaged())).build(), HttpStatus.OK);

    }


    @PostMapping("/category")
    public ResponseEntity<Response> saveCategory(HttpServletRequest req, @RequestParam("image") MultipartFile file)
    {
        //String fileName = file.getOriginalFilename();

        String location = environment.getProperty("app.file.storage.mapping");
        System.out.println(location);


        String fileName = fileStorageService.storeFile(file);

        System.out.print(file);


        return new ResponseEntity<Response>(new Response.Build().setSuccess(true).setMessage("Contact has been saved!").build(), HttpStatus.OK);
    }


}
