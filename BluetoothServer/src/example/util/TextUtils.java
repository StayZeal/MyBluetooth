package example.util;

public class TextUtils {

	public static boolean isEmpty(String string) {

		 if (string == null || string.equals(""))
//		if (string == null)
			return true;
		return false;
	}

}
