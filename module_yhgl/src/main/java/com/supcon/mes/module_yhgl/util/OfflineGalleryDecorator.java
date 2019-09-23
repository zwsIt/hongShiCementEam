package com.supcon.mes.module_yhgl.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;

import com.supcon.common.com_http.util.RxSchedulers;
import com.supcon.common.view.base.activity.BaseActivity;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.mbap.adapter.GalleryAdapter;
import com.supcon.mes.mbap.beans.GalleryBean;
import com.supcon.mes.mbap.utils.CameraUtil;
import com.supcon.mes.mbap.utils.ImageUtil;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.mbap.view.CustomGalleryView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.SingleConnectWithDecorator;
import com.supcon.mes.middleware.model.event.ImageDeleteEvent;
import com.supcon.mes.middleware.util.FaultPicHelper;
import com.supcon.mes.module_yhgl.IntentRouter;
import com.supcon.mes.module_yhgl.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;

import static android.app.Activity.RESULT_OK;
import static com.supcon.mes.middleware.constant.Constant.CAMERA_ACT.ACT_CAMERA;
import static com.supcon.mes.middleware.constant.Constant.CAMERA_ACT.ACT_CROP;
import static com.supcon.mes.middleware.constant.Constant.CAMERA_ACT.ACT_GALLERY;
import static com.supcon.mes.middleware.constant.Constant.CAMERA_ACT.ACT_PERMISSION;

/**
 * @author Xushiyun
 * @date 2018/8/27
 * Email:ciruy.victory@gmail.com
 * Desc:这个组件和activity存在有很多的耦合性，比如说onClick,OnActivityResult,OnDestroy之间都有着很强的联系，基于回调，其实真的挺烦的
 */

public class OfflineGalleryDecorator {
    /**
     * 显示图片的组件
     */
    private CustomGalleryView yhGalleryView;
    /**
     * 所在的activity
     */
    public Activity context;

    private Uri pictureUri = null;
    /**
     * 当前照片墙所储存的文件列表
     */
    private List<File> pics = new ArrayList<>();
    /**
     * 当前照片墙所储存的文件列表
     * public String localPath;本地储存路径
     * public String url;网络端所对应路径
     * public int resId;通过id访问方式
     * public String type;图片资源类型
     */
    private List<GalleryBean> picPaths = new ArrayList<>();
    /**
     * 删除照片所对应的文件
     */
    private StringBuilder deletePicPaths = new StringBuilder();

    private File mFile;

    /**
     * 图片视图适配器
     */
    private GalleryAdapter galleryAdapter;
    /**
     * 图片储存名称，用以区分图片之间的察差异
     */
    private String startName;

    /**
     * 初始化操作，因为无论如何都需要这些信息
     *
     * @param activity 根据注孤生原则，这里还是就传入activity就行了吧= =
     */
    public OfflineGalleryDecorator(SingleConnectWithDecorator activity) {
        EventBus.getDefault().register(this);
        context = (Activity) activity;
        yhGalleryView = activity.customGalleryView();
        startName = activity.startName();
        galleryAdapter = yhGalleryView.getGalleryAdapter();
    }

    /**
     * 进行照片视图的初始化操作
     *
     * @param faultPicPaths 收纳图片路径的字符串
     */
    public void setPicPaths(String faultPicPaths) {
        if (!TextUtils.isEmpty(faultPicPaths)) {
            if (faultPicPaths.contains(",")) {
                this.setPicPaths(faultPicPaths.split(","));
            } else {
                this.setPicPaths(new String[]{faultPicPaths});
            }
        }
    }

    private void setPicPaths(String[] faultPicPaths) {
        FaultPicHelper.initPics(faultPicPaths, picPaths, yhGalleryView);
    }

    /**
     * 当接收到删除图片的event时，执行删除图片操作
     */
    @Subscribe
    public void onDeleteBitmap(ImageDeleteEvent imageDeleteEvent) {
        //从GalleryBean列表中获取图片路径列表
        List<String> picStrs = FaultPicHelper.getImagePathList(picPaths);
        int position = -1;
        //标记是否有图片
        boolean isMatch = false;
        //按照event中的name来获取路径列表中图片的位置
        for (int i = 0; !isMatch && picStrs.size() > 0; i++) {
            String name = picStrs.get(i);
            if (name.equals(imageDeleteEvent.getPicName())) {
                position = picStrs.indexOf(name);
                isMatch = true;
            }
        }
        //如果存在图片，删除对应图片
        if (position != -1) {
            //从视图中删除图片，照片墙中删除图片
            yhGalleryView.deletePic(position);
            //删除图片
            deleteBitmap(position, picPaths.get(position));
        }
    }


    /**
     * 设置图片点击事件
     */
    public void onListener() {
        //Todo:设置照片控件点击事件

        yhGalleryView.setOnChildViewClickListener((View childView, int action, Object obj) -> {
            final int position = (int) obj;
            //当视图中没有图片时，启动照相机，在点击了右上角的相机图像时也会触发照相事件
            if (position == -1) {
                if(action == CustomGalleryView.ACTION_TAKE_PICTURE_FROM_CAMERA)
                    startCamera();
                else if(action == CustomGalleryView.ACTION_TAKE_PICTURE_FROM_GALLERY){
                    startGallery();
                }
                return;
            }
            //启动一个新的视图用于显示GalleryBean信息，通过Bundle来传递需要的参数
            if (action == CustomGalleryView.ACTION_VIEW) {
                viewImage(childView, position);
            }
            //删除图片
            else if (action == CustomGalleryView.ACTION_DELETE) {
                //获取当前位置上的GalleryBean信息
                final GalleryBean galleryBean = picPaths.get(position);
                //如果本地中储存了对应图片，或者服务器端储存了对应的图片，则可以删除图片信息
                //当发现可以执行删除照片操作时，显示弹出框，判断是否执行删除图片操作
                if (galleryBean.url == null && galleryBean.localPath.contains(startName)) {
                    showEnsureDeleteDialog(position, galleryBean);
                }
            }
        });
        //将适配器和视图点击事件相关联
//        galleryAdapter.setOnItemChildViewClickListener((childView, position, action, obj) ->
//                yhGalleryView.onItemChildViewClick(childView, position, action, obj));
    }

    private void showEnsureDeleteDialog(int position, GalleryBean galleryBean) {
        new CustomDialog(context)
                .twoButtonAlertDialog("是否删除图片?")
                .bindView(R.id.grayBtn, "取消")
                .bindView(R.id.redBtn, "确定")
                //当点击取消按钮时，不执行任何操作，并隐藏弹出框
                .bindClickListener(R.id.grayBtn,v1 -> { },
                        true)
                //当点击确定按钮时，执行删除操作，隐藏弹出框
                .bindClickListener(R.id.redBtn, v3 -> {
                    //在视图中删除图片信息
                    yhGalleryView.deletePic(position);
                    deleteBitmap(position, galleryBean);
                }, true)
                .show();
    }

    private void viewImage(View childView, int position) {
        Bundle bundle = new Bundle();
        //非必须
        bundle.putSerializable("images", (ArrayList) FaultPicHelper.getImagePathList(picPaths));
        bundle.putInt("position", position);
        int[] location = new int[2];
        childView.getLocationOnScreen(location);
        //必须
        bundle.putInt("locationX", location[0]);
        //必须
        bundle.putInt("locationY", location[1]);
        //必须
        bundle.putInt("width", DisplayUtil.dip2px(100, context));
        //必须
        bundle.putInt("height", DisplayUtil.dip2px(100, context));
        //可编辑状态
        bundle.putBoolean("isEditable", yhGalleryView.isEditable());
        //跳转到对应的查看视图界面
        context.getWindow().setWindowAnimations(R.style.fadeStyle);
        IntentRouter.go(context, Constant.Router.IMAGE_VIEW, bundle);
    }

    /**
     * 启动相机，startActivityForResult
     */
    private void startCamera() {

        init();
        //request the camera permission dynamic above android 6.0
        //检测是否拥有启动相机的权限
        boolean permission =
                PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
        //如果拥有启动相机的权限，传入uri启动相机，相机拍摄结果保存储存到传入的uri中
        if (permission) {
            Intent intent = CameraUtil.openCamera(pictureUri);
            context.startActivityForResult(intent, ACT_CAMERA);
        }
        //23版本之后，则请求获取相机权限
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.requestPermissions(new String[]{Manifest.permission.CAMERA}, ACT_PERMISSION);
        }
    }

    private void startGallery() {

        init();
        Intent intent = CameraUtil.openGallery();
        context.startActivityForResult(intent, ACT_GALLERY);
    }

    private void init() {
        //按照当前时间生成图片文件名称
        String fileName = CameraUtil.createFileName(".jpg");
        //在图片名称前添加当前模块的名称前缀
        fileName = startName + fileName;

        //创建文件夹
        CameraUtil.createDir(Constant.IMAGE_SAVE_YHPATH);
        //在文件夹下按照文件名称创建新的文件
        CameraUtil.createFile(Constant.IMAGE_SAVE_YHPATH, fileName);
        //创建指向图片文件的引用
        mFile = new File(Constant.IMAGE_SAVE_YHPATH, fileName);
        //按照文件引用创建uri，用以指向文件位置
        pictureUri = Uri.fromFile(mFile);
    }

    private void galleryBack(Uri inUri) {
        //get picture uri from gallery
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(inUri, projection, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                mFile = new File(filePath);
                cropBack(pictureUri);
            }
            if (!cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    @SuppressLint("CheckResult")
    private void cropBack(Uri inUri) {
        if (inUri == null || mFile == null || mFile.getTotalSpace() == 0) {
            return;
        }
        try {
            //Flowable发送一个mFile数据
            Flowable.just(mFile)
                    //如果发送的数据不为null
                    .filter(file -> file != null)
                    .map(file -> {
                        //对于每一个数据，进行图片的压缩
                        ImageUtil.compressPicture(file.getAbsolutePath(), file.getAbsolutePath(), 90);
                        return file;
                    })
                    .compose(RxSchedulers.io_main())
                    .subscribe(file -> {
                        //收到的数据之后，添加到图片列表中
                        pics.add(file);
                        ((BaseActivity) context).onLoading("正在储存图片...");
                        savePicSuccess();
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            //没看到调用
            case ACT_GALLERY:
                if(data!=null){
                    galleryBack(data.getData());
                }
                break;
            //启动相机activity后返回结果处理
            case ACT_CAMERA:
                //对返回的结果进行处理并发送处理后的图片数据到服务器（发送当前所有的数据）
                cropBack(pictureUri);
                break;
            //没看到调用
            case ACT_CROP:
                cropBack(pictureUri);
                break;
            default:
                break;
        }
    }

    /**
     * Todo: 储存图片
     *
     * @param file   储存图片的位置
     * @param bitmap 需要储存的图片
     */
    public static void saveBitmap(File file, Bitmap bitmap) {
        if (file != null && bitmap != null) {
            try {
                //根据图片输出file创建输出流
                FileOutputStream out = new FileOutputStream(file);
                //压缩图片大小，并添加到输出流
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                //如果图片并没有被回收，则执行回收图片操作
                if (!bitmap.isRecycled()) {
                    bitmap = null;
                }
                out.flush();
                out.close();
            } catch (FileNotFoundException err) {
                LogUtil.e("GalleryDecorator err: " + err.getMessage());
            } catch (IOException var4) {
                var4.printStackTrace();
            }

        }
    }

    private void savePicSuccess() {
        ((BaseActivity) context).onLoadSuccess("储存成功！");
        GalleryBean galleryBean = new GalleryBean();
        //设置图片储存路径
        galleryBean.localPath = mFile.getAbsolutePath();
        //temp
        galleryBean.type = "local";
        //设置图片储存url
        galleryBean.url = null;
        pics.clear();
        mFile = null;

        yhGalleryView.addGalleryBean(galleryBean);
        picPaths.add(galleryBean);
    }

    /**
     * 删除图片方法，这里并不对本地的图片进行操作
     */
    private void deleteBitmap(int position, GalleryBean galleryBean) {
        //如果picPaths中存在该位置的图片，则执行对图片的删除操作
        if (picPaths.size() >= position + 1) {
            picPaths.remove(position);
        }
        //本地文件，不用上传
        if (galleryBean != null && galleryBean.type.equals("local")) {
            return;
        }
        //也就是说，如果不仅仅是本地文件，则需要上传,获取在线图片的url地址，则在deletePicPaths添加该url信息，上传到服务器执行删除操作
        String picPath = galleryBean.url;
        if (TextUtils.isEmpty(deletePicPaths)) {
            deletePicPaths.append(picPath.substring(picPath.lastIndexOf("/") + 1));
        } else {
            deletePicPaths.append(",").append(picPath.substring(picPath.lastIndexOf("/") + 1));
        }
    }

    public void onDestroy() {
        EventBus.getDefault().unregister(this);
    }

    /**
     * 所有图片的本地全路径
     * @return 通过，进行分割的本地全路径拼接成的字符串
     */
    public String getLocalPaths() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < picPaths.size(); i++) {
            if (result.length() != 0) {
                result.append(",");
            }
            result.append(picPaths.get(i).localPath);
        }
        return result.toString();
    }

    /**
     * 所有图片名
     * @return 通过，进行分割的所有图片名拼接成的字符串
     */
    public String getFileNames() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < picPaths.size(); i++) {
            if (result.length() != 0) {
                result.append(",");
            }
            String path = picPaths.get(i).localPath;
            result.append(picPaths.get(i).localPath.substring(path.lastIndexOf(File.separator) + 1));
        }
        return result.toString();
    }
}
