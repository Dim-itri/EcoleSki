package be.marain.classes;

import java.time.LocalDate;

public class Lesson {
	private int lessonId;
	private int minBookings;
	private int maxBookings;
	private LocalDate date;
	
	public void setDate(LocalDate date) {
		this.date = date;
	}
	
	public void setLessonId(int lessonId) {
		this.lessonId = lessonId;
	}
	
	public void setMaxBookings(int maxBookings) {
		this.maxBookings = maxBookings;
	}
	
	public void setMinBookings(int minBookings) {
		this.minBookings = minBookings;
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
	
	public Lesson(int id,int min, int max, LocalDate date) {
		setDate(date);
		setLessonId(id);
		setMaxBookings(max);
		setMinBookings(min);
	}
	
	public Lesson(int min, int max, LocalDate date) {
		setDate(date);
		setMaxBookings(max);
		setMinBookings(min);
	}
}
