package utils;

import com.uninavigator.uninavigatorapp.controllers.User;


public class SessionContext {
    private static final ThreadLocal<User> currentUser = new ThreadLocal<>();

    public static void setCurrentUser(User user) {
        currentUser.set(user);
    }

    public static User getCurrentUser() {
        return currentUser.get();
    }

    public static void clear() {
        currentUser.remove();
    }

    public static void clearCurrentUser() {
        currentUser.remove();
    }
}
