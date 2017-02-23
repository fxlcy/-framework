package cn.fxlcy.framework;

import android.app.Application;

import cn.fxlcy.framework.basis.GlobalBase;

/**
 * Created by fxlcy
 * on 2017/1/22
 *
 * @author fxlcy
 * @version 1.0
 */
public class Global extends GlobalBase {
    protected Global(Application app) {
        super(app);
    }

    @Override
    public boolean isChange() {
        return false;
    }
}
