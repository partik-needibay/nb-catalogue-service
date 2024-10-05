package com.needibay.cart.service.payment;

import com.needibay.cart.dto.PaymentTermsDTO;
import com.needibay.cart.entity.PaymentTerms;
import com.needibay.cart.entity.order.SalesOrderPayment;
import com.needibay.cart.exception.PartialContentException;
import com.needibay.cart.repository.PaymentTermsRepo;
import com.needibay.cart.repository.order.SalesOrderPaymentRepo;
import com.needibay.cart.service.Feature;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentFeature implements Feature {

    @Autowired
    SalesOrderPaymentRepo salesOrderPaymentRepo;

    @Autowired
    PaymentTermsRepo paymentTermsRepo;

    public List<SalesOrderPayment> findAll(){
        return salesOrderPaymentRepo.findAll();
    }

    public List<SalesOrderPayment> findPaymentByCustomerId(Long customerId){
        List<SalesOrderPayment> payments = salesOrderPaymentRepo.findPaymentByCustomerId(customerId);
        if (payments == null || payments.isEmpty()) {
            throw new PartialContentException("Partial content error: No payments for customer with ID '" + customerId + "' could be fetched.");
        }
        return payments;
    }

    public List<SalesOrderPayment> findPaymentByOrderId(Long orderId){
        return salesOrderPaymentRepo.findPaymentByOrderId(orderId);
    }

    public List<PaymentTerms> findAllPaymentTerms(){

        return paymentTermsRepo.findAll();
    }

    public void savePaymentTerms(PaymentTermsDTO paymentTermsDTO){
        JSONArray jsonArray = new JSONArray(paymentTermsDTO.getPaymentTermsString());

        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject jsonPaymentTermsObj = jsonArray.getJSONObject(i);
            PaymentTerms paymentTerms = new PaymentTerms();

            paymentTerms.setDaysCount(jsonPaymentTermsObj.getInt("dayCount"));
            paymentTerms.setIsActive(jsonPaymentTermsObj.getBoolean("isActive"));
            paymentTerms.setIsDefault(jsonPaymentTermsObj.getBoolean("isDefault"));
            paymentTerms.setTermName(jsonPaymentTermsObj.getString("paymentTermName"));

            paymentTermsRepo.save(paymentTerms);
        }

    }

    public void deletePaymentTerms(Long id){
        paymentTermsRepo.deleteById(id);
    }



}
