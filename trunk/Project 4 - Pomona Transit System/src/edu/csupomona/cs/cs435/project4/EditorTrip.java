package edu.csupomona.cs.cs435.project4;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.LinkedList;
import java.util.ListIterator;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 * This class represents the {@link Editor} to be used when
 * editing a {@link Trip}.
 *
 * @author Collin Smith
 */
public class EditorTrip extends Editor<Trip> implements Constants {
	/**
	 * Component representation for the owner of this {@link Editor}.
	 * This is stored so that subforms have access to it.
	 */
	protected final Component owner;

	/**
	 * This object represents the selectable list of all {@link Trip}
	 * objects available.
	 */
	protected JComboBox<Trip> jcbTrip;

	protected LinkedList<Stop> stopList;

	protected JComboBox<Stop> origin;
	protected JTextField destination;

	protected JPanel stopsPanel;
	protected JTable path;
	protected JComboBox<Stop> newStop;
	protected JButton addNewStop;

	protected boolean disableEditing;

	public EditorTrip(Component owner, Trip trip, boolean isEditorForm) {
		super(trip, isEditorForm, false);

		this.owner = owner;
	}

	@Override
	public void addEditorForm() {
		if (initiallyNull) {
			item = new Trip(owner, Trip.getNextTripID(this), null, null);
		}

		origin = new JComboBox<Stop>();
		stopList = Stop.getStopList(this);

		Stop s;
		ListIterator<Stop> it = stopList.listIterator();
		while (it.hasNext()) {
			s = it.next();
			origin.addItem(s);
		}

		if (item.origin != null) {
			origin.setSelectedItem(item.origin);
			origin.setEnabled(false);
		} else {
			origin.setSelectedIndex(-1);
		}

		destination = new JTextField();
		destination.setEditable(false);
		destination.setPreferredSize(fieldSize);
		if (item.destination != null) {
			destination.setText(item.destination.address);
		}

		c.gridx = 0;
		c.weightx = 0.0;
		add(new JLabel(TABLE_TRIP_ORIGIN), c);

		c.gridx = 1;
		c.weightx = 1.0;
		add(origin, c);
		c.gridy++;

		c.gridx = 0;
		c.weightx = 0.0;
		add(new JLabel(TABLE_TRIP_DESTINATION), c);

		c.gridx = 1;
		c.weightx = 1.0;
		add(destination, c);
		c.gridy++;

		stopsPanel = createStopsPanel();
		stopsPanel.setBorder(BorderFactory.createTitledBorder("Stops"));

		c.gridwidth = 2;
		c.gridx = 0;
		c.weightx = 1.0;
		add(stopsPanel, c);
		c.gridy++;

		origin.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() != ItemEvent.SELECTED) {
					return;
				}

				item.origin = (Stop)e.getItem();
				((DefaultTableModel)path.getModel()).setValueAt(item.origin.address, 0, 0);
				origin.setEnabled(false);
			}
		});
	}

	private JPanel createStopsPanel() {
		JPanel p = new JPanel();
		p.setLayout(new GridBagLayout());
		GridBagConstraints c2 = new GridBagConstraints();
		c2.fill = GridBagConstraints.HORIZONTAL;
		c2.ipadx = 4;

		LinkedList<String[]> rows = new LinkedList<String[]>();
		rows.add(new String[] {item.origin == null ? "null" : item.origin.address, "+0:00"});

		long totalTime = 0;
		item.cacheStopList(owner);
		if (!item.stopList.isEmpty()) {
			Path tempPath;
			ListIterator<Path> it = item.stopList.listIterator();
			while (it.hasNext()) {
				tempPath = it.next();
				totalTime += tempPath.drivingTime;
				rows.add(new String[] {tempPath.stop.address, "+" + SQLQuery.convertTimeCountToString(totalTime)});
			}
		}

		path = new JTable(new DefaultTableModel(rows.toArray(new String[0][]), new String[] { "Address", "Time Elapsed" })) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		path.setAutoCreateRowSorter(true);
		path.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		path.getTableHeader().setReorderingAllowed(false);

		c2.gridwidth = 2;
		c2.gridx = 0;
		c2.gridy = 0;
		c2.weightx = 1.0;
		p.add(new JScrollPane(path), c2);
		c2.gridy++;

		stopList = Stop.getStopList(this);

		newStop = new JComboBox<Stop>();
		Stop s;
		ListIterator<Stop> it = stopList.listIterator();
		while (it.hasNext()) {
			s = it.next();
			if (item.origin != null && item.origin.id == s.id) {
				continue;
			}

			newStop.addItem(s);
		}

		c2.gridwidth = 1;
		c2.gridx = 0;
		c2.weightx = 1.0;
		p.add(newStop, c2);

		addNewStop = new JButton("Add");
		addNewStop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Stop s = (Stop)newStop.getSelectedItem();
				item.addPath(null, s);
				((DefaultTableModel)path.getModel()).addRow(new String[] {s.address, "+" + SQLQuery.convertTimeCountToString(item.totalTime)});
				destination.setText(item.destination.address);
			}
		});

		if (!initiallyNull || disableEditing) {
			addNewStop.setEnabled(false);
			newStop.setEnabled(false);
		}

		c2.gridx = 1;
		c2.weightx = 0.0;
		p.add(addNewStop, c2);

		return p;

	}

	@Override
	public void addSelectorForm() {
		if (initiallyNull) {
			item = new Trip(owner, Trip.getNextTripID(this), null, null);
		}

		Trip[] trips = Trip.getTripArray(this);
		jcbTrip = new JComboBox<Trip>(trips);
		if (initiallyNull) {
			jcbTrip.setSelectedIndex(-1);
		} else {
			jcbTrip.setSelectedItem(item);
		}

		c.gridx = 0;
		c.weightx = 0.0;
		add(new JLabel(TABLE_TRIP_TRIPID), c);

		c.gridx = 1;
		c.weightx = 1.0;
		add(jcbTrip, c);
		c.gridy++;

		disableEditing = true;
		stopsPanel = createStopsPanel();
		stopsPanel.setBorder(BorderFactory.createTitledBorder("Stops"));

		c.gridwidth = 2;
		c.gridx = 0;
		c.weightx = 1.0;
		add(stopsPanel, c);

		jcbTrip.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() != ItemEvent.SELECTED) {
					return;
				}

				item = (Trip)e.getItem();
				((DefaultTableModel)path.getModel()).setValueAt(item.origin.address, 0, 0);

				remove(stopsPanel);
				item.destination = null;
				stopsPanel = createStopsPanel();
				stopsPanel.setBorder(BorderFactory.createTitledBorder("Stops"));

				c.gridwidth = 2;
				c.gridx = 0;
				c.weightx = 1.0;
				add(stopsPanel, c);
				revalidate();

				ItemListener[] listeners = jcbTrip.getItemListeners();
				for (ItemListener l : listeners) {
					if (!l.equals(this)) {
						l.itemStateChanged(new ItemEvent(jcbTrip, e.getID(), e.getItem(), e.getStateChange()));
					}
				}
			}
		});
	}

	@Override
	public boolean areFieldsFilledOut() {
		return item != null && item.origin != null && item.destination != null;
	}

	@Override
	public void refreshItem() {
		//...
	}

	@Override
	public Trip getItem() {
		refreshItem();
		return item;
	}
}