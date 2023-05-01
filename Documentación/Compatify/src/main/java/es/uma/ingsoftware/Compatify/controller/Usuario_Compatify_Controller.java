package es.uma.ingsoftware.Compatify.controller;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PostMapping;

import es.uma.ingsoftware.Compatify.model.Usuario_Compatify;
import es.uma.ingsoftware.Compatify.service.Usuario_Compatify_Service;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class Usuario_Compatify_Controller {

	
	@Autowired
	Usuario_Compatify_Service usuarioCompatifyService;
	
	
	@RequestMapping("/crear-cuenta")
	public String addUsuarioCompatify (Model model) {
		model.addAttribute("usuariocompatify", new Usuario_Compatify());
		return "crear-cuenta";
	}
	
	@PostMapping("/save") //post ejecutado en crear-sesion
	public String saveUsuarioCompatify(@RequestParam("month") int month, @RequestParam("day") String day, @RequestParam("year") String year, 
			Usuario_Compatify uc, Model m) {
		String prueba = null;
		int dayInt, dayYear;
		try {
			dayInt=Integer.valueOf(day);
			dayYear=Integer.valueOf(year);
			uc.setFechanacimiento(new Date(dayYear-1900,month-1,dayInt));//Esta clase Date que se usa en Usuario_Compatify parece que está en desuso
		}catch(NumberFormatException nfe) {
		}
		Usuario_Compatify existe = usuarioCompatifyService.getById(uc.getNombre());
		try {
			prueba = existe.getContraseña();
		} catch(EntityNotFoundException e) {
			prueba=null;
		}
		if(prueba==null) {
			usuarioCompatifyService.save(uc);
			return "redirect:/inicio-de-sesion";
		}
		m.addAttribute("error", "Nombre de usuario ya usado por otra cuenta");
		m.addAttribute("usuariocompatify", new Usuario_Compatify());
		return "crear-cuenta";
		
	}


	@RequestMapping("/perfil")
	public String pruebaperfil (HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
	    String userName = (String) session.getAttribute("userName");
	    if(userName == null) {
	    	return "redirect:/inicio-de-sesion";
	    }
		model.addAttribute("usuarioperfil", usuarioCompatifyService.getById(userName));
		return "perfil";

	}


	
	@RequestMapping("/terminos-y-condiciones")
	public String terminos(){
		return "terminos-y-condiciones";
	}
	
	@RequestMapping("/inicio-de-sesion")
	public String iniciosesion (HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		String userName = (String) session.getAttribute("userName");
		if(userName != null) {
			return "redirect:/perfil";
		}
		return "inicio-de-sesion";
	}
	
	
	@PostMapping("/checkpassword") //post ejecutado en iniciar-sesion
	public String checkUsuarioCompatify(HttpServletRequest request, @ModelAttribute(name="loginForm") Usuario_Compatify uc, Model m) {
		String inputUsername = uc.getNombre();
		String inputPassword = uc.getContraseña();
		String realPassword = "???";

		Usuario_Compatify realUser = usuarioCompatifyService.getById(inputUsername);

		try {
			realPassword = realUser.getContraseña();
		} catch(EntityNotFoundException e) {
			realUser=null;
		}
		if(realUser!=null && inputPassword.equals(realPassword)) {
			HttpSession session = request.getSession();
			session.setAttribute("userName", inputUsername);//El usuario que ha iniciado sesión se guarda para poder recuperarlo en otros métodos
			return "redirect:/perfil";
		}
		m.addAttribute("error", "Usuario o contraseña incorrectos");
		return "inicio-de-sesion";
	}
	
	@RequestMapping("/cambio-de-contrasea")
	public String cambiarContraseña(Model model) {
		return "cambio-de-contrasea";
	}

	 @RequestMapping("/cambiarcontraseña")
	public String cambiarContraseña(HttpServletRequest request, Model m, @RequestParam("actual") String actual, @RequestParam("nueva1") String nueva1, @RequestParam("nueva2") String nueva2) {
		HttpSession session = request.getSession();
		String userName = (String) session.getAttribute("userName");
		Usuario_Compatify uc = usuarioCompatifyService.getById(userName);
		if(actual.equals(uc.getContraseña()) && nueva1.equals(nueva2)) {
			uc.setContraseña(nueva1);
			uc.markNotNew();
			usuarioCompatifyService.save(uc);
			return "redirect:/perfil";
		}else if(!actual.equals(uc.getContraseña())) {
			m.addAttribute("error", "Contraseña incorrecta");
		}else {
			m.addAttribute("error", "La confirmación no coincide con la nueva contraseña");
		}
		return "cambio-de-contrasea";
	}
	 
	 @RequestMapping("/cerrar-sesion")
	 public String viewCerrarSesion() {
		 return "cerrar-sesion";
	 }
	 
	 @RequestMapping("/cerrar-sesion-process")
	 public String cerrarSesion(HttpServletRequest request) {
		 HttpSession session = request.getSession();
		 session.removeAttribute("userName");
		 return "redirect:/";
	 }
	 
	 @RequestMapping("/amigos")
	 public String showBuscador() {
		 return "amigos";
	 }
	 
	 @RequestMapping("/buscarusuario")
	 public String buscarUsuario(HttpServletRequest request, RedirectAttributes ra, @RequestParam("nombre") String nombre) {
		 HttpSession session = request.getSession();
		 String userName = (String) session.getAttribute("userName");
		 Usuario_Compatify usuario_actual = usuarioCompatifyService.getById(userName);
		 java.util.List<Usuario_Compatify> resultadoBusqueda = usuarioCompatifyService.buscarPorNombre(nombre);
		 resultadoBusqueda.remove(usuario_actual);
		 ra.addFlashAttribute("usuarios",resultadoBusqueda);
		 return "redirect:/amigos";
	 }
	 
	 @RequestMapping("/comparar")//Este método hay que implementarlo, ahora mismo solo redirige al perfil para que se pueda probar
	 public String comparar(@RequestParam(value="seleccionado", required=false) String nombre) {
		 return "redirect:/perfil";
	 }
	
}
