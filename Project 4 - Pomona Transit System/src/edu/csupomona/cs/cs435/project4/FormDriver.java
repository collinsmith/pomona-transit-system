package edu.csupomona.cs.cs435.project4;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * This class represents a {@link Form} for a {@link Driver}. It
 * houses an {@link EditorDriver} which is used to input data which
 * is then committed into the SQL database.
 *
 * @author Collin Smith
 */
public class FormDriver extends Form<Driver> implements ActionListener {
	protected EditorDriver editor;

	protected JButton jbtnCancel;
	protected JButton jbtnFinished;

	public FormDriver(Component owner, Driver driver, boolean isEditorForm) {
		super(owner,
			driver == null ? "Create " + TABLE_DRIVER_DRIVERID : "Edit " + TABLE_DRIVER_DRIVERID,
			"Input a name and phone number");

		editor = new EditorDriver(driver, isEditorForm, false);
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

			Driver driver = editor.getItem();
			if (editor.initiallyNull) {
				SQLQuery.executeUpdate(this, String.format("insert into Driver (DriverName, PhoneNumber) values ('%s', '%s');",
											 driver.name, driver.phoneNumber));
			} else {
				//SQLQuery.executeUpdate(this, String.format("update Driver set PhoneNumber = '%s' where DriverName like '%s';",
				//							   driver.phoneNumber, driver.name));
			}

			dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
			dispatchFormEvent(new FormEvent<Driver>(driver));
		}
	}
}