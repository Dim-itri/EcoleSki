package be.marain.tableModels;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import be.marain.classes.Booking;
import be.marain.classes.Lesson;
import be.marain.jframes.LessonsJFrame;

public class BookingTableModel extends AbstractTableModel{
	private static final long serialVersionUID = 1L;
	String[] columnNames = {"Id", "Date","Instructeur", "Skieur", "Leçon", "Période", "Prix"};
	List<Booking> bookings;
	
	public void addBooking(Booking newBooking) {
		if(newBooking != null) {
			bookings.add(newBooking);
			fireTableRowsInserted(getRowCount()-1, getRowCount()-1);
		}
	}
	
	public void deleteBooking(int rowIndex) {
		if(rowIndex >= 0 && rowIndex < bookings.size()) {
			bookings.remove(rowIndex);
			fireTableRowsDeleted(rowIndex, rowIndex);
		}else {
			throw new IllegalArgumentException("Impossible de supprimer la réservation à l'index : "+ rowIndex);
		}
	}
	
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
	
	public Booking getBookingAt(int rowIndex) {
		return bookings.get(rowIndex);
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Booking booking = bookings.get(rowIndex);
		
		switch (columnIndex){
		case 0: 
			return booking.getBookingId();
		case 1:
			return booking.getBookingDate().toString();
		case 2:
			return booking.getInstructor().toString();
		case 3:
			return booking.getSkier().toString();
		case 4:
			return booking.getLesson().toString();
		case 5:
			return booking.getPeriod().toString();
		case 6:
			return booking.calculatePrice();
		default:
			throw new IllegalArgumentException("Unexpected value: " + columnIndex);
		}			
	}
}
