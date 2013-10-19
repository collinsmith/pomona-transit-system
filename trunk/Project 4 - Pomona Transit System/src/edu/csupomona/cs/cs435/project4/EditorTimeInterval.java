package edu.csupomona.cs.cs435.project4;

import java.sql.Time;
import java.util.Calendar;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

/**
 * This class represents the {@link Editor} to be used when
 * editing a {@link TimeInterval}.
 *
 * @author Collin Smith
 */
public class EditorTimeInterval extends Editor<TimeInterval> implements Constants {
	/**
	 * JSpinner representation for the {@link TimeInterval#departure} of this
	 * {@link TimeInterval}.
	 */
	protected JSpinner jsDeparture;

	/**
	 * JSpinner representation for the {@link TimeInterval#arrival} of this
	 * {@link TimeInterval}. Should be automatically calculated based on
	 * total {@link Trip#totalTime} and {@link Path#drivingTime}.
	 */
	protected JSpinner jsArrival;

	/**
	 * Constructor which initializes a {@link TimeInterval} {@link Editor} with
	 * the following default parameters.
	 *
	 * @param timeInterval		{@link Editor#item}. {@code null} for no default item.
	 * @param isEditorForm		{@link Editor#isEditorForm}
	 */
	public EditorTimeInterval(TimeInterval timeInterval, boolean isEditorForm) {
		super(timeInterval, isEditorForm, false);
	}

	/**
	 * @see Editor#addEditorForm()
	 */
	@Override
	public void addEditorForm() {
		jsDeparture = new JSpinner(new SpinnerDateModel());
		JSpinner.DateEditor deDeparture = new JSpinner.DateEditor(jsDeparture, FORMAT_TIME);
		jsDeparture.setEditor(deDeparture);
		jsDeparture.setPreferredSize(fieldSize);
		jsDeparture.setValue(item == null ? new Time(Calendar.getInstance().getTime().getTime()) : item.departure);

		addField(TABLE_TRIPOFFER_DEPART, jsDeparture);

		jsArrival = new JSpinner(new SpinnerDateModel());
		JSpinner.DateEditor deArrival = new JSpinner.DateEditor(jsArrival, FORMAT_TIME);
		jsArrival.setEditor(deArrival);
		jsArrival.setPreferredSize(fieldSize);
		jsArrival.setValue(item == null ? new Time(Calendar.getInstance().getTime().getTime()) : item.arrival);

		addField(TABLE_TRIPOFFER_ARRIVAL, jsArrival);
	}

	/**
	 * @see Editor#addSelectorForm()
	 */
	@Override
	public void addSelectorForm() {
		jsDeparture = new JSpinner(new SpinnerDateModel());
		JSpinner.DateEditor deDeparture = new JSpinner.DateEditor(jsDeparture, FORMAT_TIME);
		jsDeparture.setEditor(deDeparture);
		jsDeparture.setPreferredSize(fieldSize);
		jsDeparture.setValue(item == null ? new Time(Calendar.getInstance().getTime().getTime()) : item.departure);

		addField(TABLE_TRIPOFFER_DEPART, jsDeparture);

		jsArrival = new JSpinner(new SpinnerDateModel());
		JSpinner.DateEditor deArrival = new JSpinner.DateEditor(jsArrival, FORMAT_TIME);
		jsArrival.setEditor(deArrival);
		jsArrival.setPreferredSize(fieldSize);
		jsArrival.setValue(item == null ? new Time(Calendar.getInstance().getTime().getTime()) : item.arrival);
		jsArrival.setEnabled(false);

		addField(TABLE_TRIPOFFER_ARRIVAL, jsArrival);
	}

	/**
	 * @see Editor#areFieldsFilledOut()
	 */
	@Override
	public boolean areFieldsFilledOut() {
		return true;
	}

	/**
	 * @see Editor#refreshItem()
	 */
	@Override
	public void refreshItem() {
		Time departure = new Time(((java.util.Date)jsDeparture.getModel().getValue()).getTime());
		departure.setSeconds(0);
		Time arrival = new Time(((java.util.Date)jsArrival.getModel().getValue()).getTime());
		arrival.setSeconds(0);
		if (item == null) {
			item = new TimeInterval(departure, arrival);
		} else {
			item.departure = departure;
			item.arrival = arrival;
		}
	}

	/**
	 * @see Editor#getItem()
	 */
	@Override
	public TimeInterval getItem() {
		refreshItem();
		return item;
	}
}