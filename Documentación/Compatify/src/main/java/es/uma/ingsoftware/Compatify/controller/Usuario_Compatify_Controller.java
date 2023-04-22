package es.uma.ingsoftware.Compatify.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import es.uma.ingsoftware.Compatify.model.Usuario_Compatify;
import es.uma.ingsoftware.Compatify.service.Usuario_Compatify_Service;

@Controller
public class Usuario_Compatify_Controller {
	
	@Autowired
	Usuario_Compatify_Service usuarioCompatifyService;
	
	@RequestMapping("/crear-sesion")
	public String addUsuarioCompatify (Model model) {
		model.addAttribute("usuariocompatify", new Usuario_Compatify());
		return "crear-sesion";
	}
	
	@PostMapping("/usuariocompatify/save") //post ejecutado en crear-sesion
	public String saveUsuarioCompatify(Usuario_Compatify uc) {
		usuarioCompatifyService.save(uc);
		return "redirect:/perfil/"+uc.getNombre();
	}
	
	
	@RequestMapping("/perfil/{nombre}")
	public String viewUsuarioCompatify (@PathVariable("nombre") String nombre, Model model) {
		model.addAttribute("usuarioperfil", usuarioCompatifyService.getById(nombre));
		return "perfil"; 
	}
	

	


}
