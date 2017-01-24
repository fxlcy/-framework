/*
**        DroidPlugin Project
**
** Copyright(c) 2015 Andy Zhang <zhangyong232@gmail.com>
**
** This file is part of DroidPlugin.
**
** DroidPlugin is free software: you can redistribute it and/or
** modify it under the terms of the GNU Lesser General Public
** License as published by the Free Software Foundation, either
** version 3 of the License, or (at your option) any later version.
**
** DroidPlugin is distributed in the hope that it will be useful,
** but WITHOUT ANY WARRANTY; without even the implied warranty of
** MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
** Lesser General Public License for more details.
**
** You should have received a copy of the GNU Lesser General Public
** License along with DroidPlugin.  If not, see <http://www.gnu.org/licenses/lgpl.txt>
**
**/

package cn.fxlcy.framework.manager;


import android.annotation.SuppressLint;
import android.app.Application;

/**
 * 同意异常处理
 * */
public class UncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
    @SuppressLint("StaticFieldLeak")
    private static UncaughtExceptionHandler sInstanceHandler;

    private Thread.UncaughtExceptionHandler mOldHandler;
    private Application mContext;

    private UncaughtExceptionHandler(Application app) {
        mContext = app;
    }

    public static UncaughtExceptionHandler getInstance(Application app) {
        if (sInstanceHandler == null) {
            sInstanceHandler = new UncaughtExceptionHandler(app);
        }

        return sInstanceHandler;
    }

    public void register() {
        mOldHandler = Thread.getDefaultUncaughtExceptionHandler();
        if (mOldHandler != this) {
            Thread.setDefaultUncaughtExceptionHandler(this);
        }
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        try {
           /* Intent intent = new Intent(mContext, SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, 0);
            AlarmManager mgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
            mgr.set(AlarmManager.RTC, 1000, pendingIntent);*/

            //TODO 添加自己的处理
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mOldHandler != null) {
                mOldHandler.uncaughtException(thread, ex);
            }
        }

    }
}
