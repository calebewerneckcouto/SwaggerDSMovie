package com.devsuperior.dsmovie.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.devsuperior.dsmovie.dto.MovieDTO;
import com.devsuperior.dsmovie.dto.MovieGenreDTO;
import com.devsuperior.dsmovie.services.MovieService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/movies")
@Tag(name = "Movies", description = "Controller for Movie")
public class MovieController {

	@Autowired
	private MovieService service;

	@Operation(description = "Get All movies", summary = "List all movies", responses = {
			@ApiResponse(description = "Ok", responseCode = "200"), })
	@GetMapping(produces = "application/json")
	public Page<MovieDTO> findAll(Pageable pageable) {
		return service.findAll(pageable);
	}
	
	
	
	
	@Operation(description = "Get All movies", summary = "List all movies", responses = {
			@ApiResponse(description = "Ok", responseCode = "200"), })
	@GetMapping(produces = "application/vdn.devsuperior.dsmovie-v1+json")
	public Page<MovieGenreDTO> findAllV1(Pageable pageable) {
		return service.findAllGenre(pageable);
	}
	
	

	@Operation(description = "Get movie by id", summary = "Get movie by id", responses = {
			@ApiResponse(description = "Ok", responseCode = "200"),
			@ApiResponse(description = "Not Found", responseCode = "404"), })

	@GetMapping(value = "/{id}", produces = "application/json")
	public MovieDTO findById(@PathVariable Long id) {
		return service.findById(id);
	}
	
	
	
	@Operation(description = "Get movie by id", summary = "Get movie by id", responses = {
			@ApiResponse(description = "Ok", responseCode = "200"),
			@ApiResponse(description = "Not Found", responseCode = "404"), })

	@GetMapping(value = "/{id}", produces = "application/vdn.devsuperior.dsmovie-v1+json")
	public MovieGenreDTO findByIdV1(@PathVariable Long id) {
		return service.findByIdMovieGenre(id);
	}

	@Operation(description = "Create a new movie", summary = "Create a new movie", responses = {
			@ApiResponse(description = "Created", responseCode = "201"),
			@ApiResponse(description = "Bad Request", responseCode = "400"),
			@ApiResponse(description = "Unauthorized", responseCode = "401"),
			@ApiResponse(description = "Forbidden", responseCode = "403"),
			@ApiResponse(description = "Unprocessable Entity", responseCode = "422") })
	@SecurityRequirement(name = "bearerAuth")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping(produces = "application/json")
	public ResponseEntity<MovieDTO> insert(@Valid @RequestBody MovieDTO dto) {
		dto = service.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}
	
	
	
	@Operation(description = "Create a new movie", summary = "Create a new movie", responses = {
			@ApiResponse(description = "Created", responseCode = "201"),
			@ApiResponse(description = "Bad Request", responseCode = "400"),
			@ApiResponse(description = "Unauthorized", responseCode = "401"),
			@ApiResponse(description = "Forbidden", responseCode = "403"),
			@ApiResponse(description = "Unprocessable Entity", responseCode = "422") })
	@SecurityRequirement(name = "bearerAuth")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping(produces = "application/vdn.devsuperior.dsmovie-v1+json")
	public ResponseEntity<MovieGenreDTO> insertV1(@Valid @RequestBody MovieGenreDTO dto) {
		dto = service.insertMovieGenre(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}
	
	
	
	
	
	
	

	@Operation(description = "Update a  movie", summary = "Update a new movie", responses = {
			@ApiResponse(description = "Ok", responseCode = "200"),

			@ApiResponse(description = "Bad Request", responseCode = "400"),
			@ApiResponse(description = "Unauthorized", responseCode = "401"),
			@ApiResponse(description = "Forbidden", responseCode = "403"),
			@ApiResponse(description = "Not Found", responseCode = "404"),
			@ApiResponse(description = "Unprocessable Entity", responseCode = "422") })

	@SecurityRequirement(name = "bearerAuth")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<MovieDTO> update(@PathVariable Long id, @Valid @RequestBody MovieDTO dto) {
		dto = service.update(id, dto);
		return ResponseEntity.ok().body(dto);
	}
	
	
	
	
	@Operation(description = "Update a  movie", summary = "Update a new movie", responses = {
			@ApiResponse(description = "Ok", responseCode = "200"),

			@ApiResponse(description = "Bad Request", responseCode = "400"),
			@ApiResponse(description = "Unauthorized", responseCode = "401"),
			@ApiResponse(description = "Forbidden", responseCode = "403"),
			@ApiResponse(description = "Not Found", responseCode = "404"),
			@ApiResponse(description = "Unprocessable Entity", responseCode = "422") })

	@SecurityRequirement(name = "bearerAuth")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping(value = "/{id}", produces = "application/vdn.devsuperior.dsmovie-v1+json")
	public ResponseEntity<MovieGenreDTO> updatev1(@PathVariable Long id, @Valid @RequestBody MovieGenreDTO dto) {
		dto = service.updateMoviegenre(id, dto);
		return ResponseEntity.ok().body(dto);
	}


	@Operation(description = "Delete a new movie", summary = "Delete a  movie", responses = {
			@ApiResponse(description = "Sucess", responseCode = "204"),
			@ApiResponse(description = "Bad Request", responseCode = "400"),
			@ApiResponse(description = "Unauthorized", responseCode = "401"),
			@ApiResponse(description = "Forbidden", responseCode = "403"),
			@ApiResponse(description = "Not Found", responseCode = "404"),
			@ApiResponse(description = "Unprocessable Entity", responseCode = "422") })
	@SecurityRequirement(name = "bearerAuth")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<MovieDTO> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	
	
	@Operation(description = "Delete a new movie", summary = "Delete a  movie", responses = {
			@ApiResponse(description = "Sucess", responseCode = "204"),
			@ApiResponse(description = "Bad Request", responseCode = "400"),
			@ApiResponse(description = "Unauthorized", responseCode = "401"),
			@ApiResponse(description = "Forbidden", responseCode = "403"),
			@ApiResponse(description = "Not Found", responseCode = "404"),
			@ApiResponse(description = "Unprocessable Entity", responseCode = "422") })
	@SecurityRequirement(name = "bearerAuth")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping(value = "/{id}", produces = "application/vdn.devsuperior.dsmovie-v1+json")
	public ResponseEntity<MovieGenreDTO> deleteV1(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
