import Http.*;

import java.io.File;
import java.io.IOException;


/**
 * Created by Hiwoo on 18.12.2017.
 * Handling the requests. Only GET supported. List files, open files, authorization
 */
public class RequestHandler {

    private HttpSession httpSession;

    public RequestHandler(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    public void handle (HttpRequest request, HttpResponse response) throws IOException {
        if (request.getMethod() != HttpMethod.GET) {
            response.setStatus(HttpStatus.NOTIMPLEMENTED);
            response.addHeader("Content-Type", "text/html");
            response.setBody("NOTIMPLEMENTED!");

            response.sendResponse();
        } else {
            //user is logged? - try to login
            if (!httpSession.isAuth()) {
                if (!HttpUtils.checkRequestHasValidAuthHead(request, httpSession)) {
                    response.addHeader("WWW-Authenticate", "Basic realm=\"How about authorization?\"");
                    response.addHeader("Connection", "close");
                    response.addHeader("Content-Type", "text/html");
                    response.setStatus(HttpStatus.NOTAUTHORIZED);
                    response.setBody("please, login!");
                    response.sendResponse();
                }
            }
            else
            {   //if just logged - no currentFile - filling Files
                if (httpSession.getUserRoot() == null) {
                    httpSession.setUserRoot(new File(System.getProperty("user.dir") + Constants.SV_MAIN_ROOT_FOLRDER, httpSession.getUsername()));
                    if (!httpSession.getUserRoot().exists()) {
                        httpSession.getUserRoot().mkdirs();
                    }
                }
                if (httpSession.getUserCurrent() == null)
                    httpSession.setUserCurrent(httpSession.getUserRoot());



                //detecting if userCurrent != path
                String pathMod = request.getPath().replaceAll("/+$", "").replaceAll("^/", "").replace('/', '\\');
                String rootDiff = httpSession.getUserCurrent().toString().replace(httpSession.getUserRoot().toString(), "");

                if (!pathMod.equals(rootDiff)) {
                    //try to change dir
                    File toChange = new File(httpSession.getUserRoot()+"\\"+pathMod);

                    //if no such file
                    if (!toChange.exists()) {
                        response.setStatus(HttpStatus.NOTFOUND);
                        response.addHeader("Content-Type", "text/html");
                        response.setBody("404! not found!");
                        response.sendResponse();
                        return;
                    }

                   //if its a Dir
                    if (toChange.exists() && toChange.isDirectory()) {

                        httpSession.setUserCurrent(new File(toChange.getCanonicalPath()));

                        String newBody = HtmlExplorer.htmlExploreCurrentFolder(httpSession);
                        response.setBody(newBody);
                        response.addHeader("Connection", "close");
                        response.addHeader("Content-Type", "text/html");
                        response.setStatus(HttpStatus.OK);
                        response.sendResponse();
                        return;
                    }

                    //if its a file
                    if (toChange.exists() && toChange.isFile()) {
                        response.pushFile(toChange);
                    }

                }
                //if Path didnt change, list files as a dir
                else
                {
                    String newBody = HtmlExplorer.htmlExploreCurrentFolder(httpSession);
                    response.setBody(newBody);
                    response.addHeader("Connection", "close");
                    response.addHeader("Content-Type", "text/html");
                    response.setStatus(HttpStatus.OK);
                    response.sendResponse();
                }
            }
        }
    }
}
