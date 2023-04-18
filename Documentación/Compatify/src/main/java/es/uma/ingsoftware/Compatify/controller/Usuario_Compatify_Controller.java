package es.uma.ingsoftware.Compatify.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import es.uma.ingsoftware.Compatify.service.Usuario_Compatify_Service;

@Controller
public class Usuario_Compatify_Controller {
	
	@Autowired
	Usuario_Compatify_Service usuarioCompatifyService;

}
