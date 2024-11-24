package be.marain.classes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes.Name;

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
	
	@Override
	public String toString() {
	    String accreditationsString = getInstructorAccreditationString();
	    return String.format("Instructeur : %s %s, ID : %d, Date de naissance : %s, Téléphone : %d, Accréditations : %s", 
	                          getName(), getSurname(), getPersonId(), getDateOfBirth(), getPhoneNumber(), accreditationsString);
	}
	
	public boolean isInstructorAvailable(LocalDate lessonTime, List<Lesson> lessons) {
	    for (Lesson lesson : lessons) {
	        if (lesson.getInstructor().equals(this) && lesson.getDate().equals(lessonTime)) {
	            return false;
	        }
	    }
	    return true;
	}
	
	public String getDisplayName() {
		return getName() + " " + getSurname();
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
	
	@Override
	public boolean equals(Object obj) {
		if(obj != null &&  obj.getClass() == this.getClass() && ((Instructor)obj).getPersonId() == getPersonId()) {
			return true;
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}
}