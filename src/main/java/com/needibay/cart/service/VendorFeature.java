package com.needibay.cart.service;

import com.needibay.cart.component.GetServiceCommand;
import com.needibay.cart.component.IServiceCommand;
import com.needibay.cart.component.PostServiceCommand;
import com.needibay.cart.component.ServiceInvoker;
import com.needibay.cart.dto.*;
import com.needibay.cart.entity.*;
import com.needibay.cart.entity.address.Address;
import com.needibay.cart.exception.UnprocessableEntityException;
import com.needibay.cart.libs.SanboxGst.SandboxGst;
import com.needibay.cart.repository.*;
import com.needibay.cart.repository.address.AddressRepo;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class VendorFeature implements Feature {

    @Autowired
    AccountRepo accountRepo;

    @Autowired
    AddressRepo addressRepo;

    @Autowired
    ContactRepo contactRepo;

    @Autowired
    DocumentRepo documentRepo;

    @Autowired
    BankRepo bankRepo;

    @Autowired
    ModelMap model;

    @Autowired
    MediaGalleryRepo mediaGalleryRepo;

    @Value("${BASE_URL}")
    private String baseUrl;

    private final FileStorageService fileStorageService;

    final Environment environment;

    public VendorFeature(FileStorageService fileStorageService, Environment environment) {
        this.fileStorageService = fileStorageService;
        this.environment = environment;
    }

    @Transactional
    public Account createVendorAccount(VendorAccountDTO vendorAccountDTO){
        Account newVendorAccount = new Account();
        newVendorAccount.setFirstName(vendorAccountDTO.getFirstName());
        newVendorAccount.setLastName(vendorAccountDTO.getLastName());
        newVendorAccount.setEmail(vendorAccountDTO.getEmail());
        newVendorAccount.setGstReg(vendorAccountDTO.getGstReg());
        newVendorAccount.setGstTreatment(vendorAccountDTO.getGstTreatment());
        newVendorAccount.setGstDetails(vendorAccountDTO.getGstDetails());
        newVendorAccount.setWebsite(vendorAccountDTO.getWebsite());
        return accountRepo.save(newVendorAccount);
    }


    @Transactional
    public void createVendorContact(VendorContactDTO vendorContactDTO){
        Account account = accountRepo.findById(vendorContactDTO.getAccountId()).orElseThrow();
        Contact newVendorContact = new Contact();
        newVendorContact.setCompanyId(vendorContactDTO.getAccountId());
        newVendorContact.setFirstName(vendorContactDTO.getFirstName());
        newVendorContact.setLastName(vendorContactDTO.getLastName());
        newVendorContact.setEmail(vendorContactDTO.getEmail());
        newVendorContact.setPhone(vendorContactDTO.getPhone());
        contactRepo.save(newVendorContact);
    }

    @Transactional
    public void createVendorAddress(VendorAddressDTO vendorAddressDTO){

        Account account = accountRepo.findById(vendorAddressDTO.getAccountId()).orElseThrow();

        JSONObject shippingAddress = new JSONObject(vendorAddressDTO.getShippingAddress());
        JSONObject billingAddress = new JSONObject(vendorAddressDTO.getBillingAddress());

        Address shippingAddressEntity = new Address();
        Address billingAddressEntity = new Address();

        shippingAddressEntity.setAddressLineOne(shippingAddress.getString("shippingAddressLineOne"));
        shippingAddressEntity.setAddressLineTwo(shippingAddress.getString("shippingAddressLineTwo"));
        shippingAddressEntity.setCountry(1);
        shippingAddressEntity.setState(shippingAddress.getInt("shippingState"));
        shippingAddressEntity.setZipcode(shippingAddress.getInt("shippingZipcode"));
        shippingAddressEntity.setPhone(shippingAddress.getString("shippingPhone"));
        shippingAddressEntity.setAddressType(2);
        shippingAddressEntity.setModelName("VENDOR");
        shippingAddressEntity.setModelId(vendorAddressDTO.getAccountId());
        addressRepo.save(shippingAddressEntity);

        billingAddressEntity.setAddressLineOne(billingAddress.getString("billingAddressLineOne"));
        billingAddressEntity.setAddressLineTwo(billingAddress.getString("billingAddressLineTwo"));
        billingAddressEntity.setCountry(1);
        billingAddressEntity.setState(billingAddress.getInt("billingState"));
        billingAddressEntity.setZipcode(billingAddress.getInt("billingZipcode"));
        billingAddressEntity.setPhone(billingAddress.getString("billingPhone"));
        billingAddressEntity.setAddressType(1);
        billingAddressEntity.setModelName("VENDOR");
        billingAddressEntity.setModelId(vendorAddressDTO.getAccountId());
        addressRepo.save(billingAddressEntity);

    }

    @Transactional
    public void createVendorBankDetail(VendorBankDTO vendorBankDTO){
        Account account = accountRepo.findById(vendorBankDTO.getAccountId()).orElseThrow();
        Bank bankDetails = new Bank();
        bankDetails.setCompanyId(vendorBankDTO.getAccountId());
        bankDetails.setBankAccountName(vendorBankDTO.getBankAccountName());
        bankDetails.setBankAccountNumber(vendorBankDTO.getBankAccountNumber());
        bankDetails.setBankBranch(vendorBankDTO.getBankBranch());
        bankDetails.setIfsc(vendorBankDTO.getIfsc());
        bankDetails.setBankName(vendorBankDTO.getBankName());
        bankRepo.save(bankDetails);
    }

    @Transactional
    public void uploadVendorDocument(VendorDocDTO vendorDocDTO){
        Account account = accountRepo.findById(vendorDocDTO.getAccountId()).orElseThrow();
        if(vendorDocDTO.getDocs() != null){
            for(int i = 0; i < vendorDocDTO.getDocs().size(); i++){
                MultipartFile file = vendorDocDTO.getDocs().get(i);
                String info = vendorDocDTO.getDocumentType().get(i);
                String location = environment.getProperty("app.file.storage.mapping");
                String fileName = fileStorageService.storeFile(file);
                MediaGallery mediaGallery = new MediaGallery();
                mediaGallery.setMediaPath(baseUrl + "/contact/assets/docs/vendor/"+fileName);
                mediaGallery.setValue(fileName);
                mediaGallery.setPageBlockCode(info);
                mediaGallery.setMediaType(file.getContentType());
                mediaGallery.setModel("VENDOR_DOCUMENT");
                mediaGallery.setModelEntityId(vendorDocDTO.getAccountId());
                mediaGalleryRepo.save(mediaGallery);
            }
        }
    }

    @Transactional
    public Account uploadVendorNotes(VendorAccountDTO vendorAccountDTO){
        Account newVendorAccount = new Account();
        newVendorAccount.setFirstName(vendorAccountDTO.getFirstName());
        newVendorAccount.setLastName(vendorAccountDTO.getLastName());
        newVendorAccount.setEmail(vendorAccountDTO.getEmail());
        newVendorAccount.setGstReg(vendorAccountDTO.getGstReg());
        newVendorAccount.setGstTreatment(vendorAccountDTO.getGstTreatment());
        newVendorAccount.setGstDetails(vendorAccountDTO.getGstDetails());
        newVendorAccount.setWebsite(vendorAccountDTO.getWebsite());
        return accountRepo.save(newVendorAccount);
    }

    @Transactional
    public JSONObject getGstDetails(String gstin){

        try {

            // verify saved JWT token saved in session
            if (model.getAttribute("SPRING_SANDBOX_JWT_TOKEN") == null) {
                this.authenticateSandboxGst();
            }


            HttpHeaders headers = new HttpHeaders();

            // Needs to be fetched from system configuration table
            headers.set("Content-Type", "application/json");
            headers.set("x-api-version", "1.0");
            headers.set("x-api-key", "key_live_HPLW3vekonxvr2ouyOW0LfQA1cERJ50F");
            headers.set("Authorization", model.getAttribute("SPRING_SANDBOX_JWT_TOKEN").toString());
            SandboxGst sandboxGst = new SandboxGst();
            sandboxGst.setSandboxGstUrl("https://api.sandbox.co.in/gsp/public/gstin/" + gstin);
            sandboxGst.setSandboxGstHeaders(headers);
            IServiceCommand serviceCommand = new GetServiceCommand(sandboxGst);
            ServiceInvoker serviceInvoker = new ServiceInvoker(serviceCommand);
            String response = serviceCommand.executeString();

            JSONObject myObject = new JSONObject(response);

            JSONObject dataObject = new JSONObject(myObject.get("data").toString());


            return dataObject;

        }catch (Exception e) {
            log.error(e.getMessage());
            throw new UnprocessableEntityException("Invalid GST Found!");
        }

    }

    @Transactional
    public Account findVendorById(Integer id){
        Account account = accountRepo.findById(id).orElseThrow();
        return account;
    }

    @Transactional
    private Boolean authenticateSandboxGst(){

        try {

            HttpHeaders headers = new HttpHeaders();
            // Needs to be fetched from system configuration table
            headers.set("x-api-key", "key_live_HPLW3vekonxvr2ouyOW0LfQA1cERJ50F");
            headers.set("x-api-secret", "secret_live_qpcQ8BOQ0O8yEmA9X86bRHclYNcAGSE7");
            headers.set("x-api-version", "1.0");
            headers.set("accept", "application/json");

            SandboxGst sandboxGst = new SandboxGst();
            sandboxGst.setSandboxGstUrl("https://api.sandbox.co.in/authenticate");
            sandboxGst.setSandboxGstHeaders(headers);
            IServiceCommand serviceCommand = new PostServiceCommand(sandboxGst);
            ServiceInvoker serviceInvoker = new ServiceInvoker(serviceCommand);
            Object response = serviceInvoker.ExecuteRequest();

            Map<String, String> map = new HashMap<String, String>();
            map = (Map<String, String>) ((ResponseEntity) response).getBody();
            model.addAttribute("SPRING_SANDBOX_JWT_TOKEN", map.get("access_token"));

            return true;
        }catch (Exception e) {

            log.error(e.getMessage());

            return false;
        }
    }
}
