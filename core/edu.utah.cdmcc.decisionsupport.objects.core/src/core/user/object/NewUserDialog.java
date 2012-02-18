package core.user.object;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;

import core.dao.DAOFactory;
import core.dao.IUserDAO;

public class NewUserDialog extends TitleAreaDialog {
	private Text passwordText;
	private Text passwordConfirmText;
	private Text lastNameText;
	private Text firstNameText;
	private Text accountNameText;
	private User newUser;
	private Button btnAdministrativeAccount;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public NewUserDialog(final Shell parentShell, User newUser) {
		super(parentShell);
		this.newUser = newUser;
		
	}
	
	@Override
	public void create(){
		super.create();
		setMessage("Enter the user information and press the OK button.");
		setTitle("Add New User to Application");
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(final Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(3, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		new Label(container, SWT.NONE);
		
		Label lblLastName = new Label(container, SWT.NONE);
		lblLastName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblLastName.setAlignment(SWT.RIGHT);
		lblLastName.setText("Last name:");
		
		lastNameText = new Text(container, SWT.BORDER);
		lastNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(container, SWT.NONE);
		
		lastNameText.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e){
				if (lastNameText.getText().length()< 2 || lastNameText.getText().length()> 20){
					setErrorMessage("The last name must be between 2 and 20 characters in length.");
					getButton(IDialogConstants.OK_ID).setEnabled(false);
				} else {
					setErrorMessage(null);
				}
			}
		});
		
		Label lblFirstName = new Label(container, SWT.NONE);
		lblFirstName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFirstName.setAlignment(SWT.RIGHT);
		lblFirstName.setText("First name:");
		
		firstNameText = new Text(container, SWT.BORDER);
		firstNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(container, SWT.NONE);
	
		firstNameText.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e){
				if (firstNameText.getText().length()< 2 || firstNameText.getText().length()> 20){
					setErrorMessage("The first name must be between 2 and 20 characters in length.");
					getButton(IDialogConstants.OK_ID).setEnabled(false);
				} else {
					setErrorMessage(null);
				}
			}
		});
		
		Label lblAccountName = new Label(container, SWT.NONE);
		lblAccountName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblAccountName.setAlignment(SWT.RIGHT);
		lblAccountName.setText("Account name:");
		
		accountNameText = new Text(container, SWT.BORDER);
		accountNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		
		accountNameText.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e){
				if (accountNameText.getText().length()< 2 || accountNameText.getText().length()> 20){
					setErrorMessage("The account name must be between 2 and 20 characters in length.");
					getButton(IDialogConstants.OK_ID).setEnabled(false);
				} else {
					setErrorMessage(null);
				}
			}
		});
		
		accountNameText.addTraverseListener(new TraverseListener() {
			public void keyTraversed(TraverseEvent e) {
				IUserDAO userDAO = DAOFactory.instance(DAOFactory.HIBERNATE)
						.getUserDAO();
				userDAO.getSession().beginTransaction();
				User user = (User) userDAO.getSession()
						.getNamedQuery(User.GETUSERBYACCOUNTNAME)
						.setString("accountName", accountNameText.getText())
						.uniqueResult();
				userDAO.getSession().getTransaction().commit();
				if (user != null) {
					MessageDialog dialog = new MessageDialog(
							parent.getShell(),
							"Account name already in use.",
							null,
							"This account name is already in use.  Select a different account name.",
							MessageDialog.ERROR, new String[] { "Continue" }, 0);
					dialog.open();
					Display.getCurrent().asyncExec(new Runnable() {
						public void run() {
							setErrorMessage("Select a different account name.");
							accountNameText.forceFocus();
							accountNameText.selectAll();
						}
					});
				}
			}
		});
		
		btnAdministrativeAccount = new Button(container, SWT.CHECK);
		btnAdministrativeAccount.setText("Administrative Account");
		new Label(container, SWT.NONE);
		
		Label lblPassword = new Label(container, SWT.NONE);
		lblPassword.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPassword.setAlignment(SWT.RIGHT);
		lblPassword.setText("Password:");
		
		passwordText = new Text(container, SWT.BORDER | SWT.PASSWORD);
		passwordText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(container, SWT.NONE);
		
		passwordText.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e){
				if (passwordText.getText().length()< 6 || passwordText.getText().length()> 20){
					setErrorMessage("The password must be between 6 and 20 characters in length.");
					getButton(IDialogConstants.OK_ID).setEnabled(false);
				} else {
					setErrorMessage(null);
				}
			}
		});
		
		Label lblConfirmPassword = new Label(container, SWT.NONE);
		lblConfirmPassword.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblConfirmPassword.setAlignment(SWT.RIGHT);
		lblConfirmPassword.setText("Confirm password:");
		
		passwordConfirmText = new Text(container, SWT.BORDER | SWT.PASSWORD);
		passwordConfirmText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		passwordConfirmText.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e){
				if (passwordConfirmText.getText().equals(passwordText.getText())){
					getButton(IDialogConstants.OK_ID).setEnabled(true);
					setErrorMessage(null);
				} else {
					getButton(IDialogConstants.OK_ID).setEnabled(false);
					setErrorMessage("You have not entered the same password in the confirm field.");
				}
			}
		});
		
		return area;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		getButton(IDialogConstants.OK_ID).setEnabled(false);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}
	
	
	@Override
	protected void okPressed() {
		newUser.setLastName(lastNameText.getText());
		newUser.setFirstName(firstNameText.getText());
		newUser.setAccountName(accountNameText.getText());
		//newUser.setPasswordDigest(passwordText.getText());
		setPassword();
		newUser.setAdministrativeRights(btnAdministrativeAccount.getSelection());
		super.okPressed();
	}
	
	private void setPassword(){
		StrongUserAuthenticator authenticator = new StrongUserAuthenticator();
		newUser.setPasswordDigest(authenticator.encryptPassword(passwordText.getText()));
	}
}
