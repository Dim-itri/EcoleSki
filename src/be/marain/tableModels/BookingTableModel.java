package be.marain.tableModels;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import be.marain.classes.Booking;

public class BookingTableModel extends AbstractTableModel{
	private static final long serialVersionUID = 1L;
	String[] columnNames = {"Id", "Durée", "Individuelle", "Instructeur", "Skieur", "Leçon", "Période"};
	List<Booking> bookings;
	
	public BookingTableModel(List<Booking> bookings) {
		if(bookings != null) {
			this.bookings = bookings;
		}
	}
	
	@Override
	public int getRowCount() {
		return bookings.size();
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
		Booking booking = bookings.get(rowIndex);
		
		switch (columnIndex){
		case 0: 
			return booking.getBookingId();
		case 1:
			return booking.getDuration();
		case 2:
			return booking.getIndividual();
		case 3:
			return booking.getInstructor().toString();
		case 4:
			return booking.getSkier().toString();
		case 5:
			return booking.getLesson().toString();
		case 6:
			return booking.getPeriod().toString();
		default:
			throw new IllegalArgumentException("Unexpected value: " + columnIndex);
		}			
	}
}
