package be.marain.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
				
				generatedKeys.close();
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
                int lessonId = resultSet.getInt("lessonId");
             
                Accreditation LTaccreditation = buildAccreditation(resultSet);
                LessonType lessonType = buildLessonType(resultSet, LTaccreditation);
                                
                Instructor instructor = buildInstructor(resultSet, LTaccreditation);
                
                List<Accreditation> instructorAccreds = getAccredsInstructor(instructor.getPersonId());
                
                for(Accreditation curr:instructorAccreds) {
                	if(!instructor.getInstructorAccreditations().contains(curr)) {
                		instructor.addAccreditation(curr);
                	}
                }
                               
                Lesson lesson = buildLesson(resultSet, lessonType, instructor);
                
                PreparedStatement substmt = connect.prepareStatement(bookingQuery);
                substmt.setInt(1, lessonId);
                
                ResultSet subRes = substmt.executeQuery();
                
                while(subRes.next()) {                	
                	Skier skier = buildSkier(subRes);
                	
                	List<Booking> skierBookings = findBookingsBySkier(skier.getPersonId());
                	
                	for(Booking curr:skierBookings) {
                		if(!skier.getBookings().contains(curr)) {
                			skier.addBooking(curr);
                		}
                	}
                		
                	Period period = buildPeriod(subRes);
                	
                	Booking currBook = buildBooking(subRes, skier, instructor, lesson, period);
                	
                	lesson.addBooking(currBook);
                	skier.addBooking(currBook);
                }
                
                
                lessons.add(lesson);
            }
            
            statement.close();
            resultSet.close();
        }catch (SQLException e) {
			e.printStackTrace();
		}
		
		return lessons;
	}
}