package test;

import java.util.Calendar;

public class CalendarTest {

	public static void main(String[] args) {
		Calendar cal = Calendar.getInstance();
		System.out.println(cal.getTime());

		cal.set(Calendar.DAY_OF_MONTH, 31);
		System.out.println(cal.getTime());

		cal.set(Calendar.MONTH, Calendar.FEBRUARY);
		System.out.println(cal.getTime());

		cal.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
		System.out.println(cal.getTime());

	}

}
