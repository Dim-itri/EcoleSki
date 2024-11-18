package be.marain.classes;

import java.sql.Date;
import java.time.LocalDate;

public abstract class Person {
	private int personId;
	private String name;
	private String surname;
	private LocalDate dateOfBirth;
	private int phoneNumber;

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

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public void setPersonId(int personId) {
		this.personId = personId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPhoneNumber(int phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public Person(int id, String name, String surname, LocalDate dob, int phone) {
		this(name, surname, dob, phone);
		setPersonId(id);
	}

	public Person(String name, String surname, LocalDate dob, int phone) {
		personId = 0;
		setName(name);
		setSurname(surname);
		setDateOfBirth(dob);
		setPhoneNumber(phone);
	}
}
