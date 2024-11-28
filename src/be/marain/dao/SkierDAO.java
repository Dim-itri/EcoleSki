package be.marain.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
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
			
			String AccredsInstQuery = "SELECT * FROM accreditation a "
					+ "INNER JOIN instructoraccred ia ON ia.accreditationid = a.accreditationid "
					+ "WHERE ia.instructorid = ?";
			
			while (resultSet.next()) {
				Skier currSkier = new Skier(resultSet.getInt("skierId"), resultSet.getString("name"),
						resultSet.getString("surname"), resultSet.getDate("dateOfBirth").toLocalDate(),
						resultSet.getInt("phoneNumber"));
				
				PreparedStatement statement = connect.prepareStatement(bookingQuery);
				statement.setInt(1, currSkier.getPersonId());
				
				ResultSet bookingRes = statement.executeQuery();
				
				//Booking
				while(bookingRes.next()) {
					int bookingId = bookingRes.getInt("bookingid");
					LocalDate bookingDate = bookingRes.getDate("bookingdate").toLocalDate();
					boolean isInsured;
					
					if(bookingRes.getString("isInsured").charAt(0) == 'Y') {
	                	isInsured = true;
	                }else {
	                	isInsured= false;
	                }	
					
					Accreditation currAccred = new Accreditation(bookingRes.getInt("accreditationid"), bookingRes.getString("name"));
					
					LessonType currLT = new LessonType(bookingRes.getInt("ltid"), bookingRes.getString("lessonlevel"), bookingRes.getDouble("price"), currAccred, bookingRes.getInt("minage"), 
							bookingRes.getInt("maxage"));
					
					//Accreds for instructor
					PreparedStatement accredsInstStatement = connect.prepareStatement(AccredsInstQuery);
					accredsInstStatement.setInt(1, bookingRes.getInt("instructorid"));
					
					ResultSet accredInstRes = accredsInstStatement.executeQuery();
					
					List<Accreditation> accredsInst = new ArrayList<Accreditation>();
					
					while(accredInstRes.next()) {
						int currAccredId = accredInstRes.getInt("accreditationid");
						String currAccredName = accredInstRes.getString("name");
						
						Accreditation currInstAccred = new Accreditation(currAccredId, currAccredName);
						
						accredsInst.add(currInstAccred);
					}
					
					Instructor currInst = new Instructor(bookingRes.getInt("instructorid"), bookingRes.getString("name"), bookingRes.getString("surname"), 
							bookingRes.getDate("dateofbirth").toLocalDate(), Integer.parseInt(bookingRes.getString("phonenumber")),  accredsInst.get(0));;
				
					for(int i= 1;i<accredsInst.size();i++) {
						currInst.addAccreditation(accredsInst.get(i));
					}
					
					boolean isIndividual;
					
					if(bookingRes.getString("isindividual").charAt(0) == 'Y') {
	                	isIndividual = true;
	                }else {
	                	isIndividual = false;
	                }
					
					Lesson currLesson = new Lesson(bookingRes.getInt("lessonid"), bookingRes.getInt("minBookings"), bookingRes.getInt("maxbookings"), bookingRes.getDate("lessondate").toLocalDate(), 
							currInst, currLT, isIndividual, bookingRes.getInt("starthour"), bookingRes.getInt("endhour"), bookingRes.getInt("duration"));
					
					boolean isVacation;
					
					if(bookingRes.getString("isVacation").charAt(0) == 'Y') {
	                	isVacation = true;
	                }else {
	                	isVacation = false;
	                }
					
					Period p = new Period(bookingRes.getInt("periodid"), bookingRes.getDate("startdate").toLocalDate(), bookingRes.getDate("enddate").toLocalDate(), isVacation);
					
					Booking currBooking = new Booking( bookingId, bookingDate, currInst, currSkier, currLesson, p, isInsured);
					
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
