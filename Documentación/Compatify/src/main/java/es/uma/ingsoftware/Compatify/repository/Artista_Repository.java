package es.uma.ingsoftware.Compatify.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.uma.ingsoftware.Compatify.model.Artista;

public interface Artista_Repository extends JpaRepository<Artista, String> {
    
}