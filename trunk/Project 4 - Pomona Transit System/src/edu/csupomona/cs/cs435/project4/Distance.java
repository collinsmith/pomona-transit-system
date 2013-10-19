package edu.csupomona.cs.cs435.project4;

import java.sql.Time;

/**
 * Represents an object which encapsulates an origin,
 * destination, and the time (or other arbitrary distance)
 * between those two points.
 *
 * @author Collin Smith
 */
public class Distance {
	/**
	 * Stop representation for the originating location.
	 */
	protected Stop origin;

	/**
	 * Stop representation for the ending location
	 */
	protected Stop destination;

	/**
	 * Time representation for the distance between
	 * {@link Distance#origin} and {@link Distance#destination}.
	 */
	protected Time time;

	/**
	 * Constructor which initialized a {@link Distance} with specified
	 * values.
	 *
	 * @param origin		{@link Distance#origin}
	 * @param destination	{@link Distance#destination}
	 * @param time		{@link Distance#time}
	 */
	public Distance(Stop origin, Stop destination, Time time) {
		this.origin = origin;
		this.destination = destination;
		this.time = time;
	}

	/**
	 * Returns a long representation of {@link Distance#time}.
	 *
	 * @return			The number of hours and minutes converted
	 *				into milliseconds.
	 */
	public long getTimeMillis() {
		return (time.getHours()*3600 + time.getMinutes()*60)*1000;
	}
}
