package com.service.additionalservice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("noteService")
@Transactional
public class NoteServiceImpl implements NoteService{

    private final NoteRepository noteRepository;

    public NoteServiceImpl(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Override
    public Page<Note> listAllByPage(Pageable pageable) {
        return this.noteRepository.findAll(pageable);

    }
}
