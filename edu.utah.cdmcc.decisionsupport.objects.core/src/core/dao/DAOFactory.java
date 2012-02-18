
package core.dao;

/**
 * Defines all DAOs and the concrete factories to get the conrecte DAOs.
 * <p>
 * To get a concrete DAOFactory, call <tt>instance()</tt> with one of the
 * classes that extend this factory.
 * <p>
 * Implementation: If you write a new DAO, this class has to know about it.
 * If you add a new persistence mechanism, add an additional concrete factory
 * for it to the enumeration of factories.
 * 
 * @author (original code) christian.bauer@jboss.com, revised for Utah Toolbox J. Michael Dean
 * 
 */
public abstract class DAOFactory {
	/**
     * Creates a standalone DAOFactory that returns unmanaged DAO
     * beans for use in any environment Hibernate has been configured
     * for. Uses HibernateUtil/SessionFactory and Hibernate context
     * propagation (CurrentSessionContext), thread-bound or transaction-bound.
     */
	
	public static final Class<HibernateDAOFactory> HIBERNATE = core.dao.HibernateDAOFactory.class;

    /**
     * Factory method for instantiation of concrete factories.
     */

	public static DAOFactory instance(Class<?> factory) {
        try {
            return (DAOFactory)factory.getConstructor().newInstance();
        } catch (Exception ex) {
            throw new RuntimeException("Couldn't create DAOFactory: " + factory);
        }
    }

    // Add your DAO interfaces here
    public abstract IPatientDAO getPatientDAO();
    public abstract IUserDAO getUserDAO();


}
