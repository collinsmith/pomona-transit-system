package edu.csupomona.cs.cs435.project4;

import java.awt.Component;

public class TripOffering extends TransitTable<TripOffering> {
	protected Trip trip;
	protected Bus bus;
	protected Driver driver;
	protected TripDate date;
	protected TimeInterval interval;

	@Override
	public TripOffering copy() {
		return new TripOffering(trip, bus, driver, date, interval);
	}

	public TripOffering(Trip trip, Bus bus, Driver driver, TripDate date, TimeInterval interval) {
		this.trip = trip;
		this.bus = bus;
		this.driver = driver;
		this.date = date;
		this.interval = interval;
	}

	public TripOffering(Component parent, TripOffering t) {
		this(new Trip(parent, t.trip.id, new Stop(t.trip.origin.id, t.trip.origin.address), new Stop(t.trip.destination.id, t.trip.destination.address)),
		     new Bus(t.bus.id, t.bus.model, t.bus.year),
		     new Driver(t.driver.name, t.driver.phoneNumber),
		     new TripDate(t.date.date),
		     new TimeInterval(t.interval.departure, t.interval.arrival));
	}

	@Override
	public String toString() {
		return trip + " in a " + bus + " driven by " + driver + " on " + date + " from " + interval;
	}
}
