package be.marain.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.marain.classes.Accreditation;
import be.marain.classes.Booking;
import be.marain.classes.Instructor;
import be.marain.classes.Lesson;
import be.marain.classes.LessonType;
import be.marain.classes.Period;
import be.marain.classes.Skier;

public class LessonDAO extends DAO<Lesson> {
	public LessonDAO(Connection conn) {
		super(conn);
	}

	@Override
	public boolean create(Lesson lesson) {
		boolean success;
		
		try {
			String[] returnCols = {"lessonid"};
			String query = "INSERT INTO lesson (minbookings, maxbookings, instructorid, ltid, lessondate, starthour, endhour, isindividual, duration) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement statement = connect.prepareStatement(query, returnCols);
			
			statement.setInt(1, lesson.getMinBookings());
			statement.setInt(2, lesson.getMaxBookings());
			statement.setInt(3, lesson.getInstructor().getPersonId());
			statement.setInt(4, lesson.getLessonType().getLtId());
			statement.setDate(5, java.sql.Date.valueOf(lesson.getDate()));
			statement.setInt(6, lesson.getStartHour());
			statement.setInt(7, lesson.getEndHour());
			
			if (lesson.getIsIndividual() == true) {
				statement.setString(8, "Y");
			}else {
				statement.setString(8, "N");
			}
			
			statement.setInt(9, lesson.getDuration());
			
			success = statement.executeUpdate() > 0;
			
			if(success) {
				ResultSet generatedKeys = statement.getGeneratedKeys();
				
				if(generatedKeys.next()) {
					int generatedId = generatedKeys.getInt(1);
					lesson.setLessonId(generatedId);
				}
			}
			statement.close();
		}catch (Exception e) {
			success = false;
			e.printStackTrace();
		}
		
		return success;
	}

	@Override
	public boolean delete(Lesson lesson) {
		boolean success;
		
		try {
			String query = "DELETE FROM lesson WHERE lessonid = ?";
			PreparedStatement statement = connect.prepareStatement(query);
			statement.setInt(1, lesson.getLessonId());
			
			success = statement.executeUpdate() > 0;
			
			statement.close();
		}catch (SQLException e) {
			success = false;
			e.printStackTrace();
		}
		
		return success;
	}

	@Override
	public boolean update(Lesson updatedLesson) {
		boolean success;
		
		try {
			String query = "UPDATE lesson SET minbookings = ?, maxbookings = ? , instructorid = ?, ltid = ?, lessondate = ?, startHour = ?, "
					+ "endHour = ?, isIndividual = ?, duration = ? "
					+ "WHERE lessonid = ?";
			
			System.out.println(updatedLesson.getStartHour());
			
			PreparedStatement statement = connect.prepareStatement(query);
			statement.setInt(1, updatedLesson.getMinBookings());
			statement.setInt(2, updatedLesson.getMaxBookings());
			statement.setInt(3, updatedLesson.getInstructor().getPersonId());
			statement.setInt(4, updatedLesson.getLessonType().getLtId());
			statement.setDate(5, java.sql.Date.valueOf(updatedLesson.getDate()));
			statement.setInt(6, updatedLesson.getStartHour());
			statement.setInt(7, updatedLesson.getEndHour());
			if (updatedLesson.getIsIndividual() == true) {
				statement.setString(8, "Y");
			}else {
				statement.setString(8, "N");
			}
			statement.setInt(9, updatedLesson.getDuration());
			statement.setInt(10, updatedLesson.getLessonId());
			
			success = statement.executeUpdate() > 0;
			statement.close();
		}catch (SQLException e) {
			success = false;
			e.printStackTrace();
		}
		
		return success;
	}

	@Override
	public Lesson find(int id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<Lesson> findAll() {
		List<Lesson> lessons = new ArrayList<Lesson>();
		
		try {
			String query = "SELECT l.lessonId, l.minBookings, l.maxBookings, l.lessonDate, l.starthour, l.endhour, l.duration, l.isindividual, " +
                    "i.instructorId, i.name, i.surname, i.dateofbirth, i.phonenumber, " +
                    "lt.ltId, lt.lessonlevel, lt.price, lt.minage, lt.maxage, " +
                    "a.accreditationid, a.name AS \"nameAccred\"" +
                    "FROM Lesson l " +
                    "INNER JOIN Instructor i ON l.instructorId = i.instructorId " +
                    "INNER JOIN LessonType lt ON l.ltId = lt.ltId " +
                    "INNER JOIN accreditation a ON lt.accreditationid = a.accreditationid";
			
			String bookingQuery = "SELECT b.bookingid, b.bookingdate, b.isinsured, "
					+ "s.skierid, s.name, s.surname, s.dateofbirth, s.phonenumber, "
					+ "p.periodid, p.startdate, p.enddate, p.isVacation "
					+ "FROM booking b "
					+ "INNER JOIN skier s ON b.skierid = s.skierid "
					+ "INNER JOIN period p ON p.periodid = b.periodid "
					+ "WHERE b.lessonid = ?";
			
			
			PreparedStatement statement = connect.prepareStatement(query);
         	ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                // Récupération des données de la table Lesson
                int lessonId = resultSet.getInt("lessonId");
                int minBookings = resultSet.getInt("minBookings");
                int maxBookings = resultSet.getInt("maxBookings");
                LocalDate lessonDate = resultSet.getDate("lessonDate").toLocalDate();
                int startHour = resultSet.getInt("starthour");
                int endHour = resultSet.getInt("endhour");
                int duration = resultSet.getInt("duration");
                boolean isIndividual;
                
                if(resultSet.getString("isindividual").charAt(0) == 'Y') {
                	isIndividual = true;
                }else {
                	isIndividual = false;
                }

                // Récupération des données de la table Instructor
                int instructorId = resultSet.getInt("instructorId");
                String instructorName = resultSet.getString("name");
                String instructorSurname = resultSet.getString("surname");
                Date instructorDob = resultSet.getDate("dateofbirth");
                int instructorPhone = resultSet.getInt("phonenumber");

                // Récupération des données de la table LessonType
                int lessonTypeId = resultSet.getInt("ltId");
                String lessonLevel = resultSet.getString("lessonlevel");
                double lessonPrice = resultSet.getDouble("price");
                int minAge = resultSet.getInt("minage");
                int maxAge = resultSet.getInt("maxAge");

                // Récupération des données de la table Accreditation
                int accreditationId = resultSet.getInt("accreditationid");
                String accreditationName = resultSet.getString("nameAccred");
                
                // Création des objets
                Accreditation accreditation = new Accreditation(accreditationId, accreditationName);
                LessonType lessonType = new LessonType(lessonTypeId, lessonLevel, lessonPrice, accreditation, minAge, maxAge);
                Instructor instructor = new Instructor(instructorId, instructorName, instructorSurname, 
                                                        instructorDob.toLocalDate(), instructorPhone, accreditation);
                     
                // Création de la leçon
                Lesson lesson = new Lesson(lessonId, minBookings, maxBookings, lessonDate, instructor, lessonType, isIndividual, startHour, endHour, duration);
                
                PreparedStatement substmt = connect.prepareStatement(bookingQuery);
                substmt.setInt(1, lessonId);
                
                ResultSet subRes = substmt.executeQuery();
                
                while(subRes.next()) {
                	int bookingId = subRes.getInt("bookingid");
                	LocalDate bookingDate = subRes.getDate("bookingdate").toLocalDate();
                	boolean isInsured;
                	
                	if(subRes.getString("isinsured").charAt(0) == 'Y') {
                    	isInsured = true;
                    }else {
                    	isInsured = false;
                    }
                	
                	Skier skier = new Skier(subRes.getString("name"), subRes.getString("surname"), subRes.getDate("dateofbirth").toLocalDate(), Integer.parseInt(subRes.getString("phonenumber")));
                	
                	boolean isVacation;
                	
                	if(subRes.getString("isVacation").charAt(0) == 'Y') {
                    	isVacation = true;
                    }else {
                    	isVacation = false;
                    }
                	
                	
                	Period period = new Period(subRes.getInt("periodid"), subRes.getDate("startdate").toLocalDate(), subRes.getDate("enddate").toLocalDate(), isVacation);
                	
                	Booking currBook = new Booking(bookingId, bookingDate, instructor, skier, lesson, period, isInsured);
                	
                	lesson.addBooking(currBook);
                }

                // Ajout de la leçon à la liste
                lessons.add(lesson);
            }
        }catch (SQLException e) {
			e.printStackTrace();
		}
		
		return lessons;
	}
}