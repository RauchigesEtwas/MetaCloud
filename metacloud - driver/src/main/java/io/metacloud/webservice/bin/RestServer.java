package io.metacloud.webservice.bin;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.metacloud.configuration.ConfigDriver;
import io.metacloud.configuration.IConfig;
import lombok.SneakyThrows;
import sun.net.www.http.HttpClient;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class RestServer {

    private Integer runningPort;
    private Map<String, String> routeJson;
    private Map<String, String> dataLocation;
    private String authenticatorKey;
    private Boolean shutdown;
    private ServerSocket serverSocket;

    public RestServer() {
        this.shutdown = false;
        this.dataLocation = new HashMap<>();
        this.routeJson = new HashMap<>();
    }


    public Integer getRunningPort() {
        return runningPort;
    }

    public String getAuthenticatorKey() {
        return authenticatorKey;
    }

    public RestServer setAuthenticator(String authenticatorKey){
        this.authenticatorKey = authenticatorKey;
        return this;
    }

    public RestServer bind(Integer port){
        this.runningPort = port;
        return this;
    }


    public String getDataRoute(String webRoute){
        return this.dataLocation.get(webRoute);
    }

    @SneakyThrows
    public RestServer addContent(String webRoute, String dataRoute, IConfig config){
        if (!exitsContent(webRoute)){
            String jsonConfig = new ConfigDriver(dataRoute).convert(config);
            this.routeJson.put(webRoute, jsonConfig);
            this.dataLocation.put(webRoute, dataRoute);
        }
        return this;
    }

    public boolean exitsContent(String webRoute){
        if (this.routeJson.containsKey(webRoute)){
            return true;
        }
        return false;
    }

    public RestServer updateContent(String webRoute, IConfig config){
        if (exitsContent(webRoute)){
            String jsonConfig = new ConfigDriver().convert(config);
            this.routeJson.remove(webRoute);
            this.routeJson.put(webRoute, jsonConfig);
        }
        return this;
    }

    public void removeContent(String webRoute){
        if (exitsContent(webRoute)){
            this.routeJson.remove(webRoute);
            this.dataLocation.remove(webRoute);
        }
    }

    public void shutdownRestServer(){
        this.shutdown = true;
        try {
            this.serverSocket.close();
        } catch (IOException ignored) {}
    }

    public void runRestServer(){
        new Thread(() -> {
            try {
                this.serverSocket = new ServerSocket(this.runningPort);
            } catch (IOException e) {

            }
            try {

                while (!shutdown){
                    Socket client = serverSocket.accept();
                    DataOutputStream stream = new DataOutputStream(client.getOutputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    String rawString = reader.readLine();
                    if (rawString != null){
                        if (!rawString.equalsIgnoreCase(" GET /favicon.ico HTTP/1.1") && rawString.contains("/") && rawString.contains("GET")){
                            String requestString = rawString.replace("GET /", "").replace(" HTTP/1.1", "");
                            if (requestString.contains("/")){
                                String[] requests = requestString.split("/");
                                String authenticatorKey = requests[0];
                                String webRoute = requests[1];
                                if (this.authenticatorKey.equals(authenticatorKey)){
                                    if (this.exitsContent(webRoute)){
                                        deployPage(stream, this.routeJson.get(webRoute));
                                    }else {
                                        deployPage(stream, "{\n" +
                                                "  \"connectionAccept\": false,\n" +
                                                "  \"reason\": \"can't find the Web Route\"\n" +
                                                "}");
                                    }
                                }else {
                                    deployPage(stream, "{\n" +
                                            "  \"connectionAccept\": false,\n" +
                                            "  \"reason\": \"false AuthenticatorKey\"\n" +
                                            "}");
                                }
                            }else {
                                deployPage(stream, "{\n" +
                                        "  \"connectionAccept\": false,\n" +
                                        "  \"reason\": \"false AuthenticatorKey\"\n" +
                                        "}");
                            }
                        }
                    }
                    stream.close();
                    reader.close();
                }
            } catch (IOException ignored) {}
        }).start();
    }



    @SneakyThrows
    private void deployPage(DataOutputStream  stream, String json){

        String buildedJson =  json;

        stream.writeBytes("HTTP/1.1 200 OK\r\n");
        stream.writeBytes("Content-Type: application/json; charset=utf-8\r\n");
        stream.writeBytes("Cache-Control: no-cache, must-revalidate\r\n");
        stream.writeBytes("Content-Length: "+buildedJson.length()+"\r\n");
        stream.writeBytes("Server: MetaCloud Server\r\n");
        stream.writeBytes("Connection: Close\r\n\r\n");
        stream.writeBytes(buildedJson);
        stream.flush();
        stream.close();
    }

}
