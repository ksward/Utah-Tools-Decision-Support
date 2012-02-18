package edu.utah.cdmcc.decisionsupport.controller.core;

import core.user.object.User;

public class UserController extends PropertyChangeController {
	private Boolean administrativeUser = false;
	private User currentUser;
	
	public UserController(){
	}

	public Boolean isAdministrativeUser() {
		return administrativeUser;
	}

	public void setAdministrativeUser(Boolean administrativeUser) {
		this.administrativeUser = administrativeUser;
	}

	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	
	
}
