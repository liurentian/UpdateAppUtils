package com.updateapputils.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.updateapputils.view.ConfirmDialog;

/**
 * 更新 app Util
 * Created by asusnb on 2017/8/16.
 */

public class UpdateAppUtil {
    private static final String TAG="UpdateAppUtil";
    //通过 版本号或版本名 检测更新
    public static final int CHECK_BY_VERSION_NAME = 1001;
    public static final int CHECK_BY_VERSION_CODE = 1002;
    //通过 App或浏览器 下载App
    public static final int DOWNLOAD_BY_APP = 1003;
    public static final int DOWNLOAD_BY_BROWSER = 1004;

    private Activity activity;
    private int localVersionCode=0;         //本地的apk版本号
    private String localVersionName="";     //本地的apk版本名字
    private int serverVersionCode=0;        //服务器的apk版本号
    private String serverVersionName="";  //服务器的apk版本名字
    private String apkPath="";      //apk 的下载地址

    private int checkBy=CHECK_BY_VERSION_CODE;  //通过什么检测更新 默认为版本号
    private boolean isForce=false;   //是否强制下载 默认false


    public UpdateAppUtil(Activity activity) {
        this.activity=activity;
        /* 得到本地App版本信息 */
        getLocalVersion(activity);
    }

    public UpdateAppUtil from(Activity activity){
        return new UpdateAppUtil(activity);
    }

    private void getLocalVersion(Context ctx){
        PackageManager manager=ctx.getPackageManager();
        try{
            PackageInfo packageInfo = manager.getPackageInfo(ctx.getPackageName(), 0);
            localVersionCode=packageInfo.versionCode;
            localVersionName=packageInfo.versionName;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public UpdateAppUtil serverVersionCode(int serverVersionCode){
        this.serverVersionCode=serverVersionCode;
        return this;
    }

    public UpdateAppUtil serverVersionName(String serverVersionName){
        this.serverVersionName=serverVersionName;
        return this;
    }

    public UpdateAppUtil apkPath(String apkPath){
        this.apkPath=apkPath;
        return this;
    }

    public UpdateAppUtil checkBy(int checkBy){
        this.checkBy=checkBy;
        return this;
    }

    public UpdateAppUtil isForce(boolean isForce){
        this.isForce=isForce;
        return this;
    }

    public void update(){
        switch (checkBy){
            case CHECK_BY_VERSION_CODE:
                if (serverVersionCode > localVersionCode){
                    toUpdate();
                }else{
                    Log.i(TAG,"当前版本是最新版本"+serverVersionCode+"/"+serverVersionName);
                }
                break;
            case CHECK_BY_VERSION_NAME:
                if (!serverVersionName.equals(localVersionName)){
                    toUpdate();
                }else{
                    Log.i(TAG,"当前版本是最新版本"+serverVersionCode+"/"+serverVersionName);
                }
                break;
        }
    }

    private void toUpdate() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                ==PackageManager.PERMISSION_GRANTED){
            realUpdate();
        }else{//请申请权限
            Toast.makeText(activity, "请申请读写SD卡权限", Toast.LENGTH_SHORT).show();
        }
    }

    private void realUpdate() {
        ConfirmDialog dialog=new ConfirmDialog(activity, new ConfirmDialog.OnClickListener() {
            @Override
            public void sureBtnClick() { //点击确认按钮
                //TODO
                DownloadAppUtil.downloadWithAutoInstall(activity,apkPath,"demo.apk",serverVersionName);
            }

            @Override
            public void cancelBtnClick() { //点击取消按钮
                if (isForce)
                    activity.finish();
            }
        });
        dialog.setContent("发现新版本:"+serverVersionName+"\n是否下载更新?");
        dialog.setCancelable(false);
        dialog.show();
    }
}
