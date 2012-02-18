package database.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import core.hibernate.HibernateUtil;


public class TestSchemaCreateUpdateJUnit4 {
protected Session session;

@Test
	public void testSchemaCreate() {	
		session.beginTransaction();
		SchemaExport se = new SchemaExport(HibernateUtil.getConfiguration());
		se.create(false, true);
		session.getTransaction().commit();
		assertEquals("Exceptions from create should be zero ",0,se.getExceptions().size());
	}
	
@Test
	public void testSchemaUpdate(){
		session.beginTransaction();
		SchemaUpdate su = new SchemaUpdate(HibernateUtil.getConfiguration());
		su.execute(false, true);
		assertEquals("Exceptions from create should be zero ",0,su.getExceptions().size());
	}

@SuppressWarnings("unchecked")
@Test
	public void testHibernate() {
		session.beginTransaction();
		Map metadata = session.getSessionFactory().getAllClassMetadata();
		String className = "";
		for (Iterator i = metadata.values().iterator(); i.hasNext();) {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			try {
				EntityPersister persister = (EntityPersister) i.next();
				className = persister.getClassMetadata().getEntityName();
//				System.out.println("select: " + className + " c");
				List result = session.createQuery("from " + className + " c").list();
//				System.out.println("returned " + result.size() + " records for "
//								+ className);
				assertEquals("Initial mappings should be empty ",0, result.size());
			} catch (Exception ex) {
				fail("Hibernate mapping error.  Following class has incorrect mapping: "
								+ className);
			}
		}
	}
	@Before
	public void setUp() throws Exception {
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		 
	}
	@After
	public void tearDown() throws Exception {
		
		HibernateUtil.getSessionFactory().getCurrentSession().close();
	}

}
