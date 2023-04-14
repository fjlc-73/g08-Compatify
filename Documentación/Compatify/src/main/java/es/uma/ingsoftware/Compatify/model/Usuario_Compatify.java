package es.uma.ingsoftware.Compatify.model;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;


@Entity
public class Usuario_Compatify {
	
	@Id
	private String email;
	private String nombre;
	private String contraseña;
	private boolean cuenta_privada;
	
	public Usuario_Compatify() {
		
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
		return Objects.hash(email);
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
		return Objects.equals(email, other.email);
	}
	@Override
	public String toString() {
		return "Usuario_Compatify [nombre=" + nombre + ", email=" + email + ", contraseña=" + contraseña
				+ ", cuenta_privada=" + cuenta_privada + "]";
	}
	
	

}
