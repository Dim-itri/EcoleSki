package be.marain.classes;


import java.time.LocalDate;
import java.util.List;

import be.marain.dao.SkierDAO;

public class Skier extends Person {
	public Skier(int id, String name, String surname, LocalDate dob, int phone) {
		super(id, name, surname, dob, phone);
	}

	public Skier(String name, String surname, LocalDate dob, int phone) {
		this(0, name, surname, dob, phone);
	}

	public boolean createSkier(SkierDAO dao) {
		return dao.create(this);
	}

	public static List<Skier> getAllSkiers(SkierDAO dao) {
		return dao.findAll();
	}

	public static Skier getSkier(int id) {
		return null;
	}

	public boolean updateSkier(SkierDAO dao) {
		return dao.update(this);
	}

	public boolean deleteSkier(SkierDAO dao) {
		return dao.delete(this);
	}

	@Override
	public String toString() {
		return "id : " + getPersonId() + " Name : " + getName() + " Surname : " + getSurname() + " DOB : "
				+ getDateOfBirth() + " Phone : " + getPhoneNumber();
	}
}