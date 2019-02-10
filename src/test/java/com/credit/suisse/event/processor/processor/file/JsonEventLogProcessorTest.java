package com.credit.suisse.event.processor.processor.file;

import com.credit.suisse.event.processor.ApplicationTest;
import com.credit.suisse.event.processor.domain.model.EventEntity;
import com.credit.suisse.event.processor.domain.repository.EventRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Iterator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

@SpringBootTest(classes = ApplicationTest.class)
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class JsonEventLogProcessorTest {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventLogProcessor eventLogProcessor;

    @Test
    public void testRun() {
        eventLogProcessor.run();

        Iterator<EventEntity> savedEvents = eventRepository.findAll().iterator();

        savedEvents.forEachRemaining(se -> {
            if (se.getEventId().equals("scsmbstgra")) {
                assertThat(se.getEventDurationMs()).isEqualTo(5L);
                assertThat(se.getType()).isEqualTo("APPLICATION_LOG");
                assertThat(se.getHost()).isEqualTo("12345");
                assertThat(se.isAlert()).isEqualTo(true);
            }
            else if (se.getEventId().equals("scsmbstgrb")) {
                assertThat(se.getEventDurationMs()).isEqualTo(3L);
                assertThat(se.getType()).isEqualTo(null);
                assertThat(se.getHost()).isEqualTo(null);
                assertThat(se.isAlert()).isEqualTo(false);
            }
            else if (se.getEventId().equals("scsmbstgrc")) {
                assertThat(se.getEventDurationMs()).isEqualTo(8L);
                assertThat(se.getType()).isEqualTo(null);
                assertThat(se.getHost()).isEqualTo(null);
                assertThat(se.isAlert()).isEqualTo(true);
            }
            else {
                fail();
            }
        });

    }
}
