package Http;

import java.util.Map;

/**
 * Created by sanek on 18.12.2017.
 */
public class HttpRequest {
    private HttpMethod method;

    private Map<String, String> headers;

    private byte[] body;
    private String path;


    public HttpRequest(HttpMethod method, String path, Map<String, String> headers) {
        this.method = method;
        this.path = path;
        this.headers = headers;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public byte[] getBody() {
        return body;
    }

    public String getPath() {
        return path;
    }
}
