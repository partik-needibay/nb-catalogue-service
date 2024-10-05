package com.needibay.cart.dto;


import lombok.Data;

@Data
public class SendEmailDTO {

    public String email;

    public String subjectLine;

    public String template;

    public VariableData variableData;

    @Data
    public static class VariableData{
        private String customerName;
        private String incrementId;
        private String link;
    }

}
