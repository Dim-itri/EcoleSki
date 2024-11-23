package be.marain.classes;

import java.time.LocalDate;
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
	private static final String dateRegEx = "^(19[0-9]{2}|20[0-9]{2})-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$";
	
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
		return dao.create(this);
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
		if (Pattern.matches(dateRegEx, date.toString())) {
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
	
	@Override
	public String toString() {
		return "Id : " + lessonId + ", Min : " + minBookings + ", Max : " + maxBookings + ", Date : " + date.toString();
	}
}
