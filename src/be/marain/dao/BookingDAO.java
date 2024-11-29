package be.marain.dao;

import java.sql.Connection;
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

public class BookingDAO extends DAO<Booking> {
	public BookingDAO(Connection conn) {
		super(conn);
	}

	@Override
	public boolean create(Booking booking) {
		boolean success;
		String[] returnCols = {"bookingid"}; 
		String query = "INSERT INTO booking (bookingdate, lessonid, periodid, skierid, isinsured) VALUES (?, ?, ?, ?, ?)";
		
		try {
			PreparedStatement statement = connect.prepareStatement(query, returnCols);
			statement.setDate(1, java.sql.Date.valueOf(booking.getBookingDate()));
			statement.setInt(2, booking.getLesson().getLessonId());
			statement.setInt(3, booking.getPeriod().getPeriodId());
			statement.setInt(4, booking.getSkier().getPersonId());
			
			if (booking.getIsInsured()) {
				statement.setString(5, "Y");
			}else {
				statement.setString(5, "N");
			}
			
			success = statement.executeUpdate() > 0;
			
			if (success) {
				ResultSet generatedKeys = statement.getGeneratedKeys();
				
				if(generatedKeys.next()) {
					int generatedId = generatedKeys.getInt(1);
					booking.setBookingId(generatedId);
				}
			}
			statement.close();
		}catch (SQLException e) {
			success = false;
			e.printStackTrace();
		}
		
		return success;
	}

	@Override
	public boolean delete(Booking booking) {
		boolean success;
		String query = "DELETE FROM booking WHERE bookingid = ?";
		
		try {
			PreparedStatement statement = connect.prepareStatement(query);
			statement.setInt(1, booking.getBookingId());
			
			success = statement.executeUpdate() > 0;
		} catch (SQLException e) {
			success = false;
			e.printStackTrace();
		}
		
		return success;
	}

	@Override
	public boolean update(Booking obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Booking find(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Booking> findAll() {
		List<Booking> bookings = new ArrayList<Booking>();
		Map<Integer, Skier> skierMap = new HashMap<>();
		
		try {
			String query = "SELECT b.bookingid, b.bookingDate, b.isinsured,"
					+ "s.skierid, s.name AS \"skier_name\", s.surname AS \"skier_surname\", s.dateofbirth AS \"skier_dob\", s.phonenumber AS \"skier_phone\", "
					+ "p.periodid, p.startdate, p.enddate, p.isVacation, "
					+ "l.lessonid, l.minbookings, l.maxbookings, l.lessondate, l.isIndividual, l.starthour, l.endhour, l.duration, "
					+ "i.instructorid, i.name AS \"instructor_name\", i.surname AS \"instructor_surname\", i.phonenumber AS \"instructor_phone\", i.dateofbirth AS \"instructor_dob\", "
					+ "lt.ltid, lt.lessonlevel, lt.price, lt.minage, lt.maxage, "
					+ "a.accreditationid, a.name AS \"accred_name\""
					+ "FROM booking b "
					+ "INNER JOIN period p ON p.periodid = b.periodid "
					+ "INNER JOIN skier s ON s.skierid = b.skierid "
					+ "INNER JOIN lesson l ON l.lessonid = b.lessonid "
					+ "INNER JOIN instructor i ON i.instructorid = l.instructorid "
					+ "INNER JOIN lessontype lt ON lt.ltid = l.ltid "
					+ "INNER JOIN Accreditation a ON a.accreditationid = lt.accreditationid ";
						
			ResultSet res = connect.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(query);
					
			while(res.next()) {
                int skierId = res.getInt("skierid");
                String skierName = res.getString("skier_name");
                String skierSurname = res.getString("skier_surname");
                LocalDate skierDob = res.getDate("skier_dob").toLocalDate();
                int skierPhone = Integer.parseInt(res.getString("skier_phone"));

                int instructorId = res.getInt("instructorid");
                String instructorName = res.getString("instructor_name");
                String instructorSurname = res.getString("instructor_surname");
                int instructorPhone = Integer.parseInt(res.getString("instructor_phone"));
                LocalDate instructorDob = res.getDate("instructor_dob").toLocalDate();
                  
                int accredid = res.getInt("accreditationid");
                String accredName = res.getString("accred_name");
                
                List<Accreditation> accreditations = getAccredsInstructor(instructorId);
                                
                Instructor currInst = new Instructor(instructorName, instructorSurname, instructorDob, instructorPhone, accreditations.get(0));
                
                for(int i = 1;i<accreditations.size();i++) {
                	currInst.addAccreditation(accreditations.get(i));
                }
                Accreditation ltAccred = new Accreditation(accredid, accredName);
                
                LessonType currLessonType = buildLessonType(res, ltAccred);
                
                Lesson currLesson = buildLesson(res, currLessonType, currInst);
                
                Skier currSkier = skierMap.get(skierId);
                if (currSkier == null) {
                    
                    currSkier = new Skier(skierId, skierName, skierSurname, skierDob, skierPhone);
                    skierMap.put(skierId, currSkier);
                }
                
                Period currPeriod = buildPeriod(res);
                
                Booking currBooking = buildBooking(res, currSkier, currInst, currLesson, currPeriod);
                
                currSkier.addBooking(currBooking);
                
                bookings.add(currBooking);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return bookings;
	}
}