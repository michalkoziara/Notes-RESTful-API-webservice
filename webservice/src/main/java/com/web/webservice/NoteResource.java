package com.web.webservice;

import com.web.webservice.exceptions.NoteNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
public class NoteResource {

    @Autowired
    EntityManager entityManager;

    @Autowired
    private NoteRepository noteRepository;

    @GetMapping("/notes/{id}")
    public Note retrieveNote(@PathVariable long id) {
        Optional<Note> note = noteRepository.findById(id);

        note.orElseThrow(() -> new NoteNotFoundException("id-" + id));

        return note.get();
    }

    @DeleteMapping("/notes/{id}")
    public void deleteStudent(@PathVariable long id) {
        noteRepository.deleteById(id);
    }

    @PostMapping("/notes")
    public ResponseEntity<Object> createNote(@Valid @RequestBody Note note) {
        LocalDateTime timeNow = LocalDateTime.now();
        note.setCreationDate(timeNow);
        note.setModificationDate(timeNow);
        note.setArchived(false);

        Note savedNote = noteRepository.save(note);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedNote.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/notes/{id}")
    public ResponseEntity<Object> updateNote(@Valid @RequestBody Note note, @PathVariable long id) {

        Optional<Note> noteOptional = noteRepository.findById(id);

        if (!noteOptional.isPresent())
            return ResponseEntity.notFound().build();

        note.setId(id);

        LocalDateTime timeNow = LocalDateTime.now();
        note.setModificationDate(timeNow);
        note.setCreationDate(noteRepository.findById(id).get().getCreationDate());
        note.setArchived(noteRepository.findById(id).get().isArchived());

        noteRepository.save(note);

        return ResponseEntity.noContent().build();
    }
}
