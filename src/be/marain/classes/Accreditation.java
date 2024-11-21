package be.marain.classes;

import java.util.List;
import java.util.Objects;

import be.marain.dao.AccreditationDAO;

public class Accreditation {
	private int accreditationId;
	private String name;

	public int getAccreditationId() {
		return accreditationId;
	}

	public String getName() {
		return name;
	}

	public void setAccreditationId(int accreditationId) {
		this.accreditationId = accreditationId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Accreditation(int id, String name) {
		setAccreditationId(id);
		setName(name);
	}

	public static List<Accreditation> getAllAccreditations(AccreditationDAO dao) {
		return dao.findAll();
	}
	
	@Override
	public String toString() {
		return accreditationId + " - " + name;
	}
	
	@Override
	public boolean equals(Object obj) {
	    if (this == obj) return true;
	    if (obj == null || getClass() != obj.getClass()) return false;
	    Accreditation that = (Accreditation) obj;
	    return accreditationId == that.accreditationId;
	}
	
	@Override
	public int hashCode() {
	    return Objects.hash(accreditationId); // Utilise l'attribut unique pour générer le hashCode
	}
}
