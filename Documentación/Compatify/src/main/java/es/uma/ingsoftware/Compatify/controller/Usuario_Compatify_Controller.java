package es.uma.ingsoftware.Compatify.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import es.uma.ingsoftware.Compatify.service.Usuario_Compatify_Service;

@Controller
public class Usuario_Compatify_Controller {
	
	@Autowired
	Usuario_Compatify_Service usuarioCompatifyService;
	
	@RequestMapping("/crear-sesion")
	public String addUsuarioCompatify (Model model) {
		return "crear-sesion";
	}
	
	@RequestMapping("/crear-sesion.css")
	public String cssCrearSesion (Model model) {
		return "crear-sesion.css";
	}
	
	@RequestMapping("/style.css")
	public String cssStyle (Model model){
		return "style.css";
	}
	

}
