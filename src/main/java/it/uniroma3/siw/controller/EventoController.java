package it.uniroma3.siw.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.controller.validator.EventoValidator;
import it.uniroma3.siw.model.Evento;
import it.uniroma3.siw.model.Sala;
import it.uniroma3.siw.repository.EventoRepository;
import it.uniroma3.siw.repository.SalaRepository;
import it.uniroma3.siw.service.FileHandlerService;

@Controller
public class EventoController {
	@Autowired 
	private EventoRepository eventoRepository;
	
	@Autowired 
	private EventoValidator eventoValidator;
	
	@Autowired 
	private SalaRepository salaRepository;
	
	@Autowired 
	private FileHandlerService fileHandlerService;

	
///////////////// CONTROLLER PER LA GESTIONE DELLE AZIONI /////////////////
	
	
	@GetMapping("/admin/formNewEvento")
	public String formNewEvento(Model model) {
		model.addAttribute("evento", new Evento());
		model.addAttribute("action","create");
		return "admin/formNewEvento.html";
	}

	@GetMapping("/admin/manageEvento/{id}")
	public String manageEvento(@PathVariable("id") Long id, Model model) {
		Evento evento = eventoRepository.findById(id).orElse(null);
		if(evento != null) {
			model.addAttribute("evento", evento);
			model.addAttribute("action","update");
				
		}
		return "admin/formNewEvento.html";
	}

	
	
	
	
	
///////////////// INSERIMENTO, MODIFICA E CANCELLAZIONE /////////////////
	
	@PostMapping({"/admin/submitEvento", "/admin/submitEvento/{id}"})
	public String submitEvento(@PathVariable(value = "id", required = false) Long id, Evento evento, BindingResult bindingResult, Model model) {
			    
	    this.eventoValidator.validate(evento, bindingResult);
	    	    	    
	    if (bindingResult.hasErrors()) {		    
		    model.addAttribute("evento", evento);	    
	        return "admin/formNewEvento";
	    }
	
	    if (id != null) {
	 	        	    
	        Evento eventToUpdate = eventoRepository.findById(id).orElse(null);
	        if (eventToUpdate != null) {
	            eventToUpdate.setTitle(evento.getTitle());
	            eventToUpdate.setDate(evento.getDate());
	            eventToUpdate.setPrenotazioni(evento.getPrenotazioni());

	            eventToUpdate.setDescrizione(evento.getDescrizione());
	            
	            
	            if(evento.getSala().getId() != null) {
	            	Sala sala = salaRepository.findById(evento.getSala().getId()).orElse(null);
	            	eventToUpdate.setSala(sala);
	            	sala.getEvents().add(eventToUpdate);
	            }
	            
	            eventoRepository.save(eventToUpdate);
	           
	            return "redirect:/evento/" + evento.getId();
	        }
	    }
	    
	    if (id == null) {
	    
	    	evento.setSala(null);
	    	eventoRepository.save(evento);
	    	return "redirect:/evento/" + evento.getId();
    
	    }
		return "redirect:/";
	}
	
	
	
	@GetMapping("/admin/deleteEvento/{id}")
    public String deleteEvento(@PathVariable Long id) {
		
				 
		String urlToDelete = eventoRepository.findById(id).get().getUrlImage();
		fileHandlerService.deleteFileByUrl(urlToDelete);
		
        eventoRepository.deleteById(id);
        return ("/admin/indexEvento.html");
            
	}
	
///////////////// OPERAZIONI AGGIUNTA E RIMOZIONE EVENTO A SALA /////////////////

	@PostMapping("/admin/evento/{eventoId}/addSala")
	public String addEventoToSala(@PathVariable("eventoId") Long eventoId, Long salaId, Model model) {

		Sala sala = salaRepository.findById(salaId).get();
		Evento evento = eventoRepository.findById(eventoId).get();

		evento.setSala(sala);
		sala.getEvents().add(evento);
		
		eventoRepository.save(evento);

		model.addAttribute("sala", sala);
		model.addAttribute("evento", evento);

		return "redirect:/evento/" + eventoId;
	}

	@GetMapping("/admin/evento/{eventoId}/removeSala/{salaId}")
	public String removeEventoFromSala(@PathVariable("eventoId") Long eventoId, @PathVariable("salaId")Long salaId, Model model) {

		Sala sala = this.salaRepository.findById(salaId).get();
		Evento evento = this.eventoRepository.findById(eventoId).get();
		evento.setSala(null);
		sala.getEvents().remove(evento);

		eventoRepository.save(evento);
		salaRepository.save(sala);

		model.addAttribute("sala", sala);
		model.addAttribute("evento", evento);


		return  "redirect:/evento/" + eventoId;
	}


	
///////////////// RICERCA EVENTI  /////////////////
	
	@PostMapping("/searchEventi")
	public String searchEventi(Model model, LocalDate date)
			 {
		
	 if(date != null) {
		model.addAttribute("eventi", this.eventoRepository.findByDate(date));
	 }
	 
	 else { 
		model.addAttribute("eventi", this.eventoRepository.findAll());
	 
	 }
		
		return "foundEventi.html";
	}
	
	
///////////////// MAPPING PAGINE WEB /////////////////

	
	@GetMapping("/admin/indexEvento")
	public String indexEvento() {
		return "admin/indexEvento.html";
	}
		
	
	@GetMapping("/evento/{id}")
	public String getEventoPage(@PathVariable("id") Long id, Model model) {
		
		Evento evento = eventoRepository.findById(id).get();
		
		Iterable<Sala> sale = salaRepository.findAllOrderByName();

		
		model.addAttribute("evento",evento);
		model.addAttribute("sale", sale);

		return "evento.html";
	}

	@GetMapping("/evento")
	public String getEventi(Model model) {		
		model.addAttribute("eventi", this.eventoRepository.findAll());
		return "eventi.html";
	}
	
	@GetMapping("/formSearchEventi")
	public String formSearchEventi(Model model) {
		return "formSearchEventi.html";
	}

}
