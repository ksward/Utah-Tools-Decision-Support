package core.dao;

import java.util.List;
import core.user.object.User;

/**
 * This interface describes the DAO for manipulating system
 * users - this is distinct from patients.
 * 
 * @author J. Michael Dean, M.D., M.B.A. (University of Utah)
 * Copyright 2010.  All Rights Reserved.
 *
 */
public interface IUserDAO extends IGenericDAO<User, Long> {
	boolean addUser(User newUser);
	public boolean createUser(User newUser);
	boolean userAlreadyExists(User newUser);
	List<User> getAllUsers();
}
