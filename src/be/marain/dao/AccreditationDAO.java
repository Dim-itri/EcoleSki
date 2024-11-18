package be.marain.dao;

import java.sql.Connection;
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
		// TODO Auto-generated method stub
		return null;
	}
}
