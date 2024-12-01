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
	private boolean isInsured;
	
	public Booking(int id, LocalDate bookingDate, Instructor instructor, Skier skier,
			Lesson lesson, Period period, boolean insured) {
		setBookingId(id);
		setBookingDate(bookingDate);
		setInstructor(instructor);
		setLesson(lesson);
		setPeriod(period);
		setSkier(skier);
		setIsInsured(insured);
	}
	
	public double calculatePrice() {
		double price = 0;

		if(lesson.getIsIndividual()) {
			if(lesson.getDuration() == 1) {
				price = 60;
			}else {
				price = 90;
			}
		}else {
			price = lesson.getLessonType().getPrice();
			
			if (isReduced()) {
				price -= price*0.15;
			}
		}
		
		if(isInsured) {
			price += 20;
		}
		
		return price;
	}
	
	private boolean isReduced() {
	    boolean hasMorningLesson = false;
	    boolean hasAfternoonLesson = false;
	    
	    for (Booking booking : skier.getBookings()) {

	        if (booking.getLesson().getDate().equals(this.getLesson().getDate())) {
	            int lessonStartHour = booking.getLesson().getStartHour();
       
	            if (lessonStartHour == 9) {
	                hasMorningLesson = true;
	            }
	            
	            else if (lessonStartHour == 14) {
	                hasAfternoonLesson = true;
	            }
	        }
	    }

	    return hasMorningLesson && hasAfternoonLesson;
	}
	
	public Booking(LocalDate bookingDate,Instructor instructor, Skier skier,
			Lesson lesson, Period period, boolean insured) {
		this(0, bookingDate,instructor, skier, lesson, period, insured);
	}
	
	public boolean canBook(Lesson lesson) {
		if(lesson.getIsIndividual()) {
			if(period.getVacation()) {
				return !LocalDate.now().isAfter(lesson.getDate().minusWeeks(1));
			}else {
				return !LocalDate.now().isAfter(lesson.getDate().minusMonths(1));
			}
		}
		
		return true;
	}
	
	public boolean createBooking(BookingDAO dao) {
		return dao.create(this);
	}
	
	public boolean deleteBooking(BookingDAO dao) {
		return dao.delete(this);
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
	
	public boolean getIsInsured() {
		return isInsured;
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
	
	public void setIsInsured(boolean insured) {
		this.isInsured = insured;
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
