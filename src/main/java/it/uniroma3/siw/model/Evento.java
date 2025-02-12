package it.uniroma3.siw.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Evento {
    
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;
    
        @NotBlank
        private String title;
        
    	@DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate date;
        
        @OneToMany(mappedBy = "evento", cascade = CascadeType.ALL)        
        private List<Prenotazione> prenotazioni = new ArrayList<>();
        
        private String urlImage;
        private String descrizione;
        
        @ManyToOne
        @JoinColumn(name = "sala_id", nullable = true)
        private Sala sala;
                 
       
		public List<Prenotazione> getPrenotazioni() {
			return prenotazioni;
		}

		public void setPrenotazioni(List<Prenotazione> prenotazioni) {
			this.prenotazioni = prenotazioni;
		}

		public Long getId() {
            return id;
        }
    
        public void setId(Long id) {
            this.id = id;
        }
    
        public String getTitle() {
            return title;
        }
        public void setTitle(String title) {
            this.title = title;
        }
    
        public LocalDate getDate() {
            return date;
        }
    
        public void setDate(LocalDate date) {
            this.date = date;
        }
        
        public String getUrlImage() {
            return urlImage;
        }
    
        public void setUrlImage(String urlImage) {
            this.urlImage = urlImage;
        }
    
        
        
        public String getDescrizione() {
			return descrizione;
		}

		public void setDescrizione(String descrizione) {
			this.descrizione = descrizione;
		}
      
		
		public Sala getSala() {
			return sala;
		}
		
		public void setSala(Sala sala) {
			this.sala = sala;
		}
        @Override
        public int hashCode() {
            return Objects.hash(title, date);
        }
    
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Evento other = (Evento) obj;
            return Objects.equals(title, other.title) && date.equals(other.date);
        }

		
    }
