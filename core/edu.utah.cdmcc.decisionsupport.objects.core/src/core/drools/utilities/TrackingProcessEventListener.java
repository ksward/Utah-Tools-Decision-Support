package core.drools.utilities;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.drools.event.process.DefaultProcessEventListener;
import org.drools.event.process.ProcessNodeTriggeredEvent;

public class TrackingProcessEventListener extends DefaultProcessEventListener {
	static Logger logger = Logger.getLogger(TrackingProcessEventListener.class);
	List<String> nodesExecutedList = new ArrayList<String>();
	
	@Override
	public void beforeNodeTriggered(ProcessNodeTriggeredEvent event) {
		nodesExecutedList.add(event.getNodeInstance().getNodeName());
		logger.debug("Node executed: "+ event.getNodeInstance().getNodeName());
		System.err.println("Node executed: "+ event.getNodeInstance().getNodeName());
	};
	
	public boolean isNodeFired(String nodeName){
		for (String executedNode: nodesExecutedList){
			if (executedNode.equals(nodeName)){
				return true;
			}
		}
		return false;
	}
	
	public void reset(){
		nodesExecutedList.clear();
	}
}