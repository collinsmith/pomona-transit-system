package edu.csupomona.cs.cs435.project4;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * This class represents a {@link Form} for a {@link TripOfferingQuery}.
 * It houses a {@link EditorTrip} and {@link EditorDate} which are used
 * to select a trip and date which is then searched and the results returned
 * to the user.
 *
 * @author Collin Smith
 */
public class FormTripOfferingSearch extends Form<TripOfferingQuery> implements ActionListener {
	protected EditorTrip tripEditor;
	protected EditorDate dateEditor;

	protected JButton jbtnCancel;
	protected JButton jbtnSearch;

	public FormTripOfferingSearch(TransitTab owner) {
		super(owner, "Search " + LABEL_TAB_TRIPOFFERS, "Input the following information");

		tripEditor = new EditorTrip(this, null, false);
		dateEditor = new EditorDate(null, false);
	}

	@Override
	public JPanel createCenterPanel() {
		JPanel p = new JPanel();
		GridBagConstraints c = new GridBagConstraints();
		p.setLayout(new GridBagLayout());

		c.gridx = 0;
		c.fill = GridBagConstraints.HORIZONTAL;

		c.gridy = 0;
		tripEditor.setBorder(BorderFactory.createTitledBorder(TABLE_TRIP_TRIPID));
		p.add(tripEditor, c);

		c.gridy = 1;
		dateEditor.setBorder(BorderFactory.createTitledBorder("Date"));
		p.add(dateEditor, c);

		return p;
	}

	@Override
	public JPanel createSouthernPanel() {
		JPanel p = new JPanel();

		jbtnCancel = new JButton(GENERIC_DIALOG_CANCEL);
		jbtnCancel.addActionListener(this);

		jbtnSearch = new JButton(GENERIC_DIALOG_SEARCH);
		jbtnSearch.addActionListener(this);
		getRootPane().setDefaultButton(jbtnSearch);

		p.add(jbtnCancel);
		p.add(jbtnSearch);

		return p;
	}

	@Override
	public boolean isFormCompleted() {
		return (tripEditor.areFieldsFilledOut() && dateEditor.areFieldsFilledOut());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		if (actionCommand.compareTo(GENERIC_DIALOG_CANCEL) == 0) {
			dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		} else if (actionCommand.compareTo(GENERIC_DIALOG_SEARCH) == 0) {
			if (!isFormCompleted()) {
				Form.throwUncompletedMessage(this);
				return;
			}

			Trip trip = tripEditor.getItem();
			TripDate date = dateEditor.getItem();
			dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
			dispatchFormEvent(new FormEvent<TripOfferingQuery>(new TripOfferingQuery(trip, date)));
		}
	}
}
