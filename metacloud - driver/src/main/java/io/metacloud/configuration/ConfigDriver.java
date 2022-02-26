package io.metacloud.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.SneakyThrows;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class ConfigDriver {

    protected static final Gson GSON = (new GsonBuilder()).serializeNulls().setPrettyPrinting().disableHtmlEscaping().create();
    private String location;
    private final Gson gson;


    public ConfigDriver(String location) {
        this.gson = new Gson();
        this.location = location;
    }


    public ConfigDriver() {
        this.gson = new Gson();
    }

    @SneakyThrows
    public IConfig read(Class<? extends IConfig> tClass){
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(new File(this.location), tClass);
        }catch (Throwable e){
            throw e;
        }
    }

    public boolean exists(){
        if(new File(this.location).exists()){
            return true;
        }
        return false;
    }


    public IConfig convert(String json, Class<? extends IConfig> tClass){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, tClass);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String convert(IConfig iconfig){
        return GSON.toJson(iconfig);
    }

    public void save(IConfig iConfig){
        if(iConfig != null && this.location != null){
            if(!exists()){
                try {
                    new File(this.location).createNewFile();
                } catch (IOException ignored) {}
            }

            try {
                try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(this.location), StandardCharsets.UTF_8)) {
                    GSON.toJson(iConfig, writer);
                } catch (IOException e) {
                }
            }catch (Exception ignored){}
        }else{
        }
    }

}
