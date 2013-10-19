package edu.csupomona.cs.cs435.project4;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * This class represents an abstract editing panel used
 * throughout this program. An abstract editor maintains
 * an object of type <E>, which is edited under specified
 * conditions (either through custom user input, or by
 * selecting from a list), and then returned to the form
 * upon completion.
 *
 * @author Collin Smith
 * @param <E>	The object type that is being edited.
 */
public abstract class Editor<E extends TransitTable<E>> extends JPanel {
	/**
	 * Creates and adds the specialized forms for editing
	 * an abstract editor.
	 */
	public abstract void addEditorForm();

	/**
	 * Creates an adds the specialized forms for selecting
	 * an abstract object.
	 */
	public abstract void addSelectorForm();

	/**
	 * Returns whether or not the editor is completed.
	 *
	 * @return		{@code True} for completed, {@code false} otherwise.
	 */
	public abstract boolean areFieldsFilledOut();

	/**
	 * Refreshes the internal item to represent changes made
	 * to editor components.
	 */
	public abstract void refreshItem();

	/**
	 * Returns the current item represented by this editor. This
	 * method should also call {@link Editor#refreshItem()}.
	 *
	 * @return		{@link Editor#item}
	 */
	public abstract E getItem();

	/**
	 * Dimensional representation for the size of an editing component.
	 */
	public static final Dimension fieldSize = new Dimension(202, 20);

	/**
	 * This object represents the item that is being edited by this editor.
	 */
	protected E item;

	/**
	 * This object represents the initial state of the edited object. This
	 * object is only assigned a value if {@link Editor#item} is not {@code null},
	 * and uses {@link TransitTable#copy()} to clone the object.
	 */
	protected E oldItem;

	/**
	 * Boolean representation for whether or not this editor is being used as
	 * an editor form or a selector form.
	 */
	protected boolean isEditorForm;

	/**
	 * Boolean representation for whether or not {@link Editor#item} is
	 * initially {@code null}.
	 */
	protected boolean initiallyNull;

	/**
	 * Boolean representation for whether or not this editor is being used
	 * for deletion. In this case, {@link Editor#item} is used as the item that
	 * is being deleted, and will be excluded from the selector. It is asserted
	 * that if this is {@code true} that {@link Editor#isEditorForm} is also
	 * {@code true}.
	 */
	protected boolean isDeleteOperation;

	/**
	 * This variable represents the constraints used in the {@link GridBagLayout}
	 * of this editor.
	 */
	protected GridBagConstraints c;

	/**
	 * Constructor which initialized an {@link Editor} with the following default
	 * parameters as fields. Additionally, this constructor will create the JPanel
	 * and begin forming the layout manager, as well as call either {@link Editor#addEditorForm()}
	 * or {@link Editor#addSelectorForm()} depending on whether or not {@link Editor#isEditorForm} is
	 * {@code true}.
	 *
	 * @param item			The default item to be used or deleted. {@link Editor#item} {@link Editor#oldItem}
	 * @param isEditorForm		{@link Editor#isEditorForm}
	 * @param isDeleteOperation	{@link Editor#isDeleteOperation}
	 */
	public Editor(E item, boolean isEditorForm, boolean isDeleteOperation) {
		super();

		this.item = item;
		if (item != null) {
			this.oldItem = item.copy();
		}

		this.isEditorForm = isEditorForm;
		this.initiallyNull = (item == null);
		this.isDeleteOperation = isDeleteOperation;

		setLayout(new GridBagLayout());
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipadx = 4;
		c.gridy = 0;

		if (isEditorForm) {
			addEditorForm();
		} else {
			addSelectorForm();
		}
	}

	/**
	 * Adds a new field into this editor.
	 *
	 * @param field		The name of the field
	 * @param component	The JComponent representing some kind of user
	 *				input or {@link Editor#item} state.
	 */
	public void addField(String field, JComponent component) {
		c.gridx = 0;
		c.weightx = 1.0;
		add(new JLabel(field), c);

		c.gridx = 1;
		c.weightx = 0.0;
		add(component, c);
		c.gridy++;
	}
}
