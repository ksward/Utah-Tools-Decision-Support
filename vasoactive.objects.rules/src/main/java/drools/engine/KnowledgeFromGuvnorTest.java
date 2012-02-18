package drools.engine;


import static org.junit.Assert.*;

import java.util.Collection;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.definition.KnowledgePackage;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;

import org.junit.Test;


public class KnowledgeFromGuvnorTest {
	private KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
	private KnowledgeBuilder kbuilder = KnowledgeBuilderFactory
			.newKnowledgeBuilder();
	@Test
	public void testKnowledgeEngineFromURL(){
		KnowledgeEngineFromGuvnor engine;

		engine = new KnowledgeEngineFromGuvnor("http://localhost:8080/drools-guvnor/org.drools.guvnor.Guvnor/package/glucose.decision.rules/LATEST", true);

		assertNotNull("Knowledge engine should not be null", engine);
		assertFalse("There should not be builder errors", kbuilder.hasErrors());
	}
	
	public class KnowledgeEngineFromGuvnor {

		
		public KnowledgeEngineFromGuvnor(String url, boolean guvnor) {
			super();
			System.out.println("Pushing URL into kbuilder");			
			try {
				kbuilder.add(ResourceFactory.newUrlResource(url), ResourceType.PKG);
			} catch (Exception e) {
				System.out.println("Error occurred when URL was passed");
				e.printStackTrace();
			}
			if (kbuilder.hasErrors()) {
				System.out.println(kbuilder.getErrors().toString());
				System.out.println("Unable to compile " + url + ".");
				throw new RuntimeException("Bad things happened in KnowledgeEngineFromGuvnor");
			}
			final Collection<KnowledgePackage> pkgs = kbuilder
					.getKnowledgePackages();
			kbase.addKnowledgePackages(pkgs);
			System.out.println("Number of packages is : " + pkgs.size());
			KnowledgePackage packg = kbase.getKnowledgePackage("glucose.decision.rules");
			System.out.println(packg.toString());
			System.out.println("Number of rules in the package is " + packg.getRules().size());
			StatefulKnowledgeSession session = kbase.newStatefulKnowledgeSession();
			System.out.println("Happily chuggin with a session ...");
			session.dispose();
		}
	}
	}

