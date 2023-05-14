package es.uma.ingsoftware.Compatify.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uma.ingsoftware.Compatify.model.Usuario_Compatify;
import es.uma.ingsoftware.Compatify.repository.Usuario_Compatify_Repository;

@Service
public class Usuario_Compatify_Service {

	@Autowired
	Usuario_Compatify_Repository usuarioCompatifyRepository;

	public void save(Usuario_Compatify uc) {
		usuarioCompatifyRepository.saveAndFlush(uc);

	}

	public Usuario_Compatify getById(String nombre) {
		
			return usuarioCompatifyRepository.getReferenceById(nombre);
		
	}

	public java.util.List<Usuario_Compatify> buscarPorNombre(String nombre) {
		java.util.List<Usuario_Compatify> lista = usuarioCompatifyRepository.findByNombreContainingIgnoreCase(nombre);
		return lista;
	}
	
	public boolean existsbyNombre(String nombre) {
		return usuarioCompatifyRepository.existsById(nombre);
	}
	
	public void deleteByNombre(String nombre) {
		usuarioCompatifyRepository.deleteById(nombre);
	}
	
	public boolean existsByEmail(String email) {
		return usuarioCompatifyRepository.existsByEmail(email);
	}

}
