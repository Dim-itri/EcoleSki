package be.marain.dao;

import java.sql.Connection;
import java.util.List;

import be.marain.classes.Lesson;

public class LessonDAO extends DAO<Lesson> {
	public LessonDAO(Connection conn) {
		super(conn);
	}

	@Override
	public boolean create(Lesson obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(Lesson obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean update(Lesson obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Lesson find(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Lesson> findAll() {
		// TODO Auto-generated method stub
		return null;
	}
}
