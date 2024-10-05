package com.needibay.cart.controller.admin;

import com.needibay.cart.dto.*;
import com.needibay.cart.dto.response.OrderPlacementByCartIdDTO;
import com.needibay.cart.dto.response.OrderPlacementBySystemDTO;
import com.needibay.cart.dto.coupon.CouponDTO;
import com.needibay.cart.dto.order.OrderBySystem;
import com.needibay.cart.dto.product.ProductDTO;
import com.needibay.cart.entity.Category;
import com.needibay.cart.entity.coupon.Coupon;
import com.needibay.cart.entity.order.SalesOrder;
import com.needibay.cart.exception.PartialContentException;
import com.needibay.cart.response.Response;
import com.needibay.cart.service.FileStorageService;
import com.needibay.cart.service.ProductCategoryService;
import com.needibay.cart.service.ProductService;
import com.needibay.cart.service.VendorService;
import com.needibay.cart.service.cart.CartService;
import com.needibay.cart.service.coupon.CouponService;
import com.needibay.cart.service.invoice.InvoiceService;
import com.needibay.cart.service.order.SalesOrderService;
import com.needibay.cart.service.payment.PaymentService;
import com.needibay.cart.util.PaginationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/")
public class AdminController {

    @Autowired
    SalesOrderService salesOrderService;

    @Autowired
    ProductCategoryService productCategoryService;

    @Autowired
    CartService cartService;

    @Autowired
    CouponService couponService;

    @Autowired
    ProductService productService;

    @Autowired
    InvoiceService invoiceService;

    @Autowired
    VendorService vendorService;

    @Autowired
    PaymentService paymentService;

    private final FileStorageService fileStorageService;

    final Environment environment;

    public AdminController(FileStorageService fileStorageService, Environment environment) {
        this.fileStorageService = fileStorageService;
        this.environment = environment;
    }
    //Tested
    @GetMapping("/order")
    public ResponseEntity<Response> findAllOrder(HttpServletRequest request, @RequestParam(value = "page", required = false) Integer page,
                                                 @RequestParam(value = "sort", required = false) String sortBy,
                                                 @RequestParam(value = "order", required = false) String sortOrder){

        Pageable pageable = new PaginationBuilder.Build().setPageSequence(page == null ? 0 : page).setPageSize(10).build();
        Page<SalesOrder> salesOrders = salesOrderService.getFeature().getOrderFeature().findAll(pageable);
        if (salesOrders == null || salesOrders.isEmpty()) {
            throw new PartialContentException("Partial content error: Orders could not be fetched.");
        }
        return new ResponseEntity<Response>(new Response.Build().setSuccess(true)
                .setData(salesOrderService.getFeature().getOrderFeature().findAll(pageable))
                .setMessage("All Orders!").build(), HttpStatus.OK);
    }
    //Tested
    @GetMapping("/order/{id}")
    public ResponseEntity<Response> findOrderById(@PathVariable Long id, HttpServletRequest request){
        SalesOrder salesOrder = salesOrderService.getFeature().getOrderFeature().findById(id);
        Response response = new Response.Build()
                .setSuccess(true)
                .setData(salesOrder)
                .setMessage("Order Details with id " + id)
                .build();
        return ResponseEntity.ok(response);
    }
    //Function not completed
    @DeleteMapping("/order/{id}")
    public ResponseEntity<Response> findAllOrder(@PathVariable Integer id, HttpServletRequest request){
        //salesOrderService.deleteById(id);
        return new ResponseEntity<Response>(new Response.Build().setSuccess(true).setMessage("Order Deleted Successfully!").build(), HttpStatus.OK);

    }


    /*
    * Fetching product category only
    * */

    //Tested
    @GetMapping("/admin/category/product")
    public ResponseEntity<Response> findAllProductCategory(HttpServletRequest req,
                                                           @RequestParam(value = "page", required = false) Integer page,
                                                           @RequestParam(value = "sort", required = false) String sortBy,
                                                           @RequestParam(value = "order", required = false) String sortOrder
    )
    {
        Pageable pageable = new PaginationBuilder.Build()
                .setPageSequence(page == null ? 0 : page)
                .setPageSize(10)
                .build();

        Page<Category> categories = productCategoryService.getFeature().getProductCategoryFeature().findAllProductCategory(pageable);

        if (categories == null || categories.isEmpty()) {
            throw new PartialContentException("Partial content error: No product categories could be fetched.");
        }
        Response response = new Response.Build()
                .setSuccess(true)
                .setMessage("Categories fetched successfully!")
                .setData(categories)
                .build();

        return ResponseEntity.ok(response);

    }

    /*
     * Updating product category only
     * */
    //Not done
    @PutMapping("/admin/category/{id}")
        public ResponseEntity<Response> updateCategoryById(@Valid @PathVariable Integer id, ProductCategoryDTO productCategoryDTO)
    {
        System.out.print(productCategoryDTO);
        productCategoryService.getFeature().getProductCategoryFeature().updateCategoryById(id, productCategoryDTO);
        return new ResponseEntity<Response>(new Response.Build()
                .setSuccess(true)
                .setMessage("Category Saved!")
                .build(), HttpStatus.OK);

    }

    /*
     * Updating product category only
     * */
    //Not done
    @GetMapping("/admin/category/export")
    public ResponseEntity<byte[]> exportCategory()
    {
        byte[] excelData = productCategoryService.getFeature().getProductCategoryFeature().exportCategoryDataToExcel();
        return ResponseEntity
                .ok()
                .header("Content-Disposition", "attachment; filename=data.xlsx")
                .body(excelData);

    }

    /*
     * Updating category block image
     * */
    //Not done
    @PostMapping("/admin/category/media/block")
    public ResponseEntity<Response> updateCategoryMediaBlockById(MediaGalleryPageBlockUpdateDTO mediaGalleryPageBlockUpdateDTO)
    {
        productCategoryService.getFeature().getProductCategoryFeature().updateCategoryMediaBlockById(mediaGalleryPageBlockUpdateDTO);
        return new ResponseEntity<Response>(new Response.Build()
                .setSuccess(true)
                .setMessage("Category Saved!")
                .build(), HttpStatus.OK);

    }

    /**
     *
     * @param id
     * @return
     */
    //Not done
    @DeleteMapping("/admin/category/media/block/{id}")
    public ResponseEntity<Response> removeCategoryMediaBlockById(@PathVariable Long id)
    {
        productCategoryService.getFeature().getProductCategoryFeature().removeCategoryMediaBlockById(id);
        return new ResponseEntity<Response>(new Response.Build()
                .setSuccess(true)
                .setMessage("Category Saved!")
                .build(), HttpStatus.OK);

    }

    /**
     *
     * @param productCategoryDTO
     * @return
     */
    //Not done
    @PostMapping(value ="/admin/category")
    public ResponseEntity<Response> saveCategory(ProductCategoryDTO productCategoryDTO)
    {
        System.out.print(productCategoryDTO);
        productCategoryService.getFeature().getProductCategoryFeature().saveCategory(productCategoryDTO);
        return new ResponseEntity<Response>(new Response.Build()
                .setSuccess(true)
                .setMessage("Category Saved!")
                .build(), HttpStatus.OK);

    }

    /**
     * Objective: Create New Coupon
     * @param couponDTO
     * @return ResponseEntity<Response>
     */
    //Not done
    @PostMapping("/admin/coupon")
    public ResponseEntity<Response> saveCoupon(@Valid @RequestBody CouponDTO couponDTO)
    {
        couponService.getFeature().getCouponFeature().saveCoupon(couponDTO);
        return new ResponseEntity<Response>(new Response.Build()
                .setSuccess(true)
                .setMessage("Coupon Has Created Successfully!")
                .build(), HttpStatus.OK);

    }

    /**
     * Objective: Fetch All Coupon
     * @return List<Coupon>
     *
     */
    //Already exception is there
    @GetMapping("/admin/coupon")
    public ResponseEntity<Response> findAll() {
        List<Coupon> coupons = cartService.getFeature().getCartItemFeature().findAllCoupons();
        return new ResponseEntity<Response>(new Response.Build()
                .setSuccess(true).setData(coupons).setMessage("Permission Fetched!")
                .build(), HttpStatus.OK);
    }


    /**
     * Objective: Create New Product By Admin
     * @param productDTO
     * @return
     * @throws CloneNotSupportedException
     */
    //Not done
    @PostMapping("/admin/product")
    public ResponseEntity<Response> saveProduct(ProductDTO productDTO) throws CloneNotSupportedException {
        productCategoryService.getFeature().getProductCategoryFeature().saveProduct(productDTO);
        return new ResponseEntity<Response>(new Response.Build()
                .setSuccess(true).setMessage("Product Created Successfully!")
                .build(), HttpStatus.OK);
    }
    /**
     *
     * Objective: Update Product By Admin
     * @param id
     * @param productDTO
     * @return
     * @throws CloneNotSupportedException
     */
    //Not done
    @PutMapping("/admin/product/{id}")
    public ResponseEntity<Response> updateProduct(@Valid @PathVariable Integer id, ProductDTO productDTO) throws CloneNotSupportedException {
        productCategoryService.getFeature().getProductCategoryFeature().updateProduct(id, productDTO);
        return new ResponseEntity<Response>(new Response.Build()
                .setSuccess(true).setMessage("Permission Fetched!")
                .build(), HttpStatus.OK);
    }

    /**
     * Objective: Fetching Product data by product Id
     * @param id Integer
     * @return Product
     * @Validation id Integer
     */
    //Done
    @GetMapping("/admin/product/{id}")
    public ResponseEntity<Response> findProductById(@PathVariable Integer id)
    {
        return new ResponseEntity<Response>(new Response.Build()
                .setSuccess(true).setMessage("Permission Fetched!")
                .setData(productService.findById(id))
                .build(), HttpStatus.OK);

    }

    @DeleteMapping("/admin/product/image/{imageId}")
    public ResponseEntity<Response> deleteProductImageByImageId(@PathVariable Long imageId)
    {
        productCategoryService.getFeature().getProductCategoryFeature().deleteProductImageByImageId(imageId);
        return new ResponseEntity<Response>(new Response.Build()
                .setSuccess(true).setMessage("Product Image Deleted!")
                .build(), HttpStatus.OK);

    }

    @DeleteMapping("/admin/product/dynamic-pricing/{pricingId}")
    public ResponseEntity<Response> deleteProductDynamicPricingByPricingId(@PathVariable Long pricingId)
    {
        productCategoryService.getFeature().getProductCategoryFeature().deleteProductDynamicPricingByPricingId(pricingId);
        return new ResponseEntity<Response>(new Response.Build()
                .setSuccess(true).setMessage("Product Pricing Deleted!")
                .build(), HttpStatus.OK);

    }

    @DeleteMapping("/admin/product/related-product/{id}")
    public ResponseEntity<Response> deleteRelatedProductById(@PathVariable Long id)
    {
        productCategoryService.getFeature().getProductCategoryFeature().deleteRelatedProductById(id);
        return new ResponseEntity<Response>(new Response.Build()
                .setSuccess(true).setMessage("Product Relation Deleted!")
                .build(), HttpStatus.OK);

    }

    /**
     *
     * @param id
     * @return
     */
    @PatchMapping("/admin/product/{id}")
    public ResponseEntity<Response> disableProductByProductId(@PathVariable Integer id)
    {
        productCategoryService.getFeature().getProductCategoryFeature().disableProductByProductId(id);
        return new ResponseEntity<Response>(new Response.Build()
                .setSuccess(true).setMessage("Product Disabled Successfully!")
                .build(), HttpStatus.OK);
    }


    /*
    * ProductEAVAttribute
    * Request Mapping
    * */

    /*
    Done
     */
    @GetMapping("/admin/product/attribute")
    public ResponseEntity<Response> findAllProductAttribute() {

        return new ResponseEntity<Response>(new Response.Build()
                .setSuccess(true).setData(productService.getFeature().productBySlugFeature().findAllProductAttribute()).setMessage("Permission Fetched!")
                .build(), HttpStatus.OK);
    }

    /**
     * Get All Invoice
     * */
    //Done
    @GetMapping("/admin/invoice")
    public ResponseEntity<Response> findAllSalesOrderInvoice() {

        return new ResponseEntity<Response>(new Response.Build()
                .setSuccess(true).setData(invoiceService.getFeature().getInvoiceFeature().findAll()).setMessage("all invoice")
                .build(), HttpStatus.OK);
    }



    /**
     * Saving Invoice By Admin
     * @param invoiceDTO
     * @return
     */
    @PostMapping("/admin/invoice")
    public ResponseEntity<Response> createInvoice(InvoiceDTO invoiceDTO) {

        invoiceService.getFeature().getInvoiceFeature().createInvoice(invoiceDTO);

        return new ResponseEntity<Response>(new Response.Build()
                .setSuccess(true).setMessage("all invoice")
                .build(), HttpStatus.OK);
    }

    @PutMapping("/admin/invoice")
    public ResponseEntity<Response> updateInvoice(@PathVariable Long id, InvoiceDTO invoiceDTO) {

        invoiceService.getFeature().getInvoiceFeature().updateInvoice(id, invoiceDTO);

        return new ResponseEntity<Response>(new Response.Build()
                .setSuccess(true).setMessage("all invoice")
                .build(), HttpStatus.OK);
    }

    /**
     * Preview Invoice By Admin
     * @param invoiceDTO
     * @return
     */
    @PostMapping("/admin/invoice/preview")
    public ResponseEntity<Response> previewInvoice(InvoiceDTO invoiceDTO) {

        return new ResponseEntity<Response>(new Response.Build()
                .setSuccess(true)
                .setData(invoiceService.getFeature().getInvoiceFeature().previewInvoice(invoiceDTO)).setMessage("Invoice Preview!")
                .build(), HttpStatus.OK);

    }

    /**
     * Preview Invoice By Admin
     * @param quotationDTO
     * @return
     */
    @PostMapping("/admin/quotation/preview")
    public ResponseEntity<Response> previewQuotation(QuotationDTO quotationDTO) {

        return new ResponseEntity<Response>(new Response.Build()
                .setSuccess(true)
                .setData(invoiceService.getFeature().getInvoiceFeature().previewQuotation(quotationDTO)).setMessage("Invoice Preview!")
                .build(), HttpStatus.OK);

    }

    /**
     * Preview Invoice By Admin
     * @param purchaseOrderDTO
     * @return
     */
    @PostMapping("/admin/purchase-order/preview")
    public ResponseEntity<Response> previewPurchaseOrder(PurchaseOrderDTO purchaseOrderDTO) {

        return new ResponseEntity<Response>(new Response.Build()
                .setSuccess(true)
                .setData(invoiceService.getFeature().getInvoiceFeature().previewPurchaseOrder(purchaseOrderDTO)).setMessage("Invoice Preview!")
                .build(), HttpStatus.OK);

    }

    /**
     * Get All Invoice
     * */
    //Not working well
    @GetMapping("/admin/invoice?customerId={customerId}")
    public ResponseEntity<Response> findAllSalesOrderInvoiceByCustomerId(@RequestParam("customerId") Long customerId) {

        return new ResponseEntity<Response>(new Response.Build()
                .setSuccess(true).setData(invoiceService.getFeature().getInvoiceFeature().findInvoiceByCustomerId(customerId)).setMessage("all invoice")
                .build(), HttpStatus.OK);
    }

    /**
     *
     * @param id
     * @return
     */
    //Done
    @GetMapping("/admin/invoice/{id}")
    public ResponseEntity<Response> findInvoiceById(@PathVariable Long id){
        return new ResponseEntity<Response>(new Response.Build().setSuccess(true)
                .setData(invoiceService.getFeature().getInvoiceFeature().findInvoiceById(id))
                .setMessage("All Invoices!")
                .build(), HttpStatus.OK);

    }

    /**
     *
     * @param id
     * @return
     */
    @GetMapping("/admin/quotation/{id}")
    public ResponseEntity<Response> findQuotationById(@PathVariable Long id){
        return new ResponseEntity<Response>(new Response.Build().setSuccess(true)
                .setData(invoiceService.getFeature().getInvoiceFeature().findQuotationById(id))
                .setMessage("All Invoices!")
                .build(), HttpStatus.OK);

    }

    /**
     *
     * @param id
     * @return
     */
    @GetMapping("/admin/purchase-order/{id}")
    public ResponseEntity<Response> findPurchaseOrderById(@PathVariable Long id){
        return new ResponseEntity<Response>(new Response.Build().setSuccess(true)
                .setData(invoiceService.getFeature().getInvoiceFeature().findPurchaseOrderById(id))
                .setMessage("Purchase order details By Id " + id)
                .build(), HttpStatus.OK);

    }

    /**
     *
     * @return
     */
    @GetMapping("/admin/invoice/last-invoice-id")
    public ResponseEntity<Response> findLastGeneratedInvoiceId(){
        return new ResponseEntity<Response>(new Response.Build().setSuccess(true)
                .setData(invoiceService.getFeature().getInvoiceFeature().findLastInvoiceIncrementId())
                .setMessage("Last Invoice Id!")
                .build(), HttpStatus.OK);

    }

    @GetMapping("/admin/purchase-order/last-purchase-order-id")
    public ResponseEntity<Response> findLastGeneratedPurchaseOrderId(){
        return new ResponseEntity<Response>(new Response.Build().setSuccess(true)
                .setData(invoiceService.getFeature().getInvoiceFeature().findLastPurchaseOrderIncrementId())
                .setMessage("Last Purchase Order Id!")
                .build(), HttpStatus.OK);

    }

    @GetMapping("/admin/quotation/last-quotation-id")
    public ResponseEntity<Response> findLastGeneratedQuotationId(){
        return new ResponseEntity<Response>(new Response.Build().setSuccess(true)
                .setData(invoiceService.getFeature().getInvoiceFeature().findLastQuotationIncrementId())
                .setMessage("Last Quotation Id!")
                .build(), HttpStatus.OK);

    }


    /**
     * Get All Quotation
     * */
    //Done
    @GetMapping("/admin/quotation")
    public ResponseEntity<Response> findAllQuotation() {

        return new ResponseEntity<Response>(new Response.Build()
                .setSuccess(true).setData(invoiceService.getFeature().getInvoiceFeature().findAllQuotation()).setMessage("All Quotations!")
                .build(), HttpStatus.OK);
    }

    /**
     * Get All Quotation
     * */
    @PostMapping("/admin/quotation")
    public ResponseEntity<Response>  createQuotation(@Valid QuotationDTO quotationDTO) {

        invoiceService.getFeature().getInvoiceFeature().createQuotation(quotationDTO);

        return new ResponseEntity<Response>(new Response.Build()
                .setSuccess(true).setMessage("Quotation Generated!")
                .build(), HttpStatus.OK);
    }

    /**
     * Get All Quotation
     * */
    @PostMapping("/admin/purchase-order")
    public ResponseEntity<Response> createPurchaseOrder(PurchaseOrderDTO purchaseOrderDTO) {

        invoiceService.getFeature().getInvoiceFeature().createPurchaseOrder(purchaseOrderDTO);

        return new ResponseEntity<Response>(new Response.Build()
                .setSuccess(true).setMessage("Purchase Order Generated!")
                .build(), HttpStatus.OK);
    }

    /**
     * Get All Quotation
     * */
    //Tested
    @GetMapping("/admin/purchase-order")
    public ResponseEntity<Response> findAllPurchaseOrder() {

        return new ResponseEntity<Response>(new Response.Build()
                .setSuccess(true).setData(invoiceService.getFeature().getInvoiceFeature().findAllPurchaseOrder()).setMessage("All Purchase Orders!")
                .build(), HttpStatus.OK);
    }



    /**
     *
     * @param gstin
     * @return
     */

    @PostMapping("/admin/account/verify-gst/{gstin}")
    public ResponseEntity<Response> getGstDetails(@PathVariable String gstin)
    {

        return new ResponseEntity<Response>(new Response.Build()
                .setSuccess(true).setData(vendorService.getFeature().getVendorFeature().getGstDetails(gstin).toMap())
                .setMessage("Contact has been saved!!").build(), HttpStatus.OK);

    }
    //Not working
    @GetMapping("/admin/vendor/{vendorId}")
    public ResponseEntity<Response> findVendorById(@PathVariable Integer vendorId)
    {

        return new ResponseEntity<Response>(new Response.Build()
                .setSuccess(true).setData(vendorService.getFeature().getVendorFeature().findVendorById(vendorId))
                .setMessage("Vendor Details!").build(), HttpStatus.OK);

    }


    @PostMapping("/admin/account")
    public ResponseEntity<Response> createContact(@Valid @RequestBody VendorAccountDTO vendorAccountDTO)
    {

        return new ResponseEntity<Response>(new Response.Build()
                .setSuccess(true).setData(vendorService.getFeature().getVendorFeature().createVendorAccount(vendorAccountDTO))
                .setMessage("Contact has been saved!!").build(), HttpStatus.OK);

    }

    @PostMapping("/admin/account/contact-person")
    public ResponseEntity<Response> createVendorAccountContact(@Valid @RequestBody VendorContactDTO vendorContactDTO)
    {
        vendorService.getFeature().getVendorFeature().createVendorContact(vendorContactDTO);
        return new ResponseEntity<Response>(new Response.Build()
                .setSuccess(true)
                .setMessage("Contact has been saved!!").build(), HttpStatus.OK);

    }
    @PostMapping("/admin/account/address")
    public ResponseEntity<Response> createVendorAddress(@Valid VendorAddressDTO vendorAddressDTO)
    {
        vendorService.getFeature().getVendorFeature().createVendorAddress(vendorAddressDTO);
        return new ResponseEntity<Response>(new Response.Build()
                .setSuccess(true)
                .setMessage("Contact has been saved!!").build(), HttpStatus.OK);

    }

    @PostMapping("/admin/account/bank-details")
    public ResponseEntity<Response> createVendorBankDetails(@Valid @RequestBody VendorBankDTO createVendorBankDetail)
    {
        vendorService.getFeature().getVendorFeature().createVendorBankDetail(createVendorBankDetail);
        return new ResponseEntity<Response>(new Response.Build()
                .setSuccess(true)
                .setMessage("Contact has been saved!!").build(), HttpStatus.OK);

    }

    @PostMapping("/admin/account/documents")
    public ResponseEntity<Response> createVendorDocuments(VendorDocDTO vendorDocDTO)
    {
        vendorService.getFeature().getVendorFeature().uploadVendorDocument(vendorDocDTO);
        return new ResponseEntity<Response>(new Response.Build()
                .setSuccess(true)
                .setMessage("Contact has been saved!!").build(), HttpStatus.OK);

    }

    @GetMapping("/admin/cart")
    public ResponseEntity<Response> findAllCart(){
        return new ResponseEntity<Response>(new Response.Build()
                .setSuccess(true).setData(cartService.findAll()).setMessage("All Carts!")
                .build(), HttpStatus.OK);

    }


    @PostMapping("/admin/order")
    public ResponseEntity<Response> orderByCartId(@RequestBody OrderBySystem orderBySystem){
        OrderPlacementBySystemDTO salesOrder = salesOrderService
                .getFeature()
                .getOrderFeature()
                .placeOrderBySystem(orderBySystem);

        return new ResponseEntity<>(
                new Response.Build()
                        .setData(salesOrder)
                        .setSuccess(true)
                        .setMessage("Order has been placed successfully!")
                        .build(),
                HttpStatus.OK
        );
    }

    @PutMapping("/admin/order/{orderId}/status")
    public ResponseEntity<Response> createContact(@PathVariable Long orderId, @RequestBody OrderStatusUpdateDTO orderStatusUpdateDTO)
    {
        salesOrderService.getFeature().getOrderFeature().orderStatusUpdate(orderId, orderStatusUpdateDTO);
        return new ResponseEntity<Response>(new Response.Build()
                .setSuccess(true)
                .setMessage("Order status has been updated!!").build(), HttpStatus.OK);

    }

    //tested
    @GetMapping("/admin/payment/customer/{customerId}")
    public ResponseEntity<Response> findPaymentByCustomerId(@PathVariable Long customerId){

        return new ResponseEntity<Response>(new Response.Build().setSuccess(true)
                .setData(paymentService.getFeature().getPaymentFeature().findPaymentByCustomerId(customerId))
                .setMessage("Payment By Customer Id!")
                .build(), HttpStatus.OK);

    }


    /**
     * Find All Payment Terms
     * @return
     */
    @GetMapping("/admin/payment/payment-terms")
    public ResponseEntity<Response> findAllPaymentTerms(){

        return new ResponseEntity<Response>(new Response.Build().setSuccess(true)
                .setData(paymentService.getFeature().getPaymentFeature().findAllPaymentTerms())
                .setMessage("All Payment Terms!")
                .build(), HttpStatus.OK);

    }

    @PostMapping("/admin/payment/payment-terms")
    public ResponseEntity<Response> savePaymentTerms(PaymentTermsDTO paymentTermsDTO){

        paymentService.getFeature().getPaymentFeature().savePaymentTerms(paymentTermsDTO);

        return new ResponseEntity<Response>(new Response.Build().setSuccess(true)
                .setMessage("New Payment Term Added!")
                .build(), HttpStatus.OK);

    }

    @DeleteMapping("/admin/payment/payment-terms/{id}")
    public ResponseEntity<Response> deletePaymentTerms(@PathVariable Long id){

        paymentService.getFeature().getPaymentFeature().deletePaymentTerms(id);

        return new ResponseEntity<Response>(new Response.Build().setSuccess(true)
                .setMessage("Payment Deleted Successfully!")
                .build(), HttpStatus.OK);

    }


}
