package es.uma.ingsoftware.Compatify.model;

import java.util.List;
import java.util.Objects;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Usuario_Spotify {

	@Id
	private String nombre;
	private String token;
	private String refresh_token;
	private String email;
	private Date ultAct;
	private String foto;

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public Date getUltAct() {
		return ultAct;
	}

	public void setUltAct(Date ultAct) {
		this.ultAct = ultAct;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRefresh_token() {
		return refresh_token;
	}

	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}

	@OneToOne(mappedBy = "usuarioSpotify")
	private Usuario_Compatify usuarioCompatify;

	@ManyToMany
	private List<Artista> favArtistas;
	@ManyToMany
	private List<Cancion> favCancion;

	public Usuario_Spotify(){
		
	}

	public Usuario_Spotify(String nombre){
		this.nombre = nombre;
	}

	public Usuario_Spotify(String nombre, String token, String refresh_token, String email, String foto) {
		this.nombre = nombre;
		this.token = token;
		this.refresh_token = refresh_token;
		this.favArtistas = null;
		this.favCancion = null;
		this.email = email;
		this.foto = foto;
	}

	public Usuario_Compatify getUsuarioCompatify() {
		return usuarioCompatify;
	}

	public void setUsuarioCompatify(Usuario_Compatify usuarioCompatify) {
		this.usuarioCompatify = usuarioCompatify;
	}

	public List<Artista> getFavArtistas() {
		return favArtistas;
	}

	public void setFavArtistas(List<Artista> favArtistas) {
		this.favArtistas = favArtistas;
	}

	public List<Cancion> getFavCancion() {
		return favCancion;
	}

	public void setFavCancion(List<Cancion> favCancion) {
		this.favCancion = favCancion;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public int hashCode() {
		return Objects.hash(nombre);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario_Spotify other = (Usuario_Spotify) obj;
		return Objects.equals(nombre, other.nombre);
	}

	@Override
	public String toString() {
		return "Usuario_Spotify [nombre=" + nombre + ", token=" + token + "]";
	}

}
