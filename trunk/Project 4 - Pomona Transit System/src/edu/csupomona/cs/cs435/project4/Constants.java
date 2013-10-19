package edu.csupomona.cs.cs435.project4;

/**
 * This interface is intended to serve no other purpose
 * then to grant easy access to variables for editing
 * which are used throughout this program.
 *
 * @author Collin Smith
 */
public interface Constants {
	/**
	 * String representation for the name of this program.
	 */
	String PROGRAM_NAME		= "Pomona Transit System";

	/**
	 * String representation for this author of this program.
	 */
	String PROGRAM_AUTHOR		= "Collin Smith";

	/**
	 * String representation for the email contact of the author
	 * of this program.
	 */
	String PROGRAM_EMAIL		= "collinsmith@csupomona.edu";

	/**
	 * Generic dialogue strings which are used in many different
	 * forms throughout this program.
	 */
	String GENERIC_DIALOG_CANCEL	= "Cancel";
	String GENERIC_DIALOG_SEARCH	= "Search";
	String GENERIC_DIALOG_CREATE	= "Create";
	String GENERIC_DIALOG_REPLACE	= "Replace All";
	String GENERIC_DIALOG_COMMIT	= "Commit";
	String GENERIC_DIALOG_OK	= "OK";

	/**
	 * Date and time formats in String representations which are used
	 * throughout this program to ensure that they are the same.
	 */
	String FORMAT_DATE		= "%d/%d/%d";
	String FORMAT_TIME		= "h:mm a";

	/**
	 * SQL Strings which should not be changed.
	 */
	String SQL_CONNECTION		= "jdbc:odbc:PomonaTransitSystem";
	String SQL_JDBCDRIVER		= "sun.jdbc.odbc.JdbcOdbcDriver";

	/**
	 * String representation for the SQL error message given when there
	 * is some kind of error when performing a query. This is extremely
	 * arbitrary, and more information on the thrown error can be seen
	 * via the console.
	 */
	String SQL_ERR_QUERY		= "There was an error trying to execute the SQL query";

	/**
	 * String representation for the SQL error message given when there
	 * is an error trying to retrieve the driver this program needs to
	 * connect to and talk to the SQL database.
	 */
	String SQL_ERR_DRIVER		= "There was an error trying to retrieve the SQL driver";

	/**
	 * String representation for the File menu name.
	 */
	String MENU_FILE			= "File";

	/**
	 * String representation for the Help menu name.
	 */
	String MENU_HELP			= "Help";

	/**
	 * String representation for the File>Exit menu item name.
	 */
	String MENUITEM_EXIT		= "Exit";

	/**
	 * String representation for the Help>About menu item name.
	 */
	String MENUITEM_ABOUT		= "About";

	/**
	 * String representation for the name of the Trip Offerings tab.
	 */
	String LABEL_TAB_TRIPOFFERS	= "Trip Offerings";

	/**
	 * String representation for the name of the Trips tab.
	 */
	String LABEL_TAB_TRIPS		= "Trips";

	/**
	 * String representation for the name of the Stops tab.
	 */
	String LABEL_TAB_STOPS		= "Stops";

	/**
	 * String representation for the name of the Drivers tab.
	 */
	String LABEL_TAB_DRIVERS	= "Drivers";

	/**
	 * String representation for the name of the Buses tab.
	 */
	String LABEL_TAB_BUSES		= "Buses";

	/**
	 * String representation for the Refresh Buses button.
	 */
	String LABEL_BUSES_VIEW		= "<html><center>Refresh<br>Buses</center></html>";

	/**
	 * String representation for the Create Bus button.
	 */
	String LABEL_BUSES_ADD		= "<html><center>Create<br>Bus</center></html>";

	/**
	 * String representation for the Delete Bus button.
	 */
	String LABEL_BUSES_DELETE	= "<html><center>Delete<br>Bus</center></html>";

	/**
	 * String representation for the View Details of a Bus button.
	 */
	String LABEL_BUSES_EDIT		= "<html><center>View<br>Details</center></html>";

	/**
	 * String representation for the title of {@link Bus#id}.
	 */
	String TABLE_BUS_BUSID		= "Bus";

	/**
	 * String representation for the title of {@link Bus#model}.
	 */
	String TABLE_BUS_MODEL		= "Model";

	/**
	 * String representation for the title of {@link Bus#year}.
	 */
	String TABLE_BUS_YEAR		= "Year";

	/**
	 * String representation for the Refresh Drivers button.
	 */
	String LABEL_DRIVERS_VIEW	= "<html><center>Refresh<br>Drivers</center></html>";

	/**
	 * String representation for the Create Driver button.
	 */
	String LABEL_DRIVERS_ADD	= "<html><center>Create<br>Driver</center></html>";

	/**
	 * String representation for the View Details of a {@link Driver} button.
	 */
	String LABEL_DRIVERS_EDIT	= "<html><center>View<br>Details</center></html>";

	/**
	 * String representation for the View Schedule of a {@link Driver} button.
	 */
	String LABEL_DRIVERS_FIND	= "<html><center>View<br>Schedule</center></html>";

	/**
	 * String representation for the title of a {@link Driver}.
	 */
	String TABLE_DRIVER_DRIVERID	= "Driver";

	/**
	 * String representation for the title of {@link Driver#name}.
	 */
	String TABLE_DRIVER_NAME	= "Name";

	/**
	 * String representation for the title of {@link Driver#phoneNumber}.
	 */
	String TABLE_DRIVER_PHONE	= "Phone Number";

	/**
	 * String representation for the Refresh Stops button.
	 */
	String LABEL_STOPS_VIEW		= "<html><center>Refresh<br>Stops</center></html>";

	/**
	 * String representation for the Create Stop button.
	 */
	String LABEL_STOPS_ADD		= "<html><center>Create<br>Stop</center></html>";

	/**
	 * String representation for the View Details of a {@link Stop} button.
	 */
	String LABEL_STOPS_EDIT		= "<html><center>View<br>Details</center></html>";

	/**
	 * String representation for the title of {@link Stop#id}.
	 */
	String TABLE_STOP_STOPID	= "Stop";

	/**
	 * String representation for the title of {@link Stop#address}.
	 */
	String TABLE_STOP_ADDRESS	= "Address";

	/**
	 * String representation for the Refresh Trips button.
	 */
	String LABEL_TRIPS_VIEW		= "<html><center>Refresh<br>Trips</center></html>";

	/**
	 * String representation for the Create Trip button.
	 */
	String LABEL_TRIPS_ADD		= "<html><center>Create<br>Trip</center></html>";

	/**
	 * String representation for the View Details of a {@link Trip} button.
	 */
	String LABEL_TRIPS_EDIT		= "<html><center>View<br>Details</center></html>";

	/**
	 * String representation for the title of {@link Trip#id}.
	 */
	String TABLE_TRIP_TRIPID	= "Trip";

	/**
	 * String representation for the title of {@link Trip#origin}.
	 */
	String TABLE_TRIP_ORIGIN	= "Origin";

	/**
	 * String representation for the title of {@link Trip#destination}.
	 */
	String TABLE_TRIP_DESTINATION	= "Destination";

	/**
	 * String representation for the Refresh Trip Offerings button.
	 */
	String LABEL_TRIPOFFERS_VIEW	= "<html><center>Refresh<br>Trip<br>Offerings</center></html>";

	/**
	 * String representation for the Search Trip Offerings button.
	 */
	String LABEL_TRIPOFFERS_SEARCH= "<html><center>Search<br>Trip<br>Offerings</center></html>";

	/**
	 * String representation for the Create Trip Offering button.
	 */
	String LABEL_TRIPOFFERS_ADD	= "<html><center>Create<br>Trip<br>Offering</center></html>";

	/**
	 * String representation for the Delete Trip Offering button.
	 */
	String LABEL_TRIPOFFERS_DELETE= "<html><center>Delete<br>Trip<br>Offering</center></html>";

	/**
	 * String representation for the View/Edit Trip Offering of a {@link TripOffering} button.
	 */
	String LABEL_TRIPOFFERS_EDIT	= "<html><center>View/Edit<br>Trip<br>Offering</center></html>";

	/**
	 * String representation for the View Actual Final Trip of a {@link TripOffering} button.
	 */
	String LABEL_TRIPOFFERS_ACTUAL= "<html><center>View/Fill<br>Final Trip</center></html>";

	/**
	 * String representation for the title of a {@link TripOffering}.
	 */
	String TABLE_TRIPOFFER_ID	= "Trip Offering";

	/**
	 * String representation for the title of {@link TripDate#date}.
	 */
	String TABLE_TRIPOFFER_DATE	= "Date";

	/**
	 * String representation for the title of {@link TimeInterval#departure}.
	 */
	String TABLE_TRIPOFFER_DEPART	= "Departure";

	/**
	 * String representation for the title of {@link TimeInterval#arrival}.
	 */
	String TABLE_TRIPOFFER_ARRIVAL= "Arrival";
}
