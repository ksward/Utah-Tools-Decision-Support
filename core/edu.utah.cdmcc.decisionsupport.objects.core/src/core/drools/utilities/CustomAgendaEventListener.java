package core.drools.utilities;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.drools.event.rule.ActivationCancelledEvent;
import org.drools.event.rule.ActivationCreatedEvent;
import org.drools.event.rule.AfterActivationFiredEvent;
import org.drools.event.rule.AgendaEventListener;
import org.drools.event.rule.AgendaGroupPoppedEvent;
import org.drools.event.rule.AgendaGroupPushedEvent;
import org.drools.event.rule.BeforeActivationFiredEvent;

public class CustomAgendaEventListener implements AgendaEventListener {
	private static final Logger logger = Logger.getLogger(CustomAgendaEventListener.class);
	private List<String> rulesFiredList = new ArrayList<String>();
	
	@Override
	public void activationCancelled(ActivationCancelledEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void activationCreated(ActivationCreatedEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterActivationFired(AfterActivationFiredEvent event) {
		rulesFiredList.add(event.getActivation().getRule().getName());
		logger.info("Rule fired: " + event.getActivation().getRule().getName());

	}


	@Override
	public void beforeActivationFired(BeforeActivationFiredEvent arg0) {
		// TODO Auto-generated method stub

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

	@Override
	public void agendaGroupPopped(AgendaGroupPoppedEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void agendaGroupPushed(AgendaGroupPushedEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
