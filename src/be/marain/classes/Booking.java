package be.marain.classes;

public class Booking {
	private int bookingId;
	private int duration;
	private boolean isIndividual;
	
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
		this.bookingId = bookingId;
	}
	
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	public void setIndividual(boolean isIndividual) {
		this.isIndividual = isIndividual;
	}
	
	public Booking(int id, int duration, int individual) {
		setBookingId(id);
		setDuration(duration);
		setIndividual(isIndividual);
	}
}
