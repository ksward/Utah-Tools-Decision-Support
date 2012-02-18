package drools.engine;

import java.util.Collection;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.audit.WorkingMemoryInMemoryLogger;
import org.drools.builder.DecisionTableConfiguration;
import org.drools.builder.DecisionTableInputType;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.definition.KnowledgePackage;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;

import vasoactive.decision.object.VasoactiveDecision;
import vasoactive.decision.object.VasoactiveDecisionState;

public class KnowledgeEngine {
	private KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
	private KnowledgeBuilder kbuilder = KnowledgeBuilderFactory
			.newKnowledgeBuilder();
	private DecisionTableConfiguration dtableConfig = KnowledgeBuilderFactory
			.newDecisionTableConfiguration();
	private WorkingMemoryInMemoryLogger glucoseLogger;

	public KnowledgeEngine() {
		super();
		this.KnowledgeEngineFromDRLandSpreadsheet("VasoactiveDroolRules00.drl","Hypoglycemia.xls","Vasoactive.rf");
	}

	public KnowledgeEngine(String rulesFile, String tableFile, String flowFile) {
		super();
		this.KnowledgeEngineFromDRLandSpreadsheet(rulesFile, tableFile, flowFile);
	}

	private void KnowledgeEngineFromDRLandSpreadsheet(final String rulesFile, final String tableFile, final String flowFile) {
		kbuilder.add(ResourceFactory.newClassPathResource(("/" + rulesFile),
				KnowledgeEngine.class), ResourceType.DRL);
		kbuilder.add(ResourceFactory.newClassPathResource(("/" + flowFile),
				KnowledgeEngine.class), ResourceType.DRF);
		System.out.println(rulesFile + ", " + flowFile);

		//		builder.add(ResourceFactory.newClassPathResource(
//		"dataTransformation-ruleflow.drl"), ResourceType.DRL);
//		builder.add(ResourceFactory.newClassPathResource(
//		"dataTransformation.rf"), ResourceType.DRF);
		
		//dtableConfig.setInputType(DecisionTableInputType.XLS);
		//kbuilder.add(ResourceFactory.newClassPathResource(("/" + tableFile),
		//		getClass()), ResourceType.DTABLE, dtableConfig);
		if (kbuilder.hasErrors()) {
			System.out.println(kbuilder.getErrors().toString());
			System.out.println("Unable to compile " + rulesFile + "or "
					+ tableFile + ".");
			throw new RuntimeException("Bad things happened in KnowledgeEngine");
		}
		final Collection<KnowledgePackage> pkgs = kbuilder
				.getKnowledgePackages();
		kbase.addKnowledgePackages(pkgs);		
	}

	public KnowledgeEngine(String rulesFile) {
		super();
		kbuilder.add(ResourceFactory.newClassPathResource(("/" + rulesFile),
				KnowledgeEngine.class), ResourceType.DRL);
		if (kbuilder.hasErrors()) {
			System.out.println(kbuilder.getErrors().toString());
			System.out.println("Unable to compile " + rulesFile + ".");
			throw new RuntimeException("Bad things happened in KnowledgeEngine");
		}
		final Collection<KnowledgePackage> pkgs = kbuilder
				.getKnowledgePackages();
		kbase.addKnowledgePackages(pkgs);
	}
	
	public KnowledgeEngine(String url, Boolean guvnor){
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
			throw new RuntimeException("Bad things happened in KnowledgeEngine");
		}
		final Collection<KnowledgePackage> pkgs = kbuilder
				.getKnowledgePackages();
		kbase.addKnowledgePackages(pkgs);
	}
	
	public void fireRules(final VasoactiveDecision theDecision, final VasoactiveDecisionState decisionState) {
		StatefulKnowledgeSession session = kbase.newStatefulKnowledgeSession();
		glucoseLogger = new WorkingMemoryInMemoryLogger(session);
		session.insert(theDecision);
		session.insert(decisionState);
		session.startProcess("vasoactive.objects.rules.ruleflow");
		session.fireAllRules();
		session.dispose();
	}

	public void fireRules(final VasoactiveDecision glucoseDecision) {
		StatefulKnowledgeSession session = kbase.newStatefulKnowledgeSession();
		glucoseLogger = new WorkingMemoryInMemoryLogger(session);
		session.insert(glucoseDecision);
		session.fireAllRules();
		//session.dispose();
	}
	
	public KnowledgeBase getKbase() {
		return kbase;
	}

	public WorkingMemoryInMemoryLogger getGlucoseLogger() {
		return glucoseLogger;
	}
}
