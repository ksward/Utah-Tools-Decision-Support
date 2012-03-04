package edu.utah.cdmcc.drools.engine.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.io.IOException;
import java.io.InputStream;
import org.drools.KnowledgeBase;
import org.drools.audit.WorkingMemoryInMemoryLogger;
import org.drools.builder.DecisionTableConfiguration;
import org.drools.builder.DecisionTableInputType;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.junit.Before;
import org.junit.Test;
import core.drools.utilities.TrackingAgendaEventListener;
import core.drools.utilities.TrackingProcessEventListener;
import edu.utah.cdmcc.drools.engine.KnowledgeEngineInitializer;

public class KnowledgeEngineTests {

	private KnowledgeBase kbase;
	private KnowledgeBuilder builder;
	private KnowledgeEngineInitializer initializer;

	@Before
	public void setUp() {
		initializer = new KnowledgeEngineInitializer();
	}

	@Test
	public void testGetKnowledgeBaseFromInitializer() {
		kbase = initializer.getKnowledgeBase();
		assertNotNull("Knowledge base should not be null", kbase);
	}
	
	@Test
	public void testKnowledgeBaseIsInitiallyEmpty(){
		kbase = initializer.getKnowledgeBase();
	}

	@Test
	public void testGetKnowledgeBuilderFromInitializer() {
		builder = initializer.getKnowledgeBuilder();
		assertNotNull("Knowledge builder should not be null", builder);
	}

	@Test
	public void testGetProcessEventListener() {
		TrackingProcessEventListener listener = initializer
				.getTrackingProcessEventListener();
		assertNotNull("Process tracker should not be null", listener);
	}

	@Test
	public void testGetAgendaListener() {
		TrackingAgendaEventListener listener = initializer
				.getTrackingAgendaEventListener();
		assertNotNull("Agenda tracker should not be null", listener);
	}

	@Test
	public void testGetMemoryListener() {
		WorkingMemoryInMemoryLogger listener = initializer
				.getTraceRulesLogger();
		assertNotNull("Memory tracker should not be null", listener);
	}

	@Test
	public void testAccessRulesArtifactFiles() {
		InputStream inputStream = getClass().getResourceAsStream("/ValidSample.drl");
		assertNotNull("File: ValidSample.drl not found in classpath", inputStream);
		inputStream = getClass().getResourceAsStream("/ValidSample.xls");
		assertNotNull("File: ValidSample.xls not found in classpath", inputStream);
		inputStream = getClass().getResourceAsStream("/validRuleflow.rf");
		assertNotNull("File: validRuleflow.rf not found in classpath", inputStream);
	}

	@Test
	public void testAddDrlResourceToKnowledgeBase() {
		InputStream inputStream = getClass().getResourceAsStream("/ValidSample.drl");
		builder = initializer.getKnowledgeBuilder();
		builder.add(ResourceFactory.newInputStreamResource(inputStream),
				ResourceType.DRL);
		assertEquals(
				"DRL should have been added without errors.\n"
						+ builder.getErrors(), 0, builder.getErrors().size());
	}

	@Test
	public void testAddSpreadsheetResourceToKnowledgeBase() {
		InputStream inputStream = getClass().getResourceAsStream("/ValidSample.xls");
		assertNotNull("File: ValidSample.xls not found in classpath", inputStream);
		builder = initializer.getKnowledgeBuilder();
		DecisionTableConfiguration config = KnowledgeBuilderFactory
				.newDecisionTableConfiguration();
		config.setInputType(DecisionTableInputType.XLS);
		builder.add(ResourceFactory.newInputStreamResource(inputStream),
				ResourceType.DTABLE, config);
		assertEquals("Excel resource should have been added without errors.\n"
				+ builder.getErrors(), 0, builder.getErrors().size());
	}

	@Test
	public void testAddRuleflowResourceToKnowledgeBase() {
		InputStream inputStream = getClass()
				.getResourceAsStream("/validRuleflow.rf");
		builder = initializer.getKnowledgeBuilder();
		builder.add(ResourceFactory.newInputStreamResource(inputStream),
				ResourceType.DRF);
		assertEquals("Rule flow should have been added without errors.\n"
				+ builder.getErrors(), 0, builder.getErrors().size());
	}

	@Test
	public void testAddDrlResourceViaInitializer() throws Exception {
		initializer.addDrlResource("/ValidSample.drl");
		builder = initializer.getKnowledgeBuilder();
		assertEquals("Drl should have been added", 1, builder
				.getKnowledgePackages().size());
		assertEquals(
				"DRL should have been added without errors.\n"
						+ builder.getErrors(), 0, builder.getErrors().size());
	}

	@Test
	public void testAddFlowResourceViaInitializer() throws Exception {
		initializer.addFlowResource("/validRuleflow.rf");
		builder = initializer.getKnowledgeBuilder();
		assertEquals("Flow should have been added", 1, builder
				.getKnowledgePackages().size());
		assertEquals(
				"Flow should have been added without errors.\n"
						+ builder.getErrors(), 0, builder.getErrors().size());
	}

	@Test
	public void testAddTableResourceViaInitializer() throws Exception {
		initializer.addExcelTableResource("/ValidSample.xls");
		builder = initializer.getKnowledgeBuilder();
		assertEquals("Excel table should have been added", 1, builder
				.getKnowledgePackages().size());
		assertEquals("Excel table  should have been added without errors.\n"
				+ builder.getErrors(), 0, builder.getErrors().size());
	}
	
	@Test(expected = IOException.class)
	public void testAddDrlResourceFileNotFoundException() throws Exception {
		initializer.addDrlResource("/Sample2.drl");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testAddDrlResourceIllegalArgumentException() throws Exception {
		initializer.addDrlResource("/InvalidSample.drl");
	}
	
	@Test(expected = IOException.class)
	public void testAddFlowResourceFileNotFoundException() throws Exception {
		initializer.addFlowResource("/invalidRuleflowLocation.rf");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testAddFlowResourceIllegalArgumentException() throws Exception {
		initializer.addFlowResource("/invalidRuleflow.rf");
	}
	
	@Test(expected = IOException.class)
	public void testAddExceltTableResourceFileNotFoundException() throws Exception {
		initializer.addExcelTableResource("/invalidSampleLocation.xls");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testAddExcelTableResourceIllegalArgumentException() throws Exception {
		initializer.addExcelTableResource("/InvalidSample.xls");
	}
	
	
}
