package be.marain.classes;


import java.time.LocalDate;
import java.util.List;

import be.marain.dao.SkierDAO;

public class Skier extends Person {
	private List<Booking> bookings;
	
	public Skier(int id, String name, String surname, LocalDate dob, int phone) {
		super(id, name, surname, dob, phone);
	}

	public Skier(String name, String surname, LocalDate dob, int phone) {
		this(0, name, surname, dob, phone);
	}
	
	public boolean isAvailable(LocalDate date, int startHour, int endHour, Lesson lesson) {
	    for (Booking currBook : bookings) {
	            Lesson currLesson = currBook.getLesson();
	            	            
	            if (currLesson.getDate().equals(date)) {
	                if (startHour > currLesson.getStartHour() && endHour > currLesson.getEndHour()) {
	                    return false;
	                }
	            }
	    }
	    return true;
    }
	    

	
	public boolean isOldEnough(Lesson lesson) {
		if(lesson.getLessonType().getMaxAge() != 0) {
			return getAge() < lesson.getLessonType().getMaxAge() && getAge() > lesson.getLessonType().getMinAge();
		}
		
		return getAge() > lesson.getLessonType().getMinAge();
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
	
	@Override
	public boolean equals(Object obj) {
		if(obj != null && obj.getClass() == this.getClass() && ((Skier)obj).getPersonId() == getPersonId()) {
			return true;
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}
}