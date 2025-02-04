package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.controller.validator.FoodValidator;
import it.uniroma3.siw.model.Food;
import it.uniroma3.siw.repository.FoodRepository;
import it.uniroma3.siw.service.FileHandlerService;

@Controller
public class FoodController {
	
	@Autowired 
	private FoodRepository foodRepository;
	
	@Autowired
	private FoodValidator foodValidator;

	@Autowired
	private FileHandlerService fileHandlerService;
	

///////////////// GESTIONE AZIONI /////////////////

	@GetMapping("/admin/formNewFood")
	public String formNewFood(Model model) {
		model.addAttribute("food", new Food());
		model.addAttribute("action","create");
		return "admin/formNewFood.html";
	}
	
	@GetMapping("/admin/manageFood/{id}")
	public String manageFood(@PathVariable("id") Long id, Model model) {
		Food food = foodRepository.findById(id).orElse(null);
		if(food != null) {
			model.addAttribute("food", food);
			model.addAttribute("action","update");
				
		}
		
		return "admin/formNewFood";
	}
	
	
///////////////// INSERIMENTO, MODIFICA E CANCELLAZIONE /////////////////

	@PostMapping({"/admin/submitFood", "/admin/submitFood/{id}"})
	public String submitFood(@PathVariable(value = "id", required = false) Long id, Food food, BindingResult bindingResult, Model model) {

	    model.addAttribute("food", food);
	    this.foodValidator.validate(food, bindingResult);
	    
	    if (bindingResult.hasErrors()) {
	        model.addAttribute("food", food);
	        return "admin/formNewFood";
	    }
	
	    if (id != null) {
	        	    
	        Food foodToUpdate = foodRepository.findById(id).orElse(null);
	        if (foodToUpdate != null) {
	        	foodToUpdate.setName(food.getName());
	        	foodToUpdate.setDescrizione(food.getDescrizione());
	        	foodToUpdate.setPrezzo(food.getPrezzo());
	        	
	        	foodToUpdate.setUrlOfPicture(food.getUrlOfPicture());

	           
	            foodRepository.save(foodToUpdate);
	            return  "redirect:/food/" + foodToUpdate.getId();
	        }
	    }
	    foodRepository.save(food);
	    return "redirect:/food/" + food.getId();
    
	}
	
	@GetMapping("/admin/deleteFood/{id}")
    public String deleteFood(@PathVariable Long id) {
		
			
		String urlToDelete = foodRepository.findById(id).get().getUrlOfPicture();
		fileHandlerService.deleteFileByUrl(urlToDelete);
        foodRepository.deleteById(id);
        return ("/admin/indexFood.html");
            
	}
		
	
///////////////// MAPPING PAGINE WEB /////////////////

	
	@GetMapping("/admin/indexFood")
	public String indexFood() {
		return "admin/indexFood.html";
	}
	
	@GetMapping("/food/{id}")
	public String getFood(@PathVariable("id") Long id, Model model) {
		
		Food food = this.foodRepository.findById(id).get();
		
		
		model.addAttribute("food",food );	
		return "food.html";
	}

	@GetMapping("/food")
	public String getFoods(Model model) {
		model.addAttribute("foods", this.foodRepository.findAll());
		return "foods.html";
	}
}
