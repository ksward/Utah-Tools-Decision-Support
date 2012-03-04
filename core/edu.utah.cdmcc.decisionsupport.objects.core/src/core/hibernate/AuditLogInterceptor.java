package core.hibernate;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.hibernate.EmptyInterceptor;
import org.hibernate.Session;
import org.hibernate.event.ReplicateEvent;
import org.hibernate.type.Type;

@SuppressWarnings({ "rawtypes", "unused" })
public class AuditLogInterceptor extends EmptyInterceptor {
	private static final long serialVersionUID = 1L;
	private Session session;
	private String userId;
	private Set inserts = new HashSet();
	private Set updates = new HashSet();
	private Set deletes = new HashSet();
	
	public void setSession(Session session) {
		this.session = session;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Override
	public boolean onSave(Object entity, Serializable id, Object[] state,
			String[] propertyNames, Type[] types) {

		System.out.println("onSave");
		return super.onSave(entity, id, state, propertyNames, types);
	}
	
	@Override
	public void onDelete(Object entity, Serializable id, Object[] state,
			String[] propertyNames, Type[] types) {

		System.out.println("onDelete");
		super.onDelete(entity, id, state, propertyNames, types);
	}
	
	@Override
	public boolean onFlushDirty(Object entity, Serializable id,
			Object[] currentState, Object[] previousState,
			String[] propertyNames, Type[] types) {

		System.out.println("onFlushDirty");
		return super.onFlushDirty(entity, id, currentState, previousState,
				propertyNames, types);
	}
	
	//called before commit into database
	public void preFlush(Iterator iterator) {
		System.out.println("preFlush");
	}	
 
	//called after committed into database
	public void postFlush(Iterator iterator) {
		System.out.println("postFlush");
	}
	
	public void onReplicate(ReplicateEvent arg0) {
		System.out.println("Replication event listener notified in my interceptor");
	}
}
