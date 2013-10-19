package edu.csupomona.cs.cs435.project4;

import java.awt.Component;
import java.sql.SQLException;
import java.util.LinkedList;

public class Stop extends TransitTable<Stop> {
	protected int id;
	protected String address;

	@Override
	public Stop copy() {
		return new Stop(id, address);
	}

	public Stop(int stopID, String address) {
		this.id = stopID;
		this.address = address;
	}

	@Override
	public String toString() {
		return address;
	}

	public static int getNextStopID(Component parent) {
		int retValue = 0;
		SQLQuery query = SQLQuery.executeQuery(parent, "select max(s.StopID) from Stop s;");
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

	public static LinkedList<Stop> getStopList(Component parent) {
		LinkedList<Stop> stopList = new LinkedList<Stop>();
		SQLQuery query = SQLQuery.executeQuery(parent, "select s.StopID, s.Address from Stop s;");
		try {
			while (query.rs.next()) {
				stopList.add(new Stop(query.rs.getInt(1), query.rs.getString(2)));
			}
		} catch (SQLException e) {
			SQLQuery.throwError(parent, e);
		} finally {
			query.close();
		}

		return stopList;
	}

	public static Stop[] getStopArray(Component parent) {
		return getStopList(parent).toArray(new Stop[0]);
	}
}
