

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;



/**
 * Created by cks on 18-6-25.
 */

public class PopupToastUtils {
    private static Context mContext;
    private static View mAnchor;//锚，就是为了弹窗的定位
    private static PopupWindow pop;
    private static Handler handler = new Handler();
    private static final int DEFAULT_TIME = 5000;

    private static Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (pop != null && pop.isShowing()){
                pop.dismiss();
            }
        }
    };

    public PopupToastUtils(View anchor, Context context) {
        this.mAnchor = anchor;
        this.mContext = context;

    }


    public static void show(View anchor, Context context, String tips) {
        show(anchor,context,tips,DEFAULT_TIME,Gravity.CENTER);

    }

    public static void show(View anchor, Context context, int gravity,String tips) {
        show(anchor,context,tips,DEFAULT_TIME,gravity);

    }

    /**
     * @param tips     输入提示框的内容
     * @param duration 显示时间，单位：ms
     */
    public static void show(View anchor, Context context, String tips, int duration) {
        show(anchor,context,tips,duration,Gravity.CENTER);

    }

    /**
     * @param tips     输入提示框的内容
     * @param duration 显示时间，单位：ms
     */
    public static void show(View anchor, Context context, String tips, int duration, int gravity) {
        if (pop != null && pop.isShowing()){
            pop.dismiss();
            handler.removeCallbacks(runnable);
        }
        mAnchor = anchor;
        mContext = context;
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.popupwindow_toast, null);
        TextView textView = (TextView) contentView.findViewById(R.id.tv_popu);
        textView.setText(tips);
        pop = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.showAtLocation(mAnchor, gravity, 0, 0);
        pop.setAnimationStyle(R.style.ToastAnimation);
        pop.update();//必须添加 否则不显示
        handler.postDelayed(runnable, duration);
    }

    public static void showOnTop(View anchor, Context context, String tips){
        showOnTop(anchor,context,tips,DEFAULT_TIME);

    }

    public static void showOnTop(View anchor, Context context, String tips, int duration){
        if (pop != null && pop.isShowing()){
            pop.dismiss();
            handler.removeCallbacks(runnable);
        }
        mAnchor = anchor;
        mContext = context;
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.popupwindow_toast, null);
        TextView textView = (TextView) contentView.findViewById(R.id.tv_popu);
        textView.setText(tips);
        pop = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.showAtLocation(mAnchor, Gravity.TOP, 0, 80);
        pop.setAnimationStyle(R.style.ToastAnimation);
        pop.update();//必须添加 否则不显示
        handler.postDelayed(runnable, duration);
    }
}
