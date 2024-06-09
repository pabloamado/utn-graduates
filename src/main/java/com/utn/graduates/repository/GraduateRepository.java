package com.utn.graduates.repository;

import com.utn.graduates.model.Graduate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface GraduateRepository extends JpaRepository<Graduate, Long> {

    @Query("SELECT g FROM Graduate g WHERE g.fullname LIKE %:param% or g.dni LIKE %:param%")
    Page<Graduate> findByParam(@Param("param") String param, Pageable pageable);

    @Query("SELECT g.dni FROM Graduate g")
    Set<String> findAllDni();
}
