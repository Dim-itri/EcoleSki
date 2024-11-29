package be.marain.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import be.marain.classes.Period;

public class PeriodDAO extends DAO<Period>{

	public PeriodDAO(Connection conn) {
		super(conn);
	}

	@Override
	public boolean create(Period obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean update(Period obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(Period obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Period find(int index) {
		return null;
	}

	@Override
	public List<Period> findAll() {
		List<Period> periods = new ArrayList<Period>();
		
		try {
			String query = "SELECT * FROM period";
			
			ResultSet res = this.connect.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(query);
			
			while(res.next()) {				
				Period period = buildPeriod(res);
				
				periods.add(period);
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		
		return periods;
	}

}
