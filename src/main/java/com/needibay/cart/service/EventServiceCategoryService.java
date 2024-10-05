package com.needibay.cart.service;

import com.needibay.cart.entity.EventServiceCategory;

import java.util.List;

public interface EventServiceCategoryService {
    public List<EventServiceCategory> findAll();

    public EventServiceCategory findById(int theId);

    public void save(EventServiceCategory theEventServiceCategory);

    public void delete(int theId);
}
