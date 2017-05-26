

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;


import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class OsInfoUtil {

    /**
     * 获取设备信息
     *
     * @return
     */
    public static DeviceInfo buildOsInfo(Context context) {
        DeviceInfo osInfo = new DeviceInfo();

        if (context != null) {
            String androidId = Settings.Secure.getString(context.getContentResolver(),Settings.Secure.ANDROID_ID);
            osInfo.setAndroidID(androidId);
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            osInfo.setImei(getRightIMEI(tm.getDeviceId()));
            String imsi = tm.getSubscriberId();
            osInfo.setImsi(imsi);
            if (imsi != null && imsi.length() >= 5) {
                osInfo.setMcc(imsi.substring(0, 3));
                osInfo.setMnc(imsi.substring(3, 5));
            }
            osInfo.setIp(getLocalIpAddress());
            osInfo.setUa(android.os.Build.MODEL);
            String versionName = getVersionName(context);
            osInfo.setCver(versionName);
            osInfo.setOsVersion("Android" + android.os.Build.VERSION.RELEASE);
            String brand = android.os.Build.BRAND;
            osInfo.setBrand(brand);
           // 初始化分辨率
            DisplayMetrics dm = new DisplayMetrics();
            ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
            osInfo.setWidthPixels(dm.widthPixels);
            osInfo.setHeightPixels(dm.heightPixels);

        }
        return osInfo;
    }

    /**
     * 获取本地IP
     *
     * @return
     */
    private static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String ip = inetAddress.getHostAddress().toString();
                        if (verifi(ip)) {
                            return ip;
                        } else {
                            return "0.0.0.0";
                        }

                    }
                }
            }
        } catch (SocketException ex) {
            LogUtils.e("WifiPreference IpAddress", ex.toString());
        }
        return "0.0.0.0";
    }

    /**
     * 验证IP
     *
     * @param ip
     * @return
     */
    private static boolean verifi(String ip) {
        if (ip == null) {
            return false;
        }
        String patter = "\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b";
        Pattern p = Pattern.compile(patter);
        Matcher m = p.matcher(ip);
        return m.find() ? true : false;
    }

    /**
     * 获取版本名
     *
     * @return
     */
    private static String getVersionName(Context context) {
        String versionName = "";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (TextUtils.isEmpty(versionName)) {
                versionName = "";
            }
        } catch (Exception e) {
            LogUtils.e("VersionInfo", "Exception " + e.getMessage());
        }
        return versionName;
    }

    /**
     * get the right pattern imei
     */
    private static String getRightIMEI(String imei) {
        if (!"".equals(imei) && null != imei && !"null".equals(imei)) {
            int length = imei.length();
            for (int i = 0; i < length; i++) {
                if (Character.isLetter(imei.charAt(i))) {
                    imei = imei.replace(imei.charAt(i), '0');
                }
            }
            int imeiPatterLength = 15;// imei standard pattern length
            if (length < imeiPatterLength) {
                for (int i = 0; i < imeiPatterLength; i++) {
                    imei += "9";
                    if (imei.length() == imeiPatterLength) {
                        break;
                    }
                }
            } else if (length > imeiPatterLength) {
                imei = imei.substring(0, imeiPatterLength);
            }
        }
        return imei;
    }

}
