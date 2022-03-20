package io.metacloud.webservice.bin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.metacloud.Driver;
import io.metacloud.configuration.ConfigDriver;
import io.metacloud.configuration.IConfig;
import io.metacloud.console.logger.enums.MSGType;
import lombok.SneakyThrows;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
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
    public IRestConfig getDataRoute(String  json, Class<? extends IRestConfig> tClass){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, tClass);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SneakyThrows
    public RestServer addContent(String webRoute, IRestConfig config){
        if (!exitsContent(webRoute)){
         final Gson GSON = (new GsonBuilder()).serializeNulls().setPrettyPrinting().disableHtmlEscaping().create();
            String jsonConfig = GSON.toJson(config);
            this.routeJson.put(webRoute, jsonConfig.replace("§", "&"));
        }
        return this;
    }

    @SneakyThrows
    public RestServer addContent(String webRoute, String dataRoute, IConfig config){
        if (!exitsContent(webRoute)){
            String jsonConfig = new ConfigDriver(dataRoute).convert(config);
            this.routeJson.put(webRoute, jsonConfig.replace("§", "&"));
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

    public RestServer updateContent(String webRoute, IRestConfig config){
        if (exitsContent(webRoute)){
            final Gson GSON = (new GsonBuilder()).serializeNulls().setPrettyPrinting().disableHtmlEscaping().create();
            String jsonConfig = GSON.toJson(config);
            this.routeJson.remove(webRoute);
            this.routeJson.put(webRoute, jsonConfig);
        }
        return this;
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
        Thread thread = new Thread(() -> {
            try {
                this.serverSocket = new ServerSocket(this.getRunningPort());
                this.serverSocket.setPerformancePreferences(0, 2, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (!shutdown){
                Socket client = null;
                try {
                    client = this.serverSocket.accept();
                    client.setPerformancePreferences(0, 2, 1);
                    DataOutputStream stream = new DataOutputStream(client.getOutputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    String rawString = reader.readLine();
                    if (rawString != null){
                        if (!rawString.equalsIgnoreCase(" GET /favicon.ico HTTP/1.1") && rawString.contains("/") && rawString.contains("GET")){
                            String requestString = rawString.replace("GET /", "").replace(" HTTP/1.1", "");
                            if (requestString.contains("/")) {
                                String[] requests = requestString.split("/");
                                String authenticatorKey = requests[0];
                                if (requests.length == 2){

                                    String content = requests[1];
                                    if (this.authenticatorKey.contains(authenticatorKey)){
                                        if (this.exitsContent(content)){
                                            deployPage(stream, this.routeJson.get(content));
                                        }else {
                                            deployPage(stream, "{\n" +
                                                    "  \"success\": false\n" +
                                                    "}");
                                        }
                                    }else {
                                        deployPage(stream, "{\n" +
                                                "  \"success\": false\n" +
                                                "}");
                                    }
                                }else {
                                    deployPage(stream, "{\n" +
                                            "  \"success\": false\n" +
                                            "}");
                                }
                            }else{
                                deployPage(stream, "{\n" +
                                        "  \"success\": false\n" +
                                        "}");
                            }
                        }
                    }
                    stream.close();
                    reader.close();
                } catch (IOException e) {
                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_ERROR, false, e.getMessage());

                }


            }
        });
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.setDaemon(false);
        thread.start();
    }



    @SneakyThrows
    private void deployPage(DataOutputStream  stream, String json){


        stream.writeBytes("HTTP/1.1 200 OK\r\n");
        stream.writeBytes("Content-Type: application/json; charset=utf-8\r\n");
        stream.writeBytes("Cache-Control: no-cache, must-revalidate\r\n");
        stream.writeBytes("Content-Length: "+json.length()+"\r\n");
        stream.writeBytes("Server: MetaCloud Server\r\n");
        stream.writeBytes("Connection: Close\r\n\r\n");
        stream.writeBytes(json);



        stream.flush();
        stream.close();
    }

}
