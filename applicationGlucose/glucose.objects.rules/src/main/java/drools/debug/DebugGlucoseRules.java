package drools.debug;

import glucose.decision.object.GlucoseDecision;
import java.util.GregorianCalendar;
import core.patient.object.Patient;
import drools.engine.KnowledgeEngine;

public class DebugGlucoseRules {

	public static KnowledgeEngine knowledgeEngine;
	private static GlucoseDecision decision;
	private static Patient patient;
	private static GregorianCalendar decisionDate;
	
	public static void main(String[] args) {
		patient = new Patient("TestLast", "TestFirst", "12-34-56",
				"ST03CHOM0002", new GregorianCalendar(1999, 12, 12), 22.34, 25.34);
		decisionDate = new GregorianCalendar(2005, 9, 25, 14, 55, 55);
		decision = new GlucoseDecision(decisionDate, "ACCEPT", 450, 5, 1);
		patient.addDecision(decision);
		
		try{
			knowledgeEngine = new KnowledgeEngine();
			System.out.println("Created knowledge engine.\nRules now firing:\n");
			knowledgeEngine.testFireRules(decision);
		} catch (Throwable t){
			t.printStackTrace();
		}

	}

}
