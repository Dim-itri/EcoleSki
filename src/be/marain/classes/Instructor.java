package be.marain.classes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import be.marain.dao.InstructorDAO;

public class Instructor extends Person {
	private List<Accreditation> accreditations;

	public Instructor(int id, String name, String surname, LocalDate dob, int phone, Accreditation accreditation) {
		super(id, name, surname, dob, phone);
		accreditations = new ArrayList<Accreditation>();
		if(accreditation != null) {
			accreditations.add(accreditation);
		}
	}

	public Instructor(String name, String surname, LocalDate dob, int phone, Accreditation accreditation) {
		this(0, name, surname, dob, phone, accreditation);
	}
	
	public String getInstructorAccreditationString() {
		if (accreditations.isEmpty()) {
	        return "Aucune";
	    }
	    return accreditations.stream()
	                         .map(Accreditation::getName)
	                         .reduce((a, b) -> a + ", " + b)
	                         .orElse("Aucune");
	}
	
	public List<Accreditation> getInstructorAccreditations() {
		return accreditations;
	}

	public boolean isAccreditate() {
		return false;
	}

	public static List<Instructor> getAllInstructors(InstructorDAO dao) {
		return dao.findAll();
	}

	public static Instructor getInstructor(int id) {
		return null;
	}

	public boolean createInstructor(InstructorDAO dao) {
		return dao.create(this);
	}

	public boolean deleteInstructor(InstructorDAO dao) {
		accreditations.clear();
		accreditations = null;
		return dao.delete(this);
	}

	public boolean updateInstructor(InstructorDAO dao) {
		return dao.update(this);
	}

	public void addAccreditation(Accreditation newAcc) throws NullPointerException{
		if(newAcc != null) {
			accreditations.add(newAcc);
		}else {
			throw new NullPointerException("Aucune accréditation à ajouter");
		}
	}

	public void deleteAccreditation(Accreditation acc) {
		accreditations.remove(acc);
	}
}