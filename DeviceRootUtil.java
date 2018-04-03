import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;


/**
 * Created by cks on 18-3-29.
 *
 * 可行 isContainSu 检测su文件是否存在
 *
 * 不可行 isAllowSuExecutable 查询su文件的权限，其他用户无权限，需要shell用户或root用户
 *
 * 不可行 isAllowAppRoot 执行su，其他用户无权限，需要shell用户或root用户
 *
 * isContainSu 返回true时设备可获取adb root权限
 * isAllowAppRoot 返回true时App可获取root权限
 */

public class DeviceRootUtil {

    public static String[] su_paths = new String[]{"su","system/bin/su","./system/xbin/su",
            "sbin/su",
            "system/sd/xbin/su",
            "system/bin/failsafe/su",
            "data/local/xbin/su",
            "data/local/bin/su",
            "data/local/su"};

    /**
     * 判断设备是否 root
     *
     * @return the boolean{@code true}: 是<br>{@code false}: 否
     */
    public static boolean isContainSu() {
        String su = "su";
        String[] locations = {"","/system/bin/", "/system/xbin/", "/sbin/", "/system/sd/xbin/", "/system/bin/failsafe/",
                "/data/local/xbin/", "/data/local/bin/", "/data/local/"};
        for (String location : locations) {
            if (new File(location + su).exists()) {
                Log.i("DeviceRootUtil", " "+location);
                return true;
            }
        }
        return false;
    }

    /** 判断手机是否root，不弹出root请求框<br/> */
    public static boolean isAllowSuExecutable() {
        String[] filePaths = su_paths;
        for (String path: filePaths) {
            if (new File(path).exists() && isExecutable(path))
                return true;
        }
        return false;
    }

    private static boolean isExecutable(String filePath) {
        Process p = null;
        try {
            //ShellUtils.CommandResult s =ShellUtils.execCmd("ls -al system/xbin/su",false);
            p = Runtime.getRuntime().exec("ls -al " + filePath);
            // 获取返回内容
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    p.getInputStream(), "UTF-8"));

            String line;
            StringBuilder str = new StringBuilder();
            while ((line = in.readLine()) != null) {
                str.append(line + "\n");
            }
            p.waitFor();
            in.close();

            Log.i("DeviceRootUtil", " "+str);
            if (str != null && str.length() >= 4) {
                char flag = str.charAt(3);
                if (flag == 's' || flag == 'x')
                    return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(p!=null){
                p.destroy();
            }
        }
        return false;
    }

    public static boolean isAllowAppRoot(){
        for (String path: su_paths) {
            if (isSuExecutable(path))
                return true;
        }
        return false;
    }

    public static boolean isSuExecutable(String filePath) {
        Process process = null;
        DataOutputStream os = null;
        try{
            process = Runtime.getRuntime().exec(filePath);
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("exit\n");
            os.flush();
            int exitValue = process.waitFor();
            if (exitValue == 0){
                return true;
            } else{
                return false;
            }
        } catch (Exception e){
            Log.d("*** DEBUG ***", "Unexpected error - Here is what I know: " + e.getMessage());
            return false;
        } finally{
            try{
                if (os != null){
                    os.close();
                }
                process.destroy();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
