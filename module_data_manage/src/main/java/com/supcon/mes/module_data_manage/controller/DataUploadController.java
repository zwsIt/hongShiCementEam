package com.supcon.mes.module_data_manage.controller;

import com.supcon.common.view.base.controller.BasePresenterController;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;

/**
 * @author 徐时运
 * @E-mail ciruy.victory@gmail.com
 * @date 2018/10/2611:31
 * 需要理清楚到底哪些东西是真的东西哪些东西是假的东西
 * 上传的过程其实极其简单，制作zip包并上传
 * zip包中包含的数据其实真的不多，一个包含了所有dto列表的json文件，已经一堆图片文件，
 * 图片文件都是json文件中需要用到的图片
 */
public class DataUploadController extends BasePresenterController {

    private IsUploadDto dtoEntity;
    private String dirPath;

    private DataUploadController() {

    }

    private void setDtoEntity(IsUploadDto dto) {
        dtoEntity = dto;
    }

    private void setDirPath(String path) {
        dirPath = path;
    }

    public class Builder {
        private DataUploadController mDataUploadController;

        public Builder() {
            mDataUploadController = new DataUploadController();
        }

        public Builder register(IsUploadDto isUploadDto) {
            mDataUploadController.setDtoEntity(isUploadDto);
            return this;
        }

        public Builder fileDir(String fileDir) {
            mDataUploadController.setDirPath(fileDir);
            return this;
        }

        public DataUploadController build() {
            return mDataUploadController;
        }
    }

    /**
     * 整个上传过程中所执行的所有操作步骤
     */
    private void executeOperation() {
//        //获取Dto列表
//        genDtoList();
//        //获取图片文件名通过逗号所构成的字符串长串
//        genPicPaths();
//        //将DtoList文件转换成json文件
//        convertDtoList2Json();
//        //将压缩文件夹中的所有文件名进行汇总收纳到List中，便于压缩文件的解析
//        genIncludingFileNameList();
//        //压缩文件获取压缩文件
//        zipFiles();
//        //上传压缩文件
//        uploadZip();
//        //在上传成功后删除压缩文件和更新本地数据信息
//        deleteZipAndUpdateLocalData();
    }


    public interface IsUploadDto {
        String sourceDirPath();
        String zipPath();

        List<String> genPicPaths();

        List<String> genJsonPaths();

    }
}
