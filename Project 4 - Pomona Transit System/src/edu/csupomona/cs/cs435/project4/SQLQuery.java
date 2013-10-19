package edu.csupomona.cs.cs435.project4;

import java.awt.Component;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import net.sourceforge.jdatepicker.DateModel;

/**
 * This class represents a SQL {@link Connection}, {@link Statement},
 * and {@link ResultSet} which can be easily passed onto an object
 * which calls {@link SQLQuery#executeQuery(java.awt.Component, java.lang.String)}
 * and can then access the ResultSet and call {@link SQLQuery#close()} once
 * their operations are over.
 *
 * @author Collin Smith
 */
public class SQLQuery implements Constants {
	/**
	 * This is a long representation of the number of milliseconds
	 * in 8 hours. This is implemented because I am too lazy at the
	 * moment to call the current TimeZone offset automatically and
	 * store this value.
	 */
	public static final long GMT_OFFSET = 28800000;

	/**
	 * A SQL Connection created by {@link SQLQuery#executeQuery(java.awt.Component, java.lang.String)}.
	 */
	protected Connection c;

	/**
	 * A SQL Statement created by {@link SQLQuery#executeQuery(java.awt.Component, java.lang.String)}.
	 */
	protected Statement s;

	/**
	 * A SQL ResultSet created by {@link SQLQuery#executeQuery(java.awt.Component, java.lang.String)}.
	 */
	protected ResultSet rs;

	/**
	 * Constructor which initializes a new {@link SQLQuery} using the
	 * passed parameters as default values.
	 *
	 * @param c		{@link SQLQuery#c}.
	 * @param s		{@link SQLQuery#s}.
	 * @param rs	{@link SQLQuery#rs}.
	 */
	public SQLQuery(Connection c, Statement s, ResultSet rs) {
		this.c = c;
		this.s = s;
		this.rs = rs;
	}

	/**
	 * Returns whether or not {@link SQLQuery#rs} is {@code null}.
	 *
	 * @return		{@code True} if {@code null}, {@code false} otherwise.
	 */
	public boolean isNull() {
		return rs == null;
	}

	/**
	 * Calls {@link Statement#close()} on {@link SQLQuery#s} and
	 * {@link Connection#close()} on {@link SQLQuery#c}.
	 */
	public void close() {
		try {
			if (s != null) {
				s.close();
			}

			if (c != null) {
				c.close();
			}
		} catch (SQLException e) {
		}
	}

	/**
	 * Executes an query on a SQL database.
	 *
	 * @param parent		The parent component used for displaying GUI error messages.
	 * @param sql		String representation for the SQL query.
	 * @return			A {@link SQLQuery} object representing this query.
	 *				This object must call {@link SQLQuery#close()} upon completion.
	 */
	public static SQLQuery executeQuery(Component parent, String sql) {
		try {
			Class.forName(SQL_JDBCDRIVER);
		} catch (ClassNotFoundException e) {
			throwError(parent, e, SQL_ERR_DRIVER);
		}

		SQLQuery query = new SQLQuery(null, null, null);
		try {
			query.c = DriverManager.getConnection(SQL_CONNECTION);
			query.s = query.c.createStatement();

			query.s.executeQuery(sql);
			query.rs = query.s.getResultSet();
			if (query.isNull()) {
				query.close();
			}

			// Query executor is in charge of closing connection/statement
		} catch (SQLException e) {
			throwError(parent, e);
		}

		return query;
	}

	/**
	 * Executes an update query on a SQL database.
	 *
	 * @param parent		The parent component used for displaying GUI error messages.
	 * @param sql		String representation for the SQL update.
	 */
	public static void executeUpdate(Component parent, String sql) {
		try {
			Class.forName(SQL_JDBCDRIVER);
		} catch (ClassNotFoundException e) {
			throwError(parent, e, SQL_ERR_DRIVER);
		}

		SQLQuery query = new SQLQuery(null, null, null);
		try {
			query.c = DriverManager.getConnection(SQL_CONNECTION);
			query.s = query.c.createStatement();

			query.s.executeUpdate(sql);

			query.close();
		} catch (SQLException e) {
			throwError(parent, e);
		}
	}

	/**
	 * Uses {@link Constants#SQL_ERR_QUERY} in {@link SQLQuery#throwError(java.awt.Component, java.lang.Exception, java.lang.String)}
	 *
	 * @param parent		The parent component used for displaying GUI error messages.
	 * @param e			{@link Exception}
	 */
	public static void throwError(Component parent, Exception e) {
		throwError(parent, e, SQL_ERR_QUERY);
	}

	/**
	 * Prints a stack trace of an exception and displays an error message in a
	 * JOptionPane GUI window.
	 *
	 * @param parent		The parent component used for displaying GUI error messages.
	 * @param e			{@link Exception}
	 * @param message		String representation for a generalized error message.
	 */
	public static void throwError(Component parent, Exception e, String message) {
		e.printStackTrace();
		JOptionPane.showMessageDialog(parent,
							message + '\n' + e.getMessage(),
							"Error",
							JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Converts a date object into String format using {@link Constants#FORMAT_DATE}.
	 *
	 * @param date		The Date object to convert.
	 * @return			String representation of that object.
	 */
	public static String convertDateToString(Date date) {
		return String.format(FORMAT_DATE, date.getMonth()+1,
							   date.getDate(),
							   date.getYear()+1900);
	}

	/**
	 * Converts a date model object into String format using {@link Constants#FORMAT_DATE}.
	 *
	 * @param date		The DateModel object to convert.
	 * @return			String representation of that object.
	 */
	public static String convertDateToString(DateModel date) {
		return String.format(FORMAT_DATE, date.getMonth()+1,
							   date.getDay(),
							   date.getYear());
	}

	/**
	 * Converts a Date object representing a Time into String format
	 * using {@link Constants#FORMAT_TIME}.
	 *
	 * @param date		The Date object to convert.
	 * @return			String representation of the time for that object.
	 */
	public static String convertTimeToString(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_TIME);
		return sdf.format(date);
	}

	/**
	 * Converts a Date object representing a Time into Time format
	 * using {@link SQLQuery#convertTimeToString(java.sql.Date)} and
	 * {@link SQLQuery#convertStringToTime(java.lang.String)}.
	 *
	 * @param date		The Date object to convert.
	 * @return			String representation of the time for that object.
	 */
	public static Time convertDateToTime(Date date) {
		String s = convertTimeToString(date);
		return convertStringToTime(s);
	}

	/**
	 * Retrieves a String representation for the time given in long
	 * format.
	 *
	 * @param date		The time in milliseconds without {@link SQLQuery#GMT_OFFSET}.
	 * @return			String representation for the time in "H:mm" format.
	 */
	public static String convertTimeCountToString(long date) {
		SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
		return sdf.format(new Date(date+GMT_OFFSET));
	}

	/**
	 * Converts a String representation of a time in "hh:mm a" format into
	 * a Time format.
	 *
	 * @param s			String to convert.
	 * @return			Time object representing that string.
	 */
	public static Time convertStringToTime(String s) {
		int hour;
		int minute;
		int firstColon;
		int secondColon;

		if (s == null) {
			throw new java.lang.IllegalArgumentException();
		}

		firstColon = s.indexOf(':');
		secondColon = s.indexOf(' ', firstColon+1);
		if ((firstColon > 0) & (secondColon > 0) & (secondColon < s.length()-1)) {
			hour = Integer.parseInt(s.substring(0, firstColon));
			minute = Integer.parseInt(s.substring(firstColon+1, secondColon));
			if (s.charAt(secondColon+1) == 'P') {
				hour += 12;
			}
		} else {
			throw new java.lang.IllegalArgumentException();
		}

		return new Time(hour, minute, 0);
	}
}
