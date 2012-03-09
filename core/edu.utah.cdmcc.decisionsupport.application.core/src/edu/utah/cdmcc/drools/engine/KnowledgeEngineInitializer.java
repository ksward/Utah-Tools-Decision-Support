package edu.utah.cdmcc.drools.engine;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.audit.WorkingMemoryInMemoryLogger;
import org.drools.builder.DecisionTableConfiguration;
import org.drools.builder.DecisionTableInputType;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.definition.KnowledgePackage;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;

import core.drools.utilities.TrackingAgendaEventListener;
import core.drools.utilities.TrackingProcessEventListener;

public class KnowledgeEngineInitializer {

	private KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
	private KnowledgeBuilder kbuilder = KnowledgeBuilderFactory
			.newKnowledgeBuilder();
	private StatefulKnowledgeSession session = kbase.newStatefulKnowledgeSession();
	private TrackingProcessEventListener trackingProcessEventListener = new TrackingProcessEventListener();
	private TrackingAgendaEventListener trackingAgendaEventListener = new TrackingAgendaEventListener();
	private WorkingMemoryInMemoryLogger traceRulesLogger = new WorkingMemoryInMemoryLogger();

	public KnowledgeBase getKnowledgeBase() {
		return kbase;
	}

	public KnowledgeBuilder getKnowledgeBuilder() {
		return kbuilder;
	}

	public StatefulKnowledgeSession getStatefulKnowledgeSession(){
		return session;
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

	public void addDrlResource(String droolsArtifactFile) throws Exception {
		InputStream inputStream = getArtifactFile(droolsArtifactFile);
		kbuilder.add(ResourceFactory.newInputStreamResource(inputStream),
				ResourceType.DRL);
		checkForKnowledgeBuilderErrorException(droolsArtifactFile);
	}

	public void addFlowResource(String droolsArtifactFile) throws Exception {
		InputStream inputStream = getArtifactFile(droolsArtifactFile);
		kbuilder.add(ResourceFactory.newInputStreamResource(inputStream),
				ResourceType.DRF);
		checkForKnowledgeBuilderErrorException(droolsArtifactFile);
	}

	public void addExcelTableResource(String droolsArtifactFile) throws Exception {
		InputStream inputStream = getArtifactFile(droolsArtifactFile);
		DecisionTableConfiguration config = KnowledgeBuilderFactory
				.newDecisionTableConfiguration();
		config.setInputType(DecisionTableInputType.XLS);
		kbuilder.add(ResourceFactory.newInputStreamResource(inputStream),
				ResourceType.DTABLE, config);
		checkForKnowledgeBuilderErrorException(droolsArtifactFile);
	}
	
	private void checkForKnowledgeBuilderErrorException(
			String droolsArtifactFile) {
		KnowledgeBuilderErrors errors = kbuilder.getErrors();
		if (errors.size() > 0) {
			for (KnowledgeBuilderError error : errors) {
				System.err.println(error);
			}
			throw new IllegalArgumentException(
					"Could not parse knowledge file: " + droolsArtifactFile
							+ ".");
		}
	}

	private void checkForIoException(String droolsArtifactFile,
			InputStream inputStream) throws IOException {
		if (inputStream == null) {
			throw new IOException("File " + droolsArtifactFile
					+ " not found in classpath.");
		}
	}
	
	private InputStream getArtifactFile(String droolsArtifactFile)
			throws IOException {
		InputStream inputStream = getClass().getResourceAsStream(
				droolsArtifactFile);
		checkForIoException(droolsArtifactFile, inputStream);
		return inputStream;
	}

	public void addPackagesToKnowledgeBase() {
		Collection<KnowledgePackage> pkgs = kbuilder.getKnowledgePackages();
		kbase.addKnowledgePackages(pkgs);
	}
	
}
