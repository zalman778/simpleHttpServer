import Http.HttpRequest;
import Http.HttpResponse;
import Http.HttpSession;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Hiwoo on 26.11.2017.
 * https://www.reddit.com/r/dailyprogrammer/comments/6lti17/20170707_challenge_322_hard_static_http_server/
 * Basic HTTP server, with sessions, http login, ThreadPool, Singleton and other.
 * Fake Db handler allows to login for admin:password
 */


public class Server {



    private static ExecutorService service;
    private static Thread listenThread;
    private static ServerSocket ss;


    static void bind(int port) throws IOException {
        ss = new ServerSocket(port);
    }

    public static void startService(int poolSize) {
        service = Executors.newFixedThreadPool(poolSize);
    }


    public static void listen() {
        listenThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Socket socket = ss.accept();
                    service.submit(() -> {
                        try {
                            HttpRequest request = Parser.parseRequest(socket.getInputStream());
                            HttpResponse response = new HttpResponse(socket.getOutputStream());
                            HttpSession httpSession = SingletonSessionKeeper.getSessionForSocket(socket, request);
                            RequestHandler requestHandler = new RequestHandler(httpSession);
                            requestHandler.handle(request, response);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                } catch (Throwable  t) {
                    t.printStackTrace();
                }

            }
        });
        listenThread.start();
    }

    public static void main(String[] args) throws IOException {
            bind(Constants.SV_SRV_CONFIG_IP_PORT);
            SingletonSessionKeeper.getInstance();

            startService(Constants.SV_SRV_CONFIG_THREAD_POOL_SIZE);
            listen();
    }

}