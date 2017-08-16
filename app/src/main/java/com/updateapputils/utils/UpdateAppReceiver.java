package com.updateapputils.utils;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by asusnb on 2017/8/16.
 */

public class UpdateAppReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Cursor cursor=null;
        try{
            if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)){
                if (DownloadAppUtil.downloadUpdateApkId > 0){
                    long downloadId = DownloadAppUtil.downloadUpdateApkId;
                    DownloadManager.Query query=new DownloadManager.Query();
                    query.setFilterById(downloadId);
                    DownloadManager manager= (DownloadManager) context
                            .getSystemService(Context.DOWNLOAD_SERVICE);
                    cursor=manager.query(query);
                    if (cursor.moveToNext()){
                        int staus=cursor.getInt(cursor
                                .getColumnIndex(DownloadManager.COLUMN_STATUS));
                        if (staus==DownloadManager.STATUS_FAILED){
                            manager.remove(downloadId);
                        }else if (staus ==DownloadManager.STATUS_SUCCESSFUL){
                            if (DownloadAppUtil.downloadUpdateApkFilePath!=null){
                                Intent it=new Intent(Intent.ACTION_VIEW);
                                it.setDataAndType(Uri.parse("file://"
                                +DownloadAppUtil.downloadUpdateApkFilePath),
                                        "application/vnd.android.package-archive");
                                //todo 针对不同的手机 以及sdk版本  这里的uri地址可能有所不同
                                it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(it);
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor!=null){
                cursor.close();
            }
        }
    }
}
