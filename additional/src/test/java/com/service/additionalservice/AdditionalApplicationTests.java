package com.service.additionalservice;

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
public class AdditionalApplicationTests {

	private TestRestTemplate restTemplate = new TestRestTemplate();

	@Autowired
	JdbcTemplate jdbcTemplate;

	@LocalServerPort
	private int port;


	@Test
	public void getMonthlyNotes() {

		String sql = "DELETE from Note";
		jdbcTemplate.execute(sql);
		sql = "INSERT INTO note VALUES (1,FALSE,'Content','2012-10-22 11:13:00','2012-10-24 11:13:00','Title')";
		jdbcTemplate.execute(sql);

		ResponseEntity<Note[]> responseEntity = restTemplate.exchange("http://localhost:"+port+"/month", HttpMethod.GET, null, Note[].class);
		Note[] responseNotes = responseEntity.getBody();
		Note responseNote = responseNotes[0];

		assertEquals("Title",responseNote.getTitle());
		assertEquals("Content",responseNote.getContent());
		assertEquals(LocalDateTime.of(2012,10,22,11,13),responseNote.getCreationDate());
		assertEquals(LocalDateTime.of(2012,10,24,11,13),responseNote.getModificationDate());
		assertEquals(false,responseNote.isArchived());


		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	}

	@Test
	public void getAllWithPagination() {

		String sql = "DELETE from Note";
		jdbcTemplate.execute(sql);
		sql = "INSERT INTO note VALUES (1,false,'first Content','2412-10-22 11:13:00','2112-10-24 11:13:00','first Title')";
		jdbcTemplate.execute(sql);
		sql = "INSERT INTO note VALUES (2,false,'Content','2012-10-22 11:13:00','2012-10-24 11:13:00','Title')";
		jdbcTemplate.execute(sql);

		ResponseEntity<Note[]> responseEntity = restTemplate.exchange("http://localhost:"+port+"/all?sort=id,asc&size=1&page=1", HttpMethod.GET, null, Note[].class);
		Note[] responseNotes = responseEntity.getBody();
		Note responseNote = responseNotes[0];

		assertEquals("Title",responseNote.getTitle());
		assertEquals("Content",responseNote.getContent());
		assertEquals(LocalDateTime.of(2012,10,22,11,13),responseNote.getCreationDate());
		assertEquals(LocalDateTime.of(2012,10,24,11,13),responseNote.getModificationDate());
		assertEquals(false,responseNote.isArchived());


		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	}

	@Test
	public void getArchivedNotes() {

		String sql = "DELETE from Note";
		jdbcTemplate.execute(sql);
		sql = "INSERT INTO note VALUES (1,FALSE,'first Content','2412-10-22 11:13:00','2112-10-24 11:13:00','first Title')";
		jdbcTemplate.execute(sql);
		sql = "INSERT INTO note VALUES (2,TRUE,'Content','2012-10-22 11:13:00','2012-10-24 11:13:00','Title')";
		jdbcTemplate.execute(sql);

		ResponseEntity<Note[]> responseEntity = restTemplate.exchange("http://localhost:"+port+"/archive", HttpMethod.GET, null, Note[].class);
		Note[] responseNotes = responseEntity.getBody();
		Note responseNote = responseNotes[0];

		assertEquals(1,responseNotes.length);

		assertEquals("Title",responseNote.getTitle());
		assertEquals("Content",responseNote.getContent());
		assertEquals(LocalDateTime.of(2012,10,22,11,13),responseNote.getCreationDate());
		assertEquals(LocalDateTime.of(2012,10,24,11,13),responseNote.getModificationDate());
		assertEquals(true,responseNote.isArchived());


		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	}

	@Test
	public void archiveNotes() {

		String sql = "DELETE from Note";
		jdbcTemplate.execute(sql);
		sql = "INSERT INTO note VALUES (1,FALSE,'Content','2000-10-22 11:13:00','2012-10-24 11:13:00','Title')";
		jdbcTemplate.execute(sql);
		String noteResponseUrl = "http://localhost:"+port+"/archive";

		HttpEntity<Note> request = new HttpEntity<Note>(new Note("2010-10-20T13:15",null,null,null,false));
		ResponseEntity<String> responseEntity = restTemplate.exchange(noteResponseUrl, HttpMethod.PUT, request, String.class);

		assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());

		List<Boolean> listArchived = jdbcTemplate.queryForList("select archived from Note", Boolean.class);
		assertEquals(listArchived.get(0), true);
	}
}