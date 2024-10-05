package com.needibay.cart.search.product;

import com.needibay.cart.entity.Product;
import com.needibay.cart.entity.ProductEavAttribute;
import com.needibay.cart.entity.ProductEavAttributeValue;
import com.needibay.cart.entity.shipment.DeliveryType;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.*;
import java.awt.print.Pageable;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ProductSpecification implements Specification<Product> {

    private final SearchCriteria searchCriteria;

    public ProductSpecification(final SearchCriteria searchCriteria){
        super();
        this.searchCriteria = searchCriteria;
    }

    @Override
    public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        String strToSearch = searchCriteria.getValue().toString().toLowerCase();
        String attributeStrToSearch = searchCriteria.getAttributeValue().toString().toLowerCase();

        switch(Objects.requireNonNull(SearchOperation.getSimpleOperation(searchCriteria.getOperation()))){
            case CONTAINS:

                return cb.like(cb.lower(root.get(searchCriteria.getFilterKey())), "%" + strToSearch + "%");

            case DOES_NOT_CONTAIN:

                return cb.notLike(cb.lower(root.get(searchCriteria.getFilterKey())), "%" + strToSearch + "%");

            case BEGINS_WITH:

                return cb.like(cb.lower(root.get(searchCriteria.getFilterKey())), strToSearch + "%");

            case DOES_NOT_BEGIN_WITH:

                return cb.notLike(cb.lower(root.get(searchCriteria.getFilterKey())), strToSearch + "%");

            case ENDS_WITH:

                return cb.like(cb.lower(root.get(searchCriteria.getFilterKey())), "%" + strToSearch);

            case DOES_NOT_END_WITH:

                return cb.notLike(cb.lower(root.get(searchCriteria.getFilterKey())), "%" + strToSearch);

            case EQUAL:
                if(searchCriteria.getFilterKey().equals("attributeCode")){
                    Predicate attributeCode = cb.equal(cb.lower(attributeJoin(root).<String>get(searchCriteria.getFilterKey())), strToSearch);
                    Predicate attributeValue = cb.equal(cb.lower(attributeJoin(root).<String>get("value")), attributeStrToSearch);
                    Predicate combinedPredicate = cb.and(attributeCode, attributeValue);
                    return combinedPredicate;
                }
                if(searchCriteria.getFilterKey().equals("productDeliveryType")){
                    Predicate attributeCode = cb.equal(cb.lower(deliveryTypeJoin(root).<String>get("deliveryType")), strToSearch);
                    Predicate attributeValue = cb.equal(cb.lower(deliveryTypeJoin(root).<String>get("deliveryType")), attributeStrToSearch);
                    Predicate combinedPredicate = cb.or(attributeCode, attributeValue);
                    return combinedPredicate;
                }
                return cb.equal(root.get(searchCriteria.getFilterKey()), searchCriteria.getValue());

            case NOT_EQUAL:

                return cb.notEqual(root.get(searchCriteria.getFilterKey()), searchCriteria.getValue());

            case NUL:
                return cb.isNull(root.get(searchCriteria.getFilterKey()));

            case NOT_NULL:
                return cb.isNotNull(root.get(searchCriteria.getFilterKey()));

            case GREATER_THAN:
                return cb.greaterThan(root.<String> get(searchCriteria.getFilterKey()), searchCriteria.getValue().toString());

            case GREATER_THAN_EQUAL:
                return cb.greaterThanOrEqualTo(root.<String> get(searchCriteria.getFilterKey()), searchCriteria.getValue().toString());

            case LESS_THAN:
                return cb.lessThan(root.<String> get(searchCriteria.getFilterKey()), searchCriteria.getValue().toString());

            case LESS_THAN_EQUAL:
                return cb.lessThanOrEqualTo(root.<String> get(searchCriteria.getFilterKey()), searchCriteria.getValue().toString());
        }
        return null;
    }

    private Join<Product, ProductEavAttributeValue> attributeJoin(Root<Product> root){
        return root.join("extendedAttributes", JoinType.INNER);

    }

    private Join<Product, DeliveryType> deliveryTypeJoin(Root<Product> root){
        return root.join("productDeliveryType");

    }
}
