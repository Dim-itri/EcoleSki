package be.marain.dao;

import java.sql.Connection;
import java.util.List;

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
		// TODO Auto-generated method stub
		return null;
	}

}
