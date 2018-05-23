package com.service.additionalservice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoteService {
    Page<Note> listAllByPage(Pageable pageable);
}