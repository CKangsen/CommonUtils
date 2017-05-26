

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class NetWorkUtil {
	private static final String TAG = NetWorkUtil.class.getSimpleName();

    public static final int CHINA_MOBILE = 1; // 中国移动
    public static final int CHINA_UNICOM = 2; // 中国联通
    public static final int CHINA_TELECOM = 3; // 中国电信

    public static final int SIM_OK = 0;
    public static final int SIM_NO = -1;
    public static final int SIM_UNKNOW = -2;

    public static boolean proxy = false;

    public static final String CONN_TYPE_WIFI = "wifi";
    public static final String CONN_TYPE_GPRS = "gprs";
    public static final String CONN_TYPE_NONE = "none";

    /**
     * 判断网络连接有效
     *
     * @param context
     *            Context对象
     * @return 网络处于连接状态（3g or wifi)
     */
    public static boolean isAvailable(Context context) {
        boolean result = false;
        if (getNetworkType(context) >= 0)
            result = true;
        return result;
    }

    /**
     * 获取网络类型
     *
     * @param context
     *            Context对象
     * @return 当前处于连接状态的网络类型
     */
    public static int getNetworkType(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) (context
                .getSystemService(Context.CONNECTIVITY_SERVICE));
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnectedOrConnecting()) {
                return info.getType();
            }
        }
        return -9;
    }

    /**
     * 获取网络类型
     *
     * @param context
     *            Context对象
     * @return 当前处于连接状态的网络类型
     */
    public static String getNetworkInfo(Context context) {
        String result = null;
        ConnectivityManager connectivity = (ConnectivityManager) (context
                .getSystemService(Context.CONNECTIVITY_SERVICE));
        if (connectivity == null) {
            result = null;
        } else {

            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null) {
                State tem = info.getState();
                if ((tem == State.CONNECTED || tem == State.CONNECTING)) {
                    result = info.getTypeName() + " " + info.getSubtypeName()
                            + info.getExtraInfo();
                }
            }
        }
        return result;
    }

    /**
     * 判断当前的网络状态 wifi或者gprs
     *
     * @param context
     * @return
     */
    public static String getNetConnType(Context context) {
        // 获得网络连接服务
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (null == connManager) {
            LogUtils.w(TAG,"Network"+"can not get Context.CONNECTIVITY_SERVICE");
            return CONN_TYPE_NONE;
        }

        NetworkInfo info = null;
        // wifi的网络状态
        info = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (null != info) {
            State wifiState = info.getState();
            if (State.CONNECTED == wifiState) { // 判断是否正在使用WIFI网络
                return CONN_TYPE_WIFI;
            }
        } else {
        	LogUtils.w(TAG,"Network"+"can not get ConnectivityManager.TYPE_WIFI");
        }

        // gprs的网络状态
        info = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (null != info) {
            State mobileState = info.getState();
            if (State.CONNECTED == mobileState) { // 判断是否正在使用GPRS网络
                return CONN_TYPE_GPRS;
            }
        } else {
            LogUtils.w(TAG,"Network"+"can not get ConnectivityManager.TYPE_MOBILE");
        }
        return CONN_TYPE_NONE;
    }

    /**
     * 获取wifi的信号强度，网速等信息<br>
     * 参考：http://blog.csdn.net/way_ping_li/article/details/8777478<br/>
     * wifi信号强度发生变化的时候会发出广播：RSSI_CHANGED_ACTION = "android.net.wifi.RSSI_CHANGED"
     *
     * @param context
     * @return
     */
    public static WifiInfo getWifiInfo(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if(wifiManager == null){
            return null;
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if(wifiInfo == null){
            return null;
        }
        if (wifiInfo.getBSSID() == null) {
            return null;
        }
        //wifi名称
        String ssid = wifiInfo.getSSID();
        //wifi信号强度 ,[0,numLevels-1]
        int signalLevel = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), 5);
        //wifi速度
        int speed = wifiInfo.getLinkSpeed();
        //wifi速度单位
        String units = WifiInfo.LINK_SPEED_UNITS;
        LogUtils.i(TAG,"Network"+"--ssid==="+ssid+"====signalLevel====="+signalLevel+"===speed===="+speed);
        return wifiInfo;
    }

    /**
     *  判断网络是否连接成功
     * @param context
     * @return
     */
    public static String getNetType(Context context){
    	State wifiState = null;
        State mobileState = null;
        String networkType = "";
         ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
         wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
         mobileState = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
         if (wifiState != null && mobileState != null && State.CONNECTED != wifiState && State.CONNECTED == mobileState) {
             // 手机网络连接成功
             networkType = TudcConstant.NET_MOBILE;
             LogUtils.i(TAG,"--------MOBILE NET IS OK---------------------------------------");
         } else if (wifiState != null && mobileState != null && State.CONNECTED != wifiState && State.CONNECTED != mobileState) {
             // 手机没有任何的网络
             networkType =TudcConstant.NET_NULL;
         	LogUtils.i(TAG,"--------MOBILE NET AND WIFI IS ERROR---------------------------------------");
         } else if (wifiState != null && State.CONNECTED == wifiState) {
             // 无线网络连接成功
             networkType =TudcConstant.NET_WIFI;
         	LogUtils.i(TAG,"--------WIFI NET IS OK---------------------------------------");
         }
         return networkType;
    }

}
