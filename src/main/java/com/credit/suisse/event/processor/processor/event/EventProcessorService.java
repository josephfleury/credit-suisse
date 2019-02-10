package com.credit.suisse.event.processor.processor.event;

import com.credit.suisse.event.processor.data.EventData;

public interface EventProcessorService {

    void processEvent(EventData eventData);

}
