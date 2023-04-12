package es.uma.ingsoftware.Compatify.model;

import java.util.Objects;

import jakarta.persistence.Entity;

@Entity
public class Canción {
	
	private String nombre;
	
	public Canción() {
		
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
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
		Canción other = (Canción) obj;
		return Objects.equals(nombre, other.nombre);
	}

	@Override
	public String toString() {
		return "Canción [nombre=" + nombre + "]";
	}
	
	

}
