package com.needibay.cart.service.invoice;

import com.needibay.cart.dto.InvoiceDTO;
import com.needibay.cart.dto.PurchaseOrderDTO;
import com.needibay.cart.dto.QuotationDTO;
import com.needibay.cart.exception.PartialContentException;
import com.needibay.cart.entity.*;
import com.needibay.cart.entity.cart.EavEntityStore;
import com.needibay.cart.entity.invoice.InvoiceItemV2;
import com.needibay.cart.entity.invoice.InvoiceV2;
import com.needibay.cart.repository.ProductRepo;
import com.needibay.cart.repository.PurchaseOrderRepo;
import com.needibay.cart.repository.QuotationItemRepo;
import com.needibay.cart.repository.QuotationRepo;
import com.needibay.cart.repository.cart.EavEntityStoreRepo;
import com.needibay.cart.repository.invoice.InvoiceRepo;
import com.needibay.cart.service.Feature;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class InvoiceFeature implements Feature {

    @Autowired
    InvoiceRepo invoiceRepo;

    @Autowired
    QuotationRepo quotationRepo;

    @Autowired
    QuotationItemRepo quotationItemRepo;

    @Autowired
    PurchaseOrderRepo purchaseOrderRepo;

    @Autowired
    ProductRepo productRepo;

    @Autowired
    EavEntityStoreRepo eavEntityStoreRepo;

    public List<InvoiceV2> findAll(){
        List<InvoiceV2> invoices = invoiceRepo.findAll();

        if (invoices == null || invoices.isEmpty()) {
            throw new PartialContentException("Partial content error: No invoices could be fetched.");
        }

        return invoices;
    }
    //Tested
    public List<QuotationV2> findAllQuotation(){
        List<QuotationV2> quotations = quotationRepo.findAll();

        if (quotations == null || quotations.isEmpty()) {
            throw new PartialContentException("Partial content error: No quotations could be fetched.");
        }

        return quotations;
    }

    public List<PurchaseOrderV2> findAllPurchaseOrder(){
        List<PurchaseOrderV2> purchaseOrderV2s =  purchaseOrderRepo.findAll();
        if(purchaseOrderV2s == null || purchaseOrderV2s.isEmpty()) {
            throw new PartialContentException("Partial content error: No purchaseOrders could be fetched.");
        }
        return purchaseOrderV2s;
    }

    @Transactional
    public void createInvoice(InvoiceDTO invoiceDTO){
        try{
            LocalDate dueDate = LocalDate.parse(invoiceDTO.getDueDate());
            LocalDate dateIssued = LocalDate.parse(invoiceDTO.getDateIssued());
            InvoiceV2 salesInvoice = new InvoiceV2();
            salesInvoice.setInvoiceId(invoiceDTO.getInvoiceId());
            salesInvoice.setIsActive(true);
            salesInvoice.setDueDate(dueDate);
            salesInvoice.setDateIssued(dateIssued);
            salesInvoice.setIsMultiShipping(false);
            salesInvoice.setCurrencyCode("INR");
            salesInvoice.setGrandTotal(invoiceDTO.getGrandTotal());
            salesInvoice.setCheckoutMethod("SYSTEM_ADMIN");
            salesInvoice.setTaxAmount(invoiceDTO.getTax());
            salesInvoice.setSubtotal(invoiceDTO.getSubtotal());
            salesInvoice.setTotalDiscountAmount(invoiceDTO.getDiscountAmount());
            salesInvoice.setSubtotalWithDiscount(invoiceDTO.getSubtotalWithDiscount());
            salesInvoice.setCustomerId(invoiceDTO.getCustomerId());
            salesInvoice.setCustomerEmail(invoiceDTO.getCustomerEmail());
            salesInvoice.setCustomeFullname(invoiceDTO.getCustomerFullName());

            JSONArray itemArr = new JSONArray(invoiceDTO.getItems());

            salesInvoice.setItemsCount(itemArr.length());

            List<InvoiceItemV2> salesInvoiceItemList = new ArrayList<>();

            for (int i = 0; i < itemArr.length(); i++) {

                JSONObject salesInvoiceItemObj = itemArr.getJSONObject(i);

                Product product = productRepo.findById(salesInvoiceItemObj.getInt("item")).orElseThrow();

                Double priceWithCommission = salesInvoiceItemObj.getDouble("rate");

                if(salesInvoiceItemObj.getString("commission").equals("percent")){

                    priceWithCommission = salesInvoiceItemObj.getDouble("rate") + (salesInvoiceItemObj.getDouble("rate")*salesInvoiceItemObj.getDouble("commissionValue"))/100;
                }
                if(salesInvoiceItemObj.getString("commission").equals("flat")){

                    priceWithCommission = salesInvoiceItemObj.getDouble("rate") + salesInvoiceItemObj.getDouble("commissionValue");
                }

                Double rowTotalTaxAmount = priceWithCommission * salesInvoiceItemObj.getInt("qty") * salesInvoiceItemObj.getDouble("taxPercent")/100;

                Double taxAmountPerUnit = priceWithCommission * salesInvoiceItemObj.getDouble("taxPercent")/100;

                Double perUnitPriceIncludingTax = priceWithCommission + taxAmountPerUnit;

                InvoiceItemV2 salesInvoiceItem = new InvoiceItemV2();
                salesInvoiceItem.setRowTotal(salesInvoiceItemObj.getDouble("amount"));
//                salesInvoiceItem.setHsn(salesInvoiceItemObj.getString("hsnCode"));
                salesInvoiceItem.setIsSubItems(false);
                salesInvoiceItem.setHasSubItems(true);
                salesInvoiceItem.setPrice(salesInvoiceItemObj.getDouble("rate"));
                salesInvoiceItem.setPriceWithCommission(priceWithCommission);
                salesInvoiceItem.setQty(salesInvoiceItemObj.getInt("qty"));
                salesInvoiceItem.setProductId(salesInvoiceItemObj.getInt("item"));
                salesInvoiceItem.setSku(product.getSku());
                salesInvoiceItem.setTaxPercent(salesInvoiceItemObj.getDouble("taxPercent"));
                salesInvoiceItem.setCommissionType(salesInvoiceItemObj.getString("commission"));
                salesInvoiceItem.setCommissionValue(salesInvoiceItemObj.getDouble("commissionValue"));
                salesInvoiceItem.setRowTotalTaxAmount(rowTotalTaxAmount);
                salesInvoiceItem.setTaxAmount(taxAmountPerUnit);
                salesInvoiceItem.setPriceInclTax(perUnitPriceIncludingTax);

                salesInvoiceItemList.add(salesInvoiceItem);
            }

            salesInvoice.setInvoiceV2(salesInvoiceItemList);



            Long lastInvoiceIncrementId = eavEntityStoreRepo.findLastInvoiceIncrementId();

            Long incrementLastInvoiceIncrementId = lastInvoiceIncrementId + 1;

            eavEntityStoreRepo.updateLastInvoiceIncrementId(incrementLastInvoiceIncrementId);

            invoiceRepo.save(salesInvoice);



            /**
             * Add the code here if we want to generate order from invoice
             */

            //todo conditional email sending with the invoice



        }catch (Exception e){
            //todo Add logger and send email to dev mail group!
            throw new RuntimeException(e.getMessage());
        }

    }

    @Transactional
    public void updateInvoice(Long id, InvoiceDTO invoiceDTO){
        try{

            LocalDate dueDate = LocalDate.parse(invoiceDTO.getDueDate());
            LocalDate dateIssued = LocalDate.parse(invoiceDTO.getDateIssued());

            InvoiceV2 salesInvoice = invoiceRepo.findById(id).orElseThrow();
            salesInvoice.setInvoiceId(invoiceDTO.getInvoiceId());
            salesInvoice.setIsActive(true);
            salesInvoice.setDueDate(dueDate);
            salesInvoice.setDateIssued(dateIssued);
            salesInvoice.setIsMultiShipping(false);
            salesInvoice.setCurrencyCode("INR");
            salesInvoice.setGrandTotal(invoiceDTO.getGrandTotal());
            salesInvoice.setCheckoutMethod("SYSTEM_ADMIN");
            salesInvoice.setTaxAmount(invoiceDTO.getTax());
            salesInvoice.setSubtotal(invoiceDTO.getSubtotal());
            salesInvoice.setTotalDiscountAmount(invoiceDTO.getDiscountAmount());
            salesInvoice.setSubtotalWithDiscount(invoiceDTO.getSubtotalWithDiscount());
            salesInvoice.setCustomerId(invoiceDTO.getCustomerId());
            salesInvoice.setCustomerEmail(invoiceDTO.getCustomerEmail());
            salesInvoice.setCustomeFullname(invoiceDTO.getCustomerFullName());

            JSONArray itemArr = new JSONArray(invoiceDTO.getItems());

            salesInvoice.setItemsCount(itemArr.length());

            List<InvoiceItemV2> salesInvoiceItemList = new ArrayList<>();

            for (int i = 0; i < itemArr.length(); i++) {

                JSONObject salesInvoiceItemObj = itemArr.getJSONObject(i);

                Product product = productRepo.findById(salesInvoiceItemObj.getInt("item")).orElseThrow();

                Double priceWithCommission = salesInvoiceItemObj.getDouble("rate");

                if(salesInvoiceItemObj.getString("commission").equals("percent")){

                    priceWithCommission = salesInvoiceItemObj.getDouble("rate") + (salesInvoiceItemObj.getDouble("rate")*salesInvoiceItemObj.getDouble("commissionValue"))/100;
                }
                if(salesInvoiceItemObj.getString("commission").equals("flat")){

                    priceWithCommission = salesInvoiceItemObj.getDouble("rate") + salesInvoiceItemObj.getDouble("commissionValue");
                }

                Double rowTotalTaxAmount = priceWithCommission * salesInvoiceItemObj.getInt("qty") * salesInvoiceItemObj.getDouble("taxPercent")/100;

                Double taxAmountPerUnit = priceWithCommission * salesInvoiceItemObj.getDouble("taxPercent")/100;

                Double perUnitPriceIncludingTax = priceWithCommission + taxAmountPerUnit;

                InvoiceItemV2 salesInvoiceItem = new InvoiceItemV2();
                salesInvoiceItem.setRowTotal(salesInvoiceItemObj.getDouble("amount"));
                salesInvoiceItem.setHsn(salesInvoiceItemObj.getString("hsnCode"));
                salesInvoiceItem.setIsSubItems(false);
                salesInvoiceItem.setHasSubItems(true);
                salesInvoiceItem.setPrice(salesInvoiceItemObj.getDouble("rate"));
                salesInvoiceItem.setPriceWithCommission(priceWithCommission);
                salesInvoiceItem.setQty(salesInvoiceItemObj.getInt("qty"));
                salesInvoiceItem.setProductId(salesInvoiceItemObj.getInt("item"));
                salesInvoiceItem.setSku(product.getSku());
                salesInvoiceItem.setTaxPercent(salesInvoiceItemObj.getDouble("taxPercent"));
                salesInvoiceItem.setCommissionType(salesInvoiceItemObj.getString("commission"));
                salesInvoiceItem.setCommissionValue(salesInvoiceItemObj.getDouble("commissionValue"));
                salesInvoiceItem.setRowTotalTaxAmount(rowTotalTaxAmount);
                salesInvoiceItem.setTaxAmount(taxAmountPerUnit);
                salesInvoiceItem.setPriceInclTax(perUnitPriceIncludingTax);

                salesInvoiceItemList.add(salesInvoiceItem);
            }

            salesInvoice.setInvoiceV2(salesInvoiceItemList);



            Long lastInvoiceIncrementId = eavEntityStoreRepo.findLastInvoiceIncrementId();

            Long incrementLastInvoiceIncrementId = lastInvoiceIncrementId + 1;

            eavEntityStoreRepo.updateLastInvoiceIncrementId(incrementLastInvoiceIncrementId);

            invoiceRepo.save(salesInvoice);



            /**
             * Add the code here if we want to generate order from invoice
             */

            //todo conditional email sending with the invoice



        }catch (Exception e){
            //todo Add logger and send email to dev mail group!
            throw new RuntimeException(e.getMessage());
        }

    }


    @Transactional
    public Map previewInvoice(InvoiceDTO invoiceDTO){
        try{
            LocalDate dueDate = LocalDate.parse(invoiceDTO.getDueDate());
            LocalDate dateIssued = LocalDate.parse(invoiceDTO.getDateIssued());
            InvoiceV2 salesInvoice = new InvoiceV2();
            salesInvoice.setInvoiceId(invoiceDTO.getInvoiceId());
            salesInvoice.setIsActive(true);
            salesInvoice.setDueDate(dueDate);
            salesInvoice.setDateIssued(dateIssued);
            salesInvoice.setIsMultiShipping(false);
            salesInvoice.setCurrencyCode("INR");
            salesInvoice.setGrandTotal(invoiceDTO.getGrandTotal());
            salesInvoice.setCheckoutMethod("SYSTEM_ADMIN");
            salesInvoice.setTaxAmount(invoiceDTO.getTax());
            salesInvoice.setSubtotal(invoiceDTO.getSubtotal());
            salesInvoice.setTotalDiscountAmount(invoiceDTO.getDiscountAmount());
            salesInvoice.setSubtotalWithDiscount(invoiceDTO.getSubtotalWithDiscount());
            salesInvoice.setCustomerId(invoiceDTO.getCustomerId());
            salesInvoice.setCustomerEmail(invoiceDTO.getCustomerEmail());
            salesInvoice.setCustomeFullname(invoiceDTO.getCustomerFullName());

            JSONArray itemArr = new JSONArray(invoiceDTO.getItems());

            salesInvoice.setItemsCount(itemArr.length());

            List<InvoiceItemV2> salesInvoiceItemList = new ArrayList<>();

            for (int i = 0; i < itemArr.length(); i++) {

                JSONObject salesInvoiceItemObj = itemArr.getJSONObject(i);

                Product product = productRepo.findById(salesInvoiceItemObj.getInt("item")).orElseThrow();

                Double priceWithCommission = salesInvoiceItemObj.getDouble("rate");

                if(salesInvoiceItemObj.getString("commission").equals("percent")){

                    priceWithCommission = salesInvoiceItemObj.getDouble("rate") + (salesInvoiceItemObj.getDouble("rate")*salesInvoiceItemObj.getDouble("commissionValue"))/100;
                }
                if(salesInvoiceItemObj.getString("commission").equals("flat")){

                    priceWithCommission = salesInvoiceItemObj.getDouble("rate") + salesInvoiceItemObj.getDouble("commissionValue");
                }

                Double rowTotalTaxAmount = priceWithCommission * salesInvoiceItemObj.getInt("qty") * salesInvoiceItemObj.getDouble("taxPercent")/100;

                Double taxAmountPerUnit = priceWithCommission * salesInvoiceItemObj.getDouble("taxPercent")/100;

                Double perUnitPriceIncludingTax = priceWithCommission + taxAmountPerUnit;

                InvoiceItemV2 salesInvoiceItem = new InvoiceItemV2();
                salesInvoiceItem.setRowTotal(salesInvoiceItemObj.getDouble("amount"));
                salesInvoiceItem.setIsSubItems(false);
                salesInvoiceItem.setHasSubItems(true);
                salesInvoiceItem.setPrice(salesInvoiceItemObj.getDouble("rate"));
                salesInvoiceItem.setPriceWithCommission(priceWithCommission);
                salesInvoiceItem.setQty(salesInvoiceItemObj.getInt("qty"));
                salesInvoiceItem.setProductId(salesInvoiceItemObj.getInt("item"));
                salesInvoiceItem.setSku(product.getSku());
                salesInvoiceItem.setTaxPercent(salesInvoiceItemObj.getDouble("taxPercent"));
                salesInvoiceItem.setCommissionType(salesInvoiceItemObj.getString("commission"));
                salesInvoiceItem.setCommissionValue(salesInvoiceItemObj.getDouble("commissionValue"));
                salesInvoiceItem.setRowTotalTaxAmount(rowTotalTaxAmount);
                salesInvoiceItem.setTaxAmount(taxAmountPerUnit);
                salesInvoiceItem.setPriceInclTax(perUnitPriceIncludingTax);
                salesInvoiceItemList.add(salesInvoiceItem);
            }

            salesInvoice.setInvoiceV2(salesInvoiceItemList);

            JSONObject jsonObject = new JSONObject(salesInvoice);

            return jsonObject.toMap();

        }catch (Exception e){
            //todo Add logger and send email to dev mail group!
            throw new RuntimeException(e.getMessage());
        }

    }

    @Transactional
    public void createQuotation(QuotationDTO quotationDTO){
        try{
            QuotationV2 salesQuotation = new QuotationV2();
            salesQuotation.setQuotationId(quotationDTO.getQuotationId());
            salesQuotation.setIsActive(true);
            salesQuotation.setIsMultiShipping(false);
            salesQuotation.setCurrencyCode("INR");
            salesQuotation.setGrandTotal(quotationDTO.getGrandTotal());
            salesQuotation.setCheckoutMethod("SYSTEM_ADMIN");
            salesQuotation.setTaxAmount(quotationDTO.getTax());
            salesQuotation.setSubtotal(quotationDTO.getSubtotal());
            salesQuotation.setTotalDiscountAmount(quotationDTO.getDiscountAmount());
            salesQuotation.setSubtotalWithDiscount(quotationDTO.getSubtotalWithDiscount());
            salesQuotation.setCustomerId(quotationDTO.getCustomerId());
            salesQuotation.setCustomerEmail(quotationDTO.getCustomerEmail());
            salesQuotation.setCustomeFullname(quotationDTO.getCustomerFullName());

            JSONArray itemArr = new JSONArray(quotationDTO.getItems());

            salesQuotation.setItemsCount(itemArr.length());

            List<QuotationItemV2> quotationItemList = new ArrayList<>();

            for (int i = 0; i < itemArr.length(); i++) {

                JSONObject quotationItemObj = itemArr.getJSONObject(i);

                Product product = productRepo.findById(quotationItemObj.getInt("item")).orElseThrow();

                Double priceWithCommission = quotationItemObj.getDouble("rate");

                if(quotationItemObj.getString("commission").equals("percent")){

                    priceWithCommission = quotationItemObj.getDouble("rate") + (quotationItemObj.getDouble("rate")*quotationItemObj.getDouble("commissionValue"))/100;
                }
                if(quotationItemObj.getString("commission").equals("flat")){

                    priceWithCommission = quotationItemObj.getDouble("rate") + quotationItemObj.getDouble("commissionValue");
                }

                Double rowTotalTaxAmount = priceWithCommission * quotationItemObj.getInt("qty") * quotationItemObj.getDouble("taxPercent")/100;

                Double taxAmountPerUnit = priceWithCommission * quotationItemObj.getDouble("taxPercent")/100;

                Double perUnitPriceIncludingTax = priceWithCommission + taxAmountPerUnit;

                QuotationItemV2 quotationItem = new QuotationItemV2();
                quotationItem.setRowTotal(quotationItemObj.getDouble("amount"));
                quotationItem.setIsSubItems(false);
                quotationItem.setHasSubItems(true);
                quotationItem.setPrice(quotationItemObj.getDouble("rate"));
                quotationItem.setPriceWithCommission(priceWithCommission);
                quotationItem.setQty(quotationItemObj.getInt("qty"));
                quotationItem.setProductId(quotationItemObj.getInt("item"));
                quotationItem.setSku(product.getSku());
                quotationItem.setTaxPercent(quotationItemObj.getDouble("taxPercent"));
                quotationItem.setCommissionType(quotationItemObj.getString("commission"));
                quotationItem.setCommissionValue(quotationItemObj.getDouble("commissionValue"));
                quotationItem.setRowTotalTaxAmount(rowTotalTaxAmount);
                quotationItem.setTaxAmount(taxAmountPerUnit);
                quotationItem.setPriceInclTax(perUnitPriceIncludingTax);

                quotationItemList.add(quotationItem);
            }

            salesQuotation.setQuotationV2(quotationItemList);

            Long lastQuotationIncrementId = eavEntityStoreRepo.findLastQuotationId();

            Long incrementLastQuotationIncrementId = lastQuotationIncrementId + 1;

            eavEntityStoreRepo.updateLastQuotationId(incrementLastQuotationIncrementId);

            quotationRepo.save(salesQuotation);



            /**
             * Add the code here if we want to generate order from invoice
             */

            //todo conditional email sending with the invoice



        }catch (Exception e){
            //todo Add logger and send email to dev mail group!
            throw new RuntimeException(e.getMessage());
        }
    }

    @Transactional
    public Map previewQuotation(QuotationDTO quotationDTO){
        try{
            QuotationV2 salesQuotation = new QuotationV2();
            salesQuotation.setQuotationId(quotationDTO.getQuotationId());
            salesQuotation.setIsActive(true);
            salesQuotation.setIsMultiShipping(false);
            salesQuotation.setCurrencyCode("INR");
            salesQuotation.setGrandTotal(quotationDTO.getGrandTotal());
            salesQuotation.setCheckoutMethod("SYSTEM_ADMIN");
            salesQuotation.setTaxAmount(quotationDTO.getTax());
            salesQuotation.setSubtotal(quotationDTO.getSubtotal());
            salesQuotation.setTotalDiscountAmount(quotationDTO.getDiscountAmount());
            salesQuotation.setSubtotalWithDiscount(quotationDTO.getSubtotalWithDiscount());
            salesQuotation.setCustomerId(quotationDTO.getCustomerId());
            salesQuotation.setCustomerEmail(quotationDTO.getCustomerEmail());
            salesQuotation.setCustomeFullname(quotationDTO.getCustomerFullName());

            JSONArray itemArr = new JSONArray(quotationDTO.getItems());

            salesQuotation.setItemsCount(itemArr.length());

            List<QuotationItemV2> quotationItemList = new ArrayList<>();

            for (int i = 0; i < itemArr.length(); i++) {

                JSONObject quotationItemObj = itemArr.getJSONObject(i);

                Product product = productRepo.findById(quotationItemObj.getInt("item")).orElseThrow();

                Double priceWithCommission = quotationItemObj.getDouble("rate");

                if(quotationItemObj.getString("commission").equals("percent")){

                    priceWithCommission = quotationItemObj.getDouble("rate") + (quotationItemObj.getDouble("rate")*quotationItemObj.getDouble("commissionValue"))/100;
                }
                if(quotationItemObj.getString("commission").equals("flat")){

                    priceWithCommission = quotationItemObj.getDouble("rate") + quotationItemObj.getDouble("commissionValue");
                }

                Double rowTotalTaxAmount = priceWithCommission * quotationItemObj.getInt("qty") * quotationItemObj.getDouble("taxPercent")/100;

                Double taxAmountPerUnit = priceWithCommission * quotationItemObj.getDouble("taxPercent")/100;

                Double perUnitPriceIncludingTax = priceWithCommission + taxAmountPerUnit;

                QuotationItemV2 quotationItem = new QuotationItemV2();
                quotationItem.setRowTotal(quotationItemObj.getDouble("amount"));
                quotationItem.setIsSubItems(false);
                quotationItem.setHasSubItems(true);
                quotationItem.setPrice(quotationItemObj.getDouble("rate"));
                quotationItem.setPriceWithCommission(priceWithCommission);
                quotationItem.setQty(quotationItemObj.getInt("qty"));
                quotationItem.setProductId(quotationItemObj.getInt("item"));
                quotationItem.setSku(product.getSku());
                quotationItem.setTaxPercent(quotationItemObj.getDouble("taxPercent"));
                quotationItem.setCommissionType(quotationItemObj.getString("commission"));
                quotationItem.setCommissionValue(quotationItemObj.getDouble("commissionValue"));
                quotationItem.setRowTotalTaxAmount(rowTotalTaxAmount);
                quotationItem.setTaxAmount(taxAmountPerUnit);
                quotationItem.setPriceInclTax(perUnitPriceIncludingTax);

                quotationItemList.add(quotationItem);
            }

            salesQuotation.setQuotationV2(quotationItemList);

            JSONObject jsonObject = new JSONObject(salesQuotation);

            return jsonObject.toMap();

        }catch (Exception e){
            //todo Add logger and send email to dev mail group!
            throw new RuntimeException(e.getMessage());
        }
    }

    @Transactional
    public void createPurchaseOrder(PurchaseOrderDTO purchaseOrderDTO){
        try{
            PurchaseOrderV2 purchaseOrder = new PurchaseOrderV2();
            purchaseOrder.setPurchaseOrderId(purchaseOrderDTO.getPurchaseOrderId());
            purchaseOrder.setIsActive(true);
            purchaseOrder.setIsMultiShipping(false);
            purchaseOrder.setCurrencyCode("INR");
            purchaseOrder.setGrandTotal(purchaseOrderDTO.getGrandTotal());
            purchaseOrder.setCheckoutMethod("SYSTEM_ADMIN");
            purchaseOrder.setTaxAmount(purchaseOrderDTO.getTax());
            purchaseOrder.setSubtotal(purchaseOrderDTO.getSubtotal());
            purchaseOrder.setTotalDiscountAmount(purchaseOrderDTO.getDiscountAmount());
            purchaseOrder.setSubtotalWithDiscount(purchaseOrderDTO.getSubtotalWithDiscount());
            purchaseOrder.setCustomerId(purchaseOrderDTO.getVendorId());
            purchaseOrder.setCustomerEmail(purchaseOrderDTO.getCustomerEmail());
            purchaseOrder.setCustomeFullname(purchaseOrderDTO.getCustomerFullName());

            JSONArray itemArr = new JSONArray(purchaseOrderDTO.getItems());

            purchaseOrder.setItemsCount(itemArr.length());

            List<PurchaseOrderItemV2> quotationItemList = new ArrayList<>();

            for (int i = 0; i < itemArr.length(); i++) {

                JSONObject quotationItemObj = itemArr.getJSONObject(i);

                Product product = productRepo.findById(quotationItemObj.getInt("item")).orElseThrow();

                Double priceWithCommission = quotationItemObj.getDouble("rate");

                if(quotationItemObj.getString("commission").equals("percent")){

                    priceWithCommission = quotationItemObj.getDouble("rate") + (quotationItemObj.getDouble("rate")*quotationItemObj.getDouble("commissionValue"))/100;
                }
                if(quotationItemObj.getString("commission").equals("flat")){

                    priceWithCommission = quotationItemObj.getDouble("rate") + quotationItemObj.getDouble("commissionValue");
                }

                Double rowTotalTaxAmount = priceWithCommission * quotationItemObj.getInt("qty") * quotationItemObj.getDouble("taxPercent")/100;

                Double taxAmountPerUnit = priceWithCommission * quotationItemObj.getDouble("taxPercent")/100;

                Double perUnitPriceIncludingTax = priceWithCommission + taxAmountPerUnit;

                PurchaseOrderItemV2 quotationItem = new PurchaseOrderItemV2();
                quotationItem.setRowTotal(quotationItemObj.getDouble("amount"));
                quotationItem.setIsSubItems(false);
                quotationItem.setHasSubItems(true);
                quotationItem.setPrice(quotationItemObj.getDouble("rate"));
                quotationItem.setPriceWithCommission(priceWithCommission);
                quotationItem.setQty(quotationItemObj.getInt("qty"));
                quotationItem.setProductId(quotationItemObj.getInt("item"));
                quotationItem.setSku(product.getSku());
                quotationItem.setTaxPercent(quotationItemObj.getDouble("taxPercent"));
                quotationItem.setCommissionType(quotationItemObj.getString("commission"));
                quotationItem.setCommissionValue(quotationItemObj.getDouble("commissionValue"));
                quotationItem.setRowTotalTaxAmount(rowTotalTaxAmount);
                quotationItem.setTaxAmount(taxAmountPerUnit);
                quotationItem.setPriceInclTax(perUnitPriceIncludingTax);

                quotationItemList.add(quotationItem);
            }

            purchaseOrder.setPurchaseOrderV2(quotationItemList);


            Long lastPurchaseOrderIncrementId = eavEntityStoreRepo.findLastPurchaseOrderId();

            Long incrementLastPurchaseId = lastPurchaseOrderIncrementId + 1;

            eavEntityStoreRepo.updateLastPurchaseOrderIncrementId(incrementLastPurchaseId);

            purchaseOrderRepo.save(purchaseOrder);


        }catch (Exception e){
            //todo Add logger and send email to dev mail group!
            throw new RuntimeException(e.getMessage());
        }
    }

    @Transactional
    public Map previewPurchaseOrder(PurchaseOrderDTO purchaseOrderDTO){
        try{
            PurchaseOrderV2 purchaseOrder = new PurchaseOrderV2();
            purchaseOrder.setPurchaseOrderId(purchaseOrderDTO.getPurchaseOrderId());
            purchaseOrder.setIsActive(true);
            purchaseOrder.setIsMultiShipping(false);
            purchaseOrder.setCurrencyCode("INR");
            purchaseOrder.setGrandTotal(purchaseOrderDTO.getGrandTotal());
            purchaseOrder.setCheckoutMethod("SYSTEM_ADMIN");
            purchaseOrder.setTaxAmount(purchaseOrderDTO.getTax());
            purchaseOrder.setSubtotal(purchaseOrderDTO.getSubtotal());
            purchaseOrder.setTotalDiscountAmount(purchaseOrderDTO.getDiscountAmount());
            purchaseOrder.setSubtotalWithDiscount(purchaseOrderDTO.getSubtotalWithDiscount());
            purchaseOrder.setCustomerId(purchaseOrderDTO.getVendorId());
            purchaseOrder.setCustomerEmail(purchaseOrderDTO.getCustomerEmail());
            purchaseOrder.setCustomeFullname(purchaseOrderDTO.getCustomerFullName());

            JSONArray itemArr = new JSONArray(purchaseOrderDTO.getItems());

            purchaseOrder.setItemsCount(itemArr.length());

            List<PurchaseOrderItemV2> quotationItemList = new ArrayList<>();

            for (int i = 0; i < itemArr.length(); i++) {

                JSONObject quotationItemObj = itemArr.getJSONObject(i);

                Product product = productRepo.findById(quotationItemObj.getInt("item")).orElseThrow();

                Double priceWithCommission = quotationItemObj.getDouble("rate");

                if(quotationItemObj.getString("commission").equals("percent")){

                    priceWithCommission = quotationItemObj.getDouble("rate") + (quotationItemObj.getDouble("rate")*quotationItemObj.getDouble("commissionValue"))/100;
                }
                if(quotationItemObj.getString("commission").equals("flat")){

                    priceWithCommission = quotationItemObj.getDouble("rate") + quotationItemObj.getDouble("commissionValue");
                }

                Double rowTotalTaxAmount = priceWithCommission * quotationItemObj.getInt("qty") * quotationItemObj.getDouble("taxPercent")/100;

                Double taxAmountPerUnit = priceWithCommission * quotationItemObj.getDouble("taxPercent")/100;

                Double perUnitPriceIncludingTax = priceWithCommission + taxAmountPerUnit;

                PurchaseOrderItemV2 quotationItem = new PurchaseOrderItemV2();
                quotationItem.setRowTotal(quotationItemObj.getDouble("amount"));
                quotationItem.setIsSubItems(false);
                quotationItem.setHasSubItems(true);
                quotationItem.setPrice(quotationItemObj.getDouble("rate"));
                quotationItem.setPriceWithCommission(priceWithCommission);
                quotationItem.setQty(quotationItemObj.getInt("qty"));
                quotationItem.setProductId(quotationItemObj.getInt("item"));
                quotationItem.setSku(product.getSku());
                quotationItem.setTaxPercent(quotationItemObj.getDouble("taxPercent"));
                quotationItem.setCommissionType(quotationItemObj.getString("commission"));
                quotationItem.setCommissionValue(quotationItemObj.getDouble("commissionValue"));
                quotationItem.setRowTotalTaxAmount(rowTotalTaxAmount);
                quotationItem.setTaxAmount(taxAmountPerUnit);
                quotationItem.setPriceInclTax(perUnitPriceIncludingTax);

                quotationItemList.add(quotationItem);
            }

            purchaseOrder.setPurchaseOrderV2(quotationItemList);


            JSONObject jsonObject = new JSONObject(purchaseOrder);

            return jsonObject.toMap();



        }catch (Exception e){
            //todo Add logger and send email to dev mail group!
            throw new RuntimeException(e.getMessage());
        }
    }


    public List<InvoiceV2> findInvoiceByCustomerId(Long customerId){
        List<InvoiceV2> invoices = invoiceRepo.findInvoiceByCustomerId(customerId);

        if (invoices == null || invoices.isEmpty()) {
            throw new PartialContentException("Partial content error: No invoices for customer with ID '" + customerId + "' could be fetched.");
        }

        return invoices;
    }

    public InvoiceV2 findInvoiceByOrderId(Long orderId){
        return invoiceRepo.findInvoiceByOrderId(orderId);
    }

    public InvoiceV2 findInvoiceById(Long invoiceId){
        return invoiceRepo.findById(invoiceId).orElseThrow(() ->
                new PartialContentException("Partial content error: Invoice with ID '" + invoiceId + "' could not be fetched.")
        );
    }

    public QuotationV2 findQuotationById(Long quotationId){
        return quotationRepo.findById(quotationId).orElseThrow();
    }

    public PurchaseOrderV2 findPurchaseOrderById(Long purchaseOrderId){
        return purchaseOrderRepo.findById(purchaseOrderId).orElseThrow();
    }

    public Long findLastInvoiceIncrementId(){
        return eavEntityStoreRepo.findLastInvoiceIncrementId() + 1;
    }

    public Long findLastQuotationIncrementId(){
        return eavEntityStoreRepo.findLastQuotationId() + 1;
    }

    public Long findLastPurchaseOrderIncrementId(){
        return eavEntityStoreRepo.findLastPurchaseOrderId() + 1;
    }

}
