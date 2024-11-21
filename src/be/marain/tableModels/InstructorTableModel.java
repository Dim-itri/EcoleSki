package be.marain.tableModels;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import be.marain.classes.Instructor;

public class InstructorTableModel extends AbstractTableModel{
	private static final long serialVersionUID = 1L;
	private final String[] columnNames = {"Id", "Prénom", "Nom", "Téléphone", "Date de naissance", "Accréditations"};
	private List<Instructor> instructors;
	
	public InstructorTableModel(List<Instructor> instructorList) {
		 if (instructorList == null) {
	            this.instructors = new ArrayList<>();
	        } else {
	            this.instructors = instructorList;
	        }
	}
	
	@Override
	public int getRowCount() {
		return instructors.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}
	
	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Instructor instructor = instructors.get(rowIndex);
		
		switch (columnIndex){
		case 0:
			return instructor.getPersonId();
		case 1:
			return instructor.getName();
		case 2:
			return instructor.getSurname();
		case 3:
			return instructor.getPhoneNumber();
		case 4:	
			return instructor.getDateOfBirth();
		case 5:
			return instructor.getInstructorAccreditationString();
		default:
			return null;
		}
	}
	
	public void addInstructor(Instructor newInstructor) {
		if(newInstructor != null) {
			instructors.add(newInstructor);
			fireTableRowsInserted(instructors.size()-1, instructors.size()-1);
		}else {
			throw new NullPointerException("Aucun Instructeur à créer");
		}
	}
	
	public Instructor getInstructorAt(int rowIndex) {
		return instructors.get(rowIndex);
	}
	
	public void deleteInstructor(int rowIndex) {
		if(rowIndex >= 0 && rowIndex < instructors.size()) {
			instructors.remove(rowIndex);
			fireTableRowsDeleted(rowIndex, rowIndex);
		}else {
			throw new IndexOutOfBoundsException("Impossible de supprimer le skieur à l'index : " + rowIndex);
		}
	}
}
