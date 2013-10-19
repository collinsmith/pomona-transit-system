package edu.csupomona.cs.cs435.project4;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ListIterator;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * This class represents a {@link Form} for a {@link Trip}. It
 * houses an {@link EditorTrip} which is used to input data which
 * is then committed into the SQL database.
 *
 * @author Collin Smith
 */
public class FormTrip extends Form<Trip> implements ActionListener {
	protected EditorTrip editor;

	protected JButton jbtnCancel;
	protected JButton jbtnFinished;

	public FormTrip(Component owner, Trip trip, boolean isEditorForm) {
		super(owner,
			trip == null ? "Create " + TABLE_TRIP_TRIPID : "Edit " + TABLE_TRIP_TRIPID,
			"Input an origin and create a path");

		editor = new EditorTrip(owner, trip, isEditorForm);
	}

	@Override
	public JPanel createCenterPanel() {
		return editor;
	}

	@Override
	public JPanel createSouthernPanel() {
		JPanel p = new JPanel();

		jbtnCancel = new JButton(GENERIC_DIALOG_CANCEL);
		jbtnCancel.addActionListener(this);

		jbtnFinished = new JButton(editor.initiallyNull ? GENERIC_DIALOG_CREATE : GENERIC_DIALOG_COMMIT);
		jbtnFinished.addActionListener(this);
		getRootPane().setDefaultButton(jbtnFinished);

		p.add(jbtnCancel);
		p.add(jbtnFinished);

		return p;
	}

	@Override
	public boolean isFormCompleted() {
		return editor.areFieldsFilledOut();
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

			Trip trip = editor.getItem();
			if (editor.initiallyNull) {
				SQLQuery.executeUpdate(this, String.format("insert into Trip (TripID, OriginID, DestinationID) values (%s, %s, %s);",
											 trip.id, trip.origin.id, trip.destination.id));

				int i = 0;
				Path p;
				ListIterator<Path> it = trip.stopList.listIterator();
				while (it.hasNext()) {
					i++;
					p = it.next();
					SQLQuery.executeUpdate(this, String.format("insert into TripStopInfo (TripID, StopNumber, StopID, DrivingTime) values (%s, %s, %s, '%s');",
												 trip.id, i, p.stop.id, SQLQuery.convertTimeCountToString(p.drivingTime)));
				}
			} else {
				//SQLQuery.executeUpdate(this, String.format("update Trip set OriginID = %s, DestinationID = %s where TripID = %s;",
				//							 trip.origin.id, trip.destination.id, trip.id));
			}

			dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
			dispatchFormEvent(new FormEvent<Trip>(trip));
		}
	}
}