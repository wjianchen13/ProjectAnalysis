package com.cold.file;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class SDFileUtils {
    private String SDPATH;

    private String fileName = "logFile.txt";
    private static SDFileUtils logFile = null;

    // SDCRAD文件访问的构造函数
    public SDFileUtils() {
        SDPATH = Environment.getExternalStorageDirectory() + "/test/";
    }

    public static SDFileUtils get()
    {
        if(logFile == null)
        {
            logFile = new SDFileUtils();
        }

        return logFile;
    }

    // 在SDCRAD上创建文件
    private File createFile() throws IOException {
        File file = new File(SDPATH + fileName);
        file.createNewFile();
        return file;
    }


    // 向文件中写入数据
    public void writeToSDFile(String msg) {
        File file = null;
        OutputStream outputStream = null;
        try {
            file = this.createFile();

            outputStream = new FileOutputStream(file, true);

            outputStream.write(msg.getBytes());

            outputStream.flush();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
//            try {
//                outputStream.close();
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
        }
    }

}
