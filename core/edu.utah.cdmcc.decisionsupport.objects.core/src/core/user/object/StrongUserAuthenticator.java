package core.user.object;

import org.jasypt.util.password.*;

public class StrongUserAuthenticator implements IAuthenticator {
	
	StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
	
	public StrongUserAuthenticator(){
	}
	
	public String encryptPassword(String userPassword) {
		return passwordEncryptor.encryptPassword(userPassword);
	}

	public Boolean checkPassword(String inputPassword, String encryptedPassword) {
		return passwordEncryptor.checkPassword(inputPassword, encryptedPassword);
	}

}
