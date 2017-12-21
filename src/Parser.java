import Http.HttpMethod;
import Http.HttpRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Created by Hiwoo on 18.12.2017.
 * Helper class for pasing reqest method and headers.
 */
public class Parser {

    public static HttpRequest parseRequest(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line = reader.readLine();

            String[] words = line.split(String.valueOf(" "));
            HashMap<String, String> headers = new HashMap<>();
            String path = words[1];
            HttpMethod httpMethod = null;
            for (HttpMethod hm : HttpMethod.values()) {
                if (hm.name().equals(words[0]))
                    httpMethod = hm;
            }
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            words = line.split(": ");
            headers.put(words[0], words[1]);
        }
        return new HttpRequest(httpMethod, path, headers);
    }
}
