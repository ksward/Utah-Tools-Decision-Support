package core.user.object;

import org.apache.log4j.Logger;

/**
 * Wrapper class to handle user authentication without exposing
 * the underlying methods utilized. This implementation is stupid
 * and is designed to work with plain text passwords during
 * debugging.
 * 
 * @author mdean
 *
 */
public final class SimpleUserAuthenticator implements IAuthenticator {
	static Logger logger = Logger.getLogger(SimpleUserAuthenticator.class);
	
	public SimpleUserAuthenticator() {
	}

	public  String encryptPassword(String userPassword) {
		return userPassword;
	}

	public  Boolean checkPassword(String inputPassword, String encryptedPassword) {
		if (inputPassword.equals(encryptedPassword)){
			logger.debug("Password is valid");
			return true;
		} else{
			logger.debug("Password is not valid");
			return false;
		}	
	}
}
