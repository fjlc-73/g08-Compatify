package es.uma.ingsoftware.Compatify.model;

import java.util.List;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

@Entity
public class Artista {

	@Id
	private String spotify_id;
	private String nombre;
	private String generos; // genero1;genero2;genero3;etc
	private String foto; 

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	@ManyToMany(mappedBy = "favArtistas")
	private List<Usuario_Spotify> seguidores;

	public Artista(){
		
	}

	public Artista(String spotify_id, String nombre, String generos) {
		this.spotify_id = spotify_id;
		this.generos = generos;
		this.nombre = nombre;
	}

	public List<Usuario_Spotify> getSeguidores() {
		return seguidores;
	}

	public void setSeguidores(List<Usuario_Spotify> seguidores) {
		this.seguidores = seguidores;
	}

	public String getSpotify_id() {
		return spotify_id;
	}

	public void setSpotify_id(String spotify_id) {
		this.spotify_id = spotify_id;
	}

	public String getGeneros() {
		return generos;
	}

	public void setGeneros(String genero) {
		this.generos = genero;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Override
	public int hashCode() {
		return Objects.hash(spotify_id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Artista other = (Artista) obj;
		return Objects.equals(spotify_id, other.spotify_id);
	}

	@Override
	public String toString() {
		return "Artista [spotify_id=" + spotify_id + ", nombre=" + nombre + ", generos=" + generos + "]";
	}

}
