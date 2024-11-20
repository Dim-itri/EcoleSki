package be.marain.classes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import be.marain.dao.InstructorDAO;

public class Instructor extends Person {
	private List<Accreditation> accreditations;

	public Instructor(int id, String name, String surname, LocalDate dob, int phone, Accreditation accreditation) {
		this(name, surname, dob, phone, accreditation);
		setPersonId(id);
	}

	public Instructor(String name, String surname, LocalDate dob, int phone, Accreditation accreditation) {
		super(name, surname, dob, phone);
		accreditations = new ArrayList<Accreditation>();
		if (accreditation != null) {
			accreditations.add(accreditation);
		}
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

	public boolean createInstructor() {
		return false;
	}

	public boolean deleteInstructor() {
		return false;
	}

	public boolean updateInstructor() {
		return false;
	}

	public void addAccreditation(Accreditation newAcc) {
		accreditations.add(newAcc);
	}

	public void deleteAccreditation(Accreditation acc) {
		accreditations.remove(acc);
	}
}