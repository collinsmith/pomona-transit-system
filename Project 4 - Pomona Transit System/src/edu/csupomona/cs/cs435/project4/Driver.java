package edu.csupomona.cs.cs435.project4;

import java.awt.Component;
import java.sql.SQLException;
import java.util.LinkedList;

/**
 * This class represents a Driver. A Driver has a
 * name and phone number.
 *
 * @author Collin Smith
 */
public class Driver extends TransitTable<Driver> {
	/**
	 * String representation for the name of this Driver.
	 * This value should be unique.
	 */
	protected String name;

	/**
	 * String representation for the phone number of this
	 * Driver. This value should be 10 characters.
	 */
	protected String phoneNumber;

	/**
	 * @see TransitTable#copy()
	 */
	@Override
	public Driver copy() {
		return new Driver(name, phoneNumber);
	}

	/**
	 * Constructor which initializes a new {@link Driver} with
	 * the specified values;
	 *
	 * @param name		{@link Driver#name}
	 * @param phoneNumber	{@link Driver#phoneNumber}
	 */
	public Driver(String name, String phoneNumber) {
		this.name = name;
		this.phoneNumber = phoneNumber;
	}

	/**
	 * Retrieves a String representation of a {@link Driver} to be
	 * used within GUI components.
	 *
	 * @return		The {@link Driver#name} of this Driver.
	 */
	@Override
	public String toString() {
		return name;
	}

	/**
	 * Retrieves a {@link LinkedList} representation for all {@link Driver}
	 * objects stored within the database;
	 *
	 * @param parent	Parent component to which this method is called
	 *			from. Used for error message displaying purposes.
	 * @return		A LinkedList representation for all Drivers.
	 */
	public static LinkedList<Driver> getDriverList(Component parent) {
		LinkedList<Driver> driverList = new LinkedList<Driver>();
		SQLQuery query = SQLQuery.executeQuery(parent, "select d.DriverName, d.PhoneNumber from Driver d;");
		try {
			while (query.rs.next()) {
				driverList.add(new Driver(query.rs.getString(1), query.rs.getString(2)));
			}
		} catch (SQLException e) {
			SQLQuery.throwError(parent, e);
		} finally {
			query.close();
		}

		return driverList;
	}

	/**
	 * Using {@link Driver#getDriverList(java.awt.Component)}, this method instead
	 * retrieves an array of all {@link Driver} objects.
	 *
	 * @param parent	Parent component to which this method is called
	 *			from. Used for error message displaying purposes.
	 * @return		An array representation for all Drivers.
	 */
	public static Driver[] getDriverArray(Component parent) {
		return getDriverList(parent).toArray(new Driver[0]);
	}

	/**
	 * Converts a given String of digits in the form of "##########" to an
	 * easily readable version "(###) ###-####"
	 *
	 * @param digits	A String of digits to be converted.
	 * @return		The converted version of the String.
	 */
	public static String convertToPhoneNumber(String digits) {
		return String.format("(%s) %s-%s", digits.substring(0, 3), digits.substring(3, 6), digits.substring(6, 10));
	}
}
