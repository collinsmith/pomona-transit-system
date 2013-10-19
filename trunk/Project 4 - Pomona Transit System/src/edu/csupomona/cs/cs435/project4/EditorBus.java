package edu.csupomona.cs.cs435.project4;

import java.text.ParseException;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

/**
 * This class represents the {@link Editor} to be used when
 * editing a {@link Bus}.
 * 
 * @author Collin Smith
 */
public class EditorBus extends Editor<Bus> implements Constants {
	/**
	 * Represents either a {@link JTextField} or a {@link JComboBox} of
	 * all {@link Bus}es to be used.
	 */
	protected JComponent model;
	
	/**
	 * Represents a {@link JFormattedTextField} to be used when entering
	 * the year of a {@link Bus} in YYYY format.
	 */
	protected JComponent year;

	/**
	 * Constructor which initializes a {@link Bus} {@link Editor} with
	 * the following default parameters.
	 * 
	 * @param bus			{@link Editor#item}. {@code null} for no default item.
	 * @param isCustomInput		{@link Editor#isEditorForm}
	 * @param isDeleteOperation	{@link Editor#isDeleteOperation}
	 */
	public EditorBus(Bus bus, boolean isCustomInput, boolean isDeleteOperation) {
		super(bus, isCustomInput, isDeleteOperation);
	}

	/**
	 * @see Editor#addEditorForm()
	 */
	@Override
	public void addEditorForm() {
		if (initiallyNull) {
			model = new JTextField();
		} else {
			model = new JTextField(item.model);
			model.setEnabled(false);
		}

		model.setPreferredSize(fieldSize);

		addField(TABLE_BUS_MODEL, model);

		try {
			MaskFormatter formatter = new MaskFormatter("####");
			formatter.setPlaceholderCharacter('Y');
			year = new JFormattedTextField(formatter);
			if (!initiallyNull) {
				((JFormattedTextField)year).setText(item.year);
				year.setEnabled(false);
			}

			year.setPreferredSize(fieldSize);
			addField(TABLE_BUS_YEAR, year);
		} catch (ParseException e) {
		}
	}

	/**
	 * @see Editor#addSelectorForm()
	 */
	@Override
	public void addSelectorForm() {
		JComboBox<Bus> jcbBus;
		Bus[] buses = Bus.getBusArray(this);
		if (isDeleteOperation) {
			jcbBus = new JComboBox<Bus>();
			jcbBus.setPreferredSize(fieldSize);
			for (Bus b : buses) {
				if (b.id != item.id) {
					jcbBus.addItem(b);
				}
			}
		} else {
			jcbBus = new JComboBox<Bus>(buses);
			jcbBus.setPreferredSize(fieldSize);
			jcbBus.setSelectedIndex(-1);
			if (item != null) {
				for (int i = 0; i < buses.length; i++) {
					if (buses[i].id == item.id) {
						jcbBus.setSelectedIndex(i);
					}
				}
			}
		}

		model = jcbBus;
		addField(TABLE_BUS_BUSID, model);
	}

	/**
	 * @see Editor#areFieldsFilledOut()
	 */
	@Override
	public boolean areFieldsFilledOut() {
		if (isEditorForm) {
			JTextField jtfModel = (JTextField)model;
			JFormattedTextField jftfYear = (JFormattedTextField)year;
			return (!jtfModel.getText().isEmpty() && !jftfYear.getText().replaceAll("[^0-9]", "").isEmpty());
		} else {
			return ((JComboBox)model).getSelectedIndex() != -1;
		}
	}

	/**
	 * @see Editor#refreshItem()
	 */
	@Override
	public void refreshItem() {
		if (isEditorForm) {
			JTextField jtfModel = (JTextField)model;
			JFormattedTextField jftfYear = (JFormattedTextField)year;
			if (item == null) {
				item = new Bus(Bus.getNextBusID(this), jtfModel.getText(), jftfYear.getText().replaceAll("[^0-9]", ""));
			} else {
				item.model = jtfModel.getText();
				item.year = jftfYear.getText().replaceAll("[^0-9]", "");
			}
		} else {
			JComboBox<Bus> jcbBus = (JComboBox)model;
			Bus bus = (Bus)jcbBus.getSelectedItem();
			if (item == null) {
				item = bus;
			} else {
				item.id = bus.id;
				item.model = bus.model;
				item.year = bus.year;
			}
		}
	}

	/**
	 * @see Editor#getItem()
	 */
	@Override
	public Bus getItem() {
		refreshItem();
		return item;
	}
}
