package es.uma.ingsoftware.Compatify.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uma.ingsoftware.Compatify.model.Usuario_Spotify;
import es.uma.ingsoftware.Compatify.repository.Usuario_Spotify_Repository;

@Service
public class Usuario_Spotify_Service {
	
	@Autowired
	Usuario_Spotify_Repository usuarioSpotifyRepository;

	/**
	 * @param us
	 */
	public void save(Usuario_Spotify us){
		usuarioSpotifyRepository.saveAndFlush(us);
	}

}
