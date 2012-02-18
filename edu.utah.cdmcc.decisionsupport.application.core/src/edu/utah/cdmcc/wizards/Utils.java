package edu.utah.cdmcc.wizards;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Text;

//Su: need to find a better location for this utility class
public class Utils {
	public static boolean isTextNonEmpty(Text t) {
		String s = t.getText();
		if ((s != null) && (s.trim().length() > 0))
			return true;
		return false;
	}
	
	public static boolean isStringNonEmpty(String str) {
		return str!= null && str.trim().length() > 0;
	}
	
	public static int findComboItemIndex(Combo combo, String str, boolean ignoreCase) {
		String[] items = combo.getItems();
		for (int i = 0; i < items.length; i++) {
			if (ignoreCase) {
				if (items[i].equalsIgnoreCase(str))
					return i;
			}
			else {
				if (items[i].equals(str))
					return i;
			}
		}
		return -1;
	}
}
