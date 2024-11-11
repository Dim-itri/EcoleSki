package be.marain.dao;

import java.sql.Connection;
import java.util.List;

import be.marain.classes.LessonType;

public class LessonTypeDAO extends DAO<LessonType>{
	public LessonTypeDAO(Connection conn) {
		super(conn);
	}
	
	@Override
	public boolean create(LessonType obj) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean delete(LessonType obj) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean update(LessonType obj) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public LessonType find(int id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<LessonType> findAll() {
		// TODO Auto-generated method stub
		return null;
	}
}
