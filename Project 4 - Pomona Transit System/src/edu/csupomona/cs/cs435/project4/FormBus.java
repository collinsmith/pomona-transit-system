package edu.csupomona.cs.cs435.project4;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * This class represents a {@link Form} for a {@link Bus}. It
 * houses an {@link EditorBus} which is used to input data which
 * is then committed into the SQL database.
 *
 * @author Collin Smith
 */
public class FormBus extends Form<Bus> implements ActionListener {
	protected EditorBus editor;

	protected JButton jbtnCancel;
	protected JButton jbtnFinished;

	public FormBus(Component owner, Bus bus, boolean isCustomInput, boolean isDeleteOperation) {
		super(owner,
			isDeleteOperation ? "Delete " + TABLE_BUS_BUSID : bus == null ? "Create " + TABLE_BUS_BUSID : "Edit " + TABLE_BUS_BUSID,
			"Input a model and year");

		editor = new EditorBus(bus, isCustomInput, isDeleteOperation);
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

			Bus bus = editor.getItem();
			if (editor.isDeleteOperation) {
				SQLQuery.executeUpdate(this, String.format("update TripOffering set BusID = %s where BusID = %s;",
												bus.id, editor.oldItem.id));

				SQLQuery.executeUpdate(this, "delete from Bus where BusID = " + editor.oldItem.id + ";");
			} else if (editor.initiallyNull) {
				SQLQuery.executeUpdate(this, String.format("insert into Bus (BusID, Model, Year) values (%s, '%s', '%s');",
												bus.id, bus.model, bus.year));
			} else {
				//SQLQuery.executeUpdate(this, String.format("update Bus set Model = '%s', Year = '%s' where BusID = %s;",
				//							 bus.model, bus.year, bus.id));
			}

			dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
			dispatchFormEvent(new FormEvent<Bus>(bus));
		}
	}
}