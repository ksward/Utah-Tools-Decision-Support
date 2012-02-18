package core.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;

/**
 * 
 * @author mdean - Not really - copied from Hibernate repository and 2nd edition
 * <p>
 * All CRUD (create, read, update, delete) basic data access operations
 * are isolated in this interface and shared across all DAO implementations.
 *
 * I have altered this code to include session getting
 * since I am always going to use Hibernate.
 *
 * @param <T>
 * @param <ID>
 */
public interface IGenericDAO<T, ID extends Serializable> {
	
	T findById(ID id, boolean lock);
	List<T> findAll();
	//List<T> findByExample(T exampleInstance, String... excludeProperty);
	T makePersistent(T entity);
	void makeTransient(T entity);
	void flush();
	//void setSession(Session s);
	Session getSession();
}
