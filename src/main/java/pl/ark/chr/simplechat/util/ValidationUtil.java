package pl.ark.chr.simplechat.util;

/**
 * Created by arek on 24.06.17.
 */
public class ValidationUtil {

    public static boolean isBlank(String text) {
        int strLength;
        if (null == text || (strLength = text.length()) == 0) {
            return true;
        }
        for (int i = 0; i <strLength; i++) {
            if (Character.isWhitespace(text.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }
}
