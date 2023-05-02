package es.uma.ingsoftware.Compatify.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.uma.ingsoftware.Compatify.model.Cancion;

public interface Cancion_Repository extends JpaRepository<Cancion, String> {
    
}