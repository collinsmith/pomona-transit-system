package edu.csupomona.cs.cs435.project4;

import java.sql.Date;

public class TripDate extends TransitTable<TripDate> {
	protected Date date;

	@Override
	public TripDate copy() {
		return new TripDate(date);
	}

	public TripDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return date.toString();
	}
}
