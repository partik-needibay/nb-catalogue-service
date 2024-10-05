package com.needibay.cart.dto;

import lombok.Data;

import java.util.List;

@Data
public class GoogleVisionDTO {

    private String parent;
    private List<Request> requests;

    @Data
    public static class Request {
        private Image image;
        private List<Feature> features;
    }

    @Data
    public static class Feature {
        private String type;
    }

    @Data
    public static class Image {
        private Source source;
    }


    @Data
    public static class Source {
        private String imageUri;
    }
}






