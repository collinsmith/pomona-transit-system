package edu.csupomona.cs.cs435.project4;

import java.sql.Date;
import java.util.Calendar;
import javax.swing.JPanel;
import net.sourceforge.jdatepicker.DateModel;
import net.sourceforge.jdatepicker.JDateComponentFactory;
import net.sourceforge.jdatepicker.JDatePicker;
import net.sourceforge.jdatepicker.impl.SqlDateModel;

/**
 * This class represents the {@link Editor} to be used when
 * editing a {@link TripDate}.
 * 
 * @author Collin Smith
 */
public class EditorDate extends Editor<TripDate> implements Constants {
	/**
	 * JDatePicker representation for a calendar object which
	 * can select various dates.
	 */
	protected JDatePicker datePicker;

	/**
	 * Constructor which initializes a {@link TripDate} {@link Editor} with
	 * the following default parameters.
	 * 
	 * @param date		{@link Editor#item}. {@code null} for todays date.
	 * @param isEditorForm	{@link Editor#isEditorForm}
	 */
	public EditorDate(TripDate date, boolean isEditorForm) {
		super(date, isEditorForm, false);
	}

	/**
	 * @see Editor#addEditorForm()
	 */
	@Override
	public void addEditorForm() {
		addForms();
	}

	/**
	 * @see Editor#addSelectorForm()
	 */
	@Override
	public void addSelectorForm() {
		addForms();
	}

	/**
	 * Creates and formats {@link EditorDate#datePicker} and adds it
	 * using {@link Editor#addField(java.lang.String, javax.swing.JComponent)}.
	 */
	private void addForms() {
		DateModel model = new SqlDateModel(item == null ? new Date(Calendar.getInstance().getTime().getTime()) : item.date);
		datePicker = JDateComponentFactory.createJDatePicker(model, new DateComponentFormatterShort());
		datePicker.setDoubleClickAction(true);

		addField(TABLE_TRIPOFFER_DATE, (JPanel)datePicker);
	}

	/**
	 * @see Editor#areFieldsFilledOut()
	 */
	@Override
	public boolean areFieldsFilledOut() {
		return datePicker.getModel().isSelected();
	}

	/**
	 * @see Editor#refreshItem()
	 */
	@Override
	public void refreshItem() {
		DateModel date = datePicker.getModel();
		if (item == null) {
			item = new TripDate(new Date(date.getYear()-1900, date.getMonth(), date.getDay()));
		} else {
			item.date = new Date(date.getYear()-1900, date.getMonth(), date.getDay());
		}
	}

	/**
	 * @see Editor#getItem()
	 */
	@Override
	public TripDate getItem() {
		refreshItem();
		return item;
	}
}
