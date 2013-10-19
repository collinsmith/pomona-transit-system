package edu.csupomona.cs.cs435.project4;

/**
 * This class represents a form event which is forwarded using a
 * {@link FormListener} to all listening objects.
 *
 * @author Collin Smith
 * @param <E>	The type of the created object
 */
public class FormEvent<E> {
	/**
	 * Generic representation for a created object in the form.
	 */
	private final E createdObject;

	/**
	 * Constructor which initializes a new {@link FormEvent} using
	 * the single parameter as {@link FormEvent#createdObject}.
	 *
	 * @param createdObject		{@link FormEvent#createdObject}
	 */
	public FormEvent(E createdObject) {
		this.createdObject = createdObject;
	}

	/**
	 * Retrieves {@link FormEvent#createdObject}.
	 *
	 * @return		The object created using this FormEvent.
	 */
	public E getCreatedObject() {
		return createdObject;
	}
}
