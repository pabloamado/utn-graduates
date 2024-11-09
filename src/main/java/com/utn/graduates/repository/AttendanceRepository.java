package com.utn.graduates.repository;

import com.utn.graduates.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    @Query("SELECT a FROM Attendance a where a.timeSlot.id = :timeSlotId")
    List<Attendance> findAllByTimeSlotId(@Param("timeSlotId") Long timeSlotId);

    List<Attendance> findByGraduateId(Long graduateId);
}
