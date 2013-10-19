package edu.csupomona.cs.cs435.project4;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.LinkedList;
import java.util.ListIterator;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class FormActualTripInfo extends Form implements ActionListener {
	protected TripOffering tripOffering;

	protected JButton jbtnFinished;

	public FormActualTripInfo(Component owner, TripOffering tripOffering) {
		super(owner, "Actual Trip Stop Info", "Actual Trip Stop Info");

		this.tripOffering = tripOffering;
	}

	@Override
	public JPanel createCenterPanel() {
		JPanel p = new JPanel();
		p.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.fill = GridBagConstraints.HORIZONTAL;

		c.gridy = 0;
		JPanel basicPanel = createBasicInfoPanel();
		basicPanel.setBorder(BorderFactory.createTitledBorder("General Trip Info"));
		p.add(basicPanel, c);

		c.gridy = 1;
		JPanel actualStopInfo = createActualInfoPanel();
		actualStopInfo.setBorder(BorderFactory.createTitledBorder("Actual Stop Info"));
		p.add(actualStopInfo, c);

		return p;
	}

	public JPanel createBasicInfoPanel() {
		JPanel p = new JPanel();
		p.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;

		c.gridx = 0;
		c.weightx = 1.0;
		p.add(new JLabel(TABLE_TRIP_TRIPID), c);

		c.gridx = 1;
		c.weightx = 0.0;
		JTextField jtfTrip = new JTextField(tripOffering.trip.toString());
		jtfTrip.setPreferredSize(Editor.fieldSize);
		jtfTrip.setEditable(false);
		p.add(jtfTrip, c);

		c.gridy++;

		c.gridx = 0;
		c.weightx = 1.0;
		p.add(new JLabel(TABLE_DRIVER_DRIVERID), c);

		c.gridx = 1;
		c.weightx = 0.0;
		JTextField jtfDriver = new JTextField(tripOffering.driver.toString());
		jtfDriver.setPreferredSize(Editor.fieldSize);
		jtfDriver.setEditable(false);
		p.add(jtfDriver, c);

		c.gridy++;

		c.gridx = 0;
		c.weightx = 1.0;
		p.add(new JLabel(TABLE_BUS_BUSID), c);

		c.gridx = 1;
		c.weightx = 0.0;
		JTextField jtfBus = new JTextField(tripOffering.bus.toString());
		jtfBus.setPreferredSize(Editor.fieldSize);
		jtfBus.setEditable(false);
		p.add(jtfBus, c);

		c.gridy++;

		return p;
	}

	private int stopNumber;
	private Path last;
	private Path path;

	public JPanel createActualInfoPanel() {
		JPanel p = new JPanel();
		int count = getActualTripCount();
		if (count == 0) {
			stopNumber = 1;
			last = new Path(tripOffering.trip.origin, tripOffering.interval.departure.getTime());
			ListIterator<Path> it = tripOffering.trip.stopList.listIterator();
			while (it.hasNext()) {
				path = it.next();
				String title = "When did you depart from " + last.stop.address + " and arrive at " + path.stop.address + "?";
				FormTimeInterval createNewTimeInterval = new FormTimeInterval(this,
					  title,
					  new TimeInterval(new Time(last.drivingTime), new Time(last.drivingTime+path.drivingTime)),
					  new PassengerManifest(0, 0));

				createNewTimeInterval.addFormListener(new FormListener<ActualTripStopInfo>() {
					@Override
					public void formCompleted(FormEvent<ActualTripStopInfo> e) {
						ActualTripStopInfo tripStopInfo = e.getCreatedObject();
						TimeInterval interval = tripStopInfo.timeInterval;
						last = path;
						last.drivingTime = interval.arrival.getTime();

						String date = SQLQuery.convertDateToString(tripOffering.date.date);
						String sql = String.format("insert into ActualTripStopInfo (TripID, TripDate, ScheduledDeparture, StopNumber, ScheduledArrival, ActualDeparture, ActualArrival, PassengersOn, PassengersOff) "
										 + "values (%s, '%s', #%s#, %d, #%s#, #%s#, #%s#, %d, %d);",
										   tripOffering.trip.id,
										   date,
										   tripOffering.interval.departure,
										   stopNumber,
										   tripOffering.interval.arrival,
										   interval.departure,
										   interval.arrival,
										   tripStopInfo.passengers.on,
										   tripStopInfo.passengers.off);

						SQLQuery.executeUpdate(owner, sql);

						stopNumber++;
					}
				});

				createNewTimeInterval.display();
			}
		}

		LinkedList<String[]> rows = new LinkedList<String[]>();

		stopNumber = 0;
		ActualTripStopInfo[] actualStops = getActualTripStopInfoArray();
		for (ActualTripStopInfo actualStop : actualStops) {
			if (stopNumber == 0) {
				rows.add(new String[] {
					"0",
					tripOffering.trip.origin.address,
					"",
					SQLQuery.convertTimeToString(new Date(actualStop.timeInterval.departure.getTime())),
					"",
					""
				});

				stopNumber = 1;
			}

			rows.add(new String[] {
				String.valueOf(actualStop.stopNumber),
				actualStop.stop.address,
				SQLQuery.convertTimeToString(new Date(actualStop.timeInterval.departure.getTime())),
				SQLQuery.convertTimeToString(new Date(actualStop.timeInterval.arrival.getTime())),
				String.valueOf(actualStop.passengers.on),
				String.valueOf(actualStop.passengers.off)
			});

			stopNumber++;
		}



		JTable t = new JTable(new DefaultTableModel(rows.toArray(new String[0][]),
					    new String[] { "Stop Number", "Address", "Departure", "Arrival", "Passengers On", "Passengers Off" })) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		t.setAutoCreateRowSorter(true);
		t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		t.getTableHeader().setReorderingAllowed(false);
		p.add(new JScrollPane(t));

		return p;
	}

	public int getActualTripCount() {
		int retValue = 0;
		String date = SQLQuery.convertDateToString(tripOffering.date.date);
		String sql = String.format("select count(atsi.StopNumber) "
						 + "from Stop s, TripOffering to, TripStopInfo tsi, ActualTripStopInfo atsi "
						 + "where tsi.StopID = s.StopID AND to.TripID = tsi.TripID AND tsi.TripID = atsi.TripID AND atsi.TripDate = to.TripDate "
						 + "AND atsi.ScheduledDeparture = to.ScheduledDeparture AND tsi.StopNumber = atsi.StopNumber AND to.TripID = %d "
						 + "AND to.TripDate like '%s' AND to.ScheduledDeparture like #%s# "
						 + "group by atsi.StopNumber "
						 + "order by atsi.StopNumber asc;", tripOffering.trip.id, date, tripOffering.interval.departure);

		SQLQuery query = SQLQuery.executeQuery(owner, sql);
		try {
			if (query.rs.next()) {
				retValue = query.rs.getInt(1);
			} else {
				retValue = 0;
			}
		} catch (SQLException e) {
			SQLQuery.throwError(owner, e);
		} finally {
			query.close();
		}

		return retValue;
	}

	@Override
	public JPanel createSouthernPanel() {
		JPanel p = new JPanel();

		jbtnFinished = new JButton(GENERIC_DIALOG_OK);
		jbtnFinished.addActionListener(this);
		getRootPane().setDefaultButton(jbtnFinished);

		p.add(jbtnFinished);

		return p;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		dispatchFormEvent(new FormEvent(null));
	}

	@Override
	public boolean isFormCompleted() {
		return true;
	}

	public ActualTripStopInfo[] getActualTripStopInfoArray() {
		LinkedList<ActualTripStopInfo> infoList = new LinkedList<ActualTripStopInfo>();
		String date = SQLQuery.convertDateToString(tripOffering.date.date);
		String sql = String.format("select distinct atsi.StopNumber, s.StopID, s.Address, atsi.ActualDeparture, atsi.ActualArrival, atsi.PassengersOn, atsi.PassengersOff "
						 + "from Stop s, TripOffering to, TripStopInfo tsi, ActualTripStopInfo atsi "
						 + "where tsi.StopID = s.StopID AND to.TripID = tsi.TripID AND tsi.TripID = atsi.TripID "
						 + "AND atsi.TripDate = to.TripDate AND atsi.ScheduledDeparture = to.ScheduledDeparture "
						 + "AND tsi.StopNumber = atsi.StopNumber AND to.TripID = %d "
						 + "AND to.TripDate like '%s' AND to.ScheduledDeparture like #%s# "
						 + "group by atsi.StopNumber, s.StopID, s.Address, atsi.ActualDeparture, atsi.ActualArrival, atsi.PassengersOn, atsi.PassengersOff "
						 + "order by atsi.StopNumber asc;",
						 tripOffering.trip.id, date, tripOffering.interval.departure);

		SQLQuery query = SQLQuery.executeQuery(this, sql);

		ActualTripStopInfo actualInfo = null;
		TimeInterval tempTimeInterval = null;
		Stop tempStop = null;
		PassengerManifest tempPassengers = null;
		try {
			while (query.rs.next()) {
				tempTimeInterval = new TimeInterval(query.rs.getTime(4), query.rs.getTime(5));
				tempStop = new Stop(query.rs.getInt(2), query.rs.getString(3));
				tempPassengers = new PassengerManifest(query.rs.getInt(6), query.rs.getInt(7));
				actualInfo = new ActualTripStopInfo(query.rs.getInt(1), tripOffering, tempTimeInterval, tempStop, tempPassengers);
				infoList.add(actualInfo);
			}
		} catch (SQLException e) {
			SQLQuery.throwError(this, e);
		} finally {
			query.close();
		}

		return infoList.toArray(new ActualTripStopInfo[0]);
	}
}
