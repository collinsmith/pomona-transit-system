package edu.csupomona.cs.cs435.project4;

/**
 * This class represents an abstract Table within this program.
 * A TransitTable is a Table which must have some method to
 * copy itself over creating a new reference.
 *
 * @author Collin Smith
 * @param <E>	The type of object that this item represents. Each
 *			table should be able to be broken down and stored
 *			within this program as an encapsulated object E.
 */
public abstract class TransitTable<E> implements Constants {
	/**
	 * Retrieves a duplicate of this table object.
	 *
	 * @return		A duplicate of the object represented by
	 *			a table.
	 */
	public abstract E copy();
}
