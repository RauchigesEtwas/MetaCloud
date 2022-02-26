package io.metacloud.webservice.bin;

import lombok.SneakyThrows;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.Buffer;
import java.nio.charset.Charset;

public class RestAPI {

    public RestAPI() {}


    public String getInJsonString(String url){
        String jsonText = getObjectFromRest(url);
        return jsonText;
    }

    public JSONObject convertToJsonObject(String json){
        JSONObject jsonObject = new JSONObject(json);
        return jsonObject;
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
