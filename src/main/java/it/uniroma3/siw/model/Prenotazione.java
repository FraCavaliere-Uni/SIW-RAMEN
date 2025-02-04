package it.uniroma3.siw.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Prenotazione{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
   	@ManyToOne
  	@JoinColumn(name = "user_id")
	private User user;

   	private Integer numeroPersone;

    @ManyToOne
    @JoinColumn(name = "evento_id")
    private Evento evento;
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}



	public void setUser(User user) {
		this.user = user;
	}


	public Integer getNumeroPersone() {
		return numeroPersone;
	}



	public void setNumeroPersone(Integer numeroPersone) {
		this.numeroPersone = numeroPersone;
	}

	public Evento getEvento() {
		return evento;
	}

	public void setEvento(Evento evento) {
		this.evento = evento;
	}
  	
  	
  	
}
	
  	