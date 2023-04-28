package es.uma.ingsoftware.Compatify.model;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Usuario_Compatify {

	@Id
	private String nombre;
	private String email;
	private String contraseña;
	private String genero;
	private Date fechanacimiento;
	private boolean cuenta_privada;
	
	

	@OneToOne
	private Usuario_Spotify usuarioSpotify;

	@ManyToMany
	private List<Usuario_Compatify> amigos;

	public Usuario_Compatify() {

	}

	public Usuario_Spotify getUsuarioSpotify() {
		return usuarioSpotify;
	}

	public void setUsuarioSpotify(Usuario_Spotify usuarioSpotify) {
		this.usuarioSpotify = usuarioSpotify;
	}
	

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public Date getFechanacimiento() {
		return fechanacimiento;
	}

	public void setFechanacimiento(Date fechanacimiento) {
		this.fechanacimiento = fechanacimiento;
	}

	public List<Usuario_Compatify> getAmigos() {
		return amigos;
	}

	public void setAmigos(List<Usuario_Compatify> amigos) {
		this.amigos = amigos;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContraseña() {
		return contraseña;
	}

	public void setContraseña(String contraseña) {
		this.contraseña = contraseña;
	}

	public boolean isCuenta_privada() {
		return cuenta_privada;
	}

	public void setCuenta_privada(boolean cuenta_privada) {
		this.cuenta_privada = cuenta_privada;
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
		Usuario_Compatify other = (Usuario_Compatify) obj;
		return Objects.equals(nombre, other.nombre);
	}

	@Override
	public String toString() {
		return "Usuario_Compatify [nombre=" + nombre + ", email=" + email + ", contraseña=" + contraseña + ", genero="
				+ genero + ", fechanacimiento=" + fechanacimiento + ", cuenta_privada=" + cuenta_privada
				+ ", usuarioSpotify=" + usuarioSpotify + ", amigos=" + amigos + "]";
	}



}
