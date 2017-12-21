package Db;

/**
 * Created by Hiwoo on 19.12.2017.
 * Fake implementation of Db server handler
 */
public class Handler {
    public static String processRequest(String request) {

        if (request.indexOf("admin") > 0 && request.indexOf("password") > 0 )
            return "1";
        else
            return "0";
    }
}
