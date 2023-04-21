package es.uma.ingsoftware.Compatify.controller;

import java.io.IOException;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import es.uma.ingsoftware.Compatify.model.Usuario_Compatify;
import es.uma.ingsoftware.Compatify.service.Usuario_Compatify_Service;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class Usuario_Compatify_Controller {
	private final String CLIENT_ID = "1cc301f313604c8f9cbcbf59be7f4561";
	private final String REDIRECT_URI = "http://localhost:8080/callback";
	private final String SPOTIFY_AUTH_URL = "https://accounts.spotify.com/authorize";
	private final String CLIENT_SECRET = "3648ff1f0e894e74bc567770ce0618f9";
	
	@Autowired
	Usuario_Compatify_Service usuarioCompatifyService;
	
	private final RestTemplate restTemplate;//Para procesar solicitudes HTTP
	
	@Autowired
	public Usuario_Compatify_Controller(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	@RequestMapping("/crear-sesion")
	public String addUsuarioCompatify (Model model) {
		model.addAttribute("usuariocompatify", new Usuario_Compatify());
		return "crear-sesion";
	}
	
	@PostMapping("/usuariocompatify/save")
	public String saveUsuarioCompatify(Usuario_Compatify uc) {
		usuarioCompatifyService.save(uc);
		return "redirect:/perfil";
	}
	
	
	@RequestMapping("/perfil")
	public String pruebaperfil (Model model) {
		return "perfil"; //esto no debería estar aquí 
	}

	@RequestMapping("/login.html")//Se ejecuta al acceder a la página login.html
	public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
		//String state = generateRandomString(16);
		String state = "skkdiekdaosofpwo";//Esta cadena debería ser aleatoria, implementar generateRandomString
	    String scope = "user-read-private user-read-email";//Permisos que se les pide al usuario, habría que ver cuales agregar
	    
	    String url = SPOTIFY_AUTH_URL + "?" + 
	                 "response_type=code" +
	                 "&client_id=" + CLIENT_ID +
	                 "&scope=" + scope +
	                 "&redirect_uri=" + REDIRECT_URI +
	                 "&state=" + state;
	    response.sendRedirect(url);//Redirigimos al usuario a Spotify
	}
	

	@GetMapping("/callback")//Tras iniciar sesión en Spotify, se nos redirige a esta página, en la cuál se ejecuta este método
	public void getToken(@RequestParam(value = "code", required = false) String code,
	                     @RequestParam(value = "state", required = false) String state,
	                     HttpServletResponse response) throws IOException {
		//Recibimos como parámetros el código para solicitar el token y la cadena de estado enviada anteriormente. 
		//Se debería comprobar que la cadena de estado devuelta coincide con la inicialmente mandada
	    if (state == null) {
	        response.sendRedirect("/#error=state_mismatch");//Redirección a la página principal en caso de error
	    } else {
	    	//Se crea una solicitud HTTP siguiendo el formato que especifica Spotify y se envía con restTemplate
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
	        headers.setBasicAuth(CLIENT_ID, CLIENT_SECRET);
	        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
	        body.add("code", code);
	        body.add("redirect_uri", REDIRECT_URI);
	        body.add("grant_type", "authorization_code");

	        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

	        ResponseEntity<String> responseEntity =
	                restTemplate.postForEntity("https://accounts.spotify.com/api/token", request, String.class);
	        
	        //Se convierte la respuesta en un Json y se extraen sus parámetros
	        String json = responseEntity.getBody();
	        ObjectMapper objectMapper = new ObjectMapper();
	        JsonNode jsonNode = objectMapper.readTree(json);
	        
	        System.out.println(jsonNode.get("access_token").asText());
	        System.out.println(jsonNode.get("token_type").asText());
	        System.out.println(jsonNode.get("scope").asText());
	        System.out.println(jsonNode.get("expires_in").asText());
	        System.out.println(jsonNode.get("refresh_token").asText());

	        response.sendRedirect("/index.html");//Se redirige a la página principal
	    }
	}
	
	private String generateRandomString(int length) {
		// Implementación de generación de cadenas aleatorias
		return null;
	}
}
