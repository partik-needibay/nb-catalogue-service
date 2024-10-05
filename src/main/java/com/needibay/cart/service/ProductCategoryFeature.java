package com.needibay.cart.service;

import com.needibay.cart.dto.MediaGalleryPageBlockUpdateDTO;
import com.needibay.cart.dto.ProductCategoryDTO;
import com.needibay.cart.dto.product.ProductDTO;
import com.needibay.cart.entity.*;
import com.needibay.cart.repository.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
public class ProductCategoryFeature implements Feature {

    @Autowired
    ProductCategoryRepo productCategoryRepo;

    @Autowired
    MediaGalleryRepo mediaGalleryRepo;

    @Autowired
    ProductRepo productRepo;

    @Autowired
    ProductEntityMediaGallery productEntityMediaGallery;

    @Autowired
    ProductDynamicPricingRepo productDynamicPricingRepo;

    @Autowired
    ProductLinkRepo productLinkRepo;

    @Autowired
    ProductEavAttributeRepo productEavAttributeRepo;

    @Autowired
    ProductEavAttributeValueRepo productEavAttributeValueRepo;

    @Value("${BASE_URL}")
    private String baseUrl;

    private final FileStorageService fileStorageService;

    final Environment environment;

    public ProductCategoryFeature(FileStorageService fileStorageService, Environment environment) {
        this.fileStorageService = fileStorageService;
        this.environment = environment;
    }

    public List<ProductDynamicPricing> getDynamicPricingByProductId(Integer productId){

        return productDynamicPricingRepo.getDynamicPricingByProductId(productId);

    }


    public Page<Category> findAllProductCategory(Pageable pageable) {

        return productCategoryRepo.findAllProductCategory(pageable);
    }

    public Page<Category> findEmptyProductCategory(Pageable pageable) {
        return new PageImpl<>(Collections.emptyList(), pageable, 0);
    }

    @Transactional
    public void saveCategory(ProductCategoryDTO productCategoryDTO) {
        Category category = new Category();
        if(productCategoryDTO.getVendorId() != null){
            category.setVendorId(productCategoryDTO.getVendorId());
        }
        if(productCategoryDTO.getParentId() != null){
            category.setParentCategoryId(productCategoryDTO.getParentId());
        }
        category.setCategoryName(productCategoryDTO.getCategoryName());
        category.setCategorySlug(productCategoryDTO.getCategorySlug());
        category.setCategoryDescription(productCategoryDTO.getCategorySlug());
        category.setIsActive(productCategoryDTO.getIsActive());
        category.setMetaDescription(productCategoryDTO.getMetaDescription());
        category.setMetaTitle(productCategoryDTO.getMetaTitle());
        category.setIsStoreVisible(productCategoryDTO.getIsStoreVisible());
        Integer savedId = productCategoryRepo.save(category).getId();
        for( MultipartFile item : productCategoryDTO.getImage()){
            MediaGallery mediaGallery = new MediaGallery();
            mediaGallery.setMediaPath(item.getName());
            mediaGallery.setMediaType(item.getContentType());
            mediaGallery.setModel("PRODUCT_CATEGORY");
            mediaGallery.setModelEntityId(savedId);
            mediaGalleryRepo.save(mediaGallery);
        }

    }

    @Transactional
    public void updateCategoryById(Integer id, ProductCategoryDTO productCategoryDTO) {
        Category category = productCategoryRepo.findById(id).orElseThrow();
        category.setCategoryName(productCategoryDTO.getCategoryName());
        category.setCategorySlug(productCategoryDTO.getCategorySlug());
        category.setCategoryDescription(productCategoryDTO.getDescription());
        category.setIsActive(productCategoryDTO.getIsActive());
        category.setMetaDescription(productCategoryDTO.getMetaDescription());
        category.setMetaTitle(productCategoryDTO.getMetaTitle());
        category.setIsStoreVisible(productCategoryDTO.getIsStoreVisible());
        category.setParentCategoryId(productCategoryDTO.getParentId());
        productCategoryRepo.save(category);
        if(productCategoryDTO.getImage() != null){
            for(MultipartFile item : productCategoryDTO.getImage()){
                String location = environment.getProperty("app.file.storage.mapping");
                String fileName = fileStorageService.storeFile(item);
                MediaGallery mediaGallery = new MediaGallery();
                mediaGallery.setMediaPath(item.getName());
                mediaGallery.setMediaType("image");
                mediaGallery.setModel("PRODUCT_CATEGORY");
                mediaGallery.setModelEntityId(id);
                mediaGallery.setValue(baseUrl + "/contact/assets/images/product/"+fileName);
                mediaGalleryRepo.save(mediaGallery);
            }
        }


    }

    @Transactional
    public void updateCategoryMediaBlockById(MediaGalleryPageBlockUpdateDTO mediaGalleryPageBlockUpdateDTO) {
        MediaGallery mediaGallery = mediaGalleryRepo.findById(mediaGalleryPageBlockUpdateDTO.getId()).orElseThrow();
        mediaGallery.setPageBlockCode(mediaGalleryPageBlockUpdateDTO.getPageBlockCode());
        mediaGalleryRepo.save(mediaGallery);

    }

    @Transactional
    public void removeCategoryMediaBlockById(Long id) {
        mediaGalleryRepo.deleteById(id);
    }


    @Transactional
    public void saveProduct(ProductDTO productDTO) throws CloneNotSupportedException {
        Product product = new Product();
        product.setCategoryId(productDTO.getCategoryId());
        product.setProductType(productDTO.getProductType());
        product.setSku(productDTO.getSku());
        product.setProductName(productDTO.getProductName());
        product.setProductSlug(productDTO.getProductSlug());
        product.setBasePrice(productDTO.getBasePrice());
        product.setBaseCommission(productDTO.getBaseCommission());
        product.setBaseCommissionType(productDTO.getBaseCommissionType());
        product.setHsnCode(productDTO.getHsnCode());
        product.setTaxPercent(productDTO.getTaxPercent());
        product.setIsVariant(productDTO.getIsVariant());
        product.setParentConfigProductId(productDTO.getParentConfigProductId());
        product.setIsDiscounted(productDTO.getIsDiscounted());
        product.setIsCustomizable(productDTO.getIsCustomizable());
        product.setHasOption(productDTO.getHasOption());
        product.setSearchKeywords(productDTO.getSearchKeywords());
        product.setIsActive(productDTO.getIsActive());
        if(productDTO.getIsSampleEnable()){
            product.setIsSampleEnable(productDTO.getIsSampleEnable());
            product.setDeliveryCharge(productDTO.getDeliveryCharge());
        }
        product.setIsStoreVisible(productDTO.getIsStoreVisible());
        product.setMetaTitle(productDTO.getMetaTitle());
        product.setMetaDescription(productDTO.getMetaDescription());
        product.setUrlKey(productDTO.getUrlKey());
        product.setTaxPreference(productDTO.getTaxPreference());
        product.setTaxPercent(productDTO.getTaxPercent());
        product.setGenOne(productDTO.getShortDescription());
        product.setGenTwo(productDTO.getLongDescription());
        Integer productId = productRepo.save(product).getId();

        List<ProductEavAttribute> productAttrByBackendInput = productEavAttributeRepo.findAttrByBackendInput();
        List<ProductEavAttribute> productAttrByFrontendInput = productEavAttributeRepo.findProductConfAttr();

        if(productDTO.getProductType().equals("CONFIGURABLE") && productDTO.getConfiguration() != null){
            JSONArray productConfArr = new JSONArray(productDTO.getConfiguration());
            for (int i = 0; i < productConfArr.length(); i++) {
                JSONObject productConfObj = productConfArr.getJSONObject(i);
                Product productConfiguration = new Product();
                productConfiguration.setProductType("simple");
                productConfiguration.setCategoryId(productDTO.getCategoryId());
                productConfiguration.setProductName(productDTO.getProductName());
                productConfiguration.setProductSlug(productDTO.getProductSlug());
                productConfiguration.setBasePrice(productDTO.getBasePrice());
                productConfiguration.setBaseCommission(productDTO.getBaseCommission());
                productConfiguration.setBaseCommissionType(productDTO.getBaseCommissionType());
                productConfiguration.setHsnCode(productDTO.getHsnCode());
                productConfiguration.setTaxPercent(productDTO.getTaxPercent());
                productConfiguration.setIsVariant(true);
                productConfiguration.setIsDiscounted(productDTO.getIsDiscounted());
                productConfiguration.setIsCustomizable(productDTO.getIsCustomizable());
                productConfiguration.setHasOption(false);
                productConfiguration.setIsActive(productDTO.getIsActive());
                productConfiguration.setSku(productConfObj.getString("sku"));
                productConfiguration.setParentConfigProductId(productId);
                productConfiguration.setBasePrice(productConfObj.getDouble("basePrice"));
                productConfiguration.setIsStoreVisible(productDTO.getIsStoreVisible());
                productConfiguration.setMetaTitle(productDTO.getMetaTitle());
                productConfiguration.setMetaDescription(productDTO.getMetaDescription());
                productConfiguration.setUrlKey(productDTO.getUrlKey());
                productConfiguration.setTaxPreference(productDTO.getTaxPreference());
                productConfiguration.setTaxPercent(productDTO.getTaxPercent());
                productRepo.save(productConfiguration);
            }
            JSONObject productConfAttrObj = new JSONObject(productDTO.getConfAttributes());

            for(ProductEavAttribute item : productAttrByFrontendInput){
                if(productConfAttrObj.has(item.getAttributeCode())){
                    JSONArray attr = productConfAttrObj.getJSONArray(item.getAttributeCode());
                    for (int i = 0; i < attr.length(); i++) {
                        ProductEavAttributeValue productEavAttributeValue = new ProductEavAttributeValue();
                        productEavAttributeValue.setAttributeCode(item.getAttributeCode());
                        productEavAttributeValue.setAttributeId(item.getId());
                        productEavAttributeValue.setEntityId(productId);
                        productEavAttributeValue.setEntityIdSku(productDTO.getSku());
                        productEavAttributeValue.setHasMapping(false);
                        productEavAttributeValue.setParentProduct(product);


                        productEavAttributeValue.setValue(attr.get(i).toString());
                        productEavAttributeValueRepo.save(productEavAttributeValue);
                    }

                }
            }

        }

        if(productDTO.getRelatedProducts() != null) {
            JSONArray relatedProductsJsonArr = new JSONArray(productDTO.getRelatedProducts());
            for (int i = 0; i < relatedProductsJsonArr.length(); i++) {
                JSONObject relatedProductJsonObj = relatedProductsJsonArr.getJSONObject(i);
                ProductLink productLink = new ProductLink();
                productLink.setLink_type("RELATED_PRODUCT");
                productLink.setProduct_id(productId);
                productLink.setLinked_product_id(relatedProductJsonObj.getInt("id"));
                productLinkRepo.save(productLink);
            }
        }
         if(productDTO.getProductDynamicPricing() != null){
             JSONArray productDynamicPricing = new JSONArray(productDTO.getProductDynamicPricing());
             for (int i = 0; i < productDynamicPricing.length(); i++) {
                 JSONObject productDynamicPricingObj = productDynamicPricing.getJSONObject(i);
                 ProductDynamicPricing productDynamicPricingObjNew = new ProductDynamicPricing();
                 productDynamicPricingObjNew.setMinQty(productDynamicPricingObj.getInt("min"));
                 productDynamicPricingObjNew.setMaxQty(productDynamicPricingObj.getInt("max"));
                 productDynamicPricingObjNew.setPrice(productDynamicPricingObj.getDouble("price"));
                 productDynamicPricingObjNew.setProductId(productId);
                 productDynamicPricingRepo.save(productDynamicPricingObjNew);
             }
         }

         if(productDTO.getExtendedAttributes() != null){
             JSONArray extendedAttributesArr = new JSONArray(productDTO.getExtendedAttributes());
             for (int i = 0; i < extendedAttributesArr.length(); i++) {
                 JSONObject extendedAttributesObj = extendedAttributesArr.getJSONObject(i);
                 for(ProductEavAttribute item : productAttrByBackendInput){
                     if(extendedAttributesObj.has(item.getAttributeCode())){
                         ProductEavAttributeValue productEavAttributeValue = new ProductEavAttributeValue();
                         productEavAttributeValue.setAttributeCode(item.getAttributeCode());
                         productEavAttributeValue.setAttributeId(item.getId());
                         productEavAttributeValue.setEntityId(productId);
                         productEavAttributeValue.setEntityIdSku(productDTO.getSku());
                         productEavAttributeValue.setHasMapping(false);
                         productEavAttributeValue.setValue(extendedAttributesObj.getString(item.getAttributeCode()));
                         productEavAttributeValueRepo.save(productEavAttributeValue);
                     }
                 }
             }
         }


        if(productDTO.getImage() != null){
            for(MultipartFile item : productDTO.getImage()){
                String location = environment.getProperty("app.file.storage.mapping");
                String fileName = fileStorageService.storeFile(item);
                ProductMediaGallery mediaGallery = new ProductMediaGallery();
                mediaGallery.setMediaPath(baseUrl + "/contact/assets/images/product/"+fileName);
                mediaGallery.setMediaType("image");
                mediaGallery.setAttributeId(1);
                mediaGallery.setProduct(product);
                mediaGallery.setValue(baseUrl + "/contact/assets/images/product/"+fileName);
                productEntityMediaGallery.save(mediaGallery);
            }
        }
    }

    @Transactional
    public void updateProduct(Integer id, ProductDTO productDTO){
        Product product = productRepo.findById(id).orElseThrow();
        product.setCategoryId(productDTO.getCategoryId());
        product.setProductType(productDTO.getProductType());
        product.setSku(productDTO.getSku());
        product.setProductName(productDTO.getProductName());
        product.setProductSlug(productDTO.getProductSlug());
        product.setBasePrice(productDTO.getBasePrice());
        product.setBaseCommission(productDTO.getBaseCommission());
        product.setBaseCommissionType(productDTO.getBaseCommissionType());
        product.setHsnCode(productDTO.getHsnCode());
        product.setTaxPercent(productDTO.getTaxPercent());
        product.setIsVariant(productDTO.getIsVariant());
        product.setParentConfigProductId(productDTO.getParentConfigProductId());
        product.setIsDiscounted(productDTO.getIsDiscounted());
        product.setIsCustomizable(productDTO.getIsCustomizable());
        product.setHasOption(productDTO.getHasOption());
        product.setIsActive(productDTO.getIsActive());
        product.setIsStoreVisible(productDTO.getIsStoreVisible());
        product.setMetaTitle(productDTO.getMetaTitle());
        product.setMetaDescription(productDTO.getMetaDescription());
        product.setUrlKey(productDTO.getUrlKey());
        product.setTaxPreference(productDTO.getTaxPreference());
        product.setSearchKeywords(productDTO.getSearchKeywords());
        product.setGenOne(productDTO.getShortDescription());
        product.setGenTwo(productDTO.getLongDescription());
        productRepo.save(product);

        if(productDTO.getImage() != null){
            for(MultipartFile item : productDTO.getImage()){
                String location = environment.getProperty("app.file.storage.mapping");
                String fileName = fileStorageService.storeFile(item);
                ProductMediaGallery mediaGallery = new ProductMediaGallery();
                mediaGallery.setMediaPath(baseUrl + "/contact/assets/images/product/"+fileName);
                mediaGallery.setMediaType("image");
                mediaGallery.setAttributeId(1);
                mediaGallery.setProduct(product);
                mediaGallery.setValue(baseUrl + "/contact/assets/images/product/"+fileName);
                productEntityMediaGallery.save(mediaGallery);
            }
        }

        List<ProductEavAttribute> productAttrByBackendInput = productEavAttributeRepo.findAttrByBackendInput();

        if(productDTO.getExtendedAttributes() != null) {
            JSONArray extendedAttributesArr = new JSONArray(productDTO.getExtendedAttributes());
            for (int i = 0; i < extendedAttributesArr.length(); i++) {
                JSONObject extendedAttributesObj = extendedAttributesArr.getJSONObject(i);
                for(ProductEavAttribute item : productAttrByBackendInput){
                    if(extendedAttributesObj.has(item.getAttributeCode())){
                        ProductEavAttributeValue productEavAttributeValueObj = productEavAttributeValueRepo.findEavAttributeValueByEntityIdAndAttributeCode(item.getAttributeCode(), id);
                        if(productEavAttributeValueObj == null){
                            ProductEavAttributeValue productEavAttributeValue = new ProductEavAttributeValue();
                            productEavAttributeValue.setAttributeCode(item.getAttributeCode());
                            productEavAttributeValue.setAttributeId(item.getId());
                            productEavAttributeValue.setEntityId(id);
                            productEavAttributeValue.setEntityIdSku(productDTO.getSku());
                            productEavAttributeValue.setHasMapping(false);
                            productEavAttributeValue.setValue(extendedAttributesObj.getString(item.getAttributeCode()));
                            productEavAttributeValueRepo.save(productEavAttributeValue);

                        }else{
                            productEavAttributeValueObj.setValue(extendedAttributesObj.getString(item.getAttributeCode()));
                            productEavAttributeValueRepo.save(productEavAttributeValueObj);
                        }
                    }
                }
            }
        }


        if(productDTO.getProductDynamicPricing() != null) {
            JSONArray productDynamicPricing = new JSONArray(productDTO.getProductDynamicPricing());
            for (int i = 0; i < productDynamicPricing.length(); i++) {
                JSONObject productDynamicPricingObj = productDynamicPricing.getJSONObject(i);
                ProductDynamicPricing productDynamicPricingObjNew = new ProductDynamicPricing();
                productDynamicPricingObjNew.setMinQty(productDynamicPricingObj.getInt("min"));
                productDynamicPricingObjNew.setMaxQty(productDynamicPricingObj.getInt("max"));
                productDynamicPricingObjNew.setPrice(productDynamicPricingObj.getDouble("price"));
                productDynamicPricingObjNew.setProductId(id);
                productDynamicPricingRepo.save(productDynamicPricingObjNew);
            }
        }

        if(productDTO.getRelatedProducts() != null) {
            JSONArray relatedProductsJsonArr = new JSONArray(productDTO.getRelatedProducts());
            for (int i = 0; i < relatedProductsJsonArr.length(); i++) {
                JSONObject relatedProductJsonObj = relatedProductsJsonArr.getJSONObject(i);
                ProductLink productLink = new ProductLink();
                productLink.setLink_type("RELATED_PRODUCT");
                productLink.setProduct_id(id);
                productLink.setLinked_product_id(relatedProductJsonObj.getInt("id"));
                productLinkRepo.save(productLink);
            }
        }



    }

    @Transactional
    public void disableProduct(ProductDTO productDTO){
        Product product = new Product();
        product.setCategoryId(productDTO.getCategoryId());
        product.setProductType(productDTO.getProductType());
        product.setSku(productDTO.getSku());
        product.setProductName(productDTO.getProductName());
        product.setProductSlug(productDTO.getProductSlug());
        product.setBasePrice(productDTO.getBasePrice());
        product.setBaseCommission(productDTO.getBaseCommission());
        product.setBaseCommissionType(productDTO.getBaseCommissionType());
        product.setHsnCode(productDTO.getHsnCode());
        product.setTaxPercent(productDTO.getTaxPercent());
        product.setIsVariant(productDTO.getIsVariant());
        product.setParentConfigProductId(productDTO.getParentConfigProductId());
        product.setIsDiscounted(productDTO.getIsDiscounted());
        product.setIsCustomizable(productDTO.getIsCustomizable());
        product.setHasOption(productDTO.getHasOption());
        product.setIsActive(productDTO.getIsActive());
        productRepo.save(product);

    }

    public byte[] exportProductDataToExcel() {
        Iterable<Category> data = productCategoryRepo.findAll();

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Data");

            Row headerRow = sheet.createRow(0);
            String[] headers = {"Product Name", "Product SKU", "Product Price"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }
            int rowNum = 1;
            for (Category entity : data) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(entity.getCategoryName());
                row.createCell(1).setCellValue(entity.getCategorySlug());
                row.createCell(2).setCellValue(entity.getIsActive());
            }

            workbook.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public byte[] exportCategoryDataToExcel() {
        Iterable<Category> data = productCategoryRepo.findAll();

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Data");

            Row headerRow = sheet.createRow(0);
            String[] headers = {"Category Name", "Category Slug", "Category Active"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            int rowNum = 1;
            for (Category entity : data) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(entity.getCategoryName());
                row.createCell(1).setCellValue(entity.getCategorySlug());
                row.createCell(2).setCellValue(entity.getIsActive());
            }

            workbook.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Transactional
    public void disableProductByProductId(Integer id){
        try{

            Product product = productRepo.findById(id).orElseThrow();
            product.setIsActive(false);
            productRepo.save(product);

        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public void deleteProductImageByImageId(Long imageId) {
        productEntityMediaGallery.deleteById(imageId);
    }

    public void deleteProductDynamicPricingByPricingId(Long pricingId){
        productDynamicPricingRepo.deleteById(pricingId);
    }

    public void deleteRelatedProductById(Long id){
        productLinkRepo.deleteById(id);
    }

    /*@Transactional
    public void removeCategoryMedia(ProductCategoryDTO productCategoryDTO) {
        Category category = productCategoryRepo.findById(productCategoryDTO.getId()).orElseThrow();
        for(MultipartFile item : productCategoryDTO.getImage()){
            MediaGallery mediaGallery = new MediaGallery();
            mediaGallery.setMediaPath(item.getName());
            mediaGallery.setMediaType("image");
            mediaGallery.setModel("PRODUCT_CATEGORY");
            mediaGallery.setModelEntityId(productCategoryDTO.getId());
            mediaGalleryRepo.(mediaGallery);
        }

    }*/


}
