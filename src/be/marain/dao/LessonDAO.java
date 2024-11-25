package be.marain.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


import be.marain.classes.Accreditation;
import be.marain.classes.Instructor;
import be.marain.classes.Lesson;
import be.marain.classes.LessonType;

public class LessonDAO extends DAO<Lesson> {
	public LessonDAO(Connection conn) {
		super(conn);
	}

	@Override
	public boolean create(Lesson lesson) {
		boolean success;
		
		try {
			String[] returnCols = {"lessonid"};
			String query = "INSERT INTO lesson (minbookings, maxbookings, instructorid, ltid, lessondate) VALUES(?, ?, ?, ?, ?)";
			PreparedStatement statement = connect.prepareStatement(query, returnCols);
			
			statement.setInt(1, lesson.getMinBookings());
			statement.setInt(2, lesson.getMaxBookings());
			statement.setInt(3, lesson.getInstructor().getPersonId());
			statement.setInt(4, lesson.getLessonType().getLtId());
			statement.setDate(5, java.sql.Date.valueOf(lesson.getDate()));
			
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
			String query = "UPDATE lesson SET minbookings = ?, maxbookings = ? , instructorid = ?, ltid = ?, lessondate = ? "
					+ "WHERE lessonid = ?";
			
			PreparedStatement statement = connect.prepareStatement(query);
			statement.setInt(1, updatedLesson.getMinBookings());
			statement.setInt(2, updatedLesson.getMaxBookings());
			statement.setInt(3, updatedLesson.getInstructor().getPersonId());
			statement.setInt(4, updatedLesson.getLessonType().getLtId());
			statement.setDate(5, java.sql.Date.valueOf(updatedLesson.getDate()));
			statement.setInt(6, updatedLesson.getLessonId());
			
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
			String query = "SELECT l.lessonId, l.minBookings, l.maxBookings, l.lessonDate, " +
                    "i.instructorId, i.name, i.surname, i.dateofbirth, i.phonenumber, " +
                    "lt.ltId, lt.lessonlevel, lt.price, " +
                    "a.accreditationid, a.name AS \"nameAccred\"" +
                    "FROM Lesson l " +
                    "INNER JOIN Instructor i ON l.instructorId = i.instructorId " +
                    "INNER JOIN LessonType lt ON l.ltId = lt.ltId " +
                    "INNER JOIN accreditation a ON lt.accreditationid = a.accreditationid";
			
			PreparedStatement statement = connect.prepareStatement(query);
         	ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                // Récupération des données de la table Lesson
                int lessonId = resultSet.getInt("lessonId");
                int minBookings = resultSet.getInt("minBookings");
                int maxBookings = resultSet.getInt("maxBookings");
                LocalDate lessonDate = resultSet.getDate("lessonDate").toLocalDate();

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

                // Récupération des données de la table Accreditation
                int accreditationId = resultSet.getInt("accreditationid");
                String accreditationName = resultSet.getString("nameAccred");

                // Création des objets
                Accreditation accreditation = new Accreditation(accreditationId, accreditationName);
                LessonType lessonType = new LessonType(lessonTypeId, lessonLevel, lessonPrice, accreditation);
                Instructor instructor = new Instructor(instructorId, instructorName, instructorSurname, 
                                                        instructorDob.toLocalDate(), instructorPhone, accreditation);
                     
                // Création de la leçon
                Lesson lesson = new Lesson(lessonId, minBookings, maxBookings, lessonDate, instructor, lessonType);

                // Ajout de la leçon à la liste
                lessons.add(lesson);
            }
        }catch (SQLException e) {
			e.printStackTrace();
		}
		
		return lessons;
	}
}