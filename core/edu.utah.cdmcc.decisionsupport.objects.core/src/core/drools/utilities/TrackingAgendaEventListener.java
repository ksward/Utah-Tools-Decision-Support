package core.drools.utilities;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.drools.event.rule.AfterActivationFiredEvent;
import org.drools.event.rule.DefaultAgendaEventListener;

public class TrackingAgendaEventListener extends DefaultAgendaEventListener {
	static Logger logger = Logger.getLogger(TrackingAgendaEventListener.class);
	List<String> rulesFiredList = new ArrayList<String>();

	@Override
	public void afterActivationFired(AfterActivationFiredEvent event) {
		rulesFiredList.add(event.getActivation().getRule().getName());
		logger.debug("Rule fired: " + event.getActivation().getRule().getName());
		//System.err.println("Rule fired: " + event.getActivation().getRule().getName());
	}

	public boolean isRuleFired(String ruleName){
		for (String firedRuleName: rulesFiredList){
			if (firedRuleName.equals(ruleName)){
				return true;
			}
		}
		return false;
	}
	
	public void reset(){
		rulesFiredList.clear();
	}
}
