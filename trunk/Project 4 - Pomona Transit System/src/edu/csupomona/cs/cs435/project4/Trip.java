package edu.csupomona.cs.cs435.project4;

import java.awt.Component;
import java.sql.SQLException;
import java.sql.Time;
import java.util.LinkedList;

public class Trip extends TransitTable<Trip> {
	protected int id;
	protected Stop origin;
	protected Stop destination;

	protected long totalTime;
	protected LinkedList<Path> stopList;

	@Override
	public Trip copy() {
		return new Trip(null, id, origin, destination);
	}

	public Trip(Component parent, Stop origin, Stop destination) {
		this.id = getNextTripID(parent);
		this.origin = origin;
		this.destination = destination;
	}

	public Trip(Component parent, int tripID, Stop origin, Stop destination) {
		this.id = tripID;
		this.origin = origin;
		this.destination = destination;

		stopList = new LinkedList<Path>();
		cacheStopList(parent);
	}

	@Override
	public String toString() {
		return origin.address + " to " + destination.address;
	}

	public static int getNextTripID(Component parent) {
		int retValue = 0;
		SQLQuery query = SQLQuery.executeQuery(parent, "select max(t.TripID) from Trip t;");
		try {
			if (query.rs.next()) {
				retValue = query.rs.getInt(1)+1;
			} else {
				retValue = 1;
			}
		} catch (SQLException e) {
			SQLQuery.throwError(parent, e);
		} finally {
			query.close();
		}

		return retValue;
	}

	public static LinkedList<Trip> getTripList(Component parent) {
		LinkedList<Trip> tripList = new LinkedList<Trip>();
		SQLQuery query = SQLQuery.executeQuery(parent, "select t.TripID, t.OriginID, s1.Address, t.DestinationID, s2.Address "
									   + "from Trip t, Stop s1, Stop s2 "
									   + "where t.OriginID = s1.StopID AND t.DestinationID = s2.StopID;");

		Trip trip = null;
		try {
			while (query.rs.next()) {
				Stop origin = new Stop(query.rs.getInt(2), query.rs.getString(3));
				Stop destination = new Stop(query.rs.getInt(4), query.rs.getString(5));
				tripList.add(new Trip(parent, query.rs.getInt(1), origin, destination));
			}
		} catch (SQLException e) {
			SQLQuery.throwError(parent, e);
		} finally {
			query.close();
		}

		return tripList;
	}

	public static Trip[] getTripArray(Component parent) {
		return getTripList(parent).toArray(new Trip[0]);
	}

	public void addPath(Component parent, final Stop stop) {
		String sql;

		if (destination == null) {
			sql = String.format("select d.Distance "
						+ "from Distance d "
						+ "where d.OriginID = %d AND d.DestinationID = %d;", origin.id, stop.id);
		} else {
			sql = String.format("select d.Distance "
						+ "from Distance d "
						+ "where d.OriginID = %d AND d.DestinationID = %d;", destination.id, stop.id);
		}

		SQLQuery query = SQLQuery.executeQuery(parent, sql);
		try {
			if (query.rs.next()) {
				Time t = query.rs.getTime(1);
				long timeInMillis = (t.getHours()*3600 + t.getMinutes()*60)*1000;
				totalTime += timeInMillis;
				stopList.add(new Path(stop, timeInMillis));
				destination = stop;
			} else {
				FormTimeDistance timeForm;
				if (destination == null) {
					timeForm = new FormTimeDistance(parent, null, origin, stop);
				} else {
					timeForm = new FormTimeDistance(parent, null, destination, stop);
				}

				timeForm.addFormListener(new FormListener<Distance>() {
					@Override
					public void formCompleted(FormEvent<Distance> e) {
						Distance d = e.getCreatedObject();
						long time = d.getTimeMillis();
						String sql = String.format("insert into Distance (OriginID, DestinationID, Distance) "
										 + "values (%s, %s, '%s');",
										 d.origin.id, d.destination.id, SQLQuery.convertTimeCountToString(time));

						SQLQuery.executeUpdate(null, sql);
						totalTime += time;
						stopList.add(new Path(stop, time));
						destination = stop;
					}
				});

				timeForm.display();
			}
		} catch (SQLException e) {
			SQLQuery.throwError(parent, e);
		} finally {
			query.close();
		}
	}

	public void cacheStopList(Component parent) {
		totalTime = 0;
		stopList.clear();
		destination = null;

		String sql = String.format("select distinct t.StopNumber, t.StopID, s.Address "
						 + "from TripStopInfo t, Stop s "
						 + "where t.TripID = %d AND t.StopID = s.StopID "
						 + "order by t.StopNumber asc", id);

		SQLQuery query = SQLQuery.executeQuery(parent, sql);
		try {
			while (query.rs.next()) {
				addPath(parent, new Stop(query.rs.getInt(2), query.rs.getString(3)));
			}
		} catch (SQLException e) {
			SQLQuery.throwError(parent, e);
		} finally {
			query.close();
		}
	}
}
