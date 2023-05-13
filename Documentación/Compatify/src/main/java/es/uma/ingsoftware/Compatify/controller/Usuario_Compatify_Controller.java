package es.uma.ingsoftware.Compatify.controller;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PostMapping;

import es.uma.ingsoftware.Compatify.model.Artista;
import es.uma.ingsoftware.Compatify.model.Cancion;
import es.uma.ingsoftware.Compatify.model.Usuario_Compatify;
import es.uma.ingsoftware.Compatify.model.Usuario_Spotify;
import es.uma.ingsoftware.Compatify.service.Usuario_Compatify_Service;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class Usuario_Compatify_Controller {

	@Autowired
	Usuario_Compatify_Service usuarioCompatifyService;

	@RequestMapping("/crear-cuenta")
	public String addUsuarioCompatify(Model model) {
		model.addAttribute("usuariocompatify", new Usuario_Compatify());
		return "crear-cuenta";
	}

	@PostMapping("/save") // post ejecutado en crear-sesion
	public String saveUsuarioCompatify(RedirectAttributes ra, @RequestParam("month") int month,
			@RequestParam("day") String day, @RequestParam("year") String year, Usuario_Compatify uc) {
		int dayInt, yearInt;
		if (uc.getNombre().length() == 0) {
			ra.addFlashAttribute("error", "Tiene que introducir un nombre de usuario");
		} else if (usuarioCompatifyService.existsbyNombre(uc.getNombre())) {
			ra.addFlashAttribute("error", "Nombre de usuario ya usado por otra cuenta");
		} else if (uc.getEmail().length() == 0) {
			ra.addFlashAttribute("error", "Introduzca un correo");
		} else if (usuarioCompatifyService.existsByEmail(uc.getEmail())) {
			ra.addFlashAttribute("error", "Correo ya usado");
		} else if (uc.getContraseña().length() < 8) {
			ra.addFlashAttribute("error", "La contraseña debe tener al menos 8 carácteres");
		} else {
			try {
				dayInt = Integer.valueOf(day);
				yearInt = Integer.valueOf(year);
				if (!fechaValida(dayInt, month, yearInt)) {
					ra.addFlashAttribute("error", "Fecha no válida");
				} else {
					uc.setFechanacimiento(new Date(yearInt - 1900, month - 1, dayInt));// Esta clase Date que se usa en
																						// Usuario_Compatify parece que
																						// está en desuso
				}
			} catch (NumberFormatException nfe) {
				ra.addFlashAttribute("error", "Formato incorrecto en fecha de nacimiento");
			}
		}
		if (ra.getFlashAttributes().containsKey("error")) {
			ra.addFlashAttribute("usuariocompatify", new Usuario_Compatify());
			return "redirect:/crear-cuenta";
		} else {
			usuarioCompatifyService.save(uc);
			return "redirect:/inicio-de-sesion";
		}
	}

	private boolean fechaValida(int dayInt, int month, int yearInt) {
		LocalDate fecha;
		try {
			fecha = LocalDate.of(yearInt, month, dayInt);
		} catch (java.time.DateTimeException e) {
			return false;
		}
		if(fecha.compareTo(LocalDate.now()) > 0 || yearInt < 1900) {
			return false; //Fecha futura o anterior a 1900
		}
		return true;
	}

	@RequestMapping("/perfil")
	public String pruebaperfil(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		String userName = (String) session.getAttribute("userName");
		if (userName == null) {
			return "redirect:/inicio-de-sesion";
		}
		model.addAttribute("usuarioperfil", usuarioCompatifyService.getById(userName));
		return "perfil";
	}

	@RequestMapping("/editar-perfil")
	public String viewEditarPerfil(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		String userName = (String) session.getAttribute("userName");
		model.addAttribute("usuarioperfil", usuarioCompatifyService.getById(userName));
		return "editar-perfil";
	}

	@PostMapping("/edit") // post ejecutado en editar-perfil
	public String editUsuarioCompatify(RedirectAttributes ra, HttpServletRequest request,
			@RequestParam("nombre") String nombre, @RequestParam("email") String email,
			@RequestParam("genero") String genero, @RequestParam("month") int month, @RequestParam("day") String day,
			@RequestParam("year") String year) {
		int dayInt, yearInt;
		HttpSession session = request.getSession();
		String userName = (String) session.getAttribute("userName");
		Usuario_Compatify old = usuarioCompatifyService.getById(userName);
		Usuario_Compatify nuevo = new Usuario_Compatify();

		nuevo.setNombre(nombre);
		nuevo.setEmail(email);
		nuevo.setGenero(genero);
		nuevo.setContraseña(old.getContraseña());
		nuevo.setUsuarioSpotify(old.getUsuarioSpotify());

		if (nuevo.getNombre().length() == 0) {
			ra.addFlashAttribute("error", "Tiene que introducir un nombre de usuario");
		} else if (usuarioCompatifyService.existsbyNombre(nuevo.getNombre())
				&& !(old.getNombre().equals(nuevo.getNombre()))) {
			ra.addFlashAttribute("error", "Nombre de usuario ya usado por otra cuenta");
		} else if (nuevo.getEmail().length() == 0) {
			ra.addFlashAttribute("error", "Introduzca un correo");
		} else if (usuarioCompatifyService.existsByEmail(nuevo.getEmail())
				&& !(old.getEmail().equals(nuevo.getEmail()))) {
			ra.addFlashAttribute("error", "Correo ya usado");
		} else {
			try {
				dayInt = Integer.valueOf(day);
				yearInt = Integer.valueOf(year);
				if (!fechaValida(dayInt, month, yearInt)) {
					ra.addFlashAttribute("error", "Fecha no válida");
				} else {
					nuevo.setFechanacimiento(new Date(yearInt - 1900, month - 1, dayInt));// Esta clase Date que se usa
																							// en Usuario_Compatify
																							// parece que está en desuso
				}
			} catch (NumberFormatException nfe) {
				ra.addFlashAttribute("error", "Formato incorrecto en fecha de nacimiento");
			}

		}

		if (ra.getFlashAttributes().containsKey("error")) {
			ra.addFlashAttribute("usuarioperfil", old);
			return "redirect:/editar-perfil";
		} else {
			usuarioCompatifyService.deleteByNombre(userName);
			usuarioCompatifyService.save(nuevo);
			session.setAttribute("userName", nombre);
			return "redirect:/perfil";
		}

	}

	@RequestMapping("/terminos-y-condiciones")
	public String terminos() {
		return "terminos-y-condiciones";
	}

	@RequestMapping("/inicio-de-sesion")
	public String iniciosesion(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		String userName = (String) session.getAttribute("userName");
		if (userName != null) {
			return "redirect:/perfil";
		}
		return "inicio-de-sesion";
	}

	@PostMapping("/checkpassword") // post ejecutado en iniciar-sesion
	public String checkUsuarioCompatify(RedirectAttributes ra, HttpServletRequest request,
			@ModelAttribute(name = "loginForm") Usuario_Compatify uc) {
		String inputUsername = uc.getNombre();
		String inputPassword = uc.getContraseña();
		String realPassword = "???";

		Usuario_Compatify realUser = usuarioCompatifyService.getById(inputUsername);

		try {
			realPassword = realUser.getContraseña();
		} catch (EntityNotFoundException e) {
			realUser = null;
		}
		if (realUser != null && inputPassword.equals(realPassword)) {
			HttpSession session = request.getSession();
			session.setAttribute("userName", inputUsername);// El usuario que ha iniciado sesión se guarda para poder
															// recuperarlo en otros métodos
			return "redirect:/perfil";
		}
		ra.addFlashAttribute("error", "Usuario o contraseña incorrectos");
		return "redirect:/inicio-de-sesion";
	}

	@RequestMapping("/cambio-de-contrasea")
	public String cambiarContraseña(HttpServletRequest request, Model m) {
		HttpSession session = request.getSession();
		String userName = (String) session.getAttribute("userName");
		Usuario_Compatify usuario_actual = usuarioCompatifyService.getById(userName);
		m.addAttribute("usuario",usuario_actual);
		return "cambio-de-contrasea";
	}

	@RequestMapping("/cambiarcontraseña")
	public String cambiarContraseña(RedirectAttributes ra, HttpServletRequest request,
			@RequestParam("actual") String actual, @RequestParam("nueva1") String nueva1,
			@RequestParam("nueva2") String nueva2) {
		HttpSession session = request.getSession();
		String userName = (String) session.getAttribute("userName");
		Usuario_Compatify uc = usuarioCompatifyService.getById(userName);
		if (!actual.equals(uc.getContraseña())) {
			ra.addFlashAttribute("error", "Contraseña incorrecta");
		} else if (!nueva1.equals(nueva2)) {
			ra.addFlashAttribute("error", "La confirmación no coincide con la nueva contraseña");
		} else if (nueva1.length() < 8) {
			ra.addFlashAttribute("error", "La contraseña debe tener al menos 8 carácteres");
		} else {
			uc.setContraseña(nueva1);
			usuarioCompatifyService.save(uc);
			return "redirect:/perfil";
		}
		return "redirect:/cambio-de-contrasea";
	}

	@RequestMapping("/actualizar-cuenta")
	public String actualizarcuenta(HttpServletRequest request, Model m) {
		HttpSession session = request.getSession();
		String userName = (String) session.getAttribute("userName");
		Usuario_Compatify usuario_actual = usuarioCompatifyService.getById(userName);
		m.addAttribute("usuario", usuario_actual);
		return "actualizar-cuenta";
	}

	@RequestMapping("/cerrar-sesion")
	public String viewCerrarSesion(HttpServletRequest request, Model m) {
		HttpSession session = request.getSession();
		String userName = (String) session.getAttribute("userName");
		Usuario_Compatify usuario_actual = usuarioCompatifyService.getById(userName);
		m.addAttribute("usuario",usuario_actual);
		return "cerrar-sesion";
	}

	@RequestMapping("/cerrar-sesion-process")
	public String cerrarSesion(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.removeAttribute("userName");
		return "redirect:/";
	}

	@RequestMapping("/amigos")
	public String showBuscador(HttpServletRequest request, Model m) {
		HttpSession session = request.getSession();
		String userName = (String) session.getAttribute("userName");
		Usuario_Compatify usuario_actual = usuarioCompatifyService.getById(userName);
		m.addAttribute("usuarioperfil",usuario_actual);
		return "amigos";
	}

	@RequestMapping("/buscarusuario")
	public String buscarUsuario(HttpServletRequest request, RedirectAttributes ra,
			@RequestParam("nombre") String nombre) {
		HttpSession session = request.getSession();
		String userName = (String) session.getAttribute("userName");
		Usuario_Compatify usuario_actual = usuarioCompatifyService.getById(userName);
		java.util.List<Usuario_Compatify> resultadoBusqueda = usuarioCompatifyService.buscarPorNombre(nombre);
		resultadoBusqueda.remove(usuario_actual);
		if(resultadoBusqueda.isEmpty()) {
			ra.addFlashAttribute("errorspotify","No se ha encontrado ningún resultado para su búsqueda.");
		}else {
			ra.addFlashAttribute("usuarios", resultadoBusqueda);
		}
		return "redirect:/amigos";
	}

	@RequestMapping("/comparar")
	public String comparar(Model m) {
		if (!m.containsAttribute("amigo")) {
			return "redirect:/amigos";
		}
		return "comparar";
	}

	@RequestMapping("/comparar-process")
	public String comparar(HttpServletRequest request, RedirectAttributes ra,
			@RequestParam(value = "seleccionado", required = false) String nombreusuario2) {
		HttpSession session = request.getSession();
		String nombreusuario1 = (String) session.getAttribute("userName");
		Usuario_Spotify usuario1 = usuarioCompatifyService.getById(nombreusuario1).getUsuarioSpotify();
		Usuario_Spotify usuario2 = usuarioCompatifyService.getById(nombreusuario2).getUsuarioSpotify();
		try {
			List<Artista> artusuario1 = usuario1.getFavArtistas();
			List<Artista> artusuario2 = usuario2.getFavArtistas();
			List<Cancion> canusuario1 = usuario1.getFavCancion();
			List<Cancion> canusuario2 = usuario2.getFavCancion();
			canusuario1.retainAll(canusuario2);
			artusuario1.retainAll(artusuario2);
			if (canusuario1.size() == 0) {
				ra.addFlashAttribute("errorcancion", "¡Vaya! No se ha encontrado ninguna canción en común");
			} else {
				ra.addFlashAttribute("canciones", canusuario1);
			}
			if (artusuario1.size() == 0) {
				ra.addFlashAttribute("errorartista", "¡Vaya! No se ha encontrado ningún artista en común");
			} else {
				ra.addFlashAttribute("artistas", artusuario1);

			}
			ra.addFlashAttribute("amigo", nombreusuario2);
			int compatiblidad = logisticfunc(((float) 7 * artusuario1.size() + 3 * canusuario1.size()) / (float) 100);
			ra.addFlashAttribute("compatibilidad", compatiblidad);
		} catch (NullPointerException e) {
			ra.addFlashAttribute("errorspotify", "¡Vaya! Parece que alguno de los 2 usuarios no tiene cuenta de Spotify asociada");
			return "redirect:/amigos";
		}
		return "redirect:/comparar";
	}

	public int logisticfunc(double x) { // función logística para calcular la compatibilidad
		return (int) Math.round(100 * ((1.1 / (1 + Math.exp(-8.4 * (x - 0.275)))) - 0.1));
	}
	
	@RequestMapping("/maquina")
	public String showMaquina() {
		return "maquina";
	}
}
