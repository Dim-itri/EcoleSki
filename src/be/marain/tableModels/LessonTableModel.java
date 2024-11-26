package be.marain.tableModels;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import be.marain.classes.Lesson;

public class LessonTableModel extends AbstractTableModel{
	private static final long serialVersionUID = 1L;
	private String[] columnsName = {"Id", "Min. réservations", "Max. réservations", "Instructeur", "Type de leçon", "Début", "Fin", "Individuelle", "Durée"};
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
	
	public void updateLesson(int rowIndex, Lesson updatedLesson) throws IndexOutOfBoundsException{
		if(rowIndex >= 0  && rowIndex < lessons.size()) {
			lessons.set(rowIndex, updatedLesson);
			fireTableRowsUpdated(rowIndex, rowIndex);
		}else {
			throw new IndexOutOfBoundsException("Impossible de modifier la leçon : " + rowIndex);
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
	
	public Lesson getLessonAt(int rowIndex) {
		return lessons.get(rowIndex);
	}
	
	public void deleteLesson(int rowIndex) {
		if(rowIndex >= 0 && rowIndex < lessons.size()) {
			lessons.remove(rowIndex);
			fireTableRowsDeleted(rowIndex, rowIndex);
		}else {
			throw new IllegalArgumentException("Impossible de supprimer la leçon à l'index : " + rowIndex);
		}		
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
		case 5:
			return lesson.getStartHour();
		case 6:
			return lesson.getEndHour();
		case 7:
			return lesson.getIsIndividual();
		case 8:
			return lesson.getDuration();
		default:
			return null;
		}
	}

}
