package be.marain.tableModels;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import be.marain.classes.Skier;

public class SkierTableModel extends AbstractTableModel {
	private String[] columnsName = { "ID", "Prénom", "Nom", "Date de naissance", "Téléphone" };
	private List<Skier> skiers;

	public SkierTableModel(List<Skier> skiers) {
		if (skiers != null) {
			this.skiers = skiers;
		}
	}

	@Override
	public int getRowCount() {
		return skiers.size();
	}

	@Override
	public int getColumnCount() {
		return columnsName.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Skier skier = skiers.get(rowIndex);

		switch (columnIndex) {
		case 0:
			return skier.getPersonId();
		case 1:
			return skier.getName();
		case 2:
			return skier.getSurname();
		case 3:
			return skier.getDateOfBirth();
		case 4:
			return skier.getPhoneNumber();
		default:
			return null;
		}
	}

	@Override
	public String getColumnName(int column) {
		return columnsName[column];
	}
	
	public void addSkier(Skier newSkier) {
		skiers.add(newSkier);
		fireTableRowsInserted(skiers.size() - 1, skiers.size() - 1);
	}
	
	public void deleteSkier(int rowIndex) throws IndexOutOfBoundsException{
		if(rowIndex >= 0 && rowIndex < skiers.size()) {
			skiers.remove(rowIndex);
			fireTableRowsDeleted(rowIndex, rowIndex);
		}else {
			throw new IndexOutOfBoundsException("Impossible de supprimer le skieur à l'index : " + rowIndex);
		}
	}
	
	public void updateSkier(int rowIndex, Skier updatedSkier) {
		if(rowIndex >= 0 && rowIndex < skiers.size()) {
			skiers.set(rowIndex, updatedSkier);
			fireTableRowsUpdated(rowIndex, rowIndex);
		}else {
			throw new IndexOutOfBoundsException("Impossible de modifier le skieur " + rowIndex);
		}
	}
	
	public Skier getSkierAt(int rowIndex) {
		return skiers.get(rowIndex);
	}
}
