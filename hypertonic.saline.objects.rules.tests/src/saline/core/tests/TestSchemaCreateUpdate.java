package saline.core.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;
import org.junit.Test;

import core.hibernate.HibernateUtil;

public class TestSchemaCreateUpdate {
	static Map<String, String> databasePreferenceMap = new HashMap<String, String>();
static{
	
	databasePreferenceMap.put("DATABASE_CHOICE", "hsqldb");
	databasePreferenceMap.put("HSQLDB_DATABASE_CONNECTION_URL", "jdbc:hsqldb:hsql://localhost");
	databasePreferenceMap.put("HSQLDB_DATABASE_USERNAME", "sa");
	databasePreferenceMap.put("HSQLDB_DATABASE_PASSWORD", "");
}
	@Test
		public void testSchemaCreate() {
		
		HibernateUtil.injectDatabasePreference(databasePreferenceMap);
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		SchemaExport se = new SchemaExport(HibernateUtil.getConfiguration());
		se.create(false, true);
//			if (se.getExceptions().size() > 0){
//				System.out.println(se.getExceptions().get(0).toString());
//			}
			session.getTransaction().commit();
			assertEquals("Exceptions from create should be zero ",0,se.getExceptions().size());
		}
		
	@Test
		public void testSchemaUpdate(){
		HibernateUtil.injectDatabasePreference(databasePreferenceMap);
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
			SchemaUpdate su = new SchemaUpdate(HibernateUtil.getConfiguration());
			su.execute(false, true);
			session.getTransaction().commit();
			assertEquals("Exceptions from create should be zero ",0,su.getExceptions().size());
		}

	@Test
		public void testHibernate() {
		HibernateUtil.injectDatabasePreference(databasePreferenceMap);
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
			Map<?, ?> metadata = session.getSessionFactory().getAllClassMetadata();
			String className = "";
			for (Iterator<?> i = metadata.values().iterator(); i.hasNext();) {
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				try {
					EntityPersister persister = (EntityPersister) i.next();
					className = persister.getClassMetadata().getEntityName();
//					System.out.println("select: " + className + " c");
					List<?> result = session.createQuery("from " + className + " c").list();
//					System.out.println("returned " + result.size() + " records for "
//									+ className);
					assertEquals("Initial mappings should be empty ",0, result.size());
				} catch (Exception ex) {
					fail("Hibernate mapping error.  Following class has incorrect mapping: "
									+ className);
				}
			}
		}
	}
