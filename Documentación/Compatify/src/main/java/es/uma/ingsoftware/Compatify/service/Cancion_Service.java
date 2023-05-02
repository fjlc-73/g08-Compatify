package es.uma.ingsoftware.Compatify.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uma.ingsoftware.Compatify.model.Cancion;
import es.uma.ingsoftware.Compatify.repository.Cancion_Repository;

@Service
public class Cancion_Service {

    @Autowired
    Cancion_Repository cancionRepository;

    public void save(Cancion ca){
        cancionRepository.saveAndFlush(ca);
    }
}
