package com.credit.suisse.event.processor.processor.file;

import com.credit.suisse.event.processor.reader.EventLogReader;
import com.credit.suisse.event.processor.data.EventData;
import com.credit.suisse.event.processor.processor.event.EventProcessorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JsonEventLogProcessor implements EventLogProcessor {

    @Autowired
    private EventProcessorService eventProcessorService;

    @Autowired
    private EventLogReader<EventData> eventLogReader;

    private boolean run = false;

    @Override
    public void run() {
        run = true;
        log.info("Starting the JsonEventLogProcessor");
        try {
            eventLogReader.open();
            while(run && eventLogReader.hasNext()) {
                try {
                    final EventData eventData = eventLogReader.read();
                    eventProcessorService.processEvent(eventData);
                    log.debug("Event added to processor {}", eventData);
                } catch (Exception e){
                    log.error("Failed to process event", e);
                }
            }
            eventLogReader.close();
            log.info("Finished processing events");
        } catch (Exception e) {
            log.error("Failed to process events", e);
        }
    }

    @Override
    public void stop() {
        run = false;
    }

}
