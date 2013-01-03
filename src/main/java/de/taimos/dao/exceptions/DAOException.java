package de.taimos.dao.exceptions;

/**
 * Genric DAO Exception
 */
public class DAOException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * @param message
	 * @param cause
	 */
	public DAOException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause
	 */
	public DAOException(final Throwable cause) {
		super(cause);
	}

}
