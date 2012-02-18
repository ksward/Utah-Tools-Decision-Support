package core.user.object;

public interface IAuthenticator {
	/**
	 * This method encrypts the user password and returns this as
	 * a string that can be stored in a database or a file.
	 * 
	 * @param userPassword
	 * @return
	 */
	public String encryptPassword(String userPassword);
	
	/**
	 * This method checks a userPassword against an encrypted
	 * password and returns true if identical.
	 * 
	 * @param inputPassword
	 * @param encryptedPassword
	 */
	public Boolean checkPassword(String inputPassword, String encryptedPassword);
}
