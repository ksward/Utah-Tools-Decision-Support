package application.core.tests;

import java.beans.PropertyChangeListener;

import org.junit.*;
import static org.junit.Assert.*;
import core.patient.object.IPatientsListener;
import core.patient.object.Patient;
import edu.utah.cdmcc.decisionsupport.controller.core.ApplicationControllers;
import java.beans.PropertyChangeEvent;

public class TestPatientListeners {

	class PL implements IPatientsListener{
		public PropertyChangeListener listener = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
			}
		};
		public void patientsChanged(Patient patient) {			
		}
		public PL() {
		}		
	}
	
	PL listener1;
	PL listener2;
	PL listener3;
	
	@Before
	public void setUp() throws Exception {
		 listener1 = new PL();
		 listener2 = new PL();
		 listener3 = new PL();
	}

	@Test
	public void testPatientListeners() throws Exception {

		assertEquals("Patient listeners should be zero before any are added", 0, ApplicationControllers.getPatientController()
				.getPropertyChangeListeners("patients").length);
		ApplicationControllers.getPatientController().addPropertyChangeListener("patients", listener1.listener);
		assertEquals("Patient listeners should be one after view listener added", 1, ApplicationControllers
				.getPatientController().getPropertyChangeListeners("patients").length);
		ApplicationControllers.getPatientController().addPropertyChangeListener("patients", listener2.listener);
		assertEquals("Patient listeners should be two after view listener added", 2, ApplicationControllers
				.getPatientController().getPropertyChangeListeners("patients").length);
		ApplicationControllers.getPatientController().addPropertyChangeListener("patients", listener3.listener);
		assertEquals("Patient listeners should be three after view listener added", 3, ApplicationControllers
				.getPatientController().getPropertyChangeListeners("patients").length);

		ApplicationControllers.getPatientController().removePropertyChangeListener("patients", listener1.listener);
		assertEquals("Patient listeners should be two after view listener removed", 2, ApplicationControllers
				.getPatientController().getPropertyChangeListeners("patients").length);

		ApplicationControllers.getPatientController().removePropertyChangeListener("patients", listener1.listener);
		assertEquals("Patient listeners should still be two after same listener removed again", 2, ApplicationControllers
				.getPatientController().getPropertyChangeListeners("patients").length);
		ApplicationControllers.getPatientController().removePropertyChangeListener("patients", listener2.listener);
		assertEquals("Patient listeners should still be one after listener removed ", 1, ApplicationControllers
				.getPatientController().getPropertyChangeListeners("patients").length);
		ApplicationControllers.getPatientController().removePropertyChangeListener("patients", listener3.listener);
		assertEquals("Patient listeners should still be zero after last listener removed ", 0, ApplicationControllers
				.getPatientController().getPropertyChangeListeners("patients").length);
	}
}
