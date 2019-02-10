package com.credit.suisse.event.processor.processor.event;

import com.credit.suisse.event.processor.data.EventData;
import com.credit.suisse.event.processor.domain.model.EventEntity;
import com.credit.suisse.event.processor.domain.EventWriteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class EventProcessorServiceImpl implements EventProcessorService {

    public static ConcurrentHashMap<String, EventData> incompleteEvents = new ConcurrentHashMap(100);

    @Autowired
    private EventWriteService eventWriteService;

    @Async
    public void processEvent(EventData eventData) {
        if (incompleteEvents.keySet().contains(eventData.getId())) {
            EventData existingEvent = incompleteEvents.get(eventData.getId());
            log.debug("Processing event {} and existing event {}", eventData, existingEvent);

            long eventDuration = 0;
            switch (existingEvent.getState()) {
                case "STARTED": {
                    eventDuration = eventData.getTimestamp() - existingEvent.getTimestamp();
                    break;
                }
                case "FINISHED": {
                    eventDuration = existingEvent.getTimestamp() - eventData.getTimestamp();
                    break;
                }
            }

            EventEntity savedEvent = eventWriteService.save(new EventEntity(
                    eventData.getId(),
                    eventDuration,
                    eventData.getType(),
                    eventData.getHost(),
                    eventDuration > 4
            ));

            log.debug("Saved event {}", savedEvent);

            incompleteEvents.remove(existingEvent.getId());
        }
        else {
            incompleteEvents.put(eventData.getId(), eventData);
            log.debug("Adding event {} to incomplete events", eventData);
        }
    }

}
