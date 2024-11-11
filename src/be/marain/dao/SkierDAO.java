package be.marain.dao;
import java.sql.Connection;
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
		return null;
	}
}
