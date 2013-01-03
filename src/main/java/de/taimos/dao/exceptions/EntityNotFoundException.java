package de.taimos.dao.exceptions;

/**
 * 
 */
public class EntityNotFoundException extends DAOException {

	private static final long serialVersionUID = 3523202448830813321L;

	/**
	 * @param message
	 * @param cause
	 */
	public EntityNotFoundException(final String message) {
		super(message, null);
	}

	/**
	 * @param cause
	 */
	public EntityNotFoundException() {
		super(null);
	}

}
