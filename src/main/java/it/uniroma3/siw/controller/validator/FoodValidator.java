package it.uniroma3.siw.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Food;
import it.uniroma3.siw.repository.FoodRepository;

@Component
public class FoodValidator implements Validator {
	@Autowired
	private FoodRepository foodRepository;

	@Override
	public void validate(Object o, Errors errors) {
		Food food = (Food)o;
		
		
		if (food.getId() == null) {
			
			 if (foodRepository.existsByName(food.getName())) {
		            
				 errors.rejectValue("name", "duplicate", "Esiste già!");      
		     }
			 
		} else {
			
	        Food existingFood = foodRepository.findById(food.getId()).orElse(null);
	        if 	(existingFood != null &&        		
	            (!existingFood.getId().equals(food.getId()) )
	            ){
	
	            errors.rejectValue("name", "duplicate", "Esiste già!");
	        }
		}	
	}
	
	@Override
	public boolean supports(Class<?> aClass) {
		return Food.class.equals(aClass);
	}
}