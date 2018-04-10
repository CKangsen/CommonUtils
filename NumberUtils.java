
import android.text.TextUtils;

/**
 * Created by cks on 18-4-10.
 *
 *
 */

public class NumberUtils {

    /**
     * 手机号脱敏显示 （8位或11位）
     *
     **/
    public static String phoneNumHidden (String param){
        if (TextUtils.isEmpty(param)) return null;
        if (param.length() == 8){
            return param.replaceAll("(\\d{2})\\d{4}(\\d{2})","$1****$2");
        } else if (param.length() == 11) {
            return param.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
        }
        return null;
    }

    /**
     * 身份证号脱敏显示 （18位）
     *
     **/
    public static String idCardNumHidden (String param){
        if (TextUtils.isEmpty(param)) return null;
        return param.replaceAll("(\\d{4})\\d{10}(\\w{4})","$1*****$2");
    }
}
