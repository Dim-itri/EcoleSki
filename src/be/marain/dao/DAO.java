package be.marain.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import be.marain.classes.Accreditation;
import be.marain.classes.Booking;
import be.marain.classes.Instructor;
import be.marain.classes.Lesson;
import be.marain.classes.LessonType;
import be.marain.classes.Period;
import be.marain.classes.Skier;

public abstract class DAO<T> {
	protected Connection connect = null;

	public DAO(Connection conn) {
		connect = conn;
	}

	public abstract boolean create(T obj);

	public abstract boolean update(T obj);

	public abstract boolean delete(T obj);

	public abstract T find(int id);

	public abstract List<T> findAll();
	
	public Skier buildSkier(ResultSet resultSet) {
		try {
			Skier currSkier = new Skier(resultSet.getInt("skierId"), resultSet.getString("name"),
					resultSet.getString("surname"), resultSet.getDate("dateOfBirth").toLocalDate(),
					Integer.parseInt(resultSet.getString("phoneNumber")));
		
			return currSkier;
		}catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public Accreditation buildAccreditation(ResultSet res) {
		try {
			Accreditation currAccred = new Accreditation(res.getInt("accreditationid"), res.getString("name"));
			
			return currAccred;
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public Instructor buildInstructor(ResultSet res, Accreditation accred) {
		try {
			Instructor currInst = new Instructor(res.getInt("instructorid"), res.getString("name"), res.getString("surname"), 
					res.getDate("dateofbirth").toLocalDate(), Integer.parseInt(res.getString("phonenumber")),  accred);
					
			return currInst;
		}catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public LessonType buildLessonType(ResultSet resultSet, Accreditation accred) {
		try {        
	        LessonType newLt = new LessonType(resultSet.getInt("ltId"), resultSet.getString("lessonlevel"), 
	        		resultSet.getDouble("price"), accred, resultSet.getInt("minage"), resultSet.getInt("maxAge"));
	        
	        return newLt;
		}catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public Lesson buildLesson(ResultSet resultSet, LessonType lt, Instructor i) {
		try {
	        boolean isIndividual;
	        
	        if(resultSet.getString("isindividual").charAt(0) == 'Y') {
	        	isIndividual = true;
	        }else {
	        	isIndividual = false;
	        }
	        
	        Lesson newLesson = new Lesson(resultSet.getInt("lessonId"), resultSet.getInt("minBookings"), resultSet.getInt("maxBookings"), resultSet.getDate("lessonDate").toLocalDate()
	        		, i, lt, isIndividual, resultSet.getInt("starthour"), resultSet.getInt("endhour"), resultSet.getInt("duration"));
	        
	        return newLesson;
		}catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public Period buildPeriod(ResultSet res) {
		try {
			boolean isVacation;
        	
        	if(res.getString("isVacation").charAt(0) == 'Y') {
            	isVacation = true;
            }else {
            	isVacation = false;
            }
			
			Period period = new Period(res.getInt("periodid"), res.getDate("startdate").toLocalDate(), res.getDate("enddate").toLocalDate(), isVacation);
			
			return period;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public Booking buildBooking(ResultSet res, Skier skier, Instructor instructor, Lesson lesson, Period period) {
		try {
        	boolean isInsured;
        	
        	if(res.getString("isinsured").charAt(0) == 'Y') {
            	isInsured = true;
            }else {
            	isInsured = false;
            }
			
			Booking currBook = new Booking(res.getInt("bookingid"), res.getDate("bookingdate").toLocalDate(), instructor, skier, lesson, period, isInsured);
			
			return currBook;
		}catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public List<Accreditation> getAccredsInstructor(int instructorid){
		String accredQuery = "SELECT a.accreditationid, a.name FROM accreditation a "
		        + "INNER JOIN instructoraccred ia ON ia.accreditationid = a.accreditationid "
		        + "WHERE ia.instructorid = ?";
		List<Accreditation> accreditations = new ArrayList<Accreditation>();
		
		try {
			PreparedStatement accredInstStatement = connect.prepareStatement(accredQuery);
            accredInstStatement.setInt(1, instructorid);
            ResultSet accredInstRes = accredInstStatement.executeQuery();
            
            while(accredInstRes.next()) {
            	Accreditation currAcred = buildAccreditation(accredInstRes);
            	
            	accreditations.add(currAcred);
            }
            
            accredInstRes.close();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		
		return accreditations;
	}
	
	public List<Booking> findBookingsBySkier(int skierId) {
	    List<Booking> bookings = new ArrayList<>();
	    
	    try {
	        // Requête SQL modifiée pour récupérer uniquement les bookings d'un seul skieur
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
	                + "INNER JOIN Accreditation a ON a.accreditationid = lt.accreditationid "
	                + "WHERE s.skierid = ?";  // Filtre sur le skierid
	        
	        PreparedStatement stmt = connect.prepareStatement(query);
	        stmt.setInt(1, skierId);  // On paramètre la requête avec le skierId
	        
	        ResultSet res = stmt.executeQuery();
	        
	        while (res.next()) {
	            // Récupération des informations sur le skieur
	            String skierName = res.getString("skier_name");
	            String skierSurname = res.getString("skier_surname");
	            LocalDate skierDob = res.getDate("skier_dob").toLocalDate();
	            int skierPhone = Integer.parseInt(res.getString("skier_phone"));
	            
	            // Récupération des informations sur l'instructeur
	            int instructorId = res.getInt("instructorid");
	            String instructorName = res.getString("instructor_name");
	            String instructorSurname = res.getString("instructor_surname");
	            int instructorPhone = Integer.parseInt(res.getString("instructor_phone"));
	            LocalDate instructorDob = res.getDate("instructor_dob").toLocalDate();
	            
	            // Récupération des accréditations de l'instructeur
	            List<Accreditation> accreditations = getAccredsInstructor(instructorId);
	            
	            Instructor currInst = new Instructor(instructorName, instructorSurname, instructorDob, instructorPhone, accreditations.get(0));
	            
	            for (int i = 1; i < accreditations.size(); i++) {
	                currInst.addAccreditation(accreditations.get(i));
	            }
	            
	            // Récupération du type de leçon
	            int accredid = res.getInt("accreditationid");
	            String accredName = res.getString("accred_name");
	            Accreditation ltAccred = new Accreditation(accredid, accredName);
	            LessonType currLessonType = buildLessonType(res, ltAccred);
	            
	            // Récupération de la leçon
	            Lesson currLesson = buildLesson(res, currLessonType, currInst);
	            
	            // Récupération du skieur existant ou création d'un nouveau skieur
	            Skier currSkier = new Skier(skierId, skierName, skierSurname, skierDob, skierPhone);
	            
	            // Récupération de la période
	            Period currPeriod = buildPeriod(res);
	            
	            // Création du booking
	            Booking currBooking = buildBooking(res, currSkier, currInst, currLesson, currPeriod);
	            
	            // Ajout du booking à la liste du skieur
	            bookings.add(currBooking);
	        }
	        
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    
	    return bookings;  // Retourne la liste des bookings pour un seul skieur
	}

}