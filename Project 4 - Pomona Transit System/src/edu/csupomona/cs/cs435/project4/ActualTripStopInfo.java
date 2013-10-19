package edu.csupomona.cs.cs435.project4;

public class ActualTripStopInfo {
	protected TripOffering tripOffering;
	protected TimeInterval timeInterval;
	protected Stop stop;
	protected int stopNumber;
	protected PassengerManifest passengers;

	public ActualTripStopInfo(int stopNumber, TripOffering tripOffering, TimeInterval timeInterval, Stop stop, PassengerManifest passengers) {
		this.stopNumber = stopNumber;
		this.tripOffering = tripOffering;
		this.timeInterval = timeInterval;
		this.stop = stop;
		this.passengers = passengers;
	}
}
