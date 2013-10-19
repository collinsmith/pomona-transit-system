package edu.csupomona.cs.cs435.project4;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedList;
import javax.swing.JButton;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class TabDrivers extends TransitTab implements ActionListener {
	protected JButton jbtnViewDrivers;
	protected JButton jbtnAddDriver;
	protected JButton jbtnEditDriver;
	protected JButton jbtnFindSchedule;

	public TabDrivers() {
		super("Create, edit, and manage the different drivers");

		Insets i = new Insets(0, 0, 0, 0);

		jbtnViewDrivers = new JButton(LABEL_DRIVERS_VIEW);
		jbtnViewDrivers.setMargin(i);
		jbtnViewDrivers.addActionListener(this);
		addMenuComponent(jbtnViewDrivers);

		jbtnAddDriver = new JButton(LABEL_DRIVERS_ADD);
		jbtnAddDriver.setMargin(i);
		jbtnAddDriver.addActionListener(this);
		addMenuComponent(jbtnAddDriver);

		jbtnEditDriver = new JButton(LABEL_DRIVERS_EDIT);
		jbtnEditDriver.setMargin(i);
		jbtnEditDriver.addActionListener(this);
		jbtnEditDriver.setEnabled(false);
		addMenuComponent(jbtnEditDriver);

		jbtnFindSchedule = new JButton(LABEL_DRIVERS_FIND);
		jbtnFindSchedule.setMargin(i);
		jbtnFindSchedule.addActionListener(this);
		jbtnFindSchedule.setEnabled(false);
		addMenuComponent(jbtnFindSchedule);
	}

	@Override
	public void refreshTables() {
		jbtnViewDrivers.doClick(0);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		if (actionCommand.compareTo(LABEL_DRIVERS_VIEW) == 0) {
			if (jbtnEditDriver != null) {
				jbtnEditDriver.setEnabled(false);
			}

			if (jbtnFindSchedule != null) {
				jbtnFindSchedule.setEnabled(false);
			}

			String[] row = null;
			String[] columnNames = null;
			LinkedList<String[]> rows = new LinkedList<String[]>();
			SQLQuery query = SQLQuery.executeQuery(this, "select d.DriverName, d.PhoneNumber from Driver d;");
			try {
				ResultSetMetaData metaData = query.rs.getMetaData();
				columnNames = new String[metaData.getColumnCount()];
				for (int i = 0; i < columnNames.length; i++) {
					columnNames[i] = metaData.getColumnName(i+1);
				}

				while (query.rs.next()) {
					row = new String[] {
						query.rs.getString(1),
						Driver.convertToPhoneNumber(query.rs.getString(2))
					};

					rows.add(row);
				}
			} catch (SQLException ex) {
				SQLQuery.throwError(this, ex, SQL_ERR_QUERY);
			} finally {
				query.close();
			}

			changeTable(createTable("All drivers that exist", rows.toArray(new String[0][]), columnNames));
			getTable().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent e) {
					jbtnEditDriver.setEnabled(true);
					jbtnFindSchedule.setEnabled(true);
				}
			});
		} else if (actionCommand.compareTo(LABEL_DRIVERS_ADD) == 0) {
			FormDriver createForm = new FormDriver(this, null, true);
			createForm.addFormListener(new FormListener<Driver>() {
				@Override
				public void formCompleted(FormEvent<Driver> e) {
					refreshTables();
				}
			});

			createForm.display();
		} else if (actionCommand.compareTo(LABEL_DRIVERS_EDIT) == 0) {
			final int selectedRow = getTable().getSelectedRow();
			final String driverName = (String)getTable().getValueAt(selectedRow, 0);

			Driver driver = null;
			String sql = String.format("select d.PhoneNumber from Driver d where d.DriverName like '%s';", driverName);
			SQLQuery query = SQLQuery.executeQuery(this, sql);
			try {
				if (query.rs.next()) {
					driver = new Driver(driverName, query.rs.getString(1));
				}
			} catch (SQLException ex) {
				SQLQuery.throwError(this, ex);
			} finally {
				query.close();
			}

			FormDriver editingForm = new FormDriver(this, driver, true);
			/*editingForm.addFormListener(new FormListener<Driver>() {
				@Override
				public void formCompleted(FormEvent<Driver> e) {
					refreshTables();
					getTable().getSelectionModel().setSelectionInterval(selectedRow, selectedRow);
				}
			});*/

			editingForm.display();
		} else if (actionCommand.compareTo(LABEL_DRIVERS_FIND) == 0) {
			jbtnEditDriver.setEnabled(false);
			jbtnFindSchedule.setEnabled(false);

			int selectedRow = getTable().getSelectedRow();
			String driverName = (String)getTable().getValueAt(selectedRow, 0);
			String sql = String.format("select to.TripDate as [Date], s1.Address as Origin, s2.Address as Destination, "
							 + "		to.ScheduledDeparture as [Scheduled Departure], to.ScheduledArrival as [Scheduled Arrival], d.DriverName "
							 + "from Trip t, TripOffering to, Stop s1, Stop s2, Driver d "
							 + "where to.TripID = t.TripID "
							 + "		AND s1.StopID = t.OriginID "
							 + "		AND s2.StopID = t.DestinationID "
							 + "		AND to.DriverName = d.DriverName "
							 + "		AND d.DriverName like '%s';", driverName);

			String[] row = null;
			String[] columnNames = null;
			LinkedList<String[]> rows = new LinkedList<String[]>();
			SQLQuery query = SQLQuery.executeQuery(this, sql);
			try {
				ResultSetMetaData metaData = query.rs.getMetaData();
				columnNames = new String[metaData.getColumnCount()-1];
				for (int i = 0; i < columnNames.length; i++) {
					columnNames[i] = metaData.getColumnName(i+1);
				}

				while (query.rs.next()) {
					row = new String[] {
						query.rs.getDate(1).toString(),
						query.rs.getString(2),
						query.rs.getString(3),
						SQLQuery.convertTimeToString(new Date(query.rs.getTime(4).getTime())),
						SQLQuery.convertTimeToString(new Date(query.rs.getTime(5).getTime()))
					};

					rows.add(row);
				}


			} catch (SQLException ex) {
				SQLQuery.throwError(this, ex);
			} finally {
				query.close();
			}

			changeTable(createTable("List of trip offerings driven by " + driverName, rows.toArray(new String[0][]), columnNames));
		}
	}
}
