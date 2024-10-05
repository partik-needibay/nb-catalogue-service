package com.needibay.cart.repository;

import com.needibay.cart.entity.MediaGallery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MediaGalleryRepo extends JpaRepository<MediaGallery, Long> {

    @Query(value = "select * from nb_media_gallery where model = 'PRODUCT_CATEGORY' and model_entity_id = :id", nativeQuery = true)
    List<MediaGallery> findMediaByProductCategoryId(Integer id);
}
