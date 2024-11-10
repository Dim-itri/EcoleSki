package be.marain.classes;

import java.time.LocalDate;
import java.util.List;

public class Skier extends Person{
	public Skier(int id, String name, String surname, LocalDate dob, int phone) {
		this(name, surname, dob, phone);
		setPersonId(id);
	}
	
	public Skier(String name, String surname, LocalDate dob, int phone) {
		super(name, surname, dob, phone);
	}
	
	public static List<Skier> getAllSkiers(){
		//A faire
	}
	
	public static Skier getSkier(int id) {
		//A faire
	}
	
	public boolean updateSkier() {
		//A faire
	}
	
	public boolean deleteSkier() {
		//A faire
	}
}
