
/**
 * Created by cks on 2017/5/25.
 */

public class EmailRegexUtils {

    public static String EMAIL_REGEX = "^[A-Za-z\\d]+([-_.][A-Za-z\\d]+)*@([A-Za-z\\d]+[-.])+[A-Za-z\\d]{2,4}$";

    public static boolean isMatches(String str){
        return str.matches(EMAIL_REGEX);
    }
}
