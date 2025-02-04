package it.uniroma3.siw.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Evento;

public interface EventoRepository extends CrudRepository<Evento, Long> {

	public List<Evento> findByDate(LocalDate date);

	@Query(value = "SELECT * FROM Evento WHERE sala_id = ?1 ORDER BY title ASC", nativeQuery = true)
	public List<Evento> findBySalaId(Long salaId);

	@Query(value = "SELECT * FROM Evento WHERE sala_id IS NULL ORDER BY title ASC", nativeQuery = true)
	public List<Evento> findEventiNonAssegnati();

	public boolean existsByTitleAndDate(String title, LocalDate date);
		
	
}