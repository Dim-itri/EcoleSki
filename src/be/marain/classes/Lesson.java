package be.marain.classes;

import java.time.LocalDate;
import java.util.List;

import be.marain.dao.LessonDAO;

public class Lesson {
	private int lessonId;
	private int minBookings;
	private int maxBookings;
	private LocalDate date;
	private Instructor instructor;
	private LessonType lessonType;
	
	public Lesson(int id, int min, int max, LocalDate date, Instructor instructor, LessonType lessonType) {
		setLessonId(id);
		setMaxBookings(max);
		setMinBookings(min);
		setInstructor(instructor);
		setLessonType(lessonType);
		setDate(date);
	}

	public Lesson(int min, int max, LocalDate date, Instructor instructor, LessonType lessonType) {
		this(0, min, max, date, instructor, lessonType);
	}
	
	public static List<Lesson> getAllLessons(LessonDAO dao) {
		return dao.findAll();
	}
	
	public boolean addLesson(LessonDAO dao) {
		return false;
	}
	
	public boolean deleteLesson(LessonDAO dao) {
		return false;
	}
	
	public boolean updateLesson(LessonDAO dao) {
		return false;
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
	
	public Instructor getInstructor() {
		return instructor;
	}
	
	public LessonType getLessonType() {
		return lessonType;
	}
}
