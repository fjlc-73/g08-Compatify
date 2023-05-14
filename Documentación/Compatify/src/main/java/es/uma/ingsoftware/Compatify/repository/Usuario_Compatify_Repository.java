package es.uma.ingsoftware.Compatify.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.uma.ingsoftware.Compatify.model.Usuario_Compatify;

public interface Usuario_Compatify_Repository extends JpaRepository<Usuario_Compatify, String>{
	java.util.List<Usuario_Compatify> findByNombreContainingIgnoreCase(String nombre);
	boolean existsByEmail(String email);
}


