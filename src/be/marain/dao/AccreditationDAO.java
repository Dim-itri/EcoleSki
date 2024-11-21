package be.marain.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import be.marain.classes.Accreditation;

public class AccreditationDAO extends DAO<Accreditation> {
	public AccreditationDAO(Connection conn) {
		super(conn);
	}

	@Override
	public boolean create(Accreditation obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(Accreditation obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean update(Accreditation obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Accreditation find(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Accreditation> findAll() {
		List<Accreditation> accreditations = new ArrayList<Accreditation>();
		
		try {
			String query = "SELECT * FROM accreditation";
			PreparedStatement statement = connect.prepareStatement(query);
			ResultSet res = statement.executeQuery();
			
			while(res.next()) {
				Accreditation currAcc = new Accreditation(res.getInt("accreditationid"), res.getString("Name"));
				accreditations.add(currAcc);
			}
			
			statement.close();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		
		return accreditations;
	}
}
