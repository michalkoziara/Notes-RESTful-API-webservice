package com.service.additionalservice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
        Page<Note> findAll(Pageable pageable);
}