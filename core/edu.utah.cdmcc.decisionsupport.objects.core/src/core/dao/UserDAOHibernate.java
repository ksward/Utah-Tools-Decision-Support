package core.dao;

import java.util.List;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;
import core.hibernate.HibernateValidationHandler;
import core.user.object.User;

public class UserDAOHibernate extends GenericHibernateDAO<User, Long> implements IUserDAO {
	
	private ClassValidator<User> userValidator;
	private InvalidValue[] validationMessages;
	private String errorMessage;
	static Logger logger = Logger.getLogger(UserDAOHibernate.class);
	
	public boolean addUser(User newUser) {
		logger.debug("Adding new user");
		if (createUser(newUser)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean createUser(User newUser) {
		if (userAlreadyExists(newUser)) {
			logger.debug("User name already in use");
			return false;
		} else {
			logger.debug("Starting the validator in create User method");
			userValidator = new ClassValidator<User>(User.class);
			validationMessages = userValidator.getInvalidValues(newUser);
			logger.debug("Returned from validator with error length: "
					+ validationMessages.length);
			if (validationMessages.length == 0) {
				logger.debug("No validation errors so now will make persistent");
				makePersistent(newUser);
				return true;
			} else {
				errorMessage = HibernateValidationHandler.handleValidationMessages(validationMessages);
				MessageDialog.openError(null, "Error in user data entry", errorMessage);
				return false;
			}
		}
	}

	public boolean userAlreadyExists(User newUser) {
		logger.debug("Checking if user exists");
		User user = (User) getSession().getNamedQuery(User.GETUSERBYACCOUNTNAME).setString("accountName", newUser.getAccountName()).uniqueResult();
		if (user != null){
			return true;
		} else
		return false;
	}

	public List<User> getAllUsers() {
		logger.debug("Attempting to get all users");
		List<User> returnList = findAll();
		return returnList;
	}

	

}
