package com.credit.suisse.event.processor.domain;

import com.credit.suisse.event.processor.domain.model.EventEntity;
import com.credit.suisse.event.processor.domain.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventWriteService {

    @Autowired
    private EventRepository eventRepository;

    @Transactional
    public EventEntity save(EventEntity eventEntity) {
        return eventRepository.save(eventEntity);
    }

}
