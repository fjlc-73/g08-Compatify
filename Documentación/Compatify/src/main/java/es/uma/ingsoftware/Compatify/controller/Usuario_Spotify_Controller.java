package es.uma.ingsoftware.Compatify.controller;

import java.io.IOException;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.uma.ingsoftware.Compatify.model.*;
import es.uma.ingsoftware.Compatify.service.*;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class Usuario_Spotify_Controller {
	private final String CLIENT_ID = "1cc301f313604c8f9cbcbf59be7f4561";
	private final String REDIRECT_URI = "http://localhost:8080/callback";
	private final String SPOTIFY_AUTH_URL = "https://accounts.spotify.com/authorize";
	private final String CLIENT_SECRET = "3648ff1f0e894e74bc567770ce0618f9";
	
	@Autowired
	Usuario_Spotify_Service usuarioSpotifyService;

	@Autowired
	Usuario_Compatify_Service usuarioCompatifyService;

	@Autowired
	Artista_Service artistaService;

	@Autowired
	Cancion_Service cancionService;
	
	private final RestTemplate restTemplate;//Para procesar solicitudes HTTP
	
	@Autowired
	public Usuario_Spotify_Controller(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	
	@RequestMapping("/login")
	public void login(RedirectAttributes ra, HttpServletRequest request, HttpServletResponse response) throws IOException {
		String state = generateRandomString(16);
	    String scope = "user-top-read user-read-private user-read-email";//Permisos que se les pide al usuario, habría que ver cuales agregar
	    
	    String url = SPOTIFY_AUTH_URL + "?" + 
	                 "response_type=code" +
	                 "&client_id=" + CLIENT_ID +
	                 "&scope=" + scope +
	                 "&redirect_uri=" + REDIRECT_URI +
	                 "&state=" + state;
	    response.sendRedirect(url);//Redirigimos al usuario a Spotify
	}
	
	/**
	 * @param code
	 * @param state
	 * @param response
	 * @throws IOException
	 */
	@GetMapping("/callback")//Tras iniciar sesión en Spotify, se nos redirige a esta página, en la cuál se ejecuta este método
	public void GetTokens(@RequestParam(value = "code", required = false) String code,
	                     @RequestParam(value = "state", required = false) String state, RedirectAttributes ra,
						 HttpServletRequest request, HttpServletResponse response) throws IOException {
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

	        HttpEntity<MultiValueMap<String, String>> request_token = new HttpEntity<>(body, headers);

			//POST REQUEST
	        ResponseEntity<String> responseEntity =
	                restTemplate.postForEntity("https://accounts.spotify.com/api/token", request_token, String.class);
	        
	        //Se convierte la respuesta en un Json y se extraen sus parámetros
	        String json = responseEntity.getBody();
	        ObjectMapper objectMapper = new ObjectMapper();
	        JsonNode jsonNode = objectMapper.readTree(json);
	        
			String access_token = jsonNode.get("access_token").asText();
			String refresh_token = jsonNode.get("refresh_token").asText();
			
			//Otra llamada para conseguir datos del usuario spotify 
			HttpHeaders headersS = new HttpHeaders();

			headersS.setBearerAuth(access_token);//se le pasa el token
			headersS.set("Accept", MediaType.APPLICATION_JSON_VALUE);//se espera que devuelva un json

			HttpEntity<String> entityS = new HttpEntity<>(headersS);

			String urlS = "https://api.spotify.com/v1/me";
			String responseS = restTemplate.exchange(urlS, HttpMethod.GET, entityS, String.class).getBody();

			//Recogemos la información de los artistas y vamos creando objetos de los mismos:
			ObjectMapper objectMapperS = new ObjectMapper();
			JsonNode jsonNodeS = objectMapperS.readTree(responseS);	

			String email = jsonNodeS.path("email").asText();
			String usuario_s = jsonNodeS.path("display_name").asText();
			String foto_s;
			if(jsonNodeS.path("images").get(0) == null){
				foto_s = "https://www.nicepng.com/png/detail/202-2022264_usuario-annimo-usuario-annimo-user-icon-png-transparent.png";
			}else{
				foto_s = jsonNodeS.path("images").get(0).path("url").asText();
			}
			
			//creamos el usuario spotify con esas caracteristicas:
			Usuario_Spotify user_s = new Usuario_Spotify(usuario_s, access_token, refresh_token, email, foto_s);

			//conseguimos el usuario Compatify que ha iniciado sesion
			HttpSession session = request.getSession();
	    	String userName = (String) session.getAttribute("userName");
			Usuario_Compatify uc = usuarioCompatifyService.getById(userName);
			
			//lo asociamos a la cuenta:
			usuarioSpotifyService.save(user_s);
			uc.setUsuarioSpotify(user_s);

			usuarioCompatifyService.save(uc);

			response.sendRedirect("/asociar");
		}
	}


	@RequestMapping("/asociar")
	public String GetInfo(@RequestParam(value = "code", required = false) String code,
						@RequestParam(value = "state", required = false) String state, RedirectAttributes ra,
						HttpServletRequest request, HttpServletResponse response) throws IOException {

			//primero conseguimos el usuario que esta pidiendo la información:
			HttpSession session = request.getSession();
	    	String userName = (String) session.getAttribute("userName");
			Usuario_Compatify uc = usuarioCompatifyService.getById(userName);
			Usuario_Spotify user_s = uc.getUsuarioSpotify();

			//vemos si este usuario Compatify tiene asociado un usuario compatify:
			if(user_s == null){
				ra.addFlashAttribute("errorspotify", "¡Vaya! No tienes cuenta Spotify asociada, prueba a Asociar tu cuenta");
				return "redirect:/actualizar-cuenta";
			}


			user_s.setFavArtistas(null);
			user_s.setFavCancion(null);


			String access_token = user_s.getToken();

			// Crear un objeto HttpEntity con los encabezados
			HttpHeaders headersArt = new HttpHeaders();

			headersArt.setBearerAuth(access_token);//se le pasa el token
			headersArt.set("Accept", MediaType.APPLICATION_JSON_VALUE);//se espera que devuelva un json

			HttpEntity<String> entityArt = new HttpEntity<>(headersArt);

			// Realizar la petición GET a la API de Spotify
			try{
				String urlArt = "https://api.spotify.com/v1/me/top/artists?time_range=long_term&limit=50";
				String responseArt = restTemplate.exchange(urlArt, HttpMethod.GET,  entityArt, String.class).getBody();

				//Recogemos la información de los artistas y vamos creando objetos de los mismos:
				ObjectMapper objectMapperArt = new ObjectMapper();
				JsonNode jsonNodeArt = objectMapperArt.readTree(responseArt);

				List<Artista> artistas = new ArrayList<Artista>();
			

				JsonNode itemsArt = jsonNodeArt.path("items");

				for(JsonNode itemArt: itemsArt){//para cada item
					String artistId = itemArt.path("id").asText();//id
					String nombre = itemArt.path("name").asText();//nombre
					String foto;
					if(itemArt.path("images").get(0) == null){
						foto = "https://www.nicepng.com/png/detail/202-2022264_usuario-annimo-usuario-annimo-user-icon-png-transparent.png";
					}else{
						foto = itemArt.path("images").get(0).path("url").asText();
					}

					JsonNode genresNode = itemArt.get("genres");
					String gen = "";
					for(JsonNode genreNode: genresNode){
						gen += genreNode.asText() + ";";//generos
					}

					Artista artist = new Artista(artistId, nombre, gen);//este es el artista con toda la informacion
					artist.setFoto(foto);

					artistaService.save(artist);

					artistas.add(artist);
				}
				Date fechaActual = new Date();
				user_s.setUltAct(fechaActual);

				user_s.setFavArtistas(artistas);
				
			}catch(HttpClientErrorException ex){
				if(ex.getStatusCode() == HttpStatus.UNAUTHORIZED){//Cuando el token no sirve
					refrescar(code, state, ra, request, response, user_s);
				}else{
					//redirigir o dar un mensaje de error
					RequestDispatcher dd=request.getRequestDispatcher("/perfil");
				}
				
			}
	
			//haremos lo mismo con los tracks:

			// Crear un objeto HttpEntity con los encabezados
			HttpHeaders headersTra = new HttpHeaders();

			headersTra.setBearerAuth(access_token);//se le pasa el token
			headersTra.set("Accept", MediaType.APPLICATION_JSON_VALUE);//se espera que devuelva un json

			HttpEntity<String> entityTra = new HttpEntity<>(headersTra);

			try{
				// Realizar la petición GET a la API de Spotify
				String urlTra = "https://api.spotify.com/v1/me/top/tracks?time_range=long_term&limit=50";
				String responseTra = restTemplate.exchange(urlTra, HttpMethod.GET,  entityTra, String.class).getBody();

				//Recogemos la información de las canciones y vamos creando objetos de las mismas:
				ObjectMapper objectMapperTra = new ObjectMapper();
				JsonNode jsonNodeTra = objectMapperTra.readTree(responseTra);

				List<Cancion> canciones = new ArrayList<Cancion>();

				JsonNode itemsTra = jsonNodeTra.path("items");

				for(JsonNode itemTra: itemsTra){
					String Spotify_id = itemTra.path("id").asText();
					String nombre = itemTra.path("name").asText();

					Cancion can = new Cancion(Spotify_id, nombre);

					cancionService.save(can);

					canciones.add(can);
				}

				user_s.setFavCancion(canciones);

				usuarioSpotifyService.save(user_s);
				uc.setUsuarioSpotify(user_s);

				usuarioCompatifyService.save(uc);

			}catch(HttpClientErrorException ex){
				if(ex.getStatusCode() == HttpStatus.UNAUTHORIZED){//Cuando el token no sirve
					refrescar(code, state, ra, request, response, user_s);
				}else{
					//redirigir o dar un mensaje de error
					RequestDispatcher dd=request.getRequestDispatcher("/perfil");
				}
			}
			


			return "redirect:/actualizar-cuenta";
	    }

		public void refrescar(@RequestParam(value = "code", required = false) String code,
							@RequestParam(value = "state", required = false) String state, RedirectAttributes ra,
							HttpServletRequest request, HttpServletResponse response, Usuario_Spotify user_s) throws IOException{
			//refresh token:
			String refresh_token = user_s.getRefresh_token();

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			headers.setBasicAuth(CLIENT_ID, CLIENT_SECRET);

			MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
			body.add("grant_type", "refresh_token");
			body.add("refresh_token", refresh_token);

			HttpEntity<MultiValueMap<String, String>> requestRefToken = new HttpEntity<>(body, headers);

			try{
				ResponseEntity<String> responseEntity = restTemplate.postForEntity("https://accounts.spotify.com/api/token", requestRefToken, String.class);
				// Obtener el access token de la respuesta JSON
				String json = responseEntity.getBody();
				ObjectMapper objectMapper = new ObjectMapper();
				JsonNode jsonNode = objectMapper.readTree(json);

				String access_token = jsonNode.get("access_token").asText();

				user_s.setToken(access_token);

				GetInfo(code, state, ra, request, response);

			}catch(HttpClientErrorException ex){
				if(ex.getStatusCode() == HttpStatus.UNAUTHORIZED || ex.getStatusCode() == HttpStatus.BAD_REQUEST){
					login(ra, request, response);
				}else{
					//mandar a algun lado
				}
			}
		}
	
	private String generateRandomString(int length) {
		// Implementación de generación de cadenas aleatoria
		int i = 0 ;
		String cadena = "" ;
		while (i<16) {
			Random r = new Random();
			char c = (char) (r.nextInt(26) + 'a') ;
			cadena = cadena + c ;
			i++ ;
		}
		return cadena ;
	}

}
