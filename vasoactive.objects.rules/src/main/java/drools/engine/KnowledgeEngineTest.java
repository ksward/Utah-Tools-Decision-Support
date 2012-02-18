package drools.engine;

import static org.junit.Assert.assertNotNull;
import org.junit.Test;

public class KnowledgeEngineTest {

//	@Test
//	public void testKnowledgeEngine() {
//		KnowledgeEngine engine;
//		try {
//			engine = new KnowledgeEngine("VasoactiveDroolRules00.drl");
//		} catch (Exception e) {
//			e.printStackTrace();
//			engine = null;
//		}
//		assertNotNull("Knowledge engine should not be null", engine);
//	}
	@Test
	public void testKnowledgeEngineDrlAndSpreadsheet(){
		KnowledgeEngine engine;
		try {
			engine = new KnowledgeEngine("VasoactiveDroolRules00.drl","Hypoglycemia.xls", "Vasoactive.rf");
		} catch (Exception e) {
			e.printStackTrace();
			engine = null;
		}
		assertNotNull("Knowledge engine should not be null", engine);		
	}
}