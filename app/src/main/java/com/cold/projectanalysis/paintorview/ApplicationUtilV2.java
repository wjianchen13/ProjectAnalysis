package com.cold.projectanalysis.paintorview;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

/**
 * @author Vance
 */
public class ApplicationUtilV2 {
    // 用于PomeloClient发送消息的线程,由于PomeloClient处理消息并不是线程安全的，因此消息以队列的形式在单条线程中执行
    public static ExecutorService sendExecutor = Executors.newFixedThreadPool(1);

    // 缓存线程池，用于本地子线程任务
    public static ExecutorService executorService = Executors.newCachedThreadPool();

    // 3个线程的线程池，用于下载游戏
    public static ExecutorService downloadExecutor = Executors.newFixedThreadPool(4);

    // 加载礼物图片线程
    public static ExecutorService giftBmpExecutor = Executors.newFixedThreadPool(1);

    // 加载礼物图片线程
    public static ExecutorService dealBmpExecutor = Executors.newFixedThreadPool(2);

    // 用于获取屏幕宽高
    private static DisplayMetrics displayMetrics = new DisplayMetrics();

    //生成随机数，包含最大和最小
    public static int rand(int min, int max) {
        return new Random().nextInt(max) % (max - min + 1) + min;
    }

    /**
     * 检查是否存在SD卡
     */
    public static boolean hasSDCard() {
        return Environment.MEDIA_MOUNTED.equals(
                Environment.getExternalStorageState());
    }

    //SD卡剩余空间
    public static long getSDFreeSize(Context context) {
        //取得SD卡文件路径
        File path = getFlies(context);
        StatFs sf = new StatFs(path.getPath());
        //获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        //空闲的数据块的数量
        long freeBlocks = sf.getAvailableBlocks();
        //返回SD卡空闲大小
        //return freeBlocks * blockSize;  //单位Byte
        //return (freeBlocks * blockSize)/1024;   //单位KB
        return (freeBlocks * blockSize) / 1024 / 1024; //单位MB
    }

    public static File getFlies(Context context) {
        File path = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            path = context.getFilesDir();
        else
            path = Environment.getExternalStorageDirectory();
        return path;
    }

    //SD卡总容量
    public static long getSDAllSize(Context context) {
        try {
            //取得SD卡文件路径
            File path = getFlies(context);
            StatFs sf = new StatFs(path.getPath());
            //获取单个数据块的大小(Byte)
            long blockSize = sf.getBlockSize();
            //获取所有数据块数
            long allBlocks = sf.getBlockCount();
            //返回SD卡大小
            //return allBlocks * blockSize; //单位Byte
            //return (allBlocks * blockSize)/1024; //单位KB
            return (allBlocks * blockSize) / 1024 / 1024; //单位MB

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    // 返回sdcard下的完整路徑
    public static String getAbsoluteSDPath(String simplePath) {
        if(hasSDCard()) {
            return Environment.getExternalStorageDirectory().toString()
                    + File.separator + simplePath;
        }
        return "";
    }

    /**
     * 检查是否联网
     *
     * @param context
     * @return
     */
    public static boolean checkNetwork(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = manager.getActiveNetworkInfo();
        return network != null && network.isAvailable();
    }

    /**
     * 判断当前网络连接是否为wifi
     */
    public static boolean isWifiConnection(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    /**
     * 判断MOBILE网络是否连接
     *
     * @param context
     * @param context
     * @return
     */
    public static boolean isMobileConnected(Context context) {
        if (context != null) {
            //获取手机所有连接管理对象(包括对wi-fi,net等连接的管理)
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context
                    .CONNECTIVITY_SERVICE);
            //获取NetworkInfo对象
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            //判断NetworkInfo对象是否为空 并且类型是否为MOBILE
            if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE)
                return networkInfo.isAvailable();
        }
        return false;
    }

    /**
     * 判断当前网络是否是3g
     *
     * @param context
     * @return boolean
     */
    public static boolean is3G(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            return true;
        }
        return false;
    }

    /**
     * 判断当前网络是否是2G网络
     *
     * @param context
     * @return boolean
     */
    public static boolean is2G(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && (activeNetInfo.getSubtype() == TelephonyManager.NETWORK_TYPE_EDGE
                || activeNetInfo.getSubtype() == TelephonyManager.NETWORK_TYPE_GPRS || activeNetInfo
                .getSubtype() == TelephonyManager.NETWORK_TYPE_CDMA)) {
            return true;
        }
        return false;
    }

    /**
     * 系统wifi是否打开状态，不是连接
     */
    public static boolean isWifiEnabled(Context context) {
        ConnectivityManager mgrConn = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager mgrTel = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return ((mgrConn.getActiveNetworkInfo() != null && mgrConn
                .getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED) || mgrTel
                .getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);
    }

    /**
     * 获取当前网络连接的类型信息
     * 原生
     *
     * @param context
     * @return
     */
    public static int getConnectedType(Context context) {
        if (context != null) {
            //获取手机所有连接管理对象
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context
                    .CONNECTIVITY_SERVICE);
            //获取NetworkInfo对象
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()) {
                //返回NetworkInfo的类型
                return networkInfo.getType();
            }
        }
        return -1;
    }

    /**
     * 获取当前的网络状态 ：没有网络-0：WIFI网络1：4G网络-4：3G网络-3：2G网络-2
     * 自定义
     *
     * @param context
     * @return
     */
    public static int getAPNType(Context context) {
        //结果返回值
        int netType = 0;
        //获取手机所有连接管理对象
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context
                .CONNECTIVITY_SERVICE);
        //获取NetworkInfo对象
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        //NetworkInfo对象为空 则代表没有网络
        if (networkInfo == null) {
            return netType;
        }
        //否则 NetworkInfo对象不为空 则获取该networkInfo的类型
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_WIFI) {
            //WIFI
            netType = 1;
        } else if (nType == ConnectivityManager.TYPE_MOBILE) {
            int nSubType = networkInfo.getSubtype();
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService
                    (Context.TELEPHONY_SERVICE);
            //3G   联通的3G为UMTS或HSDPA 电信的3G为EVDO
            if (nSubType == TelephonyManager.NETWORK_TYPE_LTE
                    && !telephonyManager.isNetworkRoaming()) {
                netType = 4;
            } else if (nSubType == TelephonyManager.NETWORK_TYPE_UMTS
                    || nSubType == TelephonyManager.NETWORK_TYPE_HSDPA
                    || nSubType == TelephonyManager.NETWORK_TYPE_EVDO_0
                    && !telephonyManager.isNetworkRoaming()) {
                netType = 3;
                //2G 移动和联通的2G为GPRS或EGDE，电信的2G为CDMA
            } else if (nSubType == TelephonyManager.NETWORK_TYPE_GPRS
                    || nSubType == TelephonyManager.NETWORK_TYPE_EDGE
                    || nSubType == TelephonyManager.NETWORK_TYPE_CDMA
                    && !telephonyManager.isNetworkRoaming()) {
                netType = 2;
            } else {
                netType = 2;
            }
        }
        return netType;
    }

    /**
     * 返回屏幕密度
     */
    public static float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    /**
     * 返回屏幕密度
     */
    public static int getDensityDpi(Context context) {
        return context.getResources().getDisplayMetrics().densityDpi;
    }

    /**
     * 返回屏幕高(px)
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 返回屏幕宽(px)
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getSDKVersion() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 获取系统版本；如4.x.x
     *
     * @return
     */
    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取手机名称
     *
     * @return
     */
    public static String getPhoneNmae() {
        return Build.MANUFACTURER;
    }

    /**
     * 获取手机型号
     *
     * @return
     */
    public static String getPhoneModel() {
        return Build.MODEL;
    }


    /**
     * 返回屏幕宽(px)
     */
    public static int getScreenWidth(Activity activity) {
        activity.getWindowManager().getDefaultDisplay()
                .getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    /**
     * 返回屏幕高(px)
     */
    public static int getScreenHeight(Activity activity) {
        activity.getWindowManager().getDefaultDisplay()
                .getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }


    public static int getCPUCores() {
        class CpuFilter implements FileFilter {
            @Override
            public boolean accept(File pathname) {
                if (Pattern.matches("cpu[0-9]", pathname.getName())) {
                    return true;
                }
                return false;
            }
        }
        try {
            File dir = new File("/sys/devices/system/cpu/");
            File[] files = dir.listFiles(new CpuFilter());
            return files.length;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

    /**
     * 获取手机总内存MB
     */

    public static int getTotalMemory() {
        File memoryFile = new File("/proc/meminfo");
        FileInputStream fis = null;
        BufferedReader br = null;
        try {
            fis = new FileInputStream(memoryFile);
            br = new BufferedReader(new InputStreamReader(fis));
            String line = br.readLine();
            StringBuilder sb = new StringBuilder();
            for (char c : line.toCharArray()) {
                if (c >= '0' && c <= '9') {
                    sb.append(c);
                }
            }
            try {
                return Integer.parseInt(sb.toString()) / 1024;
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }


    /**
     * 从一个activity跳往另一个activity
     *
     * @param from   来源
     * @param to     目标
     * @param extras 附加内容
     */
    public static void jumpToActivity(Context from, Class<?> to, Bundle extras) {

        if (from == null || to == null) return;

        Intent intent = new Intent(from, to);
        if (extras != null) {
            intent.putExtras(extras);
        }
        from.startActivity(intent);
    }

    public static void jumpToActivity(final Context from, final Class<?> to, final Bundle extras, long delay) {
        final Handler handler = new Handler(from.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(from, to);
                if (extras != null) {
                    intent.putExtras(extras);
                }
                from.startActivity(intent);
                handler.removeCallbacksAndMessages(null);
            }
        }, delay);
    }

    /**
     * 启动一个Service
     *
     * @param from   来源
     * @param to     目标
     * @param extras 附加信息
     */
    public static void startService(Context from, Class<?> to, Bundle extras) {
        Intent intent = new Intent(from, to);
        if (extras != null) {
            intent.putExtras(extras);
        }
        from.startService(intent);
    }

    /**
     * 安装或者更新Android应用程序
     *
     * @param apkFile 本地的apk文件
     */
    public static void installOrUpdateApk(Context ctx, File apkFile) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(apkFile),
                "application/vnd.android.package-archive");
        ctx.startActivity(intent);
    }

    /**
     * 卸载Android应用程序
     *
     * @param packageName 包名
     */
    public static void uninstallApp(Context ctx, String packageName) {
        Uri packageURI = Uri.parse("package:" + packageName);
        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
        ctx.startActivity(uninstallIntent);
    }

    /**
     * 分享文本内容
     *
     * @param title   标题
     * @param message 内容
     */
    public static void shareText(Activity context, String title, String message) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, title);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        context.startActivity(Intent.createChooser(intent, context.getTitle()));
    }

    public static void shareText(Activity context, String title, String message, int resultCode) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, title);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        context.startActivityForResult(Intent.createChooser(intent, context.getTitle()), resultCode);
    }

    /**
     * 判断service是否正在运行
     *
     * @param context     句柄
     * @param packageName service的全限定名
     * @return
     */
    public static boolean isWorkService(Context context, String packageName) {
        ActivityManager myManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>) myManager
                .getRunningServices(50);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().toString()
                    .equals(packageName)) {
                return true;
            }
        }
        return false;
    }



    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        return (int) (dpValue * getDensity(context) + 0.5f);
    }

    /**
     * @param dpValue
     * @param density
     * @return
     */
    public static int dip2px(float dpValue, float density) {
        return (int) (dpValue * density + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        return (int) (pxValue / getDensity(context) + 0.5f);
    }

    /**
     * 获取Android当前可用内存大小
     *
     * @param context
     * @return
     */
    public static long getAvailableMemory(Context context) {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        MemoryInfo memoryInfo = new MemoryInfo();
        am.getMemoryInfo(memoryInfo);
        return memoryInfo.availMem / (1024 * 1024); // 返回值单位： M
    }

    /**
     * 清理内存
     *
     * @param context
     */

    public static void clearMemory(Context context) {
        try {
            ActivityManager am = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> list = am
                    .getRunningAppProcesses();
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    ActivityManager.RunningAppProcessInfo appInfo = list.get(i);
                    String[] pkgList = appInfo.pkgList;
                    // 一般数值大于ActivityManager.RunningAppProcessInfo.IMPORTANCE_SERVICE的进程都长时间没用或者空进程了
                    if (appInfo.importance > ActivityManager.RunningAppProcessInfo.IMPORTANCE_PERCEPTIBLE) {
                        for (int j = 0; j < pkgList.length; j++) {
                            if (!pkgList[j].contains(context.getPackageName()))
                                am.killBackgroundProcesses(pkgList[j]);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取版本号
     *
     * @return
     * @throws NameNotFoundException
     */
    public static int getVersionCode(Context context) throws NameNotFoundException {
        int versionCode = 0;
        versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        return versionCode;
    }

    /**
     * 获取版本名
     *
     * @return
     * @throws NameNotFoundException
     */
    public static String getVersionName(Context context) throws NameNotFoundException {
        String versionName = null;
        versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        return versionName;
    }

    /**
     * 网络字节-->主机字节
     *
     * @param nByte
     * @return
     */
    public static int ntoh(byte nByte) {
        if (nByte >= 0)
            return nByte;
        else {
            return (256 + nByte);
        }
    }

    /**
     * 从asset中获取文件并把其拷贝到指定路径
     *
     * @param context
     * @param fileName
     * @param path
     * @return
     */
    public static boolean retrieveFileFromAssets(Context context, String fileName, String path) {
        boolean bRet = false;
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            is = context.getAssets().open(fileName);

            File file = new File(path);
            file.createNewFile();
            fos = new FileOutputStream(file);

            byte[] temp = new byte[1024];
            int i = 0;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
            }
            bRet = true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bRet;
    }
    //向桌面添加快捷方式



}