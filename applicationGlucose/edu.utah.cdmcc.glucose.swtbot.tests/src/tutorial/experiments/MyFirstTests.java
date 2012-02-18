package tutorial.experiments;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SWTBotJunit4ClassRunner.class)
public class MyFirstTests {
	private static SWTWorkbenchBot bot;

	public MyFirstTests() {
		bot = new SWTWorkbenchBot();
	}


	@Test
	public void testCloseWelcomeWizard() throws Exception {
		bot.viewByTitle("Welcome").close();
		bot.viewByTitle("Active Patient").setFocus();
		SWTBotButton activePatient = bot.button();
		activePatient.click();
		SWTBotButton cancelButton = bot.button(0);
		cancelButton.click();
		SWTBotMenu glucoseMenu = bot.menu("Glucose");
		glucoseMenu.click();
		SWTBotMenu exitMenu = bot.menu("Exit");
		exitMenu.click();
		bot.sleep(1000);

	}

}
