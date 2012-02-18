package edu.utah.cdmcc.decisionsupport.application;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class PlayDates {


	public static void main(String[] args) {

		String ds = "2/2/2003";
		String hm = "23:23 pm";
		GregorianCalendar gc = new GregorianCalendar(1999,Calendar.DECEMBER,15);
		GregorianCalendar myTime = new GregorianCalendar();
		DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
		DateFormat df2 = DateFormat.getDateInstance(DateFormat.MEDIUM);
		DateFormat df3 = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT);
		df.setLenient(false);
		try {
			Date d = df.parse(ds);
			System.out.println(df.format(d));
			System.out.println(df2.format(d));
			System.out.println(df.format(d));
			System.out.println(df2.format(d));
			System.out.println(df3.format(d));
			System.out.println("Gregorian example: "+ df2.format(gc.getTime()));
		}
		catch(ParseException e){
			System.out.println("Unable to parse date "+ ds);
		}
		Pattern priorToEightPM = Pattern.compile("[0-1]?[0-9]:[0-5][0-9][ ]?[pPaA]?[mM]?");
		Pattern afterEightPMBeforeMidnight = Pattern.compile("[2][0-3]:[0-5][0-9][ ]?[pP]?[mM]?");
		Matcher matchPriorToEightPM = priorToEightPM.matcher(hm);
		Matcher matchAfterEightPMBeforeMidnight = afterEightPMBeforeMidnight.matcher(hm);
		if (matchPriorToEightPM.matches() || (matchAfterEightPMBeforeMidnight.matches())){
			System.out.println("The hh:mm format is correct.");
			String[] tokens = hm.split("[:]");
			
			if((tokens[1].contains("pm") || tokens[1].contains("PM")) && Integer.parseInt(tokens[0])<12){
				gc.set(Calendar.HOUR_OF_DAY,Integer.parseInt(tokens[0])+12);
			} else {
				gc.set(Calendar.HOUR_OF_DAY,Integer.parseInt(tokens[0]));
			}
			gc.set(Calendar.MINUTE, Integer.parseInt(tokens[1].substring(0, 2)));
			System.out.println("Gregorian example: "+ df3.format(gc.getTime()));
			System.out.println("Gregorian hour: "+ gc.get(Calendar.HOUR_OF_DAY));
			System.out.println("Gregorian minute: " + gc.get(Calendar.MINUTE));
			System.out.println("Gregorian representation of my time right now: "+ df3.format(myTime.getTime()));
		} else {
			System.out.println("Invalid hh:mm format.");
		}
		

	}

}