package Http;

import javafx.util.Pair;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sanek on 18.12.2017.
 */
public class HttpResponse {
    private static final String HTTP_1_1 = "HTTP/1.1 ";
    private static final String OUTPUT_END_OF_HEADERS = "\r\n\r";

    HashMap<String, String> mimeTypes = new HashMap<String, String>()
    {{
        put("gif", "image/gif");
        put("jpeg","image/jpeg");
        put("png", "image/png");
        put("svg", "image/svg+xml");
        put("tiff", "image/tiff");
        put("webp", "image/webp");

        put("webm", "video/webm");
        put("mp4", "video/mp4");
        put("flv", "video/x-flv");

        put("txt", "text/plain");
    }};


    private HttpStatus status;
    private Map<String, String> headers;


    private OutputStream outputStream;

    private String body;

    public HttpResponse(OutputStream outputStream) {
        this.status = HttpStatus.OK;
        this.headers = new HashMap<>();
        this.outputStream = outputStream;
        this.body = null;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;

    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    public void sendResponse() throws IOException {
        //calculating size of data
        int bodyBytes = 0;
        if (body != null)
            bodyBytes =  body.getBytes().length;

        headers.put("Content-Length", Integer.toString(bodyBytes));

        outputStream.write(HTTP_1_1.getBytes());
        outputStream.write((status + "\n").getBytes());

        for (Map.Entry<String, String> es: headers.entrySet()) {
            outputStream.write((es.getKey() + ": " + es.getValue() + "\n").getBytes());
        }
        //end of header
        outputStream.write(OUTPUT_END_OF_HEADERS.getBytes());

        if (body != null)
            outputStream.write(body.getBytes());

        outputStream.flush();
        outputStream.close();
    }


    public void pushFile(File file) throws IOException {
        //TODO - file extensions
        String fileExt = file.getName().substring(file.getName().lastIndexOf('.')+1);

        String contentType = mimeTypes.get(fileExt);
        if (contentType != null)
            addHeader("Content-Type", contentType);
        else {
            addHeader("Content-Type", "application/octet-stream");
            addHeader("Content-Disposition", "attachment; filename='"+file.getName()+"'");
        }


        outputStream.write(HTTP_1_1.getBytes());
        outputStream.write((status + "\n").getBytes());

        for (Map.Entry<String, String> es: headers.entrySet()) {
            outputStream.write((es.getKey() + ": " + es.getValue() + "\n").getBytes());
        }
        //end of header
        outputStream.write(OUTPUT_END_OF_HEADERS.getBytes());

        //the file
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        byte[] buff = new byte[1024];
        int len = bis.read(buff);

        while(len != -1) {
            outputStream.write(buff, 0, len);
            len = bis.read(buff);
        }
        /*
        byte[] buffer = new byte[(int) file.length()];
        bis.read(buffer,0,buffer.length);
        outputStream.write(buffer, 0, buffer.length);
        */

        outputStream.flush();
        outputStream.close();

    }
}
