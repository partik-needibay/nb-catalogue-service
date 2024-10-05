package com.needibay.cart.service;

import com.needibay.cart.component.IServiceCommand;
import com.needibay.cart.component.PostServiceCommand;
import com.needibay.cart.component.ServiceInvoker;
import com.needibay.cart.dto.GoogleVisionDTO;
import com.needibay.cart.entity.Product;
import com.needibay.cart.entity.ProductEavAttribute;
import com.needibay.cart.entity.ProductOffer;
import com.needibay.cart.exception.PartialContentException;
import com.needibay.cart.libs.GoogleVision.GoogleVision;
import com.needibay.cart.repository.ProductEavAttributeRepo;
import com.needibay.cart.repository.ProductOfferRepo;
import com.needibay.cart.repository.ProductRepo;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductByFeature implements Feature {

    @Autowired
    ProductRepo productRepo;

    @Autowired
    ProductOfferRepo productOfferRepo;

    @Autowired
    ProductEavAttributeRepo productEavAttributeRepo;

    private final FileStorageService fileStorageService;

    final Environment environment;

    @Value("${BASE_URL}")
    private String baseUrl;

    public ProductByFeature(FileStorageService fileStorageService, Environment environment) {
        this.fileStorageService = fileStorageService;
        this.environment = environment;
    }

    public Product findProductBySlug(String slug){

        return productRepo.findProductBySlug(slug);

    }

    public Page<Product> findProductWithPagination(Pageable pageable){

        return productRepo.findAllWithPagination(pageable);

    }

    public ProductOffer findProductOfferByOfferCode(String offerCode){

        return productOfferRepo.findProductOfferByOfferCode(offerCode);

    }

    public List<ProductEavAttribute> findAllProductAttribute() {
        List<ProductEavAttribute> attributes = productEavAttributeRepo.findAll();

        if (attributes == null || attributes.isEmpty()) {
            throw new PartialContentException("Partial content error: No product attributes could be fetched.");
        }

        return attributes;
    }

    public String productImageAnalysis(String fileName){

        List<GoogleVisionDTO.Feature> featureList = new ArrayList<>();
        List<String> featureTypeList = new ArrayList<>();
        featureTypeList.add("LABEL_DETECTION");
        featureTypeList.add("OBJECT_LOCALIZATION");
        featureTypeList.add("SAFE_SEARCH_DETECTION");
        featureTypeList.add("PRODUCT_SEARCH");
        GoogleVisionDTO.Source source =  new GoogleVisionDTO.Source();
        source.setImageUri(baseUrl + "/contact/product/search-by-image/" + fileName);
        //source.setImageUri("https://staging.needibay.com/api/v1/contact/product/search-by-image/1721116694589-file.jpg");
        GoogleVisionDTO.Image image =  new GoogleVisionDTO.Image();
        image.setSource(source);

        for (String type: featureTypeList) {
            GoogleVisionDTO.Feature feature =  new GoogleVisionDTO.Feature();
            feature.setType(type);
            featureList.add(feature);
        }

        GoogleVisionDTO.Request request =  new GoogleVisionDTO.Request();
        request.setFeatures(featureList);
        request.setImage(image);

        List<GoogleVisionDTO.Request> requestList = new ArrayList<>();
        requestList.add(request);

        GoogleVisionDTO googleVisionDTO = new GoogleVisionDTO();
        googleVisionDTO.setParent("");
        googleVisionDTO.setRequests(requestList);


        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        GoogleVision vision = new GoogleVision();
        vision.setGoogleVisionAPIUrl("https://vision.googleapis.com/v1/images:annotate?key=AIzaSyD6lLEo3Sdw72fdmjFvm1_1T6Q4MqhCAmo");
        vision.setGoogleVisionAPIHeaders(headers);
        vision.setGoogleVisionAPIRequestBody(googleVisionDTO);
        IServiceCommand serviceCommand = new PostServiceCommand(vision);
        ServiceInvoker serviceInvoker = new ServiceInvoker(serviceCommand);
        Object response = serviceCommand.execute();

        JSONObject myObject = new JSONObject(response);

        String jsonResArr = myObject.getJSONObject("body").getJSONArray("responses").getJSONObject(0).getJSONArray("labelAnnotations").toString();

        return jsonResArr;
    }


    public String saveProductSearchByImage(){

        return "testing";

    }
}
