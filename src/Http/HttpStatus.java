package Http;



/**
 * Created by sanek on 18.12.2017.
 */
public enum HttpStatus {

      OK("200 OK")
    , NOTFOUND("404 Not Found")
    , NOTAUTHORIZED("401 Authoriztion Required")
    , BADREQUEST("400 Bad Request")
    , INTERNALERROR("500 Internal Server Error")
    , NOTIMPLEMENTED("501 Not Implemented");

    private String message;

    private HttpStatus(String msg) {
        message = msg;
    }

    public String toString() {
        return message;
    }
}
