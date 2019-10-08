package lv.telepit.utils;

import lv.telepit.model.User;

/**
 * Created by Alex on 16/03/14.
 */
public class AuthUtil {
    public static boolean isAllowed (String key, User user) {
        if (user == null) {
            return false;
        } else if (user.isAdmin()) {
            return true;
        } else if (user.isServiceWorker()) {
            return "service".equals(key);
        } else {
            return PropertyUtil.get(key, false);
        }
    }
}
