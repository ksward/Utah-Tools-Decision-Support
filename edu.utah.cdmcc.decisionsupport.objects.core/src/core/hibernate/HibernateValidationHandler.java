package core.hibernate;

import org.hibernate.validator.InvalidValue;

public class HibernateValidationHandler {
	
	/**
	 * This method was scattered around but is now in the core objects plugin so that
	 * different places can use it. Handles validator messages from Hibernate.
	 * 
	 * @return The error string.
	 */
	public static String handleValidationMessages(final InvalidValue[] validationMessages) {

		String errorMessage = "";
		for (InvalidValue m : validationMessages) {
			errorMessage = errorMessage
					+ ("The patient  " + m.getPropertyName() + " "
							+ m.getMessage() + ".\n");
		}
		return errorMessage;
	}
}
