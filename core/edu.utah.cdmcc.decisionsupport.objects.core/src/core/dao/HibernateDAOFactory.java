package core.dao;

import org.hibernate.Session;
import core.hibernate.HibernateUtil;

/**
 * Returns Hibernate-specific instances of DAOs.
 * <p/>
 * One of the responsiblities of the factory is to inject a Hibernate Session
 * into the DAOs. You can customize the getCurrentSession() method if you
 * are not using the default strategy, which simply delegates to
 * Hibernates built-in "current Session" mechanism (propagation through thread-locals,
 * usually).
 * <p>
 * This factory uses HibernateUtil to get access to the SessionFactory.
 * It could as well implement a static initalizer that boots the SessionFactory and
 * expose it with a static method for other uses. You'd then no longer need
 * HibernateUtil at all, except if you find its other options useful.
 * <p/>
 * If for a particular DAO there is no additional non-CRUD functionality, we use
 * a nested static class to implement the interface in a generic way. This allows clean
 * refactoring later on, should the interface implement business data access
 * methods at some later time. Then, we would externalize the implementation into
 * its own first-level class.
 *
 * Alteration for Utah Toolbox is specific routine to create a PatientDAO.
 * 
 * @author christian.bauer@jboss.com  Adapted by J. Michael Dean for Utah Toolbox
 */
public class HibernateDAOFactory extends DAOFactory {

	@Override
	public IPatientDAO getPatientDAO() {
		return (IPatientDAO) instantiateDAO(PatientDAOHibernate.class);
	}

	@Override
	public IUserDAO getUserDAO() {
		return (IUserDAO) instantiateDAO(UserDAOHibernate.class);
	}
	
	private GenericHibernateDAO<?, ?> instantiateDAO(Class<?> daoClass) {
		try {
			GenericHibernateDAO<?, ?> dao = (GenericHibernateDAO<?, ?>)daoClass.newInstance();
			dao.setSession(getCurrentSession());
			return dao;
		} catch (Exception e) {
			throw new RuntimeException("Cannot instantiate DAO: " + daoClass, e);
		}
	}

	protected Session getCurrentSession(){
		Session returnSession = HibernateUtil.getSessionFactory()
				.getCurrentSession();
		return returnSession;
	}


}
