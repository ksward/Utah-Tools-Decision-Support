package edu.utah.cdmcc.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.jface.dialogs.IDialogConstants;

import core.dao.DAOFactory;
import core.dao.IUserDAO;
import core.user.object.NewUserDialog;
import core.user.object.User;
import edu.utah.cdmcc.decisionsupport.controller.core.ApplicationControllers;

public class NewUserHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		User newUser = new User();
		Shell shell = HandlerUtil.getActiveWorkbenchWindow(event).getShell();
		NewUserDialog newUserDialog = new NewUserDialog(shell, newUser);
		newUserDialog.open();
		if (newUserDialog.getReturnCode() == IDialogConstants.CANCEL_ID) {
			// Do Nothing since user canceled
		} else {
			addUser(newUser);
			ApplicationControllers.getDatabaseController()
					.fireDatabaseChanged();
		}
		return null;
	}

	private void addUser(User newUser) {
		IUserDAO userDAO = DAOFactory.instance(DAOFactory.HIBERNATE)
				.getUserDAO();
		userDAO.getSession().beginTransaction();
		userDAO.addUser(newUser);
		userDAO.getSession().getTransaction().commit();
	}

}
