package edu.csupomona.cs.cs435.project4;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JFormattedTextField.AbstractFormatter;

/**
 * Represents an abstract date formatter that is used within
 * this program to represent a short SQL date in a calendar.
 *
 * @author Collin Smith
 */
public class DateComponentFormatterShort extends AbstractFormatter {
	/**
	 * DateFormat representation of the format for this formatter.
	 */
	protected DateFormat format;

	/**
	 * Default constructor which initializes {@link DateComponentFormatterShort#format}
	 * to a {@link SimpleDateFormat#getDateInstance(int)} using {@link SimpleDateFormat#SHORT}.
	 */
	public DateComponentFormatterShort(){
		format = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT);
	}

	/**
	 * Converts a generic object value to a String representation of the
	 * formatted version of that value.
	 *
	 * @param value			The value to format.
	 * @return				String version of that value formatted.
	 * @throws ParseException
	 */
	@Override
	public String valueToString(Object value) throws ParseException {
		Calendar cal = (Calendar)value;
		if (cal == null) {
			return "";
		}
		
		return format.format(cal.getTime());
	}

	/**
	 * Converts a given string value which is then parsed and returned
	 * as a calendar.
	 *
	 * @param text			The String to convert.
	 * @return				A calendar representation of the String.
	 * @throws ParseException
	 */
	@Override
	public Object stringToValue(String text) throws ParseException {
		if (text == null || text.equals("")) {
			return null;
		}

		Date date = format.parse(text);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	}
}
