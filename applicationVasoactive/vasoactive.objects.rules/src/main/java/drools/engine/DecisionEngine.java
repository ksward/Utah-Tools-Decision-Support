package drools.engine;

import java.util.Properties;
import org.drools.WorkingMemory;

import vasoactive.decision.object.VasoactiveDecision;
import vasoactive.decision.object.VasoactiveDecisionState;

/**
 * This class has a variety of convenience constructors to enable different
 * ways to build the inference engine.
 * 
 * @author mdean
 *
 */
public class DecisionEngine {

	public RulesEngine rulesEngine;

	public DecisionEngine()  {
		super();
		try {
			rulesEngine = new RulesEngine("VasoactiveDroolRules00.drl");
		} catch (RulesEngineException e) {
			System.out.println("Error in DecisionEngine constructor");
			e.printStackTrace();
		}
	}
	
	/**
	 * This constructor gobbles up a Properties object
	 * that contains the relevant pieces.  
	 * 
	 * @param properties
	 * @throws RulesEngineException
	 */
	public DecisionEngine(Properties properties) throws RulesEngineException{
		super();
		rulesEngine = new RulesEngine(properties);
	}
	
	/**
	 * This constructor accepts the filename of a DRL file.  It is intended
	 * to be used when the user wants to point the application at an experimental
	 * file of rules.
	 * 
	 * @param drlFileName
	 * @throws RulesEngineException
	 */
	public DecisionEngine(String drlFileName) throws RulesEngineException{
		super();
		rulesEngine = new RulesEngine(drlFileName);
	}

	public void fireRules(final VasoactiveDecision glucoseDecision, final VasoactiveDecisionState decisionState) {
		rulesEngine.executeRules(new WorkingEnvironmentCallback() {
			public void initEnvironment(WorkingMemory workingMemory) {
				workingMemory.insert(glucoseDecision);
				workingMemory.insert(decisionState);
			};
		});
	}

	public RulesEngine getRulesEngine() {
		return rulesEngine;
	}

}
