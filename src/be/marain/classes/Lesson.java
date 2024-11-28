package be.marain.classes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


import be.marain.dao.LessonDAO;

public class Lesson {
	private int lessonId;
	private int minBookings;
	private int maxBookings;
	private LocalDate date;
	private Instructor instructor;
	private LessonType lessonType;
	private List<Booking> bookings;
	private int startHour;
	private int endHour;
	private boolean isIndividual;
	private int duration;
	private static final String dateRegEx = "^(19[0-9]{2}|20[0-9]{2})-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$";
	
	public Lesson(int id, int min, int max, LocalDate date, Instructor instructor, LessonType lessonType, boolean individual, int startHour, int endHour, int duration) {
		bookings = new ArrayList<Booking>();
		setLessonId(id);
		setMaxBookings(max);
		setMinBookings(min);
		setInstructor(instructor);
		setLessonType(lessonType);
		setDate(date);
		setIndividual(individual);
		setStartHour(startHour);
		setEndHour(endHour);
		setDuration(duration);
	}

	public Lesson(int min, int max, LocalDate date, Instructor instructor, LessonType lessonType, boolean individual, int startHour, int endHour, int duration) {
		this(0, min, max, date, instructor, lessonType, individual, startHour, endHour, duration);
	}
	
	public boolean canBeCreated() {
		if (date.isBefore(LocalDate.of(2024, 12, 6)) || date.isAfter(LocalDate.of(2025, 5, 3))) {
	        return false; 
	    }
		
		return true;
	}

	public boolean isFull() {
		return bookings.size() >= maxBookings;
	}
	
	public static List<Lesson> getAllLessons(LessonDAO dao) {
		return dao.findAll();
	}
	
	public void addBooking(Booking booking) {
		if(booking != null) {
			bookings.add(booking);
		}
	}
	
	public boolean addLesson(LessonDAO dao) {
		return dao.create(this);
	}
	
	public boolean deleteLesson(LessonDAO dao) {
		instructor = null;
		lessonType = null;
		return dao.delete(this);
	}
	
	public boolean updateLesson(LessonDAO dao) {
		return dao.update(this);
	}
	
	public void setInstructor(Instructor newInstructor) throws NullPointerException{
		if(newInstructor != null) {
			instructor = newInstructor;
		}else {
			throw new NullPointerException("Veuillez sélectionner un instructeur.");
		}
	}
	
	public void setLessonType(LessonType newLessonType) throws NullPointerException{
		if(newLessonType != null) {
			lessonType = newLessonType;
		}else {
			throw new NullPointerException("Veuillez sélectionner un type de leçon");
		}
	}

	public void setDate(LocalDate date) {
		if (Pattern.matches(dateRegEx, date.toString()) && date.isAfter(LocalDate.now())) {
			this.date = date;
		}else {
			throw new IllegalArgumentException("Date invalide.");
		}
	}

	public void setLessonId(int lessonId) throws IllegalArgumentException{
		this.lessonId = lessonId;
	}

	public void setMaxBookings(int maxBookings) {
		if(maxBookings > 0) {
			this.maxBookings = maxBookings;
		}else {
			throw new IllegalArgumentException("Maximum invalide.");
		}
	}

	public void setMinBookings(int minBookings) throws IllegalArgumentException{
		if(minBookings > 0 && minBookings < maxBookings) {
			this.minBookings = minBookings;
		}else {
			throw new IllegalArgumentException("Minimum invalide.");
		}
	}
	
	public void setDuration(int duration) {
		if(duration > 0) {
			this.duration = duration;
		}else {
			throw new IllegalArgumentException("Durée invalide.");
		}
	}
	
	public void setEndHour(int endHour) {
		if(endHour >= startHour) {
			this.endHour = endHour;
		}else {
			throw new IllegalArgumentException("Heure de fin invalide.");
		}
	}
	
	public void setIndividual(boolean isIndividual) {
		this.isIndividual = isIndividual;
	}
	
	public void setStartHour(int startHour) {
			this.startHour = startHour;			
	}
	
	public LocalDate getDate() {
		return date;
	}

	public int getLessonId() {
		return lessonId;
	}

	public int getMaxBookings() {
		return maxBookings;
	}

	public int getMinBookings() {
		return minBookings;
	}
	
	public Instructor getInstructor() {
		return instructor;
	}
	
	public LessonType getLessonType() {
		return lessonType;
	}
	
	public int getDuration() {
		return duration;
	}
	
	public int getEndHour() {
		return endHour;
	}
	
	public int getStartHour() {
		return startHour;
	}
	
	public boolean getIsIndividual() {
		return isIndividual;
	}
	
	public List<Booking> getBookings() {
		return bookings;
	}
	
	@Override
	public String toString() {
		return "Id : " + lessonId + ", Min : " + minBookings + ", Max : " + maxBookings + ", Date : " + date.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj != null && obj.getClass() == this.getClass() && this.getLessonId() == ((Lesson)obj).getLessonId()) {
			return true;
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return Integer.hashCode(lessonId);
	}
}
