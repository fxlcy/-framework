package cn.fxlcy.framework;

import android.support.annotation.NonNull;

import cn.fxlcy.framework.basis.BaseApplication;

/**
 * Created by fxlcy
 * on 2017/1/22
 *
 * @author fxlcy
 * @version 1.0
 */
public class App extends BaseApplication {
    @NonNull
    @Override
    protected Config obtainConfig() {
        return new Config().setGlobalClass(Global.class);
    }
}
