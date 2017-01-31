package cn.fxlcy.framework.util;

/**
 * Created by fxlcy
 * on 2017/1/31
 *
 * @author fxlcy
 * @version 1.0
 */
public class ObjectUtils {

    public static <T> T requireNonNull(T obj, String msg) {
        if (obj == null) {
            throw new NullPointerException(msg);
        }

        return obj;
    }

    public static <T> T requireNonNull(T obj) {
        if (obj == null) {
            throw new NullPointerException();
        }

        return obj;
    }
}
