package com.needibay.cart.controller;


import com.needibay.cart.entity.Product;
import com.needibay.cart.exception.PartialContentException;
import com.needibay.cart.repository.ProductOfferRepo;
import com.needibay.cart.repository.ProductRepo;
import com.needibay.cart.response.Response;
import com.needibay.cart.search.product.ProductSearchDTO;
import com.needibay.cart.search.product.ProductSpecificationBuilder;
import com.needibay.cart.search.product.SearchCriteria;
import com.needibay.cart.service.FileStorageService;
import com.needibay.cart.service.ProductService;
import com.needibay.cart.util.PaginationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.env.Environment;


import javax.activation.FileTypeMap;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
public class ProductController {

    @Autowired
    ProductService productService;

    private final FileStorageService fileStorageService;

    final Environment environment;

    public ProductController(FileStorageService fileStorageService, Environment environment) {
        this.fileStorageService = fileStorageService;
        this.environment = environment;
    }


    //tested it is throwing partial exception when no products is found
    @GetMapping("/product")
    public ResponseEntity<Response> findAll(HttpServletRequest req)
    {

        List<Product> products = productService.findAll();

        if (products.isEmpty()) {
            throw new PartialContentException("No products available, partial content returned.");
        }
        Response response = new Response.Build()
                .setSuccess(true)
                .setMessage("Products fetched successfully!")
                .setData(products)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    //Not fully tested
    @GetMapping("/product/pagination")
    public ResponseEntity<Response> findAllProductWithPagination(HttpServletRequest req,
                                                                 @RequestParam(value = "page", required = false) Integer page,
                                                                 @RequestParam(value = "sort", required = false) String sortBy,
                                                                 @RequestParam(value = "order", required = false) String sortOrder)
    {
        Pageable pageable = new PaginationBuilder.Build().setPageSequence(page == null ? 0 : page).setPageSize(9).build();
        Page<Product> products = productService.getFeature().productBySlugFeature().findProductWithPagination(pageable);
        if (products == null || products.isEmpty()) {
            throw new PartialContentException("Partial content error: Some products could not be fetched.");
        }
        return new ResponseEntity<Response>(new Response.Build()
                .setSuccess(true)
                .setMessage("Permission Fetched!")
                .setData(products)
                .build(), HttpStatus.OK);

    }
    //tested
    @GetMapping("/product/{slug}")
    public ResponseEntity<Response> findProductBySlug(HttpServletRequest req, @PathVariable String slug)
    {
        Product productBySlug = productService.getFeature().productBySlugFeature().findProductBySlug(slug);

        if (productBySlug == null) {
            throw new PartialContentException("Partial content error: Product with slug '" + slug + "' could not be fetched.");
        }
        Response response = new Response.Build()
                .setSuccess(true)
                .setMessage("Product fetched successfully!")
                .setData(productBySlug)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);

    }
    //ALways showing "Permission fetched" No output
    @GetMapping("/get/product/{id}")
    public ResponseEntity<Response> findProductById(@PathVariable Integer id)
    {
        return new ResponseEntity<Response>(new Response.Build()
                .setSuccess(true).setMessage("Permission Fetched!")
                .setData(productService.findById(id))
                .build(), HttpStatus.OK);

    }
    //All Products are showing here
    @GetMapping("/product/variation/{productId}")
    public ResponseEntity<Response> findProductVariationByProductId(HttpServletRequest req)
    {
        // Add Feature Product Feature Provider with Factory Pattern
        return new ResponseEntity<Response>(new Response.Build().setSuccess(true).setMessage("Permission Fetched!").setData(productService.findAll()).build(), HttpStatus.OK);

    }

    //ALl products are showing
    @GetMapping("/product/configuration/{productId}")
    public ResponseEntity<Response> findProductConfigurationByProductId(HttpServletRequest req)
    {

        return new ResponseEntity<Response>(new Response.Build().setSuccess(true).setMessage("Permission Fetched!").setData(productService.findAll()).build(), HttpStatus.OK);

    }
    //Need to discuss how to put exception
    @PostMapping("/product/search")
    public ResponseEntity<Response> searchProduct(@RequestParam(name = "pageNum", defaultValue = "0") int pageNum,
                                                  @RequestParam(name = "pageSize", defaultValue = "9") int pageSize,
                                                  @RequestBody ProductSearchDTO productSearchDTO){
        ProductSpecificationBuilder builder = new ProductSpecificationBuilder();
        /*
        * Product Search Default Criteria Added
        * */
        List<SearchCriteria> criteriaList = productSearchDTO.getSearchCriteriaList();
        SearchCriteria isActiveOnly = new SearchCriteria();
        isActiveOnly.setValue(true);
        isActiveOnly.setOperation("eq");
        isActiveOnly.setFilterKey("isActive");
        isActiveOnly.setDataOption("all");
        isActiveOnly.setAttributeValue("true");
        criteriaList.add(isActiveOnly);

        SearchCriteria isVariant = new SearchCriteria();
        isVariant.setValue(false);
        isVariant.setOperation("eq");
        isVariant.setFilterKey("isVariant");
        isVariant.setDataOption("all");
        isVariant.setAttributeValue("true");
        criteriaList.add(isVariant);

        /*
         * ========
         * */

        if(criteriaList != null){
            criteriaList.forEach(x-> {x.setDataOption(productSearchDTO.getDataOption());
                builder.with(x);
            });

        }
        Pageable page = PageRequest.of(pageNum, pageSize, Sort.by("id")
                .ascending());

        Page<Product> productPage = productService.findBySearchCriteria(builder.build(), page);

        return new ResponseEntity<Response>(new Response.Build()
                .setSuccess(true)
                .setMessage("Product Search Result!")
                .setData(productPage).build(), HttpStatus.OK);

    }
    //Not working
    @PostMapping("/product/search-by-image")
    public ResponseEntity<Response> saveCategory(HttpServletRequest req, @RequestParam("image") MultipartFile file)
    {
        //String fileName = file.getOriginalFilename();

        String location = environment.getProperty("app.file.storage.mapping");
        System.out.println(location);


        String fileName = fileStorageService.storeFile(file);

        System.out.print(file);

        return new ResponseEntity<Response>(new Response.Build()
                .setData(productService.getFeature().productBySlugFeature().productImageAnalysis(fileName))
                .setSuccess(true)
                .setMessage("Image saved for searching!")
                .build(), HttpStatus.OK);
    }
    //WORKING BUT NEED TO TEST
    @GetMapping("/product/search-by-image/{fileName}")
    public ResponseEntity<byte[]> getUploadedFile(@PathVariable String fileName) throws IOException {
        String location = environment.getProperty("app.file.storage.mapping");
        File file = new File("./uploads/files/" + fileName);
        return ResponseEntity.ok()
                // uncomment below to enable download option on browser
//               .header("Content-Disposition", "attachment; filename=" +file.getName())
                .contentType(MediaType.valueOf(FileTypeMap.getDefaultFileTypeMap().getContentType(file)))
                .body(Files.readAllBytes(file.toPath()));
    }

    //There was an error in entity class that's why previously showing SQL exception : Mail to Gaurav
    //TESTED
    @GetMapping("/product/offer/{offerCode}")
    public ResponseEntity<Response> findProductOfferByOfferCode(@PathVariable String offerCode)
    {
        Object productOffer = productService.getFeature().productBySlugFeature().findProductOfferByOfferCode(offerCode);

        if (productOffer == null) {
            throw new PartialContentException("Partial content error: Offer with code '" + offerCode + "' could not be fetched.");
        }
        Response response = new Response.Build()
                .setSuccess(true)
                .setMessage("Product offer fetched successfully!")
                .setData(productOffer)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    //WORKING BUT NEED TO DISCUSS
    @GetMapping("/assets/images/product/{fileName}")
    public ResponseEntity<byte[]> getMediaFile(@PathVariable String fileName) throws IOException {
        String location = environment.getProperty("app.file.storage.mapping");
        File file = new File("./uploads/files/" + fileName);
        return ResponseEntity.ok()
                // uncomment below to enable download option on browser
                //.header("Content-Disposition", "attachment; filename=" +file.getName())
                .contentType(MediaType.valueOf(FileTypeMap.getDefaultFileTypeMap().getContentType(file)))
                .body(Files.readAllBytes(file.toPath()));
    }

    
}

