package com.credit.suisse.event.processor.domain.repository;

import com.credit.suisse.event.processor.domain.model.EventEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends CrudRepository<EventEntity, Long> {

    List<EventEntity> findAllByAlert(boolean alert);

}
