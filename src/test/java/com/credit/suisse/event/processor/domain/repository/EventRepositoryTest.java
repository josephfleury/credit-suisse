package com.credit.suisse.event.processor.domain.repository;


import com.credit.suisse.event.processor.domain.model.EventEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Profile("test")
@RunWith(SpringRunner.class)
public class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;


    @Test
    public void findingEventByEventIdTest() {
        // Arrange
        final EventEntity savedEvent = eventRepository.save(new EventEntity("scsmbstgra", 1L, "APPLICATION_LOG", "12345", false));

        // Act
        final EventEntity eventEntity = eventRepository.findById(savedEvent.getId()).get();

        // Assert
        assertThat(eventEntity.getEventId()).isEqualTo("scsmbstgra");
        assertThat(eventEntity.getEventDurationMs()).isEqualTo(1L);
        assertThat(eventEntity.getType()).isEqualTo("APPLICATION_LOG");
        assertThat(eventEntity.getHost()).isEqualTo("12345");
        assertThat(eventEntity.isAlert()).isEqualTo(false);
    }

    @Test
    public void findingAllEventsWithAlertTest() {
        // Arrange
        eventRepository.save(new EventEntity("scsmbstgra", 1L, "APPLICATION_LOG", "12345", false));
        eventRepository.save(new EventEntity("scsmbstgrb", 10L, true));
        eventRepository.save(new EventEntity("scsmbstgrc", 5L, true));

        // Act
        List<EventEntity> eventEntities = eventRepository.findAllByAlert(true);

        // Arrange
        assertThat(eventEntities.size()).isEqualTo(2);
        assertThat(eventEntities.stream()
                .map(EventEntity::isAlert)
                .collect(Collectors.toList())
        ).containsOnly(true);
        assertThat(eventEntities.stream()
                .map(EventEntity::getType)
                .collect(Collectors.toList())
        ).containsNull();
    }

}
