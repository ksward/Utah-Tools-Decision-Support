package core.drools.utilities;

import java.util.Collection;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.audit.WorkingMemoryInMemoryLogger;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.definition.KnowledgePackage;
import org.drools.io.ResourceFactory;

/**
 * This class is the link between the application domain and underlying
 * Drools methods, which are intended to be entirely generic and should
 * not require alteration by most developers.
 * 
 * @author 	 J. Michael Dean, M.D., M.B.A., January 1, 2011
 *
 */
public class KnowledgeEngineController {

	protected KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
	private KnowledgeBuilder kbuilder = KnowledgeBuilderFactory
			.newKnowledgeBuilder();
	protected TrackingProcessEventListener trackingProcessEventListener = new TrackingProcessEventListener();
	protected TrackingAgendaEventListener trackingAgendaEventListener = new TrackingAgendaEventListener();
	protected WorkingMemoryInMemoryLogger traceRulesLogger = new WorkingMemoryInMemoryLogger();

	protected void knowledgeEngineFromDRL(String rulesFile) {
	kbuilder.add(ResourceFactory.newClassPathResource(("/" + rulesFile),
			KnowledgeEngineController.class), ResourceType.DRL);
	if (kbuilder.hasErrors()) {
		throw new RuntimeException("Bad things happened in KnowledgeEngine");
	}
	final Collection<KnowledgePackage> pkgs = kbuilder
			.getKnowledgePackages();
	kbase.addKnowledgePackages(pkgs);
}
	
	protected void knowledgeEngineFromDRLandRuleFlow(String rulesFile,
			String flowFile) {
		kbuilder.add(ResourceFactory.newClassPathResource(("/" + rulesFile),
				KnowledgeEngineController.class), ResourceType.DRL);
		if (kbuilder.hasErrors()) {
			throw new RuntimeException("Bad things happened in KnowledgeEngine");
		}
		kbuilder.add(ResourceFactory.newClassPathResource(("/" + flowFile),
				KnowledgeEngineController.class), ResourceType.DRF);
		if (kbuilder.hasErrors()) {
			throw new RuntimeException("Bad things happened in KnowledgeEngine");
		}
		final Collection<KnowledgePackage> pkgs = kbuilder
				.getKnowledgePackages();
		kbase.addKnowledgePackages(pkgs);
	}

	protected void knowledgeEngineFromDRLandSpreadsheet(final String rulesFile, final String tableFile) {
		kbuilder.add(ResourceFactory.newClassPathResource(("/" + rulesFile),
				KnowledgeEngineController.class), ResourceType.DRL);
		if (kbuilder.hasErrors()) {
			throw new RuntimeException("Bad things happened in KnowledgeEngine");
		}
		final Collection<KnowledgePackage> pkgs = kbuilder
				.getKnowledgePackages();
		kbase.addKnowledgePackages(pkgs);		
	}
	
	public TrackingProcessEventListener getTrackingProcessEventListener() {
		return trackingProcessEventListener;
	}

	public TrackingAgendaEventListener getTrackingAgendaEventListener() {
		return trackingAgendaEventListener;
	}

	public WorkingMemoryInMemoryLogger getTraceRulesLogger() {
		return traceRulesLogger;
	}
	
}
