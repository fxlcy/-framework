package cn.fxlcy.framework.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import cn.fxlcy.framework.basis.GlobalBase;

/**
 * Created by fxlcy
 * on 2017/1/21.
 *
 * @author fxlcy
 * @version 1.0
 */

public class NetworkStateBroadcastReceiver extends BroadcastReceiver {
    private static NetworkStateBroadcastReceiver sReceiver;

    public static void register(Context context) {
        if (sReceiver == null) {
            IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            context.registerReceiver(new NetworkStateBroadcastReceiver(), filter);
        } else {
            throw new RuntimeException("have already registered");
        }
    }

    public static void unregister(Context context) {
        if (sReceiver != null) {
            context.unregisterReceiver(sReceiver);
            sReceiver = null;
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        GlobalBase.getGlobal().updateNetworkState();
    }
}
