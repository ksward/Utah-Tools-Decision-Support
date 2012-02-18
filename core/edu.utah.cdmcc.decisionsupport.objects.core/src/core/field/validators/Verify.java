package core.field.validators;

import org.eclipse.swt.events.VerifyEvent;
/**
 * Class to hold verifiers for integer and float fields because these
 * verifiers are used in all our application domains.
 * 
 * @author mdean
 *
 */
public final class Verify {
	public static void verifyFloatText(final VerifyEvent evt) {
		char[] chars = getEventChars(evt);
		for (int i = 0; i < chars.length;) {
			if (!(Character.isDigit(chars[i])) && !(chars[i] == '.')) {
				evt.doit = false;
			}
			return;
		}
	}

	public static void verifyIntegerText(final VerifyEvent evt) {
		char[] chars = getEventChars(evt);
		for (int i = 0; i < chars.length;) {
			if (!(Character.isDigit(chars[i]))) {
				evt.doit = false;
			}
			return;
		}
	}
	public static void verifyLettersText(final VerifyEvent evt){
	char[] chars = getEventChars(evt);
	for (int i = 0; i < chars.length;) {
		if (!(Character.isLetter(chars[i])))
			evt.doit = false;
		return;
	}
	}
	
	private static char[] getEventChars(final VerifyEvent evt) {
		String string = evt.text;
		char[] chars = new char[string.length()];
		string.getChars(0, chars.length, chars, 0);
		return chars;
	}
}
