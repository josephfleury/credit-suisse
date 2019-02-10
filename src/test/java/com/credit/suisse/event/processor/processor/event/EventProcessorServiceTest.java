package com.credit.suisse.event.processor.processor.event;


import com.credit.suisse.event.processor.ApplicationTest;
import com.credit.suisse.event.processor.domain.repository.EventRepository;
import com.credit.suisse.event.processor.data.EventData;
import com.credit.suisse.event.processor.domain.EventWriteService;
import com.credit.suisse.event.processor.domain.model.EventEntity;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Iterator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

@SpringBootTest(classes = ApplicationTest.class)
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class EventProcessorServiceTest {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventWriteService eventWriteService;

    @Autowired
    private EventProcessorService eventProcessorService;

    @Test
    public void processInOrderEventsTest(){
        // Arrange
        List<EventData> events = Lists.newArrayList(
                new EventData("scsmbstgra", "STARTED", 1491377495212L, null, null),
                new EventData("scsmbstgra", "FINISHED", 1491377495217L, null, null),
                new EventData("scsmbstgrb", "STARTED", 1491377495213L, "APPLICATION_LOG", "12345"),
                new EventData("scsmbstgrb", "FINISHED", 1491377495216L, "APPLICATION_LOG", "12345")
        );

        // Act
        events.forEach( e -> eventProcessorService.processEvent(e) );

        // Assert
        Iterator<EventEntity> savedEvents = eventRepository.findAll().iterator();

        savedEvents.forEachRemaining(se -> {
            if (se.getEventId().equals("scsmbstgra")) {
                assertThat(se.getEventDurationMs()).isEqualTo(5L);
                assertThat(se.getType()).isEqualTo(null);
                assertThat(se.getHost()).isEqualTo(null);
                assertThat(se.isAlert()).isEqualTo(true);
            }
            else if (se.getEventId().equals("scsmbstgrb")) {
                assertThat(se.getEventDurationMs()).isEqualTo(3L);
                assertThat(se.getType()).isEqualTo("APPLICATION_LOG");
                assertThat(se.getHost()).isEqualTo("12345");
                assertThat(se.isAlert()).isEqualTo(false);
            }
            else {
                fail();
            }
        });
    }

    @Test
    public void processOutOfOrderEventsTest(){
        // Arrange
        List<EventData> events = Lists.newArrayList(
                new EventData("scsmbstgra", "STARTED", 1491377495212L, null, null),
                new EventData("scsmbstgrb", "FINISHED", 1491377495216L, "APPLICATION_LOG", "12345"),
                new EventData("scsmbstgrb", "STARTED", 1491377495213L, "APPLICATION_LOG", "12345"),
                new EventData("scsmbstgra", "FINISHED", 1491377495217L, null, null)
        );

        // Act
        events.forEach( e -> eventProcessorService.processEvent(e) );

        // Assert
        Iterator<EventEntity> savedEvents = eventRepository.findAll().iterator();

        savedEvents.forEachRemaining(se -> {
            if (se.getEventId().equals("scsmbstgra")) {
                assertThat(se.getEventDurationMs()).isEqualTo(5L);
                assertThat(se.getType()).isEqualTo(null);
                assertThat(se.getHost()).isEqualTo(null);
                assertThat(se.isAlert()).isEqualTo(true);
            }
            else if (se.getEventId().equals("scsmbstgrb")) {
                assertThat(se.getEventDurationMs()).isEqualTo(3L);
                assertThat(se.getType()).isEqualTo("APPLICATION_LOG");
                assertThat(se.getHost()).isEqualTo("12345");
                assertThat(se.isAlert()).isEqualTo(false);
            }
            else {
                fail();
            }
        });
    }

}
