package com.needibay.cart.repository;

import com.needibay.cart.dto.ProductDTO;
import com.needibay.cart.entity.Category;
import com.needibay.cart.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product> {

    @Query(value = "select * from nb_product where id = :productId and product_type = 'configurable' and is_active = 1 and has_option = 1",
            nativeQuery = true)
    List<Product> findProductVariationByProductId(Integer productId);

    /*@Query(value = "select * from nb_product where is_variant = 0 and is_active = 1",
            nativeQuery = true)
    List<Product> findAll();*/

    @Query(value = "select * from nb_product where is_variant = 0 and is_active = 1",
            countQuery = "SELECT count(*) FROM nb_product where is_variant = 0 and is_active = 1",
            nativeQuery = true)
    Page<Product> findAllWithPagination(Pageable pageable);

    @Query(value = "select * from nb_product where is_variant = 0 and is_active = 1",
            nativeQuery = true)
    List<Product> findAllTest();

    @Query(value = "select * from nb_product where product_slug = :slug and is_active = 1",
            nativeQuery = true)
    Product findProductBySlug(String slug);

    @Query(value = "select * from nb_product where product_slug = :slug and is_active = 1",
            nativeQuery = true)
    Product findEnableProduct(String slug);

    @Query(value = "select * from nb_product where product_slug = :slug and is_active = 1",
            nativeQuery = true)
    Product findDraftProduct(String slug);

    /*@Query(value = "select * from nb_product where product_slug = :slug and is_active = 1",
            nativeQuery = true)
    Product findProductByTag(String slug);

    @Query(value = "select * from nb_product where product_slug = :slug and is_active = 1",
            nativeQuery = true)
    Product findProductByPageAndSection(String page, String section);

    @Query(value = "select * from nb_product where product_slug = :slug and is_active = 1",
            nativeQuery = true)
    Product findProductByOffer(String offer);*/

}
