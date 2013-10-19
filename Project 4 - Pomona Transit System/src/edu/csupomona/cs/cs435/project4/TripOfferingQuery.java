package edu.csupomona.cs.cs435.project4;

import java.sql.Date;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedList;

public class TripOfferingQuery implements Constants {
	protected Trip trip;
	protected TripDate date;

	public TripOfferingQuery(Trip trip, TripDate date) {
		this.trip = trip;
		this.date = date;
	}

	public void performQuery(TransitTab tableOwner) {
		performQuery(tableOwner, this);
	}

	public static void performQuery(TransitTab tableOwner, TripOfferingQuery toq) {
			String sql = String.format("select s1.Address as Origin, s2.Address as Destination,"
							 + "		to.TripDate as [Date], to.ScheduledDeparture as [Scheduled Departure],"
							 + "		to.ScheduledArrival as [Scheduled Arrival], d.DriverName as [Driver], to.BusID "
							 + "from Trip t, TripOffering to, Stop s1, Stop s2, Driver d "
							 + "where to.TripID = t.TripID and to.TripDate like '%s' "
							 + "		and s1.StopID = t.OriginID and t.OriginID = %d "
							 + "		and s2.StopID = t.DestinationID and t.DestinationID = %d "
							 + "		and to.DriverName = d.DriverName;",
							   SQLQuery.convertDateToString(toq.date.date), toq.trip.origin.id, toq.trip.destination.id);

			String[] row = null;
			String[] columnNames = null;
			LinkedList<String[]> rows = new LinkedList<String[]>();
			SQLQuery query = SQLQuery.executeQuery(tableOwner, sql);
			try {
				ResultSetMetaData metaData = query.rs.getMetaData();
				columnNames = new String[metaData.getColumnCount()];
				for (int i = 0; i < columnNames.length; i++) {
					columnNames[i] = metaData.getColumnName(i+1);
				}

				while (query.rs.next()) {
					row = new String[] {
						query.rs.getString(1),
						query.rs.getString(2),
						query.rs.getDate(3).toString(),
						SQLQuery.convertTimeToString(new Date(query.rs.getTime(4).getTime())),
						SQLQuery.convertTimeToString(new Date(query.rs.getTime(5).getTime())),
						query.rs.getString(6),
						query.rs.getString(7)
					};

					rows.add(row);
				}
			} catch (SQLException ex) {
				SQLQuery.throwError(tableOwner, ex);
			} finally {
				query.close();
			}

			tableOwner.changeTable(tableOwner.createTable("All trips on " + SQLQuery.convertDateToString(toq.date.date) + " from " + toq.trip.origin.address + " to " + toq.trip.destination.address,
										    rows.toArray(new String[0][]), columnNames));
	}
}
