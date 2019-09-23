package com.supcon.mes.middleware.util;

import java.io.File;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by wangshizhan on 2018/8/23
 * Email:wangshizhan@supcom.com
 */
public class FormDataHelper {

    public static Map<String, RequestBody> createDataFormBody(Map<String, Object> requestDataMap) {
        Map<String, RequestBody> requestBodyMap = new HashMap<>();
        for (String key : requestDataMap.keySet()) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),
                    requestDataMap.get(key) == null ? "" : String.valueOf(requestDataMap.get(key)));
            requestBodyMap.put(key, requestBody);
        }
        return requestBodyMap;
    }
    /**
     * 构建表单
     * @param data 表单数据
     * @return 返回表单
     */
    public static FormBody createDataForm(Map<String, Object> data) {
        FormBody.Builder builder = new FormBody.Builder(Charset.defaultCharset());

        for(String key : data.keySet()){
            builder.add(key, String.valueOf(data.get(key)));
        }
        return builder.build();
    }

    /**
     * 构建表单
     * @param file 图片
     * @return 返回表单
     */
    public static List<MultipartBody.Part> createFileForm(File file) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);//表单类型
        RequestBody photoRequestBody = RequestBody.create(MediaType.parse("image/jpg"), file);
        builder.addFormDataPart("files", file.getName(), photoRequestBody);
        return builder.build().parts();
    }

    /**
     * 构建表单
     * @param attachmentMap map
     * @return 返回表单
     */
    public static List<MultipartBody.Part> createFileForm(Map<String, Object> attachmentMap) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);//表单类型
        RequestBody photoRequestBody = RequestBody.create(MediaType.parse("application/octet-stream"), "");
        builder.addFormDataPart("files", "", photoRequestBody);
        if(attachmentMap.get("files")!=null){
            List<Map<String, Object>> files = (List<Map<String, Object>>) attachmentMap.get("files");
            for(Map<String, Object> file: files){
                for(String key : file.keySet()){
                    builder.addFormDataPart(key, (String) file.get(key));
                }
            }
            attachmentMap.remove("files");
        }

        for(String key : attachmentMap.keySet()){
            builder.addFormDataPart(key, String.valueOf(attachmentMap.get(key)));
        }

        return builder.build().parts();
    }


    /**
     * 构建表单
     * @param files 图片
     * @return 返回表单
     */
    public static List<MultipartBody.Part> createForm(List<File> files) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);//表单类型
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            RequestBody photoRequestBody = RequestBody.create(MediaType.parse("image/jpg"), file);
            builder.addFormDataPart("faultPicture", file.getName(), photoRequestBody);
        }
        return builder.build().parts();
    }


    /**
     * 构建表单
     * @param zipFile 压缩文件
     * @return 返回表单
     */
    public static List<MultipartBody.Part> createZipFileForm(File zipFile) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);//表单类型
        RequestBody zipRequestBody = RequestBody.create(MediaType.parse("application/zip"), zipFile);
        builder.addFormDataPart("zipFile", zipFile.getName(), zipRequestBody);
        return builder.build().parts();
    }
    
}
