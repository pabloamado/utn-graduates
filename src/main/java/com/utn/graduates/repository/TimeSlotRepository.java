package com.utn.graduates.repository;

import com.utn.graduates.model.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
    public List<TimeSlot> findByEventId(Long eventId);
}
