/**
 * 
 */
package mx.com.muxiqa.util;

/**
 * @author gerardomartinezgil
 * 
 */
public final class Util {

	public static String removeNonValidCharacters(String str) {
		if (str == null)
			return null;
		StringBuffer s = new StringBuffer();
		for (char c : str.toCharArray()) {
			if ((c == 0x9) || (c == 0xA) || (c == 0xD)
					|| ((c >= 0x20) && (c <= 0xD7FF))
					|| ((c >= 0xE000) && (c <= 0xFFFD))
					|| ((c >= 0x10000) && (c <= 0x10FFFF))) {
				s.append(c);
			}
		}
		return s.toString();
	}
	
	public static String encodeStringOnGoogleStyle(final String toEncode) {
		if(toEncode != null && toEncode.trim().length() > 0) {
			StringBuffer sb = new StringBuffer();
			for (char c : toEncode.toCharArray()) {
				if(c == ' ') c = '+';
				sb.append(c);
			}
			sb.append(',');
			return sb.toString();
		}
		return null;
	}

}