package it.uniroma3.siw.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Evento;
import it.uniroma3.siw.repository.EventoRepository;

@Component
public class EventoValidator implements Validator {
	@Autowired
	private EventoRepository eventoRepository;

	@Override
	public void validate(Object o, Errors errors) {
		Evento evento = (Evento)o;
		
        // Aggiungo un nuovo evento
	    if (evento.getId() == null) { 
	        if (eventoRepository.existsByTitleAndDate(evento.getTitle(), evento.getDate())) {
	            errors.rejectValue("title", "duplicate", "Questo evento già esiste. ");
	        }
	    } else {
	        // Altrimenti sto modificando
	        Evento existingEvent = eventoRepository.findById(evento.getId()).orElse(null);
	        if 	(existingEvent != null && 
	            (!existingEvent.getTitle().equals(evento.getTitle()) || 
	             !existingEvent.getDate().equals(evento.getDate())) &&
	             eventoRepository.existsByTitleAndDate(evento.getTitle(), evento.getDate())
	            ) {
	
	            errors.rejectValue("title", "duplicate", "Questo evento già esiste.");
	        }
	    }
		
		
	}
	@Override
	public boolean supports(Class<?> aClass) {
		return Evento.class.equals(aClass);
	}
}