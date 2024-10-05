package com.needibay.cart.response;

import lombok.Data;

import java.util.List;

/*
 * Global Response Builder
 * */
@Data
public class Response {

    private Boolean success;

    private String message;

    private String dataType;

    private List error;

    private Object data;

    public Response(Build build) {
        this.message = build.message;
        this.data = build.data;
        this.dataType = build.dataType;
        this.success = build.success;
        this.error = build.error;
    }

    public static class Build {

        private Boolean success;
        private String message;

        private String dataType;

        private List error;

        private Object data;

        public Build() {
        }

        public Build setSuccess(Boolean success) {
            this.success = success;
            return this;
        }

        public Build setMessage(String message) {
            this.message = message;
            return this;
        }

        public Build setError(List error) {
            this.error = error;
            return this;
        }

        public Build setDataType(String dataType) {
            this.dataType = dataType;
            return this;
        }

        public Build setData(Object data) {
            this.data = data;
            return this;
        }

        public Response build() {
            return new Response(this);
        }

    }

}
