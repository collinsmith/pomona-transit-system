package edu.csupomona.cs.cs435.project4;

import java.text.ParseException;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

/**
 * This class represents the {@link Editor} to be used when
 * editing a {@link Driver}.
 * 
 * @author Collin Smith
 */
public class EditorDriver extends Editor<Driver> implements Constants {
	/**
	 * Represents either a {@link JTextField} or a {@link JComboBox} of
	 * all {@link Driver}s to be used.
	 */
	protected JComponent name;
	
	/**
	 * String representation for the {@link Driver#phoneNumber} of this
	 * {@link Driver}. The value of this variable should only be a maximum
	 * of 10 characters in "(###) ###-####" format.
	 */
	protected JComponent phoneNumber;

	/**
	 * Constructor which initializes a {@link Driver} {@link Editor} with
	 * the following default parameters.
	 * 
	 * @param driver			{@link Editor#item}. {@code null} for no default item.
	 * @param isEditorForm		{@link Editor#isEditorForm}
	 * @param isDeleteOperation	{@link Editor#isDeleteOperation}
	 */
	public EditorDriver(Driver driver, boolean isEditorForm, boolean isDeleteOperation) {
		super(driver, isEditorForm, isDeleteOperation);
	}

	/**
	 * @see Editor#addEditorForm()
	 */
	@Override
	public void addEditorForm() {
		if (initiallyNull) {
			name = new JTextField();
		} else {
			name = new JTextField(item.name);
			name.setEnabled(false);
		}

		name.setPreferredSize(fieldSize);
		addField(TABLE_DRIVER_NAME, name);

		try {
			MaskFormatter formatter = new MaskFormatter("(###) ###-####");
			phoneNumber = new JFormattedTextField(formatter);
			phoneNumber.setPreferredSize(fieldSize);
			if (!initiallyNull) {
				((JFormattedTextField)phoneNumber).setText(item.phoneNumber);
				phoneNumber.setEnabled(false);
			}

			addField(TABLE_DRIVER_PHONE, phoneNumber);
		} catch (ParseException e) {
		}
	}
	
	/**
	 * @see Editor#addSelectorForm()
	 */
	@Override
	public void addSelectorForm() {
		Driver[] drivers = Driver.getDriverArray(this);
		JComboBox<Driver> jcbDriver = new JComboBox<Driver>(drivers);
		jcbDriver.setPreferredSize(fieldSize);
		jcbDriver.setSelectedIndex(-1);
		if (!initiallyNull) {
			for (int i = 0; i < drivers.length; i++) {
				if (drivers[i].name.compareTo(item.name) == 0) {
					jcbDriver.setSelectedIndex(i);
				}
			}
		}

		name = jcbDriver;
		addField(TABLE_DRIVER_NAME, name);
	}

	/**
	 * @see Editor#areFieldsFilledOut()
	 */
	@Override
	public boolean areFieldsFilledOut() {
		if (isEditorForm) {
			JTextField jtfName = (JTextField)name;
			JFormattedTextField jftfPhoneNumber = (JFormattedTextField)phoneNumber;
			return (!jtfName.getText().isEmpty() && !jftfPhoneNumber.getText().replaceAll("[^0-9]", "").isEmpty());
		} else {
			return ((JComboBox)name).getSelectedIndex() != -1;
		}
	}

	/**
	 * @see Editor#refreshItem()
	 */
	@Override
	public void refreshItem() {
		if (isEditorForm) {
			JTextField jtfName = (JTextField)name;
			JFormattedTextField jftfPhoneNumber = (JFormattedTextField)phoneNumber;
			if (item == null) {
				item = new Driver(jtfName.getText(), jftfPhoneNumber.getText().replaceAll("[^0-9]", ""));
			} else {
				item.name = jtfName.getText();
				item.phoneNumber = jftfPhoneNumber.getText().replaceAll("[^0-9]", "");
			}
		} else {
			JComboBox<Bus> jcbDriver = (JComboBox)name;
			Driver driver = (Driver)jcbDriver.getSelectedItem();
			if (item == null) {
				item = driver;
			} else {
				item.name = driver.name;
				item.phoneNumber = driver.phoneNumber;
			}
		}
	}

	/**
	 * @see Editor#getItem()
	 */
	@Override
	public Driver getItem() {
		refreshItem();
		return item;
	}
}