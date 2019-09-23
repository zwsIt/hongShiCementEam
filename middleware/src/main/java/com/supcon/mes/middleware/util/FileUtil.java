package com.supcon.mes.middleware.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * Created by wangshizhan on 2018/3/29.
 * Email:wangshizhan@supcon.com
 */

public class FileUtil {

    public static void deleteFile(String filePath) {
        File uploadZip = new File(filePath);
        if (uploadZip.exists()) {
            uploadZip.delete();
        }
    }

    public static void save(String file, String inputText) {
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try {
            out = openFileOutput(file);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(inputText);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static FileOutputStream openFileOutput(String file) {

        try {
            return new FileOutputStream(new File(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        return null;
    }

    /**
     * 写入内容到SD卡中的txt文本中
     * str为内容
     */
    public static void write2File(String file, String input) {
        try {
//            FileWriter fw = new FileWriter(file);
//            File f = new File(file);
//            fw.write(input);
//            FileOutputStream os = new FileOutputStream(f);
//            DataOutputStream out = new DataOutputStream(os);
//            out.writeShort(2);
//            out.writeUTF("");
            Writer fw = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(file), "UTF-8"));
            fw.write(input);
            fw.flush();
            fw.close();
        } catch (Exception e) {

        }
    }

    /**
     * 新建文件夹到手机本地
     *
     * @param fileFolder ,文件夹的路径名称
     * @return
     */
    public static boolean createDir(String fileFolder) {
        File dir = new File(fileFolder);
        if (!dir.exists()) {
            return dir.mkdirs();
        }
        return false;
    }
}
