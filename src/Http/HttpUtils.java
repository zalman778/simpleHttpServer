package Http;

import Db.Handler;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

/**
 * Created by sanek on 19.12.2017.
 */
public class HttpUtils {
    public static boolean checkRequestHasValidAuthHead(HttpRequest httpRequest, HttpSession httpSession) {
         for (Map.Entry<String, String> entry : httpRequest.getHeaders().entrySet()) {
            if (entry.getKey().equals("Authorization")) {
                String base64authLine = entry.getValue();
                base64authLine = base64authLine.replaceAll("Basic ", "");
                String authLine = new String(Base64.getDecoder().decode(base64authLine.getBytes()), StandardCharsets.UTF_8);
                String[] authArray = authLine.split(":");
                String sqlReq = "select 1 from users where username = '"+authArray[0]+"' and pswd = '"+authArray[1]+"'";
                if (Handler.processRequest(sqlReq).equals("1")) {
                    httpSession.setUsername(authArray[0]);
                    httpSession.setAuth(true);
                    return true;
                }
                else {
                    System.out.println("false ");
                    return false;
                }

            }
        }
        return false;
    }
}
