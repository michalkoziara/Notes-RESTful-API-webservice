package com.web.webservice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class) //Starts with in-memory database, without a web tier, only data tier
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT) //Use with REST client class
public class WebserviceApplicationTests {

    private TestRestTemplate restTemplate = new TestRestTemplate();

    @Autowired
    JdbcTemplate jdbcTemplate;

    @LocalServerPort
    private int port;


    @Test
    public void getNote() {

        String sql = "DELETE from Note";
        jdbcTemplate.execute(sql);
        sql = "INSERT INTO note VALUES (1,FALSE,'Content','2012-10-22 11:13:00','2012-10-24 11:13:00','Title')";
        jdbcTemplate.execute(sql);

        Note testNote = new Note("Title","Content",
                LocalDateTime.of(2012,10,22,11,13),
                LocalDateTime.of(2012,10,24,11,13),false);

        ResponseEntity<Note> responseEntity =
                restTemplate.getForEntity("http://localhost:"+port+"/notes/1",Note.class);
        Note responseNote = responseEntity.getBody();

        assertEquals(testNote.getTitle(),responseNote.getTitle());
        assertEquals(testNote.getContent(),responseNote.getContent());
        assertEquals(testNote.getCreationDate(),responseNote.getCreationDate());
        assertEquals(testNote.getModificationDate(),responseNote.getModificationDate());
        assertEquals(testNote.isArchived(),responseNote.isArchived());


        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

    }

    @Test
    public void createNote() {

        String sql = "DELETE from Note";
        jdbcTemplate.execute(sql);
        ResponseEntity<Note> responseEntity =
                restTemplate.postForEntity("http://localhost:"+port+"/notes", new Note("New Title","Content",
                        LocalDateTime.of(2000,10,20,13,15),
                        LocalDateTime.of(2000,10,20,13,15),false), Note.class);
        Note note = responseEntity.getBody();

        List<String> listTitle = jdbcTemplate.queryForList("select title from Note", String.class);
        assertEquals(listTitle.get(0), "New Title");

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    public void deleteNote() {

        String sql = "DELETE from Note";
        jdbcTemplate.execute(sql);
        sql = "INSERT INTO note VALUES (1,FALSE,'Content','2012-10-22 11:13:00','2012-10-24 11:13:00','Title')";
        jdbcTemplate.execute(sql);

        String noteResponseUrl = "http://localhost:"+port+"/notes/1";

        ResponseEntity<Note> responseEntity = restTemplate.exchange(noteResponseUrl, HttpMethod.DELETE, null, Note.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        List<Integer> list = jdbcTemplate.queryForList("select id from Note", Integer.class);
        assertEquals(true, list.isEmpty());
    }

    @Test
    public void updateNote() {

        String sql = "DELETE from Note";
        jdbcTemplate.execute(sql);
        sql = "INSERT INTO note VALUES (1,FALSE,'Content','2012-10-22 11:13:00','2012-10-24 11:13:00','Title')";
        jdbcTemplate.execute(sql);
        String noteResponseUrl = "http://localhost:"+port+"/notes/1";

        HttpEntity<Note> request = new HttpEntity<Note>(new Note("newTitle","new content",
                LocalDateTime.of(2000,10,20,13,15),
                LocalDateTime.of(2000,10,20,13,15),true));
        ResponseEntity<Note> responseEntity = restTemplate.exchange(noteResponseUrl, HttpMethod.PUT, request, Note.class);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());

        List<String> listTitle = jdbcTemplate.queryForList("select title from Note", String.class);
        assertEquals(listTitle.get(0), "newTitle");
    }
}