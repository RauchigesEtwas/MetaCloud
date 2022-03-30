package io.metacloud.webservice.bin;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.metacloud.configuration.IConfig;
import lombok.SneakyThrows;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.Buffer;
import java.nio.charset.Charset;

public class RestAPI {

    public RestAPI() {}


    public String getInJsonString(String url){
        String jsonText = getObjectFromRest(url);
        return jsonText;
    }

    public JSONObject convertToJsonObject(String url){
        JSONObject jsonObject = new JSONObject(getInJsonString(url));
        return jsonObject;
    }

    public IRestConfig convertToRestConfig(String url, Class<? extends IRestConfig> tClass){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(getInJsonString(url), tClass);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public IConfig convertToConfig(String url, Class<? extends IConfig> tClass){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(getInJsonString(url), tClass);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @SneakyThrows
    private String getObjectFromRest(String url){

        final InputStream stream = new URL(url).openStream();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, Charset.forName("UTF-8")));
            String rawJSON = read(reader);

            return rawJSON;

        }finally {
            stream.close();
        }

    }

    @SneakyThrows
    private String read(Reader reader){

        StringBuilder builder = new StringBuilder();

        int counter;
         while ((counter = reader.read()) != -1){
             builder.append((char) counter);
         }

         return builder.toString();
    }

}
