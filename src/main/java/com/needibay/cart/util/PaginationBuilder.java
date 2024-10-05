package com.needibay.cart.util;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Pageable;

@Getter
@Setter
@ToString
public class PaginationBuilder {

    Pageable pageable;

    private Integer pageSize = 10;

    private Integer pageSequence = 0;

    private String sortBy;

    private String sortOrder;

    public PaginationBuilder(PaginationBuilder.Build build) {
        this.pageSize = build.pageSize;
        this.pageSequence = build.pageSequence;
        this.sortBy = build.sortBy;
        this.sortOrder = build.sortOrder;
    }

    public static class Build {

        private Integer pageSize;

        private Integer pageSequence;

        private String sortBy;

        private String sortOrder;

        public Build() {
        }

        public PaginationBuilder.Build setPageSize(Integer pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public PaginationBuilder.Build setPageSequence(Integer pageSequence) {
            this.pageSequence = pageSequence;
            return this;
        }

        public PaginationBuilder.Build setSortBy(String sortBy) {
            this.sortBy = sortBy;
            return this;
        }

        public PaginationBuilder.Build setSortOrder(String sortOrder) {
            this.sortOrder = sortOrder;
            return this;
        }


        public Pageable build() {

            return PageRequest.of(this.pageSequence, this.pageSize);

        }

    }

}
