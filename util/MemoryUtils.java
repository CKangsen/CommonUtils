package com.threetree.baseproject.util;

import android.app.ActivityManager;
import android.content.Context;

/**
 * Created by cks on 18-5-28.
 */

public class MemoryUtils {

    public static boolean hasAvailMemory(Context context){
        if (getAvailMemory(context) > getAppMemoryClass()){
            return true;
        }
        return false;
    }

    public static long getAvailMemory(Context context) {// 获取android当前可用内存大小

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        //mi.availMem; 当前系统的可用内存
        //String result= Formatter.formatFileSize(context, mi.availMem);// 将获取的内存大小规格化  ;
        long availMem =  mi.availMem/1024/1024;
        return availMem;
    }

    public static int getAppMemoryClass(){
        return staticGetMemoryClass();
    }

    static public int staticGetMemoryClass(){
        String vmHeapSize = PhoneUtils.getProperty("dalvik.vm.heapgrowthlimit","");
        if (vmHeapSize != null && !"".equals(vmHeapSize)){
            return Integer.parseInt(vmHeapSize.substring(0,vmHeapSize.length()-1));
        }
        return staticGetLargeMemoryClass();
    }

    static public int staticGetLargeMemoryClass(){
        String vmHeapSize = PhoneUtils.getProperty("dalvik.vm.heapsize","16m");
        return Integer.parseInt(vmHeapSize.substring(0,vmHeapSize.length()-1));
    }
}
