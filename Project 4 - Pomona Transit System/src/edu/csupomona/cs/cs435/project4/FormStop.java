package edu.csupomona.cs.cs435.project4;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * This class represents a {@link Form} for a {@link Stop}. It
 * houses an {@link EditorStop} which is used to input data which
 * is then committed into the SQL database.
 *
 * @author Collin Smith
 */
public class FormStop extends Form<Stop> implements ActionListener {
	protected EditorStop editor;

	protected JButton jbtnCancel;
	protected JButton jbtnFinished;

	public FormStop(TransitTab owner, Stop stop, boolean isEdiorForm) {
		super(owner,
			stop == null ? "Create " + TABLE_STOP_STOPID : "Edit " + TABLE_STOP_STOPID,
			"Input an address");

		editor = new EditorStop(stop, isEdiorForm);
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

			Stop stop = editor.getItem();
			if (editor.initiallyNull) {
				SQLQuery.executeUpdate(this, String.format("insert into Stop (StopID, Address) values (%s, '%s');",
											 stop.id, stop.address));
			} else {
				//SQLQuery.executeUpdate(this, String.format("update Stop set Address = '%s' where StopID = %s;",
				//							 stop.address, stop.id));
			}

			dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
			dispatchFormEvent(new FormEvent<Stop>(stop));
		}
	}
}