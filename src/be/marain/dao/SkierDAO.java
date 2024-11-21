package be.marain.dao;

import java.beans.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

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

			while (resultSet.next()) {
				Skier currSkier = new Skier(resultSet.getInt("skierId"), resultSet.getString("name"),
						resultSet.getString("surname"), resultSet.getDate("dateOfBirth").toLocalDate(),
						resultSet.getInt("phoneNumber"));
				skiers.add(currSkier);
			}
			
			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return skiers;
	}
}
