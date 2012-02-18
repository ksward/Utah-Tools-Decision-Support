package database.tests;

import org.hibernate.Session;
import org.hibernate.tool.hbm2ddl.SchemaExport;

import core.hibernate.HibernateUtil;


public class ResetTestingDatabaseSchema {
	
	
	public ResetTestingDatabaseSchema() {
		super();
	}

	public void resetDatabase() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		SchemaExport se = new SchemaExport(HibernateUtil.getConfiguration());
		se.create(false, true);
		session.getTransaction().commit();
	}
}