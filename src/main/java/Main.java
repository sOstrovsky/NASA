import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Main {
    private final static String API_KEY = "daHyca3wIJxDOMYEwnaREhB21gbbhSAZYEf1Gh6a";
    private final static String NASA_API = "https://api.nasa.gov/planetary/apod?api_key=";
    private final static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();

        HttpGet request = new HttpGet(NASA_API + API_KEY);
        try (CloseableHttpResponse response = httpClient.execute(request);
             InputStream res = response.getEntity().getContent()) {
            Post post = mapper.readValue(res, Post.class);
            String url = post.getUrl();
            String[] splited = url.split("/");
            String fileName = splited[splited.length - 1];

            HttpGet request2 = new HttpGet(url);
            try (CloseableHttpResponse response2 = httpClient.execute(request2);
                 InputStream res2 = response2.getEntity().getContent();
                 FileOutputStream output = new FileOutputStream(fileName)) {
                 output.write(res2.readAllBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
