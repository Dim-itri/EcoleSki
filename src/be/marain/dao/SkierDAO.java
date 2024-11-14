package be.marain.dao;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import be.marain.classes.Skier;

public class SkierDAO extends DAO<Skier>{
	public SkierDAO(Connection conn) {
		super(conn);
	}
	
	@Override
	public boolean create(Skier newSkier) {
		return false;
	}
	
	@Override
	public boolean delete(Skier skier) {
		return false;
	}
	
	@Override
	public boolean update(Skier skier) {
		return false;
	}
	
	@Override
	public Skier find(int id) {
		return null;
	}
	
	@Override
	public List<Skier> findAll() {
		List<Skier> skiers = new ArrayList<>();
		
		try {
			ResultSet resultSet = this.connect.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY).executeQuery("SELECT * FROM Skier WHERE skierID = 1");
			
			while(resultSet.next()) {
				Skier currSkier = new Skier(resultSet.getInt("skierId"), resultSet.getString("name"), resultSet.getString("surname"), resultSet.getDate("dateOfBirth").toLocalDate(), resultSet.getInt("phoneNumber"));
				skiers.add(currSkier);
			}	
		}catch (SQLException e) {
			e.printStackTrace();
		}
	
		return skiers;
	}
}
