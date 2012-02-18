package saline.core.tests;

import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import drools.engine.KnowledgeEngine;

public class KnowledgeEngineTest {

	@Test
	public void testKnowledgeEngine() {
		KnowledgeEngine engine;
		try {
			engine = new KnowledgeEngine();
		} catch (Exception e) {
			e.printStackTrace();
			engine = null;
		}
		assertNotNull("Knowledge engine should not be null", engine);
	}
	

}