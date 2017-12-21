import Http.HttpRequest;
import Http.HttpSession;

import java.net.Socket;
import java.util.Base64;
import java.util.HashMap;

/**
 * Created by Hiwoo on 21.12.2017.
 * Deamon-Singleton for keeping sessions
 * Identifying remote PC by IP and User-Agent field combined into Base64.
 *
 */
public class SingletonSessionKeeper extends Thread {

    public static volatile SingletonSessionKeeper sessionKeeper;
    private static HashMap<String, HttpSession> sessionMap;

    private SingletonSessionKeeper() {
        setDaemon(true);
        sessionMap = new HashMap<>();
        this.run();
    }


    public static SingletonSessionKeeper getInstance() {
        SingletonSessionKeeper localInstance = sessionKeeper;
        if (localInstance == null) {
            synchronized (SingletonSessionKeeper.class) {
                localInstance = sessionKeeper;
                if (localInstance == null) {
                    sessionKeeper = localInstance = new SingletonSessionKeeper();
                }
            }
        }
        return localInstance;
    }

    public static HttpSession getSessionForSocket(Socket socket, HttpRequest request) {
        String userAgent = request.getHeaders().get("User-Agent");
        String clientSocket = socket.getRemoteSocketAddress().toString();
        String clientAddr = clientSocket.substring(0, clientSocket.lastIndexOf(':'));
        String clientIdentify = clientAddr+userAgent;
        String base64clientIdentify = Base64.getEncoder().encodeToString(clientIdentify.getBytes());

        HttpSession httpSession = sessionMap.computeIfAbsent(base64clientIdentify, k -> new HttpSession());
        return httpSession;
    }











}
