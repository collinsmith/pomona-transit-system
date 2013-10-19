package edu.csupomona.cs.cs435.project4;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.util.LinkedList;
import javax.swing.JButton;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class TabTripOfferings extends TransitTab implements ActionListener {
	protected TabTripOfferings owner;

	protected JButton jbtnViewSchedule;
	protected JButton jbtnSearchSchedules;
	protected JButton jbtnAddSchedule;
	protected JButton jbtnDeleteSchedule;
	protected JButton jbtnEditSchedule;
	protected JButton jbtnViewActualData;

	protected int lastSelectedRow = -1;

	public TabTripOfferings() {
		super("Create, edit, and manage trip offerings");

		owner = this;

		Insets i = new Insets(0, 0, 0, 0);

		jbtnViewSchedule = new JButton(LABEL_TRIPOFFERS_VIEW);
		jbtnViewSchedule.setMargin(i);
		jbtnViewSchedule.addActionListener(this);
		addMenuComponent(jbtnViewSchedule);

		jbtnSearchSchedules = new JButton(LABEL_TRIPOFFERS_SEARCH);
		jbtnSearchSchedules.setMargin(i);
		jbtnSearchSchedules.addActionListener(this);
		addMenuComponent(jbtnSearchSchedules);

		jbtnAddSchedule = new JButton(LABEL_TRIPOFFERS_ADD);
		jbtnAddSchedule.setMargin(i);
		jbtnAddSchedule.addActionListener(this);
		addMenuComponent(jbtnAddSchedule);

		jbtnDeleteSchedule = new JButton(LABEL_TRIPOFFERS_DELETE);
		jbtnDeleteSchedule.setMargin(i);
		jbtnDeleteSchedule.addActionListener(this);
		jbtnDeleteSchedule.setEnabled(false);
		addMenuComponent(jbtnDeleteSchedule);

		jbtnEditSchedule = new JButton(LABEL_TRIPOFFERS_EDIT);
		jbtnEditSchedule.setMargin(i);
		jbtnEditSchedule.addActionListener(this);
		jbtnEditSchedule.setEnabled(false);
		addMenuComponent(jbtnEditSchedule);

		jbtnViewActualData = new JButton(LABEL_TRIPOFFERS_ACTUAL);
		jbtnViewActualData.setMargin(i);
		jbtnViewActualData.addActionListener(this);
		jbtnViewActualData.setEnabled(false);
		addMenuComponent(jbtnViewActualData);
	}

	@Override
	public void refreshTables() {
		jbtnViewSchedule.doClick(0);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		if (actionCommand.compareTo(LABEL_TRIPOFFERS_VIEW) == 0) {
			if (jbtnEditSchedule != null) {
				jbtnEditSchedule.setEnabled(false);
			}

			if (jbtnDeleteSchedule != null) {
				jbtnDeleteSchedule.setEnabled(false);
			}

			String sql = "select s1.Address as Origin, s2.Address as Destination,"
				     + "		to.TripDate as [Date], to.ScheduledDeparture as [Scheduled Departure],"
				     + "		to.ScheduledArrival as [Scheduled Arrival], d.DriverName as [Driver], to.BusID "
				     + "from Trip t, TripOffering to, Stop s1, Stop s2, Driver d "
				     + "where to.TripID = t.TripID "
				     + "		and s1.StopID = t.OriginID "
				     + "		and s2.StopID = t.DestinationID "
				     + "		and to.DriverName = d.DriverName;";

			String[] row = null;
			String[] columnNames = null;
			LinkedList<String[]> rows = new LinkedList<String[]>();
			SQLQuery query = SQLQuery.executeQuery(this, sql);
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
				SQLQuery.throwError(this, ex, SQL_ERR_QUERY);
			} finally {
				query.close();
			}

			changeTable(createTable("All trip offerings that exist", rows.toArray(new String[0][]), columnNames));
			getTable().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent e) {
					TripOffering to = getSelectedTripOffering();
					jbtnViewActualData.setEnabled(to.date.date.before(new Date(System.currentTimeMillis())));
					
					jbtnEditSchedule.setEnabled(true);
					jbtnDeleteSchedule.setEnabled(true);
				}
			});
		} else if (actionCommand.compareTo(LABEL_TRIPOFFERS_SEARCH) == 0) {
			FormTripOfferingSearch searchForm = new FormTripOfferingSearch(this);
			searchForm.addFormListener(new FormListener<TripOfferingQuery>() {
				@Override
				public void formCompleted(FormEvent<TripOfferingQuery> e) {
					TripOfferingQuery toq = e.getCreatedObject();
					toq.performQuery(owner);

					jbtnEditSchedule.setEnabled(false);
					jbtnDeleteSchedule.setEnabled(false);
					jbtnViewActualData.setEnabled(false);
				}
			});

			searchForm.display();
		} else if (actionCommand.compareTo(LABEL_TRIPOFFERS_EDIT) == 0) {
			final TripOffering tripOffering = getSelectedTripOffering();
			if (tripOffering == null) {
				return;
			}

			FormTripOffering editingForm = new FormTripOffering(this, new TripOffering(this, tripOffering), true);
			editingForm.addFormListener(new FormListener<TripOffering>() {
				@Override
				public void formCompleted(FormEvent<TripOffering> e) {
					refreshTables();
					getTable().getSelectionModel().setSelectionInterval(lastSelectedRow, lastSelectedRow);
				}
			});

			editingForm.display();
		} else if (actionCommand.compareTo(LABEL_TRIPOFFERS_ADD) == 0) {
			FormTripOffering editingForm = new FormTripOffering(this, null, true);
			editingForm.addFormListener(new FormListener<TripOffering>() {
				@Override
				public void formCompleted(FormEvent<TripOffering> e) {
					refreshTables();
				}
			});

			editingForm.display();

		} else if (actionCommand.compareTo(LABEL_TRIPOFFERS_DELETE) == 0) {
			TripOffering tripOffering = getSelectedTripOffering();
			String date = SQLQuery.convertDateToString(tripOffering.date.date);
			String sql = String.format("delete from TripOffering to "
							 + "where to.TripID = %s AND to.TripDate like '%s' AND to.ScheduledDeparture = #%s# AND to.ScheduledArrival = #%s# AND to.DriverName like '%s' AND to.BusID = %s;",
							   tripOffering.trip.id, date, tripOffering.interval.departure, tripOffering.interval.arrival, tripOffering.driver.name, tripOffering.bus.id);

			SQLQuery.executeUpdate(owner, sql);
			refreshTables();
		} else if (actionCommand.compareTo(LABEL_TRIPOFFERS_ACTUAL) == 0) {
			TripOffering to = getSelectedTripOffering();
			FormActualTripInfo actualTripInfoForm = new FormActualTripInfo(this, to);
			actualTripInfoForm.display();
		}
	}

	private TripOffering getSelectedTripOffering() {
			jbtnEditSchedule.setEnabled(false);
			jbtnDeleteSchedule.setEnabled(false);
			jbtnViewActualData.setEnabled(false);

			lastSelectedRow = getTable().getSelectedRow();
			Stop origin = new Stop(0, (String)getTable().getValueAt(lastSelectedRow, 0));
			Stop destination = new Stop(0, (String)getTable().getValueAt(lastSelectedRow, 1));
			String destinationName = (String)getTable().getValueAt(lastSelectedRow, 1);

			String sql = String.format("select t.TripID, t.OriginID, t.DestinationID "
							 + "from Trip t, Stop s1, Stop s2 "
							 + "where s1.StopID = t.OriginID AND s1.Address like '%s' "
							 + "	AND s2.StopID = t.DestinationID AND s2.Address like '%s';", origin.address, destination.address);

			Trip trip = null;
			SQLQuery tripQuery = SQLQuery.executeQuery(this, sql);
			try {
				if (tripQuery.rs.next()) {
					origin.id = tripQuery.rs.getInt(2);
					destination.id = tripQuery.rs.getInt(3);
					trip = new Trip(this, tripQuery.rs.getInt(1), origin, destination);
				}


			} catch (SQLException ex) {
				SQLQuery.throwError(this, ex);
			} finally {
				tripQuery.close();
			}

			Bus bus = new Bus(Integer.valueOf((String)getTable().getValueAt(lastSelectedRow, 6)), null, null);
			sql = String.format("select b.Model, b.Year "
						+ "from Bus b "
						+ "where b.BusID = %d;", bus.id);

			SQLQuery busQuery = SQLQuery.executeQuery(this, sql);
			try {
				if (busQuery.rs.next()) {
					bus.model = busQuery.rs.getString(1);
					bus.year = busQuery.rs.getString(2);
				}
			} catch (SQLException ex) {
				SQLQuery.throwError(this, ex);
			} finally {
				tripQuery.close();
			}

			Driver driver = new Driver((String)getTable().getValueAt(lastSelectedRow, 5), null);
			sql = String.format("select d.PhoneNumber "
						+ "from Driver d "
						+ "where d.DriverName like '%s';", driver.name);

			SQLQuery driverQuery = SQLQuery.executeQuery(this, sql);
			try {
				if (driverQuery.rs.next()) {
					driver.phoneNumber = driverQuery.rs.getString(1);
				}
			} catch (SQLException ex) {
				SQLQuery.throwError(this, ex);
			} finally {
				driverQuery.close();
			}

			Date date = Date.valueOf((String)getTable().getValueAt(lastSelectedRow, 2));
			Time departure = SQLQuery.convertStringToTime((String)getTable().getValueAt(lastSelectedRow, 3));
			Time arrival = SQLQuery.convertStringToTime((String)getTable().getValueAt(lastSelectedRow, 4));
			return new TripOffering(trip, bus, driver, new TripDate(date), new TimeInterval(departure, arrival));
	}
}