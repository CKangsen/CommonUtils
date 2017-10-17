

import android.content.Context;
import android.widget.Toast;

/**
 * Created by cks on 2017/5/16.
 */

public class ToastUtil {
    private static Toast mToast;

    public static void showToast(Context mContext, String text) {
        if(mContext==null){
            return;
        }
        if (mToast != null)
            mToast.setText(text);
        else {
            mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
        }

        mToast.show();
    }

    public static void showToast(Context mContext, int resId) {
        showToast(mContext, mContext.getResources().getString(resId));
    }
}
