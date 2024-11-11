package be.marain.classes;

import java.time.LocalDate;
import java.util.List;

public class Instructor extends Person{
	private List<Accreditation> accreditations;
	
	public Instructor(int id, String name, String surname, LocalDate dob, int phone, Accreditation accreditation) {
		this(name, surname, dob, phone, accreditation);
		setPersonId(id);
	}
	
	public Instructor(String name, String surname, LocalDate dob, int phone, Accreditation accreditation) {
		super(name, surname, dob, phone);
		if(accreditation != null) {
			accreditations.add(accreditation);
		}
	}
	
	public List<Accreditation> getInstructorAccreditations() {
		return accreditations;
	}
	
	public boolean isAccreditate() {
		//A faire
	}
	
	public static List<Instructor> getAllInstructors(){
		//A faire
	}
	
	public static Instructor getInstructor(int id) {
		//A faire
	}
	
	public boolean createInstructor() {
		//A faire
	}
	
	public boolean deleteInstructor() {
		//a faire
	}
	
	public boolean updateInstructor() {
		//A faire
	}
	
	public void addAccreditation(Accreditation newAcc) {
		accreditations.add(newAcc);
	}
	
	public void deleteAccreditation(Accreditation acc) {
		accreditations.remove(acc);
	}
}