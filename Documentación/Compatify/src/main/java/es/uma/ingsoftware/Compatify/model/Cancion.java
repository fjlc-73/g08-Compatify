package es.uma.ingsoftware.Compatify.model;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

@Entity
public class Cancion {

	@Id
	private String spotify_id;
	private String nombre;
	private String[] genero = new String[3];

	@ManyToMany
	private List<Artista> autores;
	@ManyToMany(mappedBy = "favCancion")
	private List<Usuario_Spotify> oyentes;

	public Cancion() {

	}

	public List<Artista> getAutores() {
		return autores;
	}

	public void setAutores(List<Artista> autores) {
		this.autores = autores;
	}

	public List<Usuario_Spotify> getOyentes() {
		return oyentes;
	}

	public void setOyentes(List<Usuario_Spotify> oyentes) {
		this.oyentes = oyentes;
	}

	public String getSpotify_id() {
		return spotify_id;
	}

	public void setSpotify_id(String spotify_id) {
		this.spotify_id = spotify_id;
	}

	public String[] getGenero() {
		return genero;
	}

	public void setGenero(String[] genero) {
		this.genero = genero;
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
		Cancion other = (Cancion) obj;
		return Objects.equals(spotify_id, other.spotify_id);
	}

	@Override
	public String toString() {
		return "Canción [spotify_id=" + spotify_id + ", nombre=" + nombre + ", genero=" + Arrays.toString(genero) + "]";
	}

}
