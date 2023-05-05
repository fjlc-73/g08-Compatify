package es.uma.ingsoftware.Compatify;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;

import es.uma.ingsoftware.Compatify.controller.Usuario_Compatify_Controller;
import es.uma.ingsoftware.Compatify.model.Usuario_Compatify;
import es.uma.ingsoftware.Compatify.service.Usuario_Compatify_Service;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;


@SpringBootTest
class CompatifyApplicationTests {

	@Test
	void guardarCuentas() {
		/*Usuario_Compatify_Controller c1 = new Usuario_Compatify_Controller();
		
		
		Usuario_Compatify u1 = new Usuario_Compatify();
		u1.setNombre("Pepe");
		u1.setEmail("ar");
		u1.setContraseña("pericope");
		u1.setGenero("Mujer");

		BindingAwareModelMap b1 = new BindingAwareModelMap();
		
		assert(c1.saveUsuarioCompatify(5, "24", "2002", u1, b1).equalsIgnoreCase("redirect:/inicio-de-sesion"));
		*/
	}
	
	@Test
	void contraseñaIncorrecta() {
		
	}
	
	@Test
	void usuarioInexistente() {
		
	}
	
	@Test
	void sinSpotify() {
		
	}

	@Test
	void recibirNuevoToken() {
		
	}
	
	@Test
	void muestraGraficas() {
		
	}
	
	@Test
	void listaArtistas() {
		
	}
	
	@Test
	void listaCanciones() {
		
	}
	
	@Test
	void compruebaCompararArtistas() {
		
	}
	
	@Test
	void compruebaCompararCanciones() {
		
	}
	
	@Test
	void funcionaToken() {
		
	}
}
