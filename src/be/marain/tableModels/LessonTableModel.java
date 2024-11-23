package be.marain.tableModels;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import be.marain.classes.Instructor;
import be.marain.classes.Lesson;

public class LessonTableModel extends AbstractTableModel{
	private String[] columnsName = {"Id", "Min. réservations", "Max. réservations", "Instructeur", "Type de leçon"};
	private List<Lesson> lessons;
	
	public LessonTableModel(List<Lesson> lessons) {
		if(lessons != null) {
			this.lessons = lessons;
		}else {
			this.lessons = new ArrayList<Lesson>();
		}
	}
	
	public void addLesson(Lesson newLesson) throws NullPointerException{
		if(newLesson != null) {
			lessons.add(newLesson);
			fireTableRowsInserted(lessons.size()-1, lessons.size()-1);
		}else {
			throw new NullPointerException("Aucune leçon à créer.");
		}
	}
	
	@Override
	public int getRowCount() {
		return lessons.size();
	}

	@Override
	public int getColumnCount() {
		return columnsName.length;
	}
	
	@Override
	public String getColumnName(int column) {
		return columnsName[column];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Lesson lesson = lessons.get(rowIndex);
		
		switch (columnIndex){
		case 0:
			return lesson.getLessonId();
		case 1:
			return lesson.getMinBookings();
		case 2:
			return lesson.getMaxBookings();
		case 3:
			return lesson.getInstructor().getName() + " " + lesson.getInstructor().getSurname();
		case 4:	
			return lesson.getLessonType().toString();
		default:
			return null;
		}
	}

}
