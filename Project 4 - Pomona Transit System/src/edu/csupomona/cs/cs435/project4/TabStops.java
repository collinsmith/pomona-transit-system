package edu.csupomona.cs.cs435.project4;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedList;
import javax.swing.JButton;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class TabStops extends TransitTab implements ActionListener {
	protected JButton jbtnViewStops;
	protected JButton jbtnAddStop;
	protected JButton jbtnEditStop;

	public TabStops() {
		super("Create, edit, and manage stops");

		Insets i = new Insets(0, 0, 0, 0);

		jbtnViewStops = new JButton(LABEL_STOPS_VIEW);
		jbtnViewStops.setMargin(i);
		jbtnViewStops.addActionListener(this);
		addMenuComponent(jbtnViewStops);

		jbtnAddStop = new JButton(LABEL_STOPS_ADD);
		jbtnAddStop.setMargin(i);
		jbtnAddStop.addActionListener(this);
		addMenuComponent(jbtnAddStop);

		jbtnEditStop = new JButton(LABEL_STOPS_EDIT);
		jbtnEditStop.setMargin(i);
		jbtnEditStop.addActionListener(this);
		jbtnEditStop.setEnabled(false);
		addMenuComponent(jbtnEditStop);
	}

	@Override
	public void refreshTables() {
		jbtnViewStops.doClick(0);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		if (actionCommand.compareTo(LABEL_STOPS_VIEW) == 0) {
			if (jbtnEditStop != null) {
				jbtnEditStop.setEnabled(false);
			}

			String[] row = null;
			String[] columnNames = null;
			LinkedList<String[]> rows = new LinkedList<String[]>();
			SQLQuery query = SQLQuery.executeQuery(this, "select s.StopID, s.Address from Stop s;");
			try {
				ResultSetMetaData metaData = query.rs.getMetaData();
				columnNames = new String[metaData.getColumnCount()];
				for (int i = 0; i < columnNames.length; i++) {
					columnNames[i] = metaData.getColumnName(i+1);
				}

				while (query.rs.next()) {
					row = new String[] {
						query.rs.getString(1),
						query.rs.getString(2)
					};

					rows.add(row);
				}


			} catch (SQLException ex) {
				SQLQuery.throwError(this, ex);
			} finally {
				query.close();
			}

			changeTable(createTable("All stops that exist", rows.toArray(new String[0][]), columnNames));
			getTable().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent e) {
					jbtnEditStop.setEnabled(true);
				}
			});
		} else if (actionCommand.compareTo(LABEL_STOPS_ADD) == 0) {
			FormStop createForm = new FormStop(this, null, true);
			createForm.addFormListener(new FormListener<Stop>() {
				@Override
				public void formCompleted(FormEvent<Stop> e) {
					refreshTables();
				}
			});

			createForm.display();
		} else if (actionCommand.compareTo(LABEL_STOPS_EDIT) == 0) {
			final int selectedRow = getTable().getSelectedRow();
			final int stopID = Integer.valueOf((String)getTable().getValueAt(selectedRow, 0));

			String sql = String.format("select s.Address "
							 + "from Stop s "
							 + "where s.StopID = %d;", stopID);

			Stop stop = null;
			SQLQuery query = SQLQuery.executeQuery(this, sql);
			try {
				if (query.rs.next()) {
					stop = new Stop(stopID, query.rs.getString(1));
				}
			} catch (SQLException ex) {
				SQLQuery.throwError(this, ex);
			} finally {
				query.close();
			}

			FormStop editingForm = new FormStop(this, stop, true);
			/*editingForm.addFormListener(new FormListener<Stop>() {
				@Override
				public void formCompleted(FormEvent<Stop> e) {
					refreshTables();
					getTable().getSelectionModel().setSelectionInterval(selectedRow, selectedRow);
				}
			});*/

			editingForm.display();
		}
	}
}
