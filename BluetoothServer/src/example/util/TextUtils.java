package example.util;

public class TextUtils {

	public static boolean isEmpty(String string) {

		if (string == null || string.equals(""))
			return true;
		return false;
	}

}
