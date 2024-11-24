package be.marain.tableModels;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import be.marain.classes.Booking;

public class BookingTableModel extends AbstractTableModel{
	String[] columnNames = {"Id", "Durée", "Individuelle"};
	List<Booking> bookings;
	
	@Override
	public int getRowCount() {
		return bookings.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}
}
