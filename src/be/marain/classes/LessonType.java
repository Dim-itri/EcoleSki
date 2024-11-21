package be.marain.classes;

public class LessonType {
	private int ltId;
	private String level;
	private int price;

	public String getLevel() {
		return level;
	}

	public int getLtId() {
		return ltId;
	}

	public int getPrice() {
		return price;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public void setLtId(int ltId) {
		this.ltId = ltId;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public LessonType(int id, String level, int price) {
		setLevel(level);
		setLtId(id);
		setPrice(price);
	}
	
	@Override
	public String toString() {
		return "Niveau : " + level + ", Prix : " + price;
	}
}