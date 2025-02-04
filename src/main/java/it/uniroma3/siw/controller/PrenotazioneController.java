package it.uniroma3.siw.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Evento;
import it.uniroma3.siw.model.Prenotazione;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.CredentialsRepository;
import it.uniroma3.siw.repository.EventoRepository;
import it.uniroma3.siw.repository.PrenotazioneRepository;
import it.uniroma3.siw.repository.UserRepository;

@Controller
public class PrenotazioneController {
	
	@Autowired 
	private PrenotazioneRepository prenotazioneRepository;
		
	@Autowired 
	private UserRepository userRepository;

	@Autowired 
	private CredentialsRepository credentialsRepository;
	
	@Autowired
	private EventoRepository eventoRepository;

		
///////////////// OPERAZIONI AGGIUNTA E RIMOZIONE PRENOTAZIONI /////////////////

	@PostMapping("user/prenotati/{eventoId}")
	public String Prenotati(@AuthenticationPrincipal UserDetails userDetails, Integer numeroPersone, @PathVariable Long eventoId, Model model) {
	    
	
		Credentials userCred = credentialsRepository.findByUsername(userDetails.getUsername()).orElse(null);
		User user = userRepository.findById(userCred.getId()).orElse(null);
		Evento evento = eventoRepository.findById(eventoId).orElse(null);
		
			if(prenotazioneRepository.existsByUserIdAndEventoId(user.getId(),eventoId)) {
				model.addAttribute("evento", evento);
				model.addAttribute("error", "Hai già una prenotazione attiva per questo evento");
				return "evento";
			}
		
		Prenotazione prenotazione = new Prenotazione();
		prenotazione.setUser(user);
		prenotazione.setNumeroPersone(numeroPersone);
		
		
		prenotazione.setEvento(evento);
		
		prenotazioneRepository.save(prenotazione);
 
		return "redirect:/user/prenotazioni";
	}
		
	
	@GetMapping("/user/prenotazioni/cancella/{prenotazioneId}")
	public String rimuoviPrenotazione(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long prenotazioneId, Model model) {
		
		
		Credentials userCred = credentialsRepository.findByUsername(userDetails.getUsername()).orElse(null);
		User user = userRepository.findById(userCred.getId()).orElse(null);
		
		Prenotazione prenotazioneDaRimuovere = prenotazioneRepository.findById(prenotazioneId).orElse(null);
			
		if(prenotazioneDaRimuovere.getUser().equals(user)) {
				prenotazioneRepository.deleteById(prenotazioneId);
				userRepository.save(user);
		}
		
 
		return "redirect:/user/prenotazioni";
		
		
	}
	
	@GetMapping("/admin/prenotazioni/cancella/{prenotazioneId}")
	public String rimuoviPrenotazioneAdmin(@PathVariable Long prenotazioneId, Model model) {
		
		prenotazioneRepository.deleteById(prenotazioneId);

		return "redirect:/admin/prenotazioni";
	}
	
///////////////// MAPPING PAGINE WEB /////////////////

	
	
	@GetMapping("/user/prenotazioni")
	public String prenotazioni(@AuthenticationPrincipal UserDetails userDetails, Model model) {
		
		Credentials userCred = credentialsRepository.findByUsername(userDetails.getUsername()).orElse(null);
		User user = userRepository.findById(userCred.getId()).orElse(null);
		List<Prenotazione> prenotazioni = prenotazioneRepository.findAllByUserId(user.getId());
		
		model.addAttribute("prenotazioni", prenotazioni);
		return "prenotazioni.html";
	}
	
	@GetMapping("/admin/prenotazioni")
	public String prenotazioniAdmin (Model model) {
		
		
		Iterable<Prenotazione> prenotazioni = prenotazioneRepository.findAll();
		
		model.addAttribute("prenotazioni", prenotazioni);
		return "prenotazioni.html";
	}
		
	
}
