package es.uma.ingsoftware.Compatify.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uma.ingsoftware.Compatify.model.Artista;
import es.uma.ingsoftware.Compatify.repository.Artista_Repository;

@Service
public class Artista_Service {
    
    @Autowired
    Artista_Repository artistaRepository;

    public void save(Artista ar){
        artistaRepository.saveAndFlush(ar);
    }
}
