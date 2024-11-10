package be.marain.classes;

import java.time.LocalDate;

public class Booking {
	private int bookingId;
	private LocalDate date;
	private int duration;
	private boolean isIndividual;
	
	public int getBookingId() {
		return bookingId;
	}
	
	public LocalDate getDate() {
		return date;
	}
	
	public int getDuration() {
		return duration;
	}
	
	public boolean getIndividual() {
		return isIndividual;
	}
	
	public void setBookingId(int bookingId) {
		this.bookingId = bookingId;
	}
	
	public void setDate(LocalDate date) {
		this.date = date;
	}
	
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	public void setIndividual(boolean isIndividual) {
		this.isIndividual = isIndividual;
	}
	
	public Booking(int id, LocalDate date, int duration, int individual) {
		setBookingId(id);
		setDate(date);
		setDuration(duration);
		setIndividual(isIndividual);
	}
}
