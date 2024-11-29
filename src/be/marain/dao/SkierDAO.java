package be.marain.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import be.marain.classes.Accreditation;
import be.marain.classes.Booking;
import be.marain.classes.Instructor;
import be.marain.classes.Lesson;
import be.marain.classes.LessonType;
import be.marain.classes.Period;
import be.marain.classes.Skier;

public class SkierDAO extends DAO<Skier> {
	public SkierDAO(Connection conn) {
		super(conn);
	}

	@Override
	public boolean create(Skier newSkier) {
		boolean success;
		
		try {
			String returnCols[] = {"skierid"};
			PreparedStatement statement = connect.prepareStatement("INSERT INTO skier (name, surname, dateofbirth, phonenumber) VALUES(?, ?, ?, ?)", returnCols);
			statement.setString(1, newSkier.getName());
			statement.setString(2, newSkier.getSurname());
			statement.setDate(3, java.sql.Date.valueOf(newSkier.getDateOfBirth()));
			statement.setInt(4, newSkier.getPhoneNumber());
			
			
			success = statement.executeUpdate() > 0;
			
			if(success) {
				ResultSet generatedKeys = statement.getGeneratedKeys();
				
				if(generatedKeys.next()) {
					int generatedId = generatedKeys.getInt(1);
					newSkier.setPersonId(generatedId);
				}
			}
			
			statement.close();
		}catch(SQLException ex) {
			success = false;
			JOptionPane.showMessageDialog(null, ex.getMessage());
		}
		
		return success;
	}

	@Override
	public boolean delete(Skier skier) {
		boolean success;
		
		try {
			PreparedStatement statement = connect.prepareStatement("DELETE FROM skier where skierid = ?");
			statement.setInt(1, skier.getPersonId());
			
			success = statement.executeUpdate() >= 1;
			
			statement.close();
		}catch (SQLException e) {
			success = false;
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
		return success;
	}

	@Override
	public boolean update(Skier skier) {
		boolean success;
		
		try {
			PreparedStatement statement = connect.prepareStatement("UPDATE skier SET name=?, surname=?, dateofbirth=?, phonenumber=? WHERE skierid=?");
			
			statement.setString(1, skier.getName());
			statement.setString(2, skier.getSurname());
			statement.setDate(3, java.sql.Date.valueOf(skier.getDateOfBirth()));
			statement.setInt(4, skier.getPhoneNumber());
			statement.setInt(5, skier.getPersonId());
			
			success = statement.executeUpdate() >= 1;	
			
			statement.close();
		}catch(SQLException e) {
			success = false;
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
		return success;
	}

	@Override
	public Skier find(int id) {
		return null;
	}

	@Override
	public List<Skier> findAll() {
		List<Skier> skiers = new ArrayList<>();

		try {
			ResultSet resultSet = this.connect
					.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)
					.executeQuery("SELECT * FROM Skier");

			String bookingQuery = "SELECT b.bookingid, b.bookingDate, b.isinsured, "
					+ "p.periodid, p.startdate, p.enddate, p.isVacation, "
					+ "l.lessonid, l.minbookings, l.maxbookings, l.lessonDate, l.starthour, l.endhour,l.isIndividual, l.duration, "
					+ "i.instructorid, i.name, i.surname, i.phonenumber, i.dateofbirth, "
					+ "lt.ltid, lt.lessonlevel, lt.price, lt.minage, lt.maxage, "
					+ "a.accreditationid, a.name "
					+ "FROM booking b "
					+ "INNER JOIN period p ON p.periodid = b.periodid "
					+ "INNER JOIN lesson l ON l.lessonid = b.lessonid "
					+ "INNER JOIN instructor i ON i.instructorid = l.instructorid "
					+ "INNER JOIN lessontype lt ON lt.ltid = l.ltid "
					+ "INNER JOIN accreditation a ON a.accreditationid = lt.accreditationid "
					+ "WHERE b.skierid = ?";
						
			while (resultSet.next()) {
				Skier currSkier = buildSkier(resultSet);
				
				PreparedStatement statement = connect.prepareStatement(bookingQuery);
				statement.setInt(1, currSkier.getPersonId());
				
				ResultSet bookingRes = statement.executeQuery();
				
				//Booking
				while(bookingRes.next()) {					
					Accreditation currAccred = buildAccreditation(bookingRes);
					
					LessonType currLT = buildLessonType(bookingRes, currAccred);
					
					//Accreds for instructor
					List<Accreditation> accredsInst = getAccredsInstructor(bookingRes.getInt("accreditationid"));
					
					Instructor currInst = buildInstructor(bookingRes, currAccred);
				
					for(int i= 1;i<accredsInst.size();i++) {
						currInst.addAccreditation(accredsInst.get(i));
					}
										
					Lesson currLesson = buildLesson(bookingRes, currLT, currInst);
					
					Period p = buildPeriod(bookingRes);
					
					Booking currBooking = buildBooking(bookingRes, currSkier, currInst, currLesson, p);
					
					currSkier.addBooking(currBooking);
				}
				
				skiers.add(currSkier);
			}
			
			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return skiers;
	}
}
