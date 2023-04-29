package es.uma.ingsoftware.Compatify.service;

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

}
