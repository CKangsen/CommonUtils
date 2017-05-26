

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.afmobi.tudcsdk.Tudcsdk;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * tudc log
 */

public class LogUtils {
    /**log最大值*/
    private static final int LOG_FILE_SIZE = 5242880;// 5 * 1024 * 1024 = 5MB
    /**日期格式*/
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    static LooperThread looperThread;
    static {
        looperThread = new LogUtils.LooperThread();
        looperThread.setName("LogUtils5242880");
        looperThread.start();
    }
    /**
     * log输出
     * @param tag
     * @param msg
     */
    public static void e(String tag, String msg) {
        //storeLogInfo(tag, msg, "E");
        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putString("tag",tag);
        bundle.putString("msg",msg);
        bundle.putString("priority","E");
        message.what = LooperThread.WRITH_LOG;
        message.setData(bundle);
        if(looperThread != null && looperThread.handler != null) {
            looperThread.handler.sendMessage(message);
        }
    }

    /**
     * log输出
     * @param tag
     * @param msg
     */
    public static void e(String tag,boolean isStore, String msg) {
        if(isStore){
            storeLogInfo(tag, msg, "E");
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("tag",tag);
            bundle.putString("msg",msg);
            bundle.putString("priority","E");
            message.what = LooperThread.WRITH_LOG;
            message.setData(bundle);
            if(looperThread != null && looperThread.handler != null) {
                looperThread.handler.sendMessage(message);
            }
        }
    }

    /**
     * log输出
     * @param tag
     * @param msg
     */
    public static void d(String tag, String msg) {
        //storeLogInfo(tag, msg, "D");
        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putString("tag",tag);
        bundle.putString("msg",msg);
        bundle.putString("priority","D");
        message.what = LooperThread.WRITH_LOG;
        message.setData(bundle);
        if(looperThread != null && looperThread.handler != null) {
            looperThread.handler.sendMessage(message);
        }
    }

    /**
     * log输出
     * @param tag
     * @param msg
     */
    public static void v(String tag, String msg) {
        //storeLogInfo(tag, msg, "V");
        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putString("tag",tag);
        bundle.putString("msg",msg);
        bundle.putString("priority","V");
        message.what = LooperThread.WRITH_LOG;
        message.setData(bundle);
        if(looperThread != null && looperThread.handler != null) {
            looperThread.handler.sendMessage(message);
        }
    }

    /**
     * log输出
     * @param tag
     * @param msg
     */
    public static void i(String tag, String msg) {
        //storeLogInfo(tag, msg, "I");
        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putString("tag",tag);
        bundle.putString("msg",msg);
        bundle.putString("priority","I");
        message.what = LooperThread.WRITH_LOG;
        message.setData(bundle);
        if(looperThread != null && looperThread.handler != null) {
            looperThread.handler.sendMessage(message);
        }
    }

    /**
     * log输出
     * @param tag
     * @param msg
     */
    public static void i(String tag,boolean isStore, String msg) {
        if(isStore){
            //storeLogInfo(tag, msg, "I");
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("tag",tag);
            bundle.putString("msg",msg);
            bundle.putString("priority","I");
            message.what = LooperThread.WRITH_LOG;
            message.setData(bundle);
            if(looperThread != null && looperThread.handler != null) {
                looperThread.handler.sendMessage(message);
            }
        }
    }

    /**
     * log输出
     * @param tag
     * @param msg
     */
    public static void w(String tag, String msg) {
        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putString("tag",tag);
        bundle.putString("msg",msg);
        bundle.putString("priority","W");
        message.what = LooperThread.WRITH_LOG;
        message.setData(bundle);
        if(looperThread != null && looperThread.handler != null) {
            looperThread.handler.sendMessage(message);
        }
       // storeLogInfo(tag, msg, "W");
    }

    public static void writePerformanceLog(  String msg) {

        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putString("tag","[performance]");
        bundle.putString("msg",msg);
        bundle.putString("priority","D");
        message.what = LooperThread.WRITH_PERFORMANCE_LOG;
        message.setData(bundle);
        if(looperThread != null && looperThread.handler != null) {
            looperThread.handler.sendMessage(message);
        }
        // storeLogInfo(tag, msg, "W");
    }

     static class LooperThread extends Thread {
        private static final int WRITH_LOG = 9999;
         private static final int  WRITH_PERFORMANCE_LOG=9998;
        Handler handler;
        Looper looper;

        public void run() {
            Looper.prepare();
            looper = Looper.myLooper();
            handler = new Handler() {
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case WRITH_LOG:
                            Bundle bundle = msg.getData();
                            if(bundle != null){
                                String tag = bundle.getString("tag");
                                String msgContent = bundle.getString("msg");
                                String priority = bundle.getString("priority");
                                storeLogInfo(tag, msgContent, priority);
                            }
                            break;
                        case WRITH_PERFORMANCE_LOG:
                            Bundle bundleP = msg.getData();
                            if(bundleP != null){
                                String tag = bundleP.getString("tag");
                                String msgContent = bundleP.getString("msg");
                                String priority = bundleP.getString("priority");
                               // storePerformanceLogInfo(tag, msgContent, priority);
                            }
                            break;
                    }

                }
            };
            Looper.loop();
        }
    }

    /**
     *log保存到文件
     * @param tag
     * @param msg
     * @param priority
     */
    private static void storeLogInfo(String tag, String msg, String priority) {

        System.out.println("TEST------------storeLogInfo-------------");
        synchronized(LogUtils.class){
            String path = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"tudc"+File.separator;
            if(Tudcsdk.getApplicationContext()!=null){
            	File externalFilesDir = Tudcsdk.getApplicationContext().getExternalFilesDir(null);
                if (externalFilesDir != null) {
                	path = externalFilesDir.getAbsolutePath() + File.separator;
                }
            }
            mkdirectory(path);

            path += "javalog.txt";



            File logFile = new File(path);
            if(logFile.exists()){
                long size = logFile.length();
                if(size >= LOG_FILE_SIZE){
                    boolean isDelete = logFile.delete();
                    System.out.println("storeLogInfo isDelete:"+isDelete);
                }
            }
            FileWriter outPutStream = null;
            PrintWriter out = null;
            try {
                System.out.println("TEST------------storeLogInfo---------write--path=="+path);
                outPutStream = new FileWriter(logFile, true);
                out = new PrintWriter(outPutStream);
                out.println(dateFormat.format(new Date(System.currentTimeMillis())) + "	" + priority
                        + "	" + ">>" + tag + "<<    	" + msg + '\r');
                outPutStream.flush();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (null != outPutStream) {
                        outPutStream.close();
                    }
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static long  toMib(long size){
        return size/1024/1024;
    }

    private static void mkdirectory(String file) {
        File f = new File(file);
        if (!f.exists()) {
            f.mkdirs();
        }
    }

}
