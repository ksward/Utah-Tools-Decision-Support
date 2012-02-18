package core.hibernate;

import java.util.Date;

import org.hibernate.annotations.Entity;

@Entity
public class AuditLogRecord {

	public String message;
	public String entityId;
	public Class<?> entityClass;
	public String userId;
	public Date created;
	
	AuditLogRecord(){}
	
	public AuditLogRecord(String message,
						String entityId,
						Class<?> entityClass,
						String userId){
		this.message = message;
		this.entityId = entityId;
		this.entityClass = entityClass;
		this.userId = userId;
		this.created = new Date();
	}
}
