package be.marain.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import be.marain.classes.Accreditation;
import be.marain.classes.Instructor;

public class InstructorDAO extends DAO<Instructor> {
	public InstructorDAO(Connection conn) {
		super(conn);
	}

	@Override
	public boolean create(Instructor newInstructor) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(Instructor instructor) {
		// TODO Auto-generated method stub
		return false;
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
