package be.marain.classes;

import java.util.List;

import be.marain.dao.LessonTypeDAO;

public class LessonType {
	private int ltId;
	private String level;
	private double price;
	private Accreditation accreditation;
	
	public static List<LessonType> getAllLessonTypes(LessonTypeDAO dao){
		return dao.findAll();
	}
	
	public String getLevel() {
		return level;
	}

	public int getLtId() {
		return ltId;
	}

	public double getPrice() {
		return price;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public void setLtId(int ltId) {
		this.ltId = ltId;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	
	public void setAccreditation(Accreditation accreditation) {
		if(accreditation != null) {
			this.accreditation = accreditation;
		}
	}

	public LessonType(int id, String level, double price, Accreditation accreditation) {
		setLevel(level);
		setLtId(id);
		setPrice(price);
		setAccreditation(accreditation);
	}
	
	@Override
	public String toString() {
		return "Niveau : " + level + ", Prix : " + price;
	}
}