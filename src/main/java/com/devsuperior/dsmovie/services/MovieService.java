package com.devsuperior.dsmovie.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dsmovie.controllers.MovieController;
import com.devsuperior.dsmovie.controllers.MovieControllerV1;
import com.devsuperior.dsmovie.dto.MovieDTO;
import com.devsuperior.dsmovie.dto.MovieGenreDTO;
import com.devsuperior.dsmovie.entities.MovieEntity;
import com.devsuperior.dsmovie.repositories.MovieRepository;
import com.devsuperior.dsmovie.services.exceptions.DatabaseException;
import com.devsuperior.dsmovie.services.exceptions.ResourceNotFoundException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import jakarta.persistence.EntityNotFoundException;

@Service
public class MovieService {

	@Autowired
	private MovieRepository repository;

	@Transactional(readOnly = true)
	public Page<MovieDTO> findAll(Pageable pageable) {
		Page<MovieEntity> result = repository.findAll(pageable);
		Page<MovieDTO> page = result.map(x -> new MovieDTO(x)
				.add(linkTo(methodOn(MovieController.class).findAll(null)).withSelfRel())
				.add(linkTo(methodOn(MovieController.class).findById(x.getId())).withRel("Get movie by id")));
		return page;
	}
	
	
	@Transactional(readOnly = true)
	public Page<MovieGenreDTO> findAllGenre(Pageable pageable) {
		Page<MovieEntity> result = repository.findAll(pageable);
		Page<MovieGenreDTO> page = result.map(x -> new MovieGenreDTO(x)
				.add(linkTo(methodOn(MovieControllerV1.class).findAll(null)).withSelfRel())
				.add(linkTo(methodOn(MovieControllerV1.class).findById(x.getId())).withRel("Get movie by id")));
		return page;
	}

	@Transactional(readOnly = true)
	public MovieDTO findById(Long id) {
		MovieEntity result = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado"));
		return new MovieDTO(result).
				add(linkTo(methodOn(MovieController.class).findById(id)).withSelfRel())
				.add(linkTo(methodOn(MovieController.class).findAll(null)).withRel("All moveis"))
				.add(linkTo(methodOn(MovieController.class).update(id,null)).withRel("Update movie"))
		         .add(linkTo(methodOn(MovieController.class).delete(id)).withRel("Delete movie"));
	}
	
	
	@Transactional(readOnly = true)
	public MovieGenreDTO findByIdMovieGenre(Long id) {
		MovieEntity result = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado"));
		return new MovieGenreDTO(result).
				add(linkTo(methodOn(MovieControllerV1.class).findById(id)).withSelfRel())
				.add(linkTo(methodOn(MovieControllerV1.class).findAll(null)).withRel("All moveis"))
				.add(linkTo(methodOn(MovieControllerV1.class).update(id,null)).withRel("Update movie"))
		         .add(linkTo(methodOn(MovieControllerV1.class).delete(id)).withRel("Delete movie"));
	}

	@Transactional
	public MovieDTO insert(MovieDTO dto) {
		MovieEntity entity = new MovieEntity();
		copyDtoToEntity(dto, entity);
		entity = repository.save(entity);
		return new MovieDTO(entity).add(linkTo(methodOn(MovieController.class).findById(entity.getId())).withRel("Get movie by id"));
	}
	
	
	@Transactional
	public MovieGenreDTO insertMovieGenre(MovieGenreDTO dto) {
		MovieEntity entity = new MovieEntity();
		copyDtoToEntityGenre(dto, entity);
		entity = repository.save(entity);
		return new MovieGenreDTO(entity).add(linkTo(methodOn(MovieControllerV1.class).findById(entity.getId())).withRel("Get movie by id"));
	}

	@Transactional
	public MovieDTO update(Long id, MovieDTO dto) {
		try {
			MovieEntity entity = repository.getReferenceById(id);
			copyDtoToEntity(dto, entity);
			entity = repository.save(entity);
			return new MovieDTO(entity).add(linkTo(methodOn(MovieController.class).findById(entity.getId())).withRel("Get movie by id"));
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Recurso não encontrado");
		}
	}
	
	
	
	@Transactional
	public MovieGenreDTO updateMoviegenre(Long id, MovieGenreDTO dto) {
		try {
			MovieEntity entity = repository.getReferenceById(id);
			copyDtoToEntityGenre(dto, entity);
			entity = repository.save(entity);
			return new MovieGenreDTO(entity).add(linkTo(methodOn(MovieController.class).findById(entity.getId())).withRel("Get movie by id"));
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Recurso não encontrado");
		}
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	public void delete(Long id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Recurso não encontrado");
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Falha de integridade referencial");
		}
	}
	
	
	private void copyDtoToEntityGenre(MovieGenreDTO dto, MovieEntity entity) {
		entity.setTitle(dto.getTitle());
		entity.setScore(dto.getScore());
		entity.setCount(dto.getCount());
		entity.setImage(dto.getImage());
	}

	private void copyDtoToEntity(MovieDTO dto, MovieEntity entity) {
		entity.setTitle(dto.getTitle());
		entity.setScore(dto.getScore());
		entity.setCount(dto.getCount());
		entity.setImage(dto.getImage());
	}
}