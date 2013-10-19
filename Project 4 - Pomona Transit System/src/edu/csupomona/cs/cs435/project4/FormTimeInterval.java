package edu.csupomona.cs.cs435.project4;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.text.NumberFormatter;

public class FormTimeInterval extends Form<ActualTripStopInfo> implements ActionListener {
	protected EditorTimeInterval timeIntervalEditor;

	protected PassengerManifest passengers;

	protected JSpinner jsOn;
	protected JSpinner jsOff;

	protected JButton jbtnFinished;

	public FormTimeInterval(Component owner, String title, TimeInterval timeInterval, PassengerManifest passengers) {
		super(owner, title, title);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		timeIntervalEditor = new EditorTimeInterval(timeInterval, true);

		if (passengers == null) {
			passengers = new PassengerManifest(0, 0);
		}

		this.passengers = passengers;
	}

	@Override
	public JPanel createCenterPanel() {
		JPanel p = new JPanel();
		p.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;

		c.gridwidth = 2;
		c.gridx = 0;
		c.weightx = 1.0;
		p.add(timeIntervalEditor, c);
		c.gridwidth = 1;

		c.gridy++;

		c.gridx = 0;
		c.weightx = 1.0;
		p.add(new JLabel("Passengers On"), c);

		c.gridx = 1;
		c.weightx = 0.0;
		SpinnerNumberModel numSpinOn = new SpinnerNumberModel(passengers.on, 0, Integer.MAX_VALUE, 1);
		jsOn = new JSpinner(numSpinOn);
		jsOn.setPreferredSize(Editor.fieldSize);
		JFormattedTextField txtOn = ((JSpinner.NumberEditor)jsOn.getEditor()).getTextField();
		((NumberFormatter)txtOn.getFormatter()).setAllowsInvalid(false);
		p.add(jsOn, c);

		c.gridy++;

		c.gridx = 0;
		c.weightx = 1.0;
		p.add(new JLabel("Passengers Off"), c);

		c.gridx = 1;
		c.weightx = 0.0;
		SpinnerNumberModel numSpinOff = new SpinnerNumberModel(passengers.off, 0, Integer.MAX_VALUE, 1);
		jsOff = new JSpinner(numSpinOff);
		jsOff.setPreferredSize(Editor.fieldSize);
		JFormattedTextField txtOff = ((JSpinner.NumberEditor)jsOff.getEditor()).getTextField();
		((NumberFormatter)txtOff.getFormatter()).setAllowsInvalid(false);
		p.add(jsOff, c);

		c.gridy++;

		return p;
	}

	@Override
	public JPanel createSouthernPanel() {
		JPanel p = new JPanel();

		jbtnFinished = new JButton(GENERIC_DIALOG_COMMIT);
		jbtnFinished.addActionListener(this);
		getRootPane().setDefaultButton(jbtnFinished);

		p.add(jbtnFinished);

		return p;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		passengers.on = (int)jsOn.getValue();
		passengers.off = (int)jsOff.getValue();
		dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		dispatchFormEvent(new FormEvent<ActualTripStopInfo>(new ActualTripStopInfo(-1, null, timeIntervalEditor.getItem(), null, passengers)));
	}

	@Override
	public boolean isFormCompleted() {
		return true;
	}

}
