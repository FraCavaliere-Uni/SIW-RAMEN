package it.uniroma3.siw.repository;




import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import it.uniroma3.siw.model.Food;

public interface FoodRepository extends CrudRepository<Food, Long> {

	
	@Query("SELECT f FROM Food f ORDER BY f.name ASC")
	public Iterable<Food> findAllOrderByName();


	public boolean existsByName(String name);



}