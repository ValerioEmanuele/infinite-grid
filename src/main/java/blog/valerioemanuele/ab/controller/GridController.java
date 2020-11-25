package blog.valerioemanuele.ab.controller;

import java.io.IOException;
import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import blog.valerioemanuele.ab.service.GridService;
import lombok.extern.slf4j.Slf4j;

@EnableWebMvc
@RestController
@Slf4j
public class GridController {
	
	@Autowired
	private GridService gridService;
	
	

	@PutMapping(path="/grid/{numberOfSteps}", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> generateGridFor(@PathVariable(value = "numberOfSteps") BigInteger numberOfSteps) {
		try {
			gridService.gridFor(numberOfSteps);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return ResponseEntity.ok().build();
	}

}
