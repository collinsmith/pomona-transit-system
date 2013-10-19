package edu.csupomona.cs.cs435.project4;

public class Path {
	protected Stop stop;
	protected long drivingTime;

	public Path(Stop stop, long drivingTime) {
		this.stop = stop;
		this.drivingTime = drivingTime;
	}
}
