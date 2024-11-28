package be.marain.classes;

import java.time.LocalDate;
import java.util.regex.Pattern;

public abstract class Person {
	private int personId;
	private String name;
	private String surname;
	private LocalDate dateOfBirth;
	private int phoneNumber;
	private static final String nameRegEx = "^[A-ZÀ-Ÿ][a-zà-ÿ]+(?:[-\\s][A-ZÀ-Ÿ][a-zà-ÿ]+)*$";
	private static final String dobRegEx = "^(19[0-9]{2}|20[0-9]{2})-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$";
	private static final String phoneRegEx = "^(\\+\\d{1,3})?\\s?(\\(?\\d{1,4}\\)?)?[\\s.-]?\\d{2,4}[\\s.-]?\\d{2,4}[\\s.-]?\\d{2,4}$";

	public int getAge() {
		return java.time.Period.between(dateOfBirth, LocalDate.now()).getYears();
	}
	
	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public int getPersonId() {
		return personId;
	}

	public String getName() {
		return name;
	}

	public int getPhoneNumber() {
		return phoneNumber;
	}

	public String getSurname() {
		return surname;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) throws IllegalArgumentException{
		if(Pattern.matches(dobRegEx, dateOfBirth.toString())) {
			this.dateOfBirth = dateOfBirth;
		}else {
			throw new IllegalArgumentException("Erreur dans la date de naissance : " + dateOfBirth.toString());
		}
	}

	public void setPersonId(int personId) {
		this.personId = personId;
	}

	public void setName(String name) throws IllegalArgumentException{		
		if(Pattern.matches(nameRegEx, name)) {
			this.name = name;
		}else {
			throw new IllegalArgumentException("Erreur dans le prénom : " + name);
		}
	}

	public void setPhoneNumber(int phoneNumber) throws IllegalArgumentException{
		if(Pattern.matches(phoneRegEx, String.valueOf(phoneNumber))) {
			this.phoneNumber = phoneNumber;
		}else {
			throw new IllegalArgumentException("Erreur dans le numéro de téléphone : " + phoneNumber);
		}
	}

	public void setSurname(String surname) throws IllegalArgumentException {
		if(Pattern.matches(nameRegEx, surname)) {
			this.surname = surname;
		}else {
			throw new IllegalArgumentException("Erreur dans le nom : " + surname);
		}
	}

	public Person(int id, String name, String surname, LocalDate dob, int phone) {
		setPersonId(id);
		setName(name);
		setSurname(surname);
		setPhoneNumber(phone);
		setDateOfBirth(dob);
	}

	public Person(String name, String surname, LocalDate dob, int phone) {
		this(0, name, surname, dob, phone);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj != null && obj.getClass() == this.getClass() && ((Person)obj).getPersonId() == this.getPersonId()) {
			return true;
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return Integer.hashCode(personId);
	}
}
