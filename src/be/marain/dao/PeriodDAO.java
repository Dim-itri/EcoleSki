package be.marain.dao;

import java.security.interfaces.RSAKey;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import be.marain.classes.Accreditation;
import be.marain.classes.Booking;
import be.marain.classes.Instructor;
import be.marain.classes.Lesson;
import be.marain.classes.LessonType;
import be.marain.classes.Period;
import be.marain.classes.Skier;

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
				int periodid = res.getInt("periodid");
				LocalDate startDate = res.getDate("startdate").toLocalDate();
				LocalDate enddate = res.getDate("enddate").toLocalDate();
				boolean isVacation;
            	
            	if(res.getString("isVacation").charAt(0) == 'Y') {
                	isVacation = true;
                }else {
                	isVacation = false;
                }
				
				Period period = new Period(periodid, startDate, enddate, isVacation);
				
				periods.add(period);
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		
		return periods;
	}

}
