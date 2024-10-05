package com.needibay.cart.service;

import com.needibay.cart.repository.EventServiceCategoryRepo;
import com.needibay.cart.entity.EventServiceCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventServiceCategoryServiceImpl implements EventServiceCategoryService {

    @Autowired
    EventServiceCategoryRepo eventServiceCategoryRepo;

    @Override
    public List<EventServiceCategory> findAll() {
        return eventServiceCategoryRepo.findAll();
    }

    @Override
    public EventServiceCategory findById(int theId) {
        return eventServiceCategoryRepo.findById(theId).orElseThrow();
    }

    @Override
    public void save(EventServiceCategory theEventServiceCategory) {
        eventServiceCategoryRepo.save(theEventServiceCategory);
    }

    @Override
    public void delete(int theId) {
        eventServiceCategoryRepo.deleteById(theId);
    }
}
