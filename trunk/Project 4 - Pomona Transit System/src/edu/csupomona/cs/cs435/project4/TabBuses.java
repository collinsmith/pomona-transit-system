package edu.csupomona.cs.cs435.project4;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedList;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class TabBuses extends TransitTab implements ActionListener {
	protected JButton jbtnViewBuses;
	protected JButton jbtnAddBus;
	protected JButton jbtnDeleteBus;
	protected JButton jbtnEditBus;

	public TabBuses() {
		super("Create, edit, and manage the different buses");

		Insets i = new Insets(0, 0, 0, 0);

		jbtnViewBuses = new JButton(LABEL_BUSES_VIEW);
		jbtnViewBuses.setMargin(i);
		jbtnViewBuses.addActionListener(this);
		addMenuComponent(jbtnViewBuses);

		jbtnAddBus = new JButton(LABEL_BUSES_ADD);
		jbtnAddBus.setMargin(i);
		jbtnAddBus.addActionListener(this);
		addMenuComponent(jbtnAddBus);

		jbtnDeleteBus = new JButton(LABEL_BUSES_DELETE);
		jbtnDeleteBus.setMargin(i);
		jbtnDeleteBus.addActionListener(this);
		jbtnDeleteBus.setEnabled(false);
		addMenuComponent(jbtnDeleteBus);

		jbtnEditBus = new JButton(LABEL_BUSES_EDIT);
		jbtnEditBus.setMargin(i);
		jbtnEditBus.addActionListener(this);
		jbtnEditBus.setEnabled(false);
		addMenuComponent(jbtnEditBus);
	}

	@Override
	public void refreshTables() {
		jbtnViewBuses.doClick(0);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		if (actionCommand.compareTo(LABEL_BUSES_VIEW) == 0) {
			if (jbtnDeleteBus != null) {
				jbtnDeleteBus.setEnabled(false);
			}

			if (jbtnEditBus != null) {
				jbtnEditBus.setEnabled(false);
			}

			String[] row = null;
			String[] columnNames = null;
			LinkedList<String[]> rows = new LinkedList<String[]>();
			SQLQuery query = SQLQuery.executeQuery(this, "select b.BusID, b.Model, b.Year from Bus b;");
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
				SQLQuery.throwError(this, ex);
			} finally {
				query.close();
			}

			changeTable(createTable("All buses that exist", rows.toArray(new String[0][]), columnNames));
			getTable().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent e) {
					jbtnDeleteBus.setEnabled(true);
					jbtnEditBus.setEnabled(true);
				}
			});
		} else if (actionCommand.compareTo(LABEL_BUSES_ADD) == 0) {
			FormBus createForm = new FormBus(this, null, true, false);
			createForm.addFormListener(new FormListener<Bus>() {
				@Override
				public void formCompleted(FormEvent<Bus> e) {
					refreshTables();
				}
			});

			createForm.display();
		} else if (actionCommand.compareTo(LABEL_BUSES_DELETE) == 0) {
			jbtnDeleteBus.setEnabled(false);
			jbtnEditBus.setEnabled(false);

			int selectedRow = getTable().getSelectedRow();
			final int busID = Integer.valueOf((String)getTable().getValueAt(selectedRow, 0));
			String busName = (String)getTable().getValueAt(selectedRow, 1);
			String busModel = (String)getTable().getValueAt(selectedRow, 2);

			String sql = String.format("select count(to.BusID) "
							 + "from TripOffering to "
							 + "where to.BusID = %d "
							 + "group by to.BusID;", busID);

			int count = 0;
			SQLQuery query = SQLQuery.executeQuery(this, sql);
			try {
				if (query.rs.next()) {
					count = Integer.valueOf(query.rs.getString(1));
				}
			} catch (SQLException ex) {
				SQLQuery.throwError(this, ex);
			} finally {
				query.close();
			}

			if (count > 0) {
				Object[] options = {"Yes", "No"};
				int selection = JOptionPane.showOptionDialog(this,
											   busName + " is referenced in " + count + " trip offerings. Still delete it?",
											   "Error",
											   JOptionPane.YES_NO_OPTION,
											   JOptionPane.QUESTION_MESSAGE,
											   null,
											   options,
											   options[1]);

				switch (selection) {
					case JOptionPane.NO_OPTION:
						return;
					case JOptionPane.YES_OPTION:
						options = new Object[] {"Yes, replace bus", "No, just delete them"};
						selection = JOptionPane.showOptionDialog(this,
												     "Do you want to replace trip offerings with this bus with another one?",
												     "Error",
												     JOptionPane.YES_NO_OPTION,
												     JOptionPane.QUESTION_MESSAGE,
												     null,
												     options,
												     options[1]);

						switch (selection) {
							case JOptionPane.NO_OPTION:
								sql = String.format("delete from Bus where BusID = %d;", busID);
								SQLQuery.executeUpdate(this, sql);
								refreshTables();
								break;
							case JOptionPane.YES_OPTION:
								final TabBuses owner = this;
								FormBus deleteForm = new FormBus(this, new Bus(busID, busName, busModel), false, true);
								deleteForm.addFormListener(new FormListener<Bus>() {
									@Override
									public void formCompleted(FormEvent<Bus> e) {
										refreshTables();
									}
								});

								deleteForm.display();
								break;
						}

						break;
				}
			} else {
				sql = String.format("delete from Bus where BusID = %d;", busID);
				SQLQuery.executeUpdate(this, sql);
				refreshTables();
			}
		} else if (actionCommand.compareTo(LABEL_BUSES_EDIT) == 0) {
			final int selectedRow = getTable().getSelectedRow();
			final int busID = Integer.valueOf((String)getTable().getValueAt(selectedRow, 0));

			String sql = String.format("select b.Model, b.Year "
							 + "from Bus b "
							 + "where b.BusID = %d;", busID);

			Bus bus = null;
			SQLQuery query = SQLQuery.executeQuery(this, sql);
			try {
				if (query.rs.next()) {
					bus = new Bus(busID, query.rs.getString(1), query.rs.getString(2));
				}
			} catch (SQLException ex) {
				SQLQuery.throwError(this, ex, SQL_ERR_QUERY);
			} finally {
				query.close();
			}

			FormBus editingForm = new FormBus(this, bus, true, false);
			/*editingForm.addFormListener(new FormListener<Bus>() {
				@Override
				public void formCompleted(FormEvent<Bus> e) {
					refreshTables();
					getTable().getSelectionModel().setSelectionInterval(selectedRow, selectedRow);
				}
			});*/

			editingForm.display();
		}
	}
}
