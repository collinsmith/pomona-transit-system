package edu.csupomona.cs.cs435.project4;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.sql.Time;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * This class represents a {@link Form} for a {@link TripOffering}. It
 * houses structured which are used to input data which is then
 * committed into the SQL database.
 *
 * @author Collin Smith
 */
public class FormTripOffering extends Form<TripOffering> implements ActionListener {
	protected EditorTrip tripEditor;
	protected EditorDriver driverEditor;
	protected EditorBus busEditor;
	protected EditorDate dateEditor;
	protected EditorTimeInterval timeIntervalEditor;

	protected JButton jbtnCancel;
	protected JButton jbtnFinished;

	protected boolean initiallyNull;

	public FormTripOffering(Component owner, TripOffering tripOffering, boolean isCustomInput) {
		super(owner,
			(tripOffering == null) ? "Create " + TABLE_TRIPOFFER_ID : "Edit " + TABLE_TRIPOFFER_ID,
			"Input the following information");

		if (tripOffering == null) {
			tripEditor = new EditorTrip(this, null, false);
			driverEditor = new EditorDriver(null, false, false);
			busEditor = new EditorBus(null, false, false);
			dateEditor = new EditorDate(null, false);
			timeIntervalEditor = new EditorTimeInterval(null, false);
			initiallyNull = true;
		} else {
			tripEditor = new EditorTrip(this, tripOffering.trip, false);
			driverEditor = new EditorDriver(tripOffering.driver, false, false);
			busEditor = new EditorBus(tripOffering.bus, false, false);
			dateEditor = new EditorDate(tripOffering.date, false);
			timeIntervalEditor = new EditorTimeInterval(tripOffering.interval, false);
			initiallyNull = false;
		}

		timeIntervalEditor.jsDeparture.setEnabled(false);
	}

	@Override
	public JPanel createCenterPanel() {
		JPanel p = new JPanel();
		p.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.fill = GridBagConstraints.HORIZONTAL;

		c.gridy = 0;
		tripEditor.setBorder(BorderFactory.createTitledBorder(TABLE_TRIP_TRIPID));
		p.add(tripEditor, c);

		c.gridy = 1;
		driverEditor.setBorder(BorderFactory.createTitledBorder(TABLE_DRIVER_DRIVERID));
		p.add(driverEditor, c);

		c.gridy = 2;
		busEditor.setBorder(BorderFactory.createTitledBorder(TABLE_BUS_BUSID));
		p.add(busEditor, c);


		JPanel subPanel = new JPanel();
		subPanel.setLayout(new GridBagLayout());

		GridBagConstraints c2 = new GridBagConstraints();
		c2.weightx = 1.0;
		c2.gridx = 0;
		c2.gridy = 0;
		c2.fill = GridBagConstraints.HORIZONTAL;
		subPanel.add(dateEditor, c2);

		c2.gridy = 1;
		subPanel.add(timeIntervalEditor, c2);

		c.gridy = 3;
		subPanel.setBorder(BorderFactory.createTitledBorder("Schedule"));
		p.add(subPanel, c);

		tripEditor.jcbTrip.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() != ItemEvent.SELECTED) {
					return;
				}

				timeIntervalEditor.jsDeparture.setEnabled(true);

				ChangeListener[] listeners = timeIntervalEditor.jsDeparture.getChangeListeners();
				for (ChangeListener l : listeners) {
					l.stateChanged(new ChangeEvent(timeIntervalEditor.jsDeparture));
				}
			}
		});

		timeIntervalEditor.jsDeparture.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				timeIntervalEditor.refreshItem();
				timeIntervalEditor.item.arrival = new Time(timeIntervalEditor.item.departure.getTime()+tripEditor.item.totalTime);
				timeIntervalEditor.jsArrival.setValue(timeIntervalEditor.item.arrival);
			}
		});

		return p;
	}

	@Override
	public JPanel createSouthernPanel() {
		JPanel p = new JPanel();

		jbtnCancel = new JButton(GENERIC_DIALOG_CANCEL);
		jbtnCancel.addActionListener(this);

		jbtnFinished = new JButton((tripEditor.initiallyNull || driverEditor.initiallyNull || busEditor.initiallyNull) ? GENERIC_DIALOG_CREATE : GENERIC_DIALOG_COMMIT);
		jbtnFinished.addActionListener(this);
		getRootPane().setDefaultButton(jbtnFinished);

		p.add(jbtnCancel);
		p.add(jbtnFinished);

		return p;
	}

	@Override
	public boolean isFormCompleted() {
		return (tripEditor.areFieldsFilledOut() && driverEditor.areFieldsFilledOut() && busEditor.areFieldsFilledOut());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		if (actionCommand.compareTo(GENERIC_DIALOG_CANCEL) == 0) {
			dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		} else if (actionCommand.compareTo(GENERIC_DIALOG_CREATE) == 0 || actionCommand.compareTo(GENERIC_DIALOG_COMMIT) == 0) {
			if (!isFormCompleted()) {
				Form.throwUncompletedMessage(this);
				return;
			}

			TripOffering newItem = new TripOffering(
					tripEditor.getItem(),
					busEditor.getItem(),
					driverEditor.getItem(),
					dateEditor.getItem(),
					timeIntervalEditor.getItem());

			if (initiallyNull) {
				String date = SQLQuery.convertDateToString(newItem.date.date);
				String sql = String.format("insert into TripOffering (TripID, TripDate, ScheduledDeparture, ScheduledArrival, DriverName, BusID) "
								 + "values (%s, '%s', '%s', '%s', '%s', %s);",
								 newItem.trip.id,
								 date,
								 newItem.interval.departure,
								 newItem.interval.arrival,
								 newItem.driver.name,
								 newItem.bus.id);

				SQLQuery.executeUpdate(owner, sql);
			} else {
				TripOffering oldItem = new TripOffering(
						tripEditor.oldItem,
						busEditor.oldItem,
						driverEditor.oldItem,
						dateEditor.oldItem,
						timeIntervalEditor.oldItem);

				String originalDate = SQLQuery.convertDateToString(dateEditor.oldItem.date);
				String newDate = SQLQuery.convertDateToString(newItem.date.date);

				String sql = String.format("update TripOffering to "
								 + "set to.TripID = %s, to.TripDate = '%s', to.ScheduledDeparture = '%s', to.ScheduledArrival = '%s', to.DriverName = '%s', to.BusID = %s "
								 + "where to.TripID = %s AND to.TripDate like '%s' AND to.ScheduledDeparture = #%s# AND to.ScheduledArrival = #%s# AND to.DriverName like '%s' AND to.BusID = %s;",
								   newItem.trip.id, newDate, newItem.interval.departure, newItem.interval.arrival, newItem.driver.name, newItem.bus.id,
								   oldItem.trip.id, originalDate, oldItem.interval.departure, oldItem.interval.arrival, oldItem.driver.name, oldItem.bus.id);

				SQLQuery.executeUpdate(owner, sql);
			}

			dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
			dispatchFormEvent(new FormEvent<TripOffering>(newItem));
		}
	}
}