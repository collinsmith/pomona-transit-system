package edu.csupomona.cs.cs435.project4;

import java.sql.Time;

public class TimeInterval extends TransitTable<TimeInterval> {
	protected Time departure;
	protected Time arrival;

	@Override
	public TimeInterval copy() {
		return new TimeInterval(departure, arrival);
	}

	public TimeInterval(Time departure, Time arrival) {
		this.departure = departure;
		this.arrival = arrival;
	}

	@Override
	public String toString() {
		return departure + " to " + arrival;
	}
}
