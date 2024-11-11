package be.marain.classes;

import java.time.LocalDate;

public class Period {
	private int periodId;
	private LocalDate startDate;
	private LocalDate endDate;
	private boolean isVacation;
	
	public LocalDate getEndDate() {
		return endDate;
	}
	
	public LocalDate getStartDate() {
		return startDate;
	}
	
	public int getPeriodId() {
		return periodId;
	}
	
	public boolean getVacation() {
		return isVacation;
	}
	
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	
	public void setPeriodId(int periodId) {
		this.periodId = periodId;
	}
	
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	
	public void setVacation(boolean isVacation) {
		this.isVacation = isVacation;
	}
	
	public Period(int id, LocalDate start, LocalDate end, boolean Vacation) {
		setEndDate(end);
		setPeriodId(id);
		setStartDate(start);
		setVacation(Vacation);
	}
}