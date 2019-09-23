package com.supcon.mes.module_yhgl.util;

/**
 * Created by wangshizhan on 2017/8/18.
 * Email:wangshizhan@supcon.com
 */

public class Util {


/*
    public static File writeResponseBodyToDisk(String url, ResponseBody body) {
        try {
            InputStream is = body.byteStream();
            String suffix = url.substring(url.lastIndexOf("/"));
            String path = Constant.YH_PATH;
            File fileDr = new File(path);
            if (!fileDr.exists()) {
                fileDr.mkdir();
            }
            File file = new File(path, suffix);
            if (file.exists()) {
                return file;
//                file.delete();
//                file= new File(path,suffix);
            }
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }

            fos.flush();
            fos.close();
            bis.close();
            is.close();

            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    public static final String getInputName(String name, String def) {
        if (TextUtils.isEmpty(name) || name.trim().equals("")) {
            return def;
        }
        return name;
    }*/

}
