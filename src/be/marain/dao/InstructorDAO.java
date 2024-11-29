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

import javax.swing.JOptionPane;


import be.marain.classes.Accreditation;
import be.marain.classes.Instructor;

public class InstructorDAO extends DAO<Instructor> {
	public InstructorDAO(Connection conn) {
		super(conn);
	}

	@Override
	public boolean create(Instructor newInstructor) {
		boolean success = true;
		
		try {
			String[] returnCols = {"instructorid"}; 
			String query = "Insert into instructor (name, surname, phonenumber, dateofbirth) "
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
			query = "INSERT INTO instructoraccred (instructorid, accreditationid) VALUES (?, ?)";
			statement = connect.prepareStatement(query);
			List<Accreditation> accreditations = newInstructor.getInstructorAccreditations();
			
			for(Accreditation curr:accreditations) {
				statement.setInt(1, newInstructor.getPersonId());
				statement.setInt(2, curr.getAccreditationId());
				statement.addBatch();
			}
			
			statement.executeBatch();
			statement.close();
		}catch (SQLException e) {
			success = false;
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
			
			statement.close();
		}catch (SQLException e) {
			success = false;
			e.printStackTrace();
		}
		
		return success;
	}

	@Override
	public boolean update(Instructor instructor) {
		boolean success;
		
		try {
			String query = "UPDATE instructor SET name = ?, surname = ?, "
					+ "dateofbirth = ?, phonenumber = ? WHERE instructorid = ?";
			
			PreparedStatement statement = connect.prepareStatement(query);
			
			statement.setString(1, instructor.getName());
			statement.setString(2, instructor.getSurname());
			statement.setDate(3, java.sql.Date.valueOf(instructor.getDateOfBirth()));
			statement.setInt(4, instructor.getPhoneNumber());
			statement.setInt(5, instructor.getPersonId());
			
			success = statement.executeUpdate() > 0;
			
			query = "DELETE FROM instructoraccred WHERE instructorid = ?";
			statement = connect.prepareStatement(query);
			statement.setInt(1, instructor.getPersonId());
			
			success = statement.executeUpdate() > 0;
			
			query = "INSERT INTO instructoraccred(instructorid, accreditationid) VALUES (?, ?)";
			statement = connect.prepareStatement(query);
					
			for(Accreditation curr : instructor.getInstructorAccreditations()) {
				statement.setInt(1, instructor.getPersonId());
				statement.setInt(2, curr.getAccreditationId());
				statement.addBatch();
			}
			
			statement.executeBatch();
			statement.close();
		}catch (SQLException e) {
			success = false;
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
		return success;
	}

	@Override
	public Instructor find(int id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<Instructor> findAll() {
	    List<Instructor> instructors = new ArrayList<>();
	    Map<Integer, Instructor> instructorMap = new HashMap<>(); // Map temporaire pour éviter les doublons d'instructeurs

	    String query = """
	        SELECT i.instructorid, i.name, i.surname, i.dateofbirth, i.phonenumber,
	               a.accreditationid AS accred_id, a.name AS accred_name
	        FROM instructor i
	        LEFT JOIN InstructorAccred ia ON i.instructorid = ia.instructorid
	        LEFT JOIN accreditation a ON ia.accreditationid = a.accreditationid
	        ORDER BY i.instructorid
	    """;

	    try (PreparedStatement stmt = connect.prepareStatement(query);
	         ResultSet rs = stmt.executeQuery()) {

	        while (rs.next()) {
	            int instructorId = rs.getInt("instructorid");

	            // Si l'instructeur n'est pas encore dans la Map, on le crée
	            Instructor instructor = instructorMap.computeIfAbsent(instructorId, id -> {
	                try {
	                    String name = rs.getString("name");
	                    String surname = rs.getString("surname");
	                    LocalDate dob = rs.getDate("dateofbirth").toLocalDate();
	                    int phone = rs.getInt("phonenumber");
	                    return new Instructor(id, name, surname, dob, phone, null);
	                } catch (SQLException e) {
	                    throw new RuntimeException(e); // Rejeter l'erreur SQL dans ce contexte
	                }
	            });

	            // Ajouter l'accréditation si elle existe
	            int accredId = rs.getInt("accred_id");
	            if (!rs.wasNull()) { // Vérifier si l'accréditation est présente
	                String accredName = rs.getString("accred_name");
	                Accreditation accreditation = new Accreditation(accredId, accredName);
	                instructor.addAccreditation(accreditation);
	            }
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    // Récupérer la liste des instructeurs à partir de la Map
	    instructors.addAll(instructorMap.values());
	    return instructors;
	}
}
