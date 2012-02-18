package core.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import core.hibernate.HibernateUtil;

/**
 * Implements the generic CRUD data access operations using Hibernate APIs.
 * <p>
 * To write a DAO, subclass and parameterize this class with your persistent class.
 * Of course, assuming that you have a traditional 1:1 appraoch for Entity:DAO design.
 * <p>
 * You may inject a current Hibernate <tt>Session</tt> to use a DAO or
 * else this will automatically call HibernateUtil to obtain a session.
 *
 * @see HibernateDAOFactory
 *
 **
 * @author christian.bauer@jboss.com
 */
public abstract class GenericHibernateDAO<T, ID extends Serializable>
implements IGenericDAO<T, ID> {

private Class<T> persistentClass;
private Session session;

@SuppressWarnings("unchecked")
public GenericHibernateDAO() {
this.persistentClass = (Class<T>) ((ParameterizedType) getClass()
                        .getGenericSuperclass()).getActualTypeArguments()[0];
}

public void setSession(Session s) {
this.session = s;
}

public Session getSession() {
if (session == null || (!session.isOpen()))
    session = HibernateUtil.getSessionFactory().getCurrentSession();
return session;
}

public Class<T> getPersistentClass() {
return persistentClass;
}

@SuppressWarnings("unchecked")
public T findById(ID id, boolean lock) {
T entity;
if (lock)
    entity = (T) getSession().load(getPersistentClass(), id, LockMode.UPGRADE);
else
    entity = (T) getSession().load(getPersistentClass(), id);

return entity;
}

public List<T> findAll() {
return findByCriteria();
}


//public List<T> findByExample(T exampleInstance, String[] excludeProperty) {
//Criteria crit = getSession().createCriteria(getPersistentClass());
//Example example =  Example.create(exampleInstance);
//for (String exclude : excludeProperty) {
//    example.excludeProperty(exclude);
//}
//crit.add(example);
//return crit.list();
//}

public T makePersistent(T entity) {
getSession().saveOrUpdate(entity);
return entity;
}

public void makeTransient(T entity) {
getSession().delete(entity);
}

public void flush() {
getSession().flush();
}

/**
* Use this inside subclasses as a convenience method.
*/
@SuppressWarnings("unchecked")
protected List<T> findByCriteria(Criterion... criterion) {
Criteria crit = getSession().createCriteria(getPersistentClass());
for (Criterion c : criterion) {
    crit.add(c);
}
return crit.list();
}

}

