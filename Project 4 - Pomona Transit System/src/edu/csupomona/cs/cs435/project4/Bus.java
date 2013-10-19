package edu.csupomona.cs.cs435.project4;

import java.awt.Component;
import java.sql.SQLException;
import java.util.LinkedList;

/**
 * This class represents a Bus. A Bus is comprised of an
 * index identifier, model and year of manufacture.
 *
 * @author Collin Smith
 */
public class Bus extends TransitTable<Bus> {
	/**
	 * Integer representation for the unique index of this
	 * Bus used for outside reference.
	 */
	protected int id;

	/**
	 * String representation for the model of this Bus.
	 */
	protected String model;

	/**
	 * String representation for the year this Bus was
	 * manufactured. The value of this variable should
	 * only be a maximum of 4 characters in YYYY format.
	 */
	protected String year;

	/**
	 * @see TransitTable#copy()
	 */
	@Override
	public Bus copy() {
		return new Bus(id, model, year);
	}

	/**
	 * Constructor which initialized a new {@link Bus} using the
	 * specified parameters as default values.
	 *
	 * @param busID	{@link Bus#id}
	 * @param model	{@link Bus#model}
	 * @param year	{@link Bus#year}
	 */
	public Bus(int busID, String model, String year) {
		this.id = busID;
		this.model = model;
		this.year = year;
	}

	/**
	 * Retrieves a String representation for this object which
	 * is then used within many GUI components.
	 *
	 * @return		A String representation of this object.
	 */
	@Override
	public String toString() {
		return year + " " + model;
	}

	/**
	 * Retrieves the next unique available {@link Bus#id}. This number
	 * is based off of the current {@code max_value + 1}.
	 *
	 * @param parent	Parent component to which this method is called
	 *			from. Used for error message displaying purposes.
	 * @return		The next unique {@link Bus#id}.
	 */
	public static int getNextBusID(Component parent) {
		int retValue = 0;
		SQLQuery query = SQLQuery.executeQuery(parent, "select max(b.BusID) from Bus b;");
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

	/**
	 * Retrieves a {@link LinkedList} representation for all {@link Bus}
	 * objects stored within the database;
	 *
	 * @param parent	Parent component to which this method is called
	 *			from. Used for error message displaying purposes.
	 * @return		A LinkedList representation for all Buses.
	 */
	public static LinkedList<Bus> getBusList(Component parent) {
		LinkedList<Bus> busList = new LinkedList<Bus>();
		SQLQuery query = SQLQuery.executeQuery(parent, "select b.BusID, b.Model, b.Year from Bus b;");

		try {
			while (query.rs.next()) {
				busList.add(new Bus(query.rs.getInt(1), query.rs.getString(2), query.rs.getString(3)));
			}
		} catch (SQLException e) {
			SQLQuery.throwError(parent, e, SQL_ERR_QUERY);
		} finally {
			query.close();
		}

		return busList;
	}

	/**
	 * Using {@link Bus#getBusList(java.awt.Component)}, this method instead
	 * retrieves an array of all {@link Bus} objects.
	 *
	 * @param parent	Parent component to which this method is called
	 *			from. Used for error message displaying purposes.
	 * @return		An array representation for all Buses.
	 */
	public static Bus[] getBusArray(Component parent) {
		return getBusList(parent).toArray(new Bus[0]);
	}
}
