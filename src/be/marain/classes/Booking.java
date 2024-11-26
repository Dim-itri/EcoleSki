package be.marain.classes;

import java.time.LocalDate;
import java.util.List;

import be.marain.dao.BookingDAO;

public class Booking {
	private int bookingId;
	private LocalDate bookingDate;
	private Instructor instructor;
	private Period period;
	private Skier skier;
	private Lesson lesson;
	
	public Booking(int id, LocalDate bookingDate, Instructor instructor, Skier skier,
			Lesson lesson, Period period) {
		setBookingId(id);
		setInstructor(instructor);
		setLesson(lesson);
		setPeriod(period);
		setSkier(skier);
	}

	public Booking(LocalDate bookingDate,Instructor instructor, Skier skier,
			Lesson lesson, Period period) {
		this(0, bookingDate,instructor, skier, lesson, period);
	}
	
	public static List<Booking> getAllBookings(BookingDAO dao){
		return dao.findAll();
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
	
	public LocalDate getBookingDate() {
		return bookingDate;
	}

	public void setBookingId(int bookingId) {
		if(bookingId >= 0) {
			this.bookingId = bookingId;
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
	
	public void setBookingDate(LocalDate bookingDate) {
		if(bookingDate != null) {
			this.bookingDate = bookingDate;
		}else {
			throw new NullPointerException("Date invalide");
		}
	}

	@Override
	public String toString() {
		return "Id : " + bookingId;
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
