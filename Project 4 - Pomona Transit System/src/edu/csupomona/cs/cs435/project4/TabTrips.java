package edu.csupomona.cs.cs435.project4;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedList;
import javax.swing.JButton;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class TabTrips extends TransitTab implements ActionListener {
	protected JButton jbtnViewTrips;
	protected JButton jbtnAddTrip;
	protected JButton jbtnEditTrip;

	public TabTrips() {
		super("Create, edit, and manage trips");

		Insets i = new Insets(0, 0, 0, 0);

		jbtnViewTrips = new JButton(LABEL_TRIPS_VIEW);
		jbtnViewTrips.setMargin(i);
		jbtnViewTrips.addActionListener(this);
		addMenuComponent(jbtnViewTrips);

		jbtnAddTrip = new JButton(LABEL_TRIPS_ADD);
		jbtnAddTrip.setMargin(i);
		jbtnAddTrip.addActionListener(this);
		addMenuComponent(jbtnAddTrip);

		jbtnEditTrip = new JButton(LABEL_TRIPS_EDIT);
		jbtnEditTrip.setMargin(i);
		jbtnEditTrip.addActionListener(this);
		jbtnEditTrip.setEnabled(false);
		addMenuComponent(jbtnEditTrip);
	}

	@Override
	public void refreshTables() {
		jbtnViewTrips.doClick(0);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		if (actionCommand.compareTo(LABEL_TRIPS_VIEW) == 0) {
			if (jbtnEditTrip != null) {
				jbtnEditTrip.setEnabled(false);
			}

			String sql =   "select t.TripID, s1.Address as Origin, s2.Address as Destination "
					 + "from Trip t, Stop s1, Stop s2 "
					 + "where s1.StopID = t.OriginID "
					 + "		and s2.StopID = t.DestinationID;";

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
						query.rs.getString(3)
					};

					rows.add(row);
				}


			} catch (SQLException ex) {
				SQLQuery.throwError(this, ex, SQL_ERR_QUERY);
			} finally {
				query.close();
			}

			changeTable(createTable("All trips that exist", rows.toArray(new String[0][]), columnNames));
			getTable().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent e) {
					jbtnEditTrip.setEnabled(true);
				}
			});
		} else if (actionCommand.compareTo(LABEL_TRIPS_ADD) == 0) {
			FormTrip createForm = new FormTrip(this, null, true);
			createForm.addFormListener(new FormListener<Trip>() {
				@Override
				public void formCompleted(FormEvent<Trip> e) {
					refreshTables();
				}
			});

			createForm.display();
		} else if (actionCommand.compareTo(LABEL_TRIPS_EDIT) == 0) {
			final int selectedRow = getTable().getSelectedRow();
			final int tripID = Integer.valueOf((String)getTable().getValueAt(selectedRow, 0));

			String sql = String.format("select t.OriginID, s1.Address, t.DestinationID, s2.Address "
							 + "from Trip t, Stop s1, Stop s2 "
							 + "where s1.StopID = t.OriginID AND s2.StopID = t.DestinationID AND t.TripID = %d;", tripID);

			Trip trip = null;
			SQLQuery query = SQLQuery.executeQuery(this, sql);
			try {
				if (query.rs.next()) {
					Stop origin = new Stop(query.rs.getInt(1), query.rs.getString(2));
					Stop destination = new Stop(query.rs.getInt(3), query.rs.getString(4));
					trip = new Trip(this, tripID, origin, destination);
				}
			} catch (SQLException ex) {
				SQLQuery.throwError(this, ex);
			} finally {
				query.close();
			}

			FormTrip editingForm = new FormTrip(this, trip, true);
			/*editingForm.addFormListener(new FormListener<Trip>() {
				@Override
				public void formCompleted(FormEvent<Trip> e) {
					refreshTables();
					getTable().getSelectionModel().setSelectionInterval(selectedRow, selectedRow);
				}
			});*/

			editingForm.display();
		}
	}
}

