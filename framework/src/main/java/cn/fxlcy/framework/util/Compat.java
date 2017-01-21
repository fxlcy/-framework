package cn.fxlcy.framework.util;

import android.app.Notification;
import android.os.Build;
import android.os.PowerManager;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RemoteViews;

/**
 * Created by Xiong on 2016/9/5.
 */
public final class Compat {
    private Compat(){
        throw new RuntimeException("Stub!");
    }

    public static void removeOnGlobalLayoutListener(View view,ViewTreeObserver.OnGlobalLayoutListener listener){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
        }else{
            view.getViewTreeObserver().removeGlobalOnLayoutListener(listener);
        }
    }

    public static class PowerManagerCompat{
        private PowerManagerCompat(){
            throw new RuntimeException("Stub");
        }

        /**
         * 是否是锁屏状态
         * */
        public static boolean isInteractive(PowerManager pm){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                return pm.isInteractive();
            }else{
                return pm.isScreenOn();
            }
        }
    }

    public static class NotificationBuilderCompat{
        private Notification.Builder mBuilder;

        public NotificationBuilderCompat(Notification.Builder builder){
            mBuilder = builder;
        }

        public NotificationBuilderCompat setCustomContentView(RemoteViews remoteViews){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mBuilder.setCustomContentView(remoteViews);
            }else{
                mBuilder.setContent(remoteViews);
            }

            return this;
        }


        public Notification build(){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                return mBuilder.build();
            }else{
                return mBuilder.getNotification();
            }
        }
    }
}
