package be.marain.classes;

import java.util.List;

import be.marain.dao.BookingDAO;

public class Booking {
	private int bookingId;
	private int duration;
	private boolean isIndividual;
	private Instructor instructor;
	private Period period;
	private Skier skier;
	private Lesson lesson;
	
	public Booking(int id, int duration, int individual, Instructor instructor, Skier skier,
			Lesson lesson, Period period) {
		setBookingId(id);
		setDuration(duration);
		setIndividual(isIndividual);	
		setInstructor(instructor);
		setLesson(lesson);
		setPeriod(period);
		setSkier(skier);
	}

	public Booking(int duration, int individual, Instructor instructor, Skier skier,
			Lesson lesson, Period period) {
		this(0, duration, individual, instructor, skier, lesson, period);
	}
	
	public static List<Booking> getAllBookings(BookingDAO dao){
		return null;
	}
	
	public Skier getSkier() {
		return skier;
	}
	
	public Period getPeriod() {
		return period;
	}

	public Lesson getLesson() {
		return lesson;
	}
	
	public Instructor getInstructor() {
		return instructor;
	}

	public int getBookingId() {
		return bookingId;
	}

	public int getDuration() {
		return duration;
	}

	public boolean getIndividual() {
		return isIndividual;
	}

	public void setBookingId(int bookingId) {
		if(bookingId >= 0) {
			this.bookingId = bookingId;
		}
	}

	public void setDuration(int duration) {
		if(duration > 0) {
			this.duration = duration;
		}
	}
	
	public void setInstructor(Instructor instructor) throws NullPointerException{
		if(instructor != null) {
			this.instructor = instructor;
		}else {
			throw new NullPointerException("Aucun instructeur à ajouter.");
		}
	}
	
	public void setLesson(Lesson lesson) throws NullPointerException{
		if(lesson != null) {
			this.lesson = lesson;
		}else {
			throw new NullPointerException("Aucune leçon à ajouter.");
		}
	}
	
	public void setPeriod(Period period) throws NullPointerException{
		if(period != null) {
			this.period = period;
		}else {
			throw new NullPointerException("Aucune période à ajouter.");
		}
	}
	
	public void setSkier(Skier skier) throws NullPointerException{
		if(skier != null) {
			this.skier = skier;
		}else {
			throw new NullPointerException("Aucun skieur à ajouter.");
		}
	}

	public void setIndividual(boolean isIndividual) {
		this.isIndividual = isIndividual;
	}
	
	@Override
	public String toString() {
		return "Id : " + bookingId + ", Durée : " + duration + ", Individuel : " + isIndividual;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj != null && this.getClass() == obj.getClass() && ((Booking)obj).getBookingId() == getBookingId()) {
			return true;
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return Integer.hashCode(bookingId);
	}
}
