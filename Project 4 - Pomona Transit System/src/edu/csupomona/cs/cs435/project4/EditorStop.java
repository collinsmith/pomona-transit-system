package edu.csupomona.cs.cs435.project4;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;

/**
 * This class represents the {@link Editor} to be used when
 * editing a {@link Stop}.
 * 
 * @author Collin Smith
 */
public class EditorStop extends Editor<Stop> implements Constants {
	/**
	 * Represents either a {@link JTextField} for the {@link Stop#address}
	 * of a {@link Stop}, or a {@link JComboBox} of all Stops being used.
	 */
	protected JComponent address;

	/**
	 * Constructor which initializes a {@link Stop} {@link Editor} with
	 * the following default parameters.
	 * 
	 * @param stop			{@link Editor#item}. {@code null} for no default item.
	 * @param isEditorForm		{@link Editor#isEditorForm}
	 */
	public EditorStop(Stop stop, boolean isEditorForm) {
		super(stop, isEditorForm, false);
	}

	/**
	 * @see Editor#addEditorForm()
	 */
	@Override
	public void addEditorForm() {
		if (initiallyNull) {
			address = new JTextField();
		} else {
			address = new JTextField(item.address);
			address.setEnabled(false);
		}

		address.setPreferredSize(fieldSize);
		addField(TABLE_STOP_ADDRESS, address);
	}

	/**
	 * @see Editor#addSelectorForm()
	 */
	@Override
	public void addSelectorForm() {
		Stop[] stops = Stop.getStopArray(this);
		JComboBox<Stop> jcbStop = new JComboBox<Stop>(stops);
		jcbStop.setPreferredSize(fieldSize);
		jcbStop.setSelectedIndex(-1);
		if (!initiallyNull) {
			for (int i = 0; i < stops.length; i++) {
				if (stops[i].id == item.id) {
					jcbStop.setSelectedIndex(i);
				}
			}
		}

		address = jcbStop;
		addField(TABLE_STOP_ADDRESS, address);
	}

	/**
	 * @see Editor#areFieldsFilledOut()
	 */
	@Override
	public boolean areFieldsFilledOut() {
		if (isEditorForm) {
			JTextField jtfAddress = (JTextField)address;
			return (!jtfAddress.getText().isEmpty());
		} else {
			return ((JComboBox)address).getSelectedIndex() != -1;
		}
	}

	/**
	 * @see Editor#refreshItem()
	 */
	@Override
	public void refreshItem() {
		if (isEditorForm) {
			JTextField jtfAddress = (JTextField)address;
			if (item == null) {
				item = new Stop(Stop.getNextStopID(this), jtfAddress.getText());
			} else {
				item.address = jtfAddress.getText();
			}
		} else {
			JComboBox<Bus> jcbStop = (JComboBox)address;
			Stop stop = (Stop)jcbStop.getSelectedItem();
			if (item == null) {
				item = stop;
			} else {
				item.id = stop.id;
				item.address = stop.address;
			}
		}
	}

	/**
	 * @see Editor#getItem()
	 */
	@Override
	public Stop getItem() {
		refreshItem();
		return item;
	}
}