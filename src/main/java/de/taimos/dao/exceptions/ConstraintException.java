package de.taimos.dao.exceptions;

/**
 * 
 */
public class ConstraintException extends DAOException {

	private static final long serialVersionUID = 3523202448830813321L;

	/**
	 * @param message
	 * @param cause
	 */
	public ConstraintException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause
	 */
	public ConstraintException(final Throwable cause) {
		super(cause);
	}

}
