package edu.utah.cdmcc.views.drools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.drools.audit.event.ActivationLogEvent;
import org.drools.audit.event.LogEvent;
import org.drools.audit.event.ObjectLogEvent;
import org.drools.audit.event.RuleBaseLogEvent;
import org.drools.audit.event.RuleFlowGroupLogEvent;
import org.drools.audit.event.RuleFlowLogEvent;
import org.drools.audit.event.RuleFlowNodeLogEvent;
import org.eclipse.debug.ui.AbstractDebugView;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
//import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import com.thoughtworks.xstream.XStream;
import core.decision.object.ClinicalDecision;
import core.decision.object.IDecisionListener;
import core.patient.object.IPatientsListener;
import core.patient.object.Patient;
//import edu.utah.cdmcc.decisionsupport.application.DecisionSupportActivator;
import edu.utah.cdmcc.decisionsupport.application.core.Activator;
import edu.utah.cdmcc.decisionsupport.controller.core.ApplicationControllers;
//import edu.utah.cdmcc.jface.viewers.DecisionTableView;
//import edu.utah.cdmcc.views.drools.AuditView.Event;


/**
 * This class has been essentially lifted from the Drools distribution, with
 * minimal changes to accommodate the decision support applications.  I
 * do not understand the underlying code so changes should be restricted
 * to those linking this view to our class objects (decisions).
 * 
 * @author J. Michael Dean, M.D., M.B.A. (University of Utah) Copyright 2005 -
 *         2008. All Rights Reserved.
 * 
 */
public class AuditView extends AbstractDebugView implements ISelectionListener, IPatientsListener, IDecisionListener {

	private static final String LOG_FILE_NAME = "LogFileName";
	private static final String CAUSE_EVENT_COLOR = "CauseEventColor";
	public static final String ID = "edu.utah.cdmcc.decisionsupport.drools.auditView";

	private String logFileName;
	private IAction deleteAction;
	private IAction refreshAction;

	protected Viewer createViewer(Composite parent) {
		final TreeViewer variablesViewer = new TreeViewer(parent);
		variablesViewer.setContentProvider(new AuditViewContentProvider());
		variablesViewer.setLabelProvider(new AuditLabelProvider());
		variablesViewer.setUseHashlookup(true);
		getSite().setSelectionProvider(variablesViewer);
		getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(this);
		ApplicationControllers.getPatientController().addPatientsListener(this);
		ApplicationControllers.getDecisionController().addDecisionFiredListener(this);
		variablesViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				getViewer().refresh();
			}
		});
		return variablesViewer;
	}

	@Override
	public void dispose() {
		getSite().getWorkbenchWindow().getSelectionService().removeSelectionListener(this);
		ApplicationControllers.getPatientController().removePatientsListener(this);
		ApplicationControllers.getDecisionController().removeDecisionFiredListener(this);
	};

	public void setLogFile(String logFileName) {
		this.logFileName = logFileName;
		refresh();
		deleteAction.setEnabled(logFileName != null);
		refreshAction.setEnabled(logFileName != null);
	}

	@SuppressWarnings("unchecked")
	public void refresh() {
		if (logFileName == null) {
			getViewer().setInput(null);
			return;
		}
		try {
			XStream xstream = new XStream();
			ObjectInputStream in = xstream.createObjectInputStream(new FileReader(logFileName));
			getViewer().setInput(createEventList((List<LogEvent>) in.readObject()));
			((TreeViewer) getViewer()).expandToLevel(1);
		} catch (FileNotFoundException e) {
			setLogFile(null);
			getViewer().setInput(null);
		} catch (Throwable t) {
			// DroolsEclipsePlugin.log(t);
			getViewer().setInput(null);
		}
	}

	protected List<Event> createEventList(List<LogEvent> logEvents) {
		Iterator<LogEvent> iterator = logEvents.iterator();
		List<Event> events = new ArrayList<Event>();
		Stack<Event> beforeEvents = new Stack<Event>();
		List<Event> newActivations = new ArrayList<Event>();
		Map<String, Event> activationMap = new HashMap<String, Event>();
		Map<Long, Event> objectMap = new HashMap<Long, Event>();
		while (iterator.hasNext()) {
			LogEvent inEvent = (LogEvent) iterator.next();
			Event event = new Event(inEvent.getType());
			switch (inEvent.getType()) {
				case LogEvent.INSERTED:
					ObjectLogEvent inObjectEvent = (ObjectLogEvent) inEvent;
					event.setString("Object inserted (" + inObjectEvent.getFactId() + "): " + inObjectEvent.getObjectToString());
					if (!beforeEvents.isEmpty()) {
						((Event) beforeEvents.peek()).addSubEvent(event);
					} else {
						events.add(event);
					}
					event.addSubEvents(newActivations);
					newActivations.clear();
					objectMap.put(new Long(((ObjectLogEvent) inEvent).getFactId()), event);
					break;
				case LogEvent.UPDATED:
					inObjectEvent = (ObjectLogEvent) inEvent;
					event.setString("Object updated (" + inObjectEvent.getFactId() + "): " + inObjectEvent.getObjectToString());
					if (!beforeEvents.isEmpty()) {
                        ((Event) beforeEvents.peek()).addSubEvent(event);
					} else {
						events.add(event);
					}
					event.addSubEvents(newActivations);
					newActivations.clear();
					Event assertEvent = (Event) objectMap.get(new Long(((ObjectLogEvent) inEvent).getFactId()));
					if (assertEvent != null) {
						event.setCauseEvent(assertEvent);
					}
					break;
				case LogEvent.RETRACTED:
					inObjectEvent = (ObjectLogEvent) inEvent;
					event.setString("Object removed (" + inObjectEvent.getFactId() + "): " + inObjectEvent.getObjectToString());
					if (!beforeEvents.isEmpty()) {
                        ((Event) beforeEvents.peek()).addSubEvent(event);
					} else {
						events.add(event);
					}
					event.addSubEvents(newActivations);
					newActivations.clear();
					assertEvent = (Event) objectMap.get(new Long(((ObjectLogEvent) inEvent).getFactId()));
					if (assertEvent != null) {
						event.setCauseEvent(assertEvent);
					}
					break;
				case LogEvent.ACTIVATION_CREATED:
					ActivationLogEvent inActivationEvent = (ActivationLogEvent) inEvent;
					event.setString("Activation created: Rule " + inActivationEvent.getRule() + " " + inActivationEvent.getDeclarations());
					newActivations.add(event);
					activationMap.put(((ActivationLogEvent) inEvent).getActivationId(), event);
					break;
				case LogEvent.ACTIVATION_CANCELLED:
					inActivationEvent = (ActivationLogEvent) inEvent;
					event.setString("Activation cancelled: Rule " + inActivationEvent.getRule() + " " + inActivationEvent.getDeclarations());
					newActivations.add(event);
					event.setCauseEvent((Event) activationMap.get(((ActivationLogEvent) inEvent).getActivationId()));
					break;
				case LogEvent.BEFORE_ACTIVATION_FIRE:
					inActivationEvent = (ActivationLogEvent) inEvent;
					event.setString("Activation executed: Rule " + inActivationEvent.getRule() + " " + inActivationEvent.getDeclarations());
					events.add(event);
					beforeEvents.push(event);
					event.setCauseEvent((Event) activationMap.get(((ActivationLogEvent) inEvent).getActivationId()));
					break;
				case LogEvent.AFTER_ACTIVATION_FIRE:
				    beforeEvents.pop();
					break;
				case LogEvent.BEFORE_RULEFLOW_CREATED:
					RuleFlowLogEvent inRuleFlowEvent = (RuleFlowLogEvent) inEvent;
					event.setString("Process started: " + inRuleFlowEvent.getProcessName() + "[" + inRuleFlowEvent.getProcessId() + "]");
					if (!beforeEvents.isEmpty()) {
                        ((Event) beforeEvents.peek()).addSubEvent(event);
					} else {
						events.add(event);
					}
                    beforeEvents.push(event);
					break;
                case LogEvent.AFTER_RULEFLOW_CREATED:
                    beforeEvents.pop();
                    break;
				case LogEvent.BEFORE_RULEFLOW_COMPLETED:
					inRuleFlowEvent = (RuleFlowLogEvent) inEvent;
					event.setString("Process completed: " + inRuleFlowEvent.getProcessName() + "[" + inRuleFlowEvent.getProcessId() + "]");
					if (!beforeEvents.isEmpty()) {
                        ((Event) beforeEvents.peek()).addSubEvent(event);
					} else {
						events.add(event);
					}
                    beforeEvents.push(event);
					break;
                case LogEvent.AFTER_RULEFLOW_COMPLETED:
                    beforeEvents.pop();
                    break;
                case LogEvent.BEFORE_RULEFLOW_NODE_TRIGGERED:
                    RuleFlowNodeLogEvent inRuleFlowNodeEvent = (RuleFlowNodeLogEvent) inEvent;
                    event.setString("Process node triggered: " + inRuleFlowNodeEvent.getNodeName() + " in process " + inRuleFlowNodeEvent.getProcessName() + "[" + inRuleFlowNodeEvent.getProcessId() + "]");
                    if (!beforeEvents.isEmpty()) {
                        ((Event) beforeEvents.peek()).addSubEvent(event);
                    } else {
                        events.add(event);
                    }
                    beforeEvents.push(event);
                    break;
                case LogEvent.AFTER_RULEFLOW_NODE_TRIGGERED:
                    beforeEvents.pop();
                    break;
				case LogEvent.BEFORE_RULEFLOW_GROUP_ACTIVATED:
					RuleFlowGroupLogEvent inRuleFlowGroupEvent = (RuleFlowGroupLogEvent) inEvent;
					event.setString("RuleFlow Group activated: " + inRuleFlowGroupEvent.getGroupName() + "[size=" + inRuleFlowGroupEvent.getSize() + "]");
					if (!beforeEvents.isEmpty()) {
                        ((Event) beforeEvents.peek()).addSubEvent(event);
					} else {
						events.add(event);
					}
					beforeEvents.push(event);
                    break;
                case LogEvent.AFTER_RULEFLOW_GROUP_ACTIVATED:
                    beforeEvents.pop();
                    break;
				case LogEvent.BEFORE_RULEFLOW_GROUP_DEACTIVATED:
					inRuleFlowGroupEvent = (RuleFlowGroupLogEvent) inEvent;
					event.setString("RuleFlow Group deactivated: " + inRuleFlowGroupEvent.getGroupName() + "[size=" + inRuleFlowGroupEvent.getSize() + "]");
					if (!beforeEvents.isEmpty()) {
                        ((Event) beforeEvents.peek()).addSubEvent(event);
					} else {
						events.add(event);
					}
                    beforeEvents.push(event);
                    break;
                case LogEvent.AFTER_RULEFLOW_GROUP_DEACTIVATED:
                    beforeEvents.pop();
                    break;
				case LogEvent.BEFORE_PACKAGE_ADDED:
					RuleBaseLogEvent ruleBaseEvent = (RuleBaseLogEvent) inEvent;
					event.setString("Package added: " + ruleBaseEvent.getPackageName());
					if (!beforeEvents.isEmpty()) {
                        ((Event) beforeEvents.peek()).addSubEvent(event);
					} else {
						events.add(event);
					}
                    beforeEvents.push(event);
					break;
				case LogEvent.AFTER_PACKAGE_ADDED:
                    beforeEvents.pop();
					break;
				case LogEvent.BEFORE_PACKAGE_REMOVED:
					ruleBaseEvent = (RuleBaseLogEvent) inEvent;
					event.setString("Package removed: " + ruleBaseEvent.getPackageName());
					if (!beforeEvents.isEmpty()) {
                        ((Event) beforeEvents.peek()).addSubEvent(event);
					} else {
						events.add(event);
					}
                    beforeEvents.push(event);
					break;
				case LogEvent.AFTER_PACKAGE_REMOVED:
                    beforeEvents.pop();
					break;
                case LogEvent.BEFORE_RULE_ADDED:
					ruleBaseEvent = (RuleBaseLogEvent) inEvent;
					event.setString("Rule added: " + ruleBaseEvent.getRuleName());
					if (!beforeEvents.isEmpty()) {
                        ((Event) beforeEvents.peek()).addSubEvent(event);
					} else {
						events.add(event);
					}
					beforeEvents.push(event);
					break;
                case LogEvent.AFTER_RULE_ADDED:
                    if (!beforeEvents.isEmpty()) {
                        Event beforeEvent = (Event) beforeEvents.pop();
                        beforeEvent.addSubEvents(newActivations);
                        newActivations.clear();
                    }
                    break;
				case LogEvent.BEFORE_RULE_REMOVED:
					ruleBaseEvent = (RuleBaseLogEvent) inEvent;
					event.setString("Rule removed: " + ruleBaseEvent.getRuleName());
					if (!beforeEvents.isEmpty()) {
                        ((Event) beforeEvents.peek()).addSubEvent(event);
					} else {
						events.add(event);
					}
					beforeEvents.push(event);
					break;
                case LogEvent.AFTER_RULE_REMOVED:
                    if (!beforeEvents.isEmpty()) {
                        Event beforeEvent = (Event) beforeEvents.pop();
                        beforeEvent.addSubEvents(newActivations);
    					newActivations.clear();
                    }
					break;
			    default:
			    	// do nothing
			    	break;
			}
		}
		return events;
    }

	public void deleteLog() {
		if (logFileName != null) {
			File file = new File(logFileName);
			try {
				file.delete();
				// TODO delete file cause this doesn't seem to work
				setLogFile(null);
				refresh();
			} catch (Throwable t) {
				t.printStackTrace();
				// DroolsEclipsePlugin.log(t);
			}
		}
	}

	protected void becomesVisible() {
		refresh();
	}

	protected String getHelpContextId() {
		return null;
	}

	public Event getSelectedEvent() {
		ISelection selection = getViewer().getSelection();
		if (selection instanceof IStructuredSelection) {
			Object selected = ((IStructuredSelection) selection).getFirstElement();
			if (selected instanceof Event) {
				return (Event) selected;
			}
		}
		return null;
	}

	public void showEvent(Event event) {
		((TreeViewer) getViewer()).reveal(event);
	}

	protected void fillContextMenu(IMenuManager menu) {
		Event selected = getSelectedEvent();
		if (selected != null) {
			Event causeEvent = selected.getCauseEvent();
			if (causeEvent != null) {
				menu.add(getAction("ShowEventCause"));
			}
		}
		menu.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	protected void createActions() {
		deleteAction = new DeleteLogAction(this);
		setAction("ClearLog", deleteAction);
		deleteAction.setEnabled(logFileName != null);
		refreshAction = new RefreshLogAction(this);
		setAction("RefreshLog", refreshAction);
		refreshAction.setEnabled(logFileName != null);
		IAction action = new OpenLogAction(this);
		setAction("OpenLog", action);
		action = new ShowEventCauseAction(this);
		setAction("ShowEventCause", action);
	}

	protected void configureToolBar(IToolBarManager tbm) {
		tbm.add(getAction("OpenLog"));
		tbm.add(getAction("RefreshLog"));
		tbm.add(getAction("ClearLog"));
	}

	public void saveState(IMemento memento) {
		memento.putString(LOG_FILE_NAME, logFileName);
	}

	public void init(IViewSite site, IMemento memento) throws PartInitException {
		super.init(site, memento);
		if (memento != null) {
			logFileName = memento.getString(LOG_FILE_NAME);
		}
	}

	public class Event {

		private String toString;
		private int type;
		private List<Event> subEvents = new ArrayList<Event>();
		private Event causeEvent;

		public Event(int type) {
			this.type = type;
		}

		public void setString(String toString) {
			this.toString = toString;
		}

		public String toString() {
			return toString;
		}

		public int getType() {
			return type;
		}


		public void addSubEvent(Event subEvent) {
			subEvents.add(subEvent);
		}


		public void addSubEvents(Collection<Event> subEvents) {
			this.subEvents.addAll(subEvents);
		}

		public Object[] getSubEvents() {
			return subEvents.toArray();
		}

		public boolean hasSubEvents() {
			return !subEvents.isEmpty();
		}

		public void setCauseEvent(Event causeEvent) {
			this.causeEvent = causeEvent;
		}

		public Event getCauseEvent() {
			return causeEvent;
		}
	}

	public class AuditLabelProvider extends LabelProvider implements IColorProvider {

		public Color getForeground(Object element) {
			return null;
		}

		public Color getBackground(Object element) {
			Event selected = getSelectedEvent();
			if (selected != null) {
				if (element.equals(selected.getCauseEvent())) {
					Color color = Activator.getDefault().getColor(CAUSE_EVENT_COLOR);
					if (color == null) {
						color = new Color(getControl().getDisplay(), 0, 255, 0);
						Activator.getDefault().setColor(CAUSE_EVENT_COLOR, color);
					}
					return color;
				}
			}
			return null;
		}

		public Image getImage(Object element) {
			if (element instanceof Event) {
				int type = ((Event) element).getType();
				switch (type) {
    			case LogEvent.INSERTED: return DroolsPluginImages.getImage(DroolsPluginImages.INSERT);
    			case LogEvent.UPDATED: return DroolsPluginImages.getImage(DroolsPluginImages.UPDATE);
    			case LogEvent.RETRACTED: return DroolsPluginImages.getImage(DroolsPluginImages.RETRACT);
    			case LogEvent.ACTIVATION_CREATED: return DroolsPluginImages.getImage(DroolsPluginImages.CREATE_ACTIVATION);
    			case LogEvent.ACTIVATION_CANCELLED: return DroolsPluginImages.getImage(DroolsPluginImages.CANCEL_ACTIVATION);
    			case LogEvent.BEFORE_ACTIVATION_FIRE: return DroolsPluginImages.getImage(DroolsPluginImages.EXECUTE_ACTIVATION);
    			case LogEvent.BEFORE_RULEFLOW_CREATED: return DroolsPluginImages.getImage(DroolsPluginImages.RULEFLOW);
    			case LogEvent.BEFORE_RULEFLOW_COMPLETED: return DroolsPluginImages.getImage(DroolsPluginImages.RULEFLOW);
                case LogEvent.BEFORE_RULEFLOW_NODE_TRIGGERED: return DroolsPluginImages.getImage(DroolsPluginImages.RULEFLOW_NODE_TRIGGERED);
    			case LogEvent.BEFORE_RULEFLOW_GROUP_ACTIVATED: return DroolsPluginImages.getImage(DroolsPluginImages.RULEFLOW_GROUP);
    			case LogEvent.BEFORE_RULEFLOW_GROUP_DEACTIVATED: return DroolsPluginImages.getImage(DroolsPluginImages.RULEFLOW_GROUP);
    			case LogEvent.BEFORE_PACKAGE_ADDED: return DroolsPluginImages.getImage(DroolsPluginImages.DROOLS);
    			case LogEvent.BEFORE_PACKAGE_REMOVED: return DroolsPluginImages.getImage(DroolsPluginImages.DROOLS);
    			case LogEvent.BEFORE_RULE_ADDED: return DroolsPluginImages.getImage(DroolsPluginImages.DROOLS);
    			case LogEvent.BEFORE_RULE_REMOVED: return DroolsPluginImages.getImage(DroolsPluginImages.DROOLS);

				}
				return null;
			}
			return null;
		}
	}

	/**
	 * This routine connects the AuditView to the decision selected by the user.
	 */
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			if(!selection.isEmpty()){
				IStructuredSelection ss = (IStructuredSelection) selection;
				if (ss.getFirstElement() instanceof ClinicalDecision){
					displayRuleTraceStream((ClinicalDecision) ss.getFirstElement());
				}
			}
		}
	}


	@SuppressWarnings("unchecked")
	private void displayRuleTraceStream(ClinicalDecision decision) {
		XStream xstream = new XStream();
		try {
			ObjectInputStream in = xstream.createObjectInputStream(new StringReader(decision.getRulesFiredText()));
			getViewer().setInput(createEventList((List<LogEvent>) in.readObject()));
			((TreeViewer) getViewer()).expandToLevel(TreeViewer.ALL_LEVELS);
		} catch (IOException e) {
			getViewer().setInput(null);
		} catch (Throwable e) {
			getViewer().setInput(null);
		}
	}

	/**
	 * Takes care of changes in patients. When the patient selection changes,
	 * there should not be anything in the AuditView, since no specific decision
	 * has been yet selected.
	 */
	public void patientsChanged(Patient patient) {
		System.out.println("Got to patients changed in audit view");
		getViewer().setInput(null);
	}

	public void decisionChanged(ClinicalDecision decision) {
		if (decision == null){
			getViewer().setInput(null);
		} 
	}

	public void decisionStored(ClinicalDecision decision) {
		displayRuleTraceStream(decision);
	}
}
