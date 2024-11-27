package be.marain.classes;

import java.util.List;

import be.marain.dao.LessonTypeDAO;

public class LessonType {
	private int ltId;
	private String level;
	private double price;
	private Accreditation accreditation;
	private int minAge;
	private int maxAge;
	
	public static List<LessonType> getAllLessonTypes(LessonTypeDAO dao){
		return dao.findAll();
	}
	
	public String getLevel() {
		return level;
	}

	public int getLtId() {
		return ltId;
	}
	
	public int getMaxAge() {
		return maxAge;
	}
	
	public int getMinAge() {
		return minAge;
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
	
	public void setMaxAge(int maxAge) {
		this.maxAge = maxAge;
	}
	
	public void setMinAge(int minAge) {
		this.minAge = minAge;
	}
	
	public Accreditation getAccreditation() {
		return accreditation;
	}

	public LessonType(int id, String level, double price, Accreditation accreditation, int minAge, int maxAge) {
		setLevel(level);
		setLtId(id);
		setPrice(price);
		setAccreditation(accreditation);
		setMinAge(minAge);
		setMaxAge(maxAge);
	}
	
	@Override
	public String toString() {
		return "Niveau : " + level + ", Prix : " + price;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj != null && obj.getClass() == this.getClass() && ((LessonType)obj).getLtId() == ltId) {
			return true;
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return Integer.hashCode(ltId);
	}
}