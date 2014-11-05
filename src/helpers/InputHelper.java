package helpers;

import android.widget.EditText;

public class InputHelper {
	public static boolean isEmpty(EditText et, String errorMessage) {
		String text = et.getText().toString();
		if (text.isEmpty()) {
			et.setError(errorMessage);
			return true;
		}
		et.setError(null);
		return false;
	}
	
	public static boolean areMatching(EditText et1, EditText et2, String errorMessage1, String errorMessage2) {
		String pwd1 = et1.getText().toString();
		String pwd2 = et2.getText().toString();

		if (!pwd1.equals(new String(pwd2))) {
			et1.setError(errorMessage1);
			et2.setError(errorMessage2);
			return false;
		}
		et1.setError(null);
		et2.setError(null);
		return true;
	}
}
