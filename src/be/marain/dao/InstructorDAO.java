package be.marain.dao;

import java.lang.invoke.CallSite;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.toedter.calendar.IDateEditor;

import be.marain.classes.Accreditation;
import be.marain.classes.Booking;
import be.marain.classes.Instructor;
import be.marain.classes.Lesson;

public class InstructorDAO extends DAO<Instructor> {
	public InstructorDAO(Connection conn) {
		super(conn);
	}

	@Override
	public boolean create(Instructor newInstructor) {
		boolean success = true;
		
		try {
			String[] returnCols = {"instructorid"}; 
			String query = "Insert into instructor (name, surname, phonenumber, dateofbirth)"
					+ "VALUES (?, ?, ?, ?)";
			
			PreparedStatement statement = connect.prepareStatement(query, returnCols);
			
			statement.setString(1, newInstructor.getName());
			statement.setString(2, newInstructor.getSurname());
			statement.setInt(3, newInstructor.getPhoneNumber());
			statement.setDate(4, java.sql.Date.valueOf(newInstructor.getDateOfBirth()));
			
			success = statement.executeUpdate() > 0;
			
			if (success) {
				ResultSet generatedKeys = statement.getGeneratedKeys();
				
				if(generatedKeys.next()) {
					int generatedId = generatedKeys.getInt(1);
					newInstructor.setPersonId(generatedId);
				}
			}
			
			//Creating the link accreditation-Instructor in DB
			StringBuilder queryBuilder = new StringBuilder(
					"INSERT INTO instructoraccred (instructorid, accreditationid) VALUES ");
			List<Accreditation> accreditations = newInstructor.getInstructorAccreditations();
			for(int i=0;i<accreditations.size();i++) {
				queryBuilder.append("(?, ?)");
				if(i<accreditations.size()-1) {
					queryBuilder.append(", ");
				}
			}
			
			statement = connect.prepareStatement(queryBuilder.toString());
			
			int paramIndex = 1;
			
			for(Accreditation currAccred:accreditations) {
				statement.setInt(paramIndex++, newInstructor.getPersonId());
				statement.setInt(paramIndex++, currAccred.getAccreditationId());
			}
			
			success = statement.executeUpdate() > 0;
			statement.close();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		
		return success;
	}

	@Override
	public boolean delete(Instructor instructor) {
		boolean success;
		
		try {
			String query = "DELETE FROM instructor WHERE instructorid = ?";
			
			PreparedStatement statement = connect.prepareStatement(query);
			statement.setInt(1, instructor.getPersonId());
			
			success = statement.executeUpdate() > 0;			
		}catch (SQLException e) {
			success = false;
			e.printStackTrace();
		}
		
		return success;
	}

	@Override
	public boolean update(Instructor instructor) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Instructor find(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Instructor> findAll() {
		List<Instructor> instructors = new ArrayList<>();
		
		try {
			ResultSet resultSet = this.connect
					.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)
					.executeQuery("SELECT * FROM instructor");
			
			while(resultSet.next()) {
				int currId = resultSet.getInt("instructorid");
				String currNameString = resultSet.getString("name");
				String currSurnameString = resultSet.getString("surname");
				LocalDate currDobDate = resultSet.getDate("dateofbirth").toLocalDate();
				int currPhoneNumber = resultSet.getInt("phonenumber");
				List<Accreditation> currAccreds = new ArrayList<Accreditation>();
				
				//Getting accreditations
				PreparedStatement statement = connect.prepareStatement
								("SELECT * FROM accreditation a "
								+ "INNER JOIN InstructorAccred ia ON ia.accreditationid = a.accreditationid "
								+ "WHERE ia.instructorid = ?");
				
				statement.setInt(1, currId);
				
				ResultSet accredRes = statement.executeQuery();
				
				while(accredRes.next()) {
					Accreditation currAccreditation = new Accreditation(accredRes.getInt("accreditationId"), accredRes.getString("name"));
					
					currAccreds.add(currAccreditation);
				}
				
				Instructor currInstructor = new Instructor(currId, currNameString, currSurnameString, currDobDate, currPhoneNumber, currAccreds.isEmpty() ? 
		                new Accreditation(0, "Accréditation par défaut") : currAccreds.get(0));
				
				for (int i = 1; i < currAccreds.size(); i++) {
	                currInstructor.addAccreditation(currAccreds.get(i));
	            }
								
				instructors.add(currInstructor);
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		
		return instructors;
	}
}
