package Http;

import java.io.File;

/**
 * Created by sanek on 19.12.2017.
 */
public class HttpSession {
    private String username;

    private File userRoot = null;
    private File userCurrent = null;

    private boolean isAuth;

    public boolean isAuth() {
        return isAuth;
    }

    public void setAuth(boolean auth) {
        isAuth = auth;
    }

    public HttpSession() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public File getUserRoot() {
        return userRoot;
    }

    public void setUserRoot(File userRoot) {
        this.userRoot = userRoot;
    }

    public File getUserCurrent() {
        return userCurrent;
    }

    public void setUserCurrent(File userCurrent) {
        this.userCurrent = userCurrent;
    }
}
