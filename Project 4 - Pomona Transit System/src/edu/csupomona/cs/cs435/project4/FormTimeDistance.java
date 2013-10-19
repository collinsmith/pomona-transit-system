package edu.csupomona.cs.cs435.project4;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.sql.Time;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

/**
 * This class represents a {@link Form} for a {@link Distance}. It
 * houses structured which are used to input data which is then
 * committed into the SQL database.
 *
 * @author Collin Smith
 */
public class FormTimeDistance extends Form<Distance> implements ActionListener {
	protected JSpinner jsTime;

	protected JButton jbtnFinished;

	protected Distance distance;

	public FormTimeDistance(Component owner, Time time, Stop origin, Stop destination) {
		super(owner, "Time Interval", "How long does it take to go from " + origin.address + " to " + destination.address);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		distance = new Distance(origin, destination, null);
	}

	@Override
	public JPanel createCenterPanel() {
		JPanel p = new JPanel();
		p.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipadx = 4;
		c.gridy = 0;

		c.gridx = 0;
		c.weightx = 1.0;
		p.add(new JLabel("Time"), c);

		jsTime = new JSpinner(new SpinnerDateModel());
		JSpinner.DateEditor deDeparture = new JSpinner.DateEditor(jsTime, "H:mm");
		jsTime.setEditor(deDeparture);
		jsTime.setPreferredSize(Editor.fieldSize);
		jsTime.setValue(new Time(SQLQuery.GMT_OFFSET+300000));

		c.gridx = 1;
		c.weightx = 0.0;
		p.add(jsTime, c);
		c.gridy++;

		return p;
	}

	@Override
	public JPanel createSouthernPanel() {
		JPanel p = new JPanel();
		jbtnFinished = new JButton(GENERIC_DIALOG_OK);
		jbtnFinished.addActionListener(this);
		getRootPane().setDefaultButton(jbtnFinished);

		p.add(jbtnFinished);

		return p;
	}

	@Override
	public boolean isFormCompleted() {
		return true;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		if (actionCommand.compareTo(GENERIC_DIALOG_OK) == 0) {
			if (!isFormCompleted()) {
				Form.throwUncompletedMessage(this);
				return;
			}

			distance.time = new Time(((java.util.Date)jsTime.getModel().getValue()).getTime());
			distance.time.setSeconds(0);

			dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
			dispatchFormEvent(new FormEvent<Distance>(distance));
		}
	}
}
