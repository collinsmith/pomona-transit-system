package edu.csupomona.cs.cs435.project4;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.util.LinkedList;
import java.util.ListIterator;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * This class represents an abstract form that is used
 * to house various {@link Editor}s along with methods
 * for completing and commit changes. These forms will
 * also access and change the database values.
 * 
 * @author Collin Smith
 * @param <E>	The object type that is being edited.
 */
public abstract class Form<E> extends JDialog implements Constants {
	/**
	 * Abstract method called to create and return the center
	 * panel of this form.
	 * 
	 * @return		A JPanel representation for the center panel
	 *			of this form.
	 */
	public abstract JPanel createCenterPanel();
	
	/**
	 * Abstract method called to create and return the southern
	 * panel of this form.
	 * 
	 * @return		A JPanel representation for the southern panel
	 *			of this form.
	 */
	public abstract JPanel createSouthernPanel();
	
	/**
	 * Abstract method which retrieves whether or not this form
	 * has all fields filled out.
	 * 
	 * @return		{@code True} if this form is completed,
	 *			{@code false} otherwise.
	 */
	public abstract boolean isFormCompleted();

	/**
	 * Component representation for the owner of this {@link Form}.
	 * This is stored so that subforms have access to it.
	 */
	protected final Component owner;
	
	/**
	 * String representation for the description of this form. This
	 * usually tells the user what information the form wants and is
	 * configured using {@link Form#createNorthernPanel()}.
	 */
	protected String description;

	/**
	 * LinkedList representation for all {@link FormListener} objects
	 * attached to this Form.
	 */
	private LinkedList<FormListener<E>> listeners;

	/**
	 * Constructor which initializes a new {@link Form} using the following
	 * parameters as default values. This is then used to create a {@link JDialog}
	 * to display the components over an owner.
	 * 
	 * @param owner		{@link Form#owner}
	 * @param title		The title of this form.
	 * @param description	{@link Form#description}
	 */
	public Form(Component owner, String title, String description) {
		super(owner == null ? null : (Frame)SwingUtilities.getWindowAncestor(owner), title, true);

		this.owner = owner;
		this.description = description;

		listeners = new LinkedList<FormListener<E>>();
	}

	/**
	 * Dispatches a {@link FormEvent} to all listeners in {@link Form#listeners}.
	 * 
	 * @param e			The FormEvent to dispatch.
	 */
	public void dispatchFormEvent(FormEvent<E> e) {
		ListIterator<FormListener<E>> it = listeners.listIterator();
		while (it.hasNext()) {
			it.next().formCompleted(e);
		}
	}

	/**
	 * Removes a {@link FormListener} from {@link Form#listeners}.
	 * 
	 * @param l			The FormListener to remove.
	 * @return			{@code True} if removed, {@code false} otherwise.
	 */
	public boolean removeFormListener(FormListener<E> l) {
		return listeners.remove(l);
	}

	/**
	 * Adds a new {@link FormListener} into {@link Form#listeners}.
	 * 
	 * @param l			The new FormListener to add.
	 */
	public void addFormListener(FormListener<E> l) {
		listeners.add(l);
	}

	/**
	 * Briefly adds all components to this {@link Form} and then
	 * performs {@link JDialog#setVisible(boolean)} to {@code true}.
	 */
	public void display() {
		JPanel contentPane = (JPanel)getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(createNorthernPanel(), BorderLayout.NORTH);
		contentPane.add(createCenterPanel(), BorderLayout.CENTER);
		contentPane.add(createSouthernPanel(), BorderLayout.SOUTH);
		contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		pack();
		setLocationRelativeTo(getOwner());
		setResizable(false);

		setVisible(true);
	}

	/**
	 * Creates and initializes the northern panel of this {@link Form}.
	 * 
	 * @return			A JPanel representation for the northern
	 *				panel of this form.
	 */
	public final JPanel createNorthernPanel() {
		JPanel p = new JPanel();

		JLabel jlblSearchLabel = new JLabel(description, JLabel.CENTER);
		jlblSearchLabel.setFont(jlblSearchLabel.getFont().deriveFont(14f));
		p.add(jlblSearchLabel);

		return p;
	}

	/**
	 * Displays a JOptionPane dialog informing the user that this {@link Form}
	 * remains incomplete.
	 * 
	 * @param parent		The parent component to display this message over.			
	 */
	public static void throwUncompletedMessage(Component parent) {
		JOptionPane.showMessageDialog(parent, "You haven't completed the form!", "Error", JOptionPane.ERROR_MESSAGE);
	}
}
