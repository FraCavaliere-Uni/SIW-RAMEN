package it.uniroma3.siw.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.OneToMany;

@Entity
public class Sala {

	
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;
    
        private String name;
        
        
        @OneToMany(mappedBy = "sala")
        private List<Evento> events = new ArrayList<>();
        
        public Long getId() {
            return id;
        }
    
        public void setId(Long id) {
            this.id = id;
        }
    
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
    
        public List<Evento> getEvents() {
            return events;
        }

        public void addEvent(Evento event) {
            this.events.add(event);
        }
                
        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Sala other = (Sala) obj;
            return Objects.equals(name, other.name);
        }

		
    }
