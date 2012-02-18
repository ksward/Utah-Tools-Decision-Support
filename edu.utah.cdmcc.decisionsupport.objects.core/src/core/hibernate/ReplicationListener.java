package core.hibernate;

import org.hibernate.event.ReplicateEvent;
import org.hibernate.event.def.DefaultReplicateEventListener;

public class ReplicationListener extends DefaultReplicateEventListener {
	private static final long serialVersionUID = 1L;

@Override
public void onReplicate(ReplicateEvent arg0) {
	System.out.println("Replication event listener notified");
	super.onReplicate(arg0);
}
}
