package com.service.additionalservice;

import com.fasterxml.jackson.databind.node.TextNode;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class NoteResource {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private NoteService service;

    @GetMapping("/month")
    public List<Note> retrieveMonthlyNotes() {
        List<Note> list = noteRepository.findAll();

        LocalDateTime timeNow = LocalDateTime.now();

        return  list.stream()
                .filter(note -> note.getModificationDate().isBefore(timeNow.minusMonths(1)))
                .collect(Collectors.toList());
    }

    @PutMapping("/archive")
    public ResponseEntity<Object> archiveNotes(@RequestBody Note givenNote) {

        List<Note> list = noteRepository.findAll();

        LocalDateTime dateTime = LocalDateTime.parse(givenNote.getTitle(), DateTimeFormatter.ISO_DATE_TIME);
        System.out.println(dateTime);
        for(Note note: list)
        {
            if(note.getCreationDate().isBefore(dateTime))
            {
                Note newNote = noteRepository.getOne(note.getId());
                newNote.setArchived(true);
                noteRepository.save(newNote);
            }
        }

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/archive")
    public List<Note> retrieveArchivedNotes() {
        List<Note> list = noteRepository.findAll();

        return  list.stream()
                .filter(note -> note.isArchived())
                .collect(Collectors.toList());
    }

    @GetMapping("/all")
    public List<Note> retrieveAllNotesPaginated(Pageable pageable) {
        List<Note> resultPage = service.listAllByPage(pageable).getContent();

        return  resultPage.stream()
                .filter(note -> !note.isArchived())
                .collect(Collectors.toList());
    }
}
