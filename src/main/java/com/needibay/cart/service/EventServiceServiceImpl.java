package com.needibay.cart.service;

import com.needibay.cart.repository.EventServiceRepo;
import com.needibay.cart.entity.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventServiceServiceImpl implements EventServiceService {

    @Autowired
    EventServiceRepo eventServiceRepo;

    @Override
    public List<EventService> findAll() {
        return eventServiceRepo.findAll();
    }

    @Override
    public EventService findById(int theId) {
        return eventServiceRepo.findById(theId).orElseThrow();
    }

    @Override
    public void save(EventService theEventService) {
        eventServiceRepo.save(theEventService);
    }

    @Override
    public void delete(int theId) {
        eventServiceRepo.deleteById(theId);
    }
}
