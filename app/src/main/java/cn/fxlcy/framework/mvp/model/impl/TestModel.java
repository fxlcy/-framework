package cn.fxlcy.framework.mvp.model.impl;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import cn.fxlcy.framework.mvp.BaseModel;
import cn.fxlcy.framework.mvp.model.ITestModel;

/**
 * Created by fxlcy
 * on 2017/1/18.
 *
 * @author fxlcy
 * @version 1.0
 */

public class TestModel extends BaseModel implements ITestModel {
    @Override
    public String getTestText() {
        Toast.makeText(getContext(), "男男女女女女女女女女女女女", Toast.LENGTH_SHORT).show();
        return "哈哈哈哈哈哈";
    }
}
