package example.util;

public class Log {

	public static void e(String tag, String string, Exception e) {

		System.err.println(tag + " " + string);
		e.printStackTrace();
	}

	public static void i(String string) {

		System.out.println(string);

	}

}
