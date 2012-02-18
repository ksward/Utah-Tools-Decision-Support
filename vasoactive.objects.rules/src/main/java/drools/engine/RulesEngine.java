package drools.engine;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Properties;
import org.drools.RuleBase;
import org.drools.RuleBaseFactory;
import org.drools.StatefulSession;
import org.drools.WorkingMemory;
import org.drools.agent.RuleAgent;
import org.drools.audit.WorkingMemoryInMemoryLogger;
import org.drools.compiler.DroolsParserException;
import org.drools.compiler.PackageBuilder;
import org.drools.decisiontable.InputType;
import org.drools.decisiontable.SpreadsheetCompiler;
import org.drools.rule.Package;

public class RulesEngine {

	private RuleBase rules;
	private RuleAgent agent;
	public WorkingMemoryInMemoryLogger glucoseLogger;

	public RulesEngine(String rulesFile) throws RulesEngineException {
		super();
		try {
			readDRLintoPackage(rulesFile);
			addSpreadsheetToPackage();
		} catch (Exception e) {
			System.out.println(e);
			throw new RulesEngineException("Could not load/compile rules file: " + rulesFile, e);
		}
	}

	private void addSpreadsheetToPackage() throws DroolsParserException, IOException, Exception {
		PackageBuilder builder;
		Package pkg;
		final SpreadsheetCompiler converter = new SpreadsheetCompiler();
		final String drl = converter.compile("/Hypoglycemia.xls", InputType.XLS);
		builder = new PackageBuilder();
		builder.addPackageFromDrl(new StringReader(drl));
		pkg = builder.getPackage();
		rules.addPackage(pkg);
	}

	private void readDRLintoPackage(String rulesFile) throws DroolsParserException, IOException, Exception {
		try {
			Reader source;
			source = new InputStreamReader(RulesEngine.class.getResourceAsStream("/" + rulesFile));
			PackageBuilder builder = new PackageBuilder();
			builder.addPackageFromDrl(source);
			Package pkg = builder.getPackage();
			rules = RuleBaseFactory.newRuleBase();
			rules.addPackage(pkg);
		} catch (Exception e) {
			System.out.println("Dead in readDRL routine");
			e.printStackTrace();
		}
	}

	public RulesEngine() throws RulesEngineException {
		super();
		try {
			System.out.println("Instantiating an agent with properties file");
			
//			try {
				agent = RuleAgent.newRuleAgent("/glucoseRules.properties");
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			System.out.println("Should have instantiated an agent");
			rules = agent.getRuleBase();
			System.out.println("Have a rule base from the agent");
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new RulesEngineException("Could not load/compile rules files: ", e);
		}
	}

	public RulesEngine(String rulesFile, boolean debug) throws RulesEngineException {
		this(rulesFile);
	}

	public RulesEngine(Properties properties) {
		super();
		try {
			RuleAgent agent = RuleAgent.newRuleAgent(properties);
			rules = agent.getRuleBase();
		} catch (Exception e){
			System.out.println("Bad trouble in rules engine reading property object");
			System.out.println(e.toString());
			throw new RulesEngineException("Could not load the property file", e);
		}
		
	}

	public void executeRules(WorkingEnvironmentCallback callback) {
		WorkingMemory workingMemory = rules.newStatefulSession();
		glucoseLogger = new WorkingMemoryInMemoryLogger(workingMemory);
		callback.initEnvironment(workingMemory);
		workingMemory.fireAllRules();
		((StatefulSession) workingMemory).dispose(); // very important to
		// dispose
	}

	public WorkingMemoryInMemoryLogger getGlucoseLogger() {
		return glucoseLogger;
	}

}
