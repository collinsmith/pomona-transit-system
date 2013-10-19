package edu.csupomona.cs.cs435.project4;

/**
 * This interface represents a listener which is used to forward
 * {@link FormEvent} objects to components which implement this
 * interface.
 *
 * @author Collin Smith
 * @param <E>	The type of object of {@link FormEvent#createdObject}.
 */
public interface FormListener<E> {
	/**
	 * Called when an object who has had implemented this interface is
	 * receiving a {@link FormEvent} from a foreign object has completed
	 * a {@link Form}.
	 *
	 * @param e		The FormEvent being passed to this object.
	 */
	void formCompleted(FormEvent<E> e);
}
