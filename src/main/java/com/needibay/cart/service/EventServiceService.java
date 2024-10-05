package com.needibay.cart.service;

import com.needibay.cart.entity.EventService;

import java.util.List;

public interface EventServiceService {
    public List<EventService> findAll();

    public EventService findById(int theId);

    public void save(EventService theEventService);

    public void delete(int theId);
}
