package be.marain.tableModels;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

import be.marain.classes.Instructor;

public class InstructorTableModel extends AbstractTableModel{
	private static final long serialVersionUID = 1L;
	private final String[] columnNames = {"Id", "Prénom", "Nom", "Téléphone", "Date de naissance", "Accréditations"};
	private List<Instructor> instructors;
	
	public InstructorTableModel(List<Instructor> instructorList) {
		 if (instructorList == null) {
	            this.instructors = new ArrayList<>();
	            System.out.println("Instructor list is null. Initializing with an empty list.");
	        } else {
	            this.instructors = instructorList;
	            System.out.println("Instructor list received with size: " + instructors.size());
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

}
