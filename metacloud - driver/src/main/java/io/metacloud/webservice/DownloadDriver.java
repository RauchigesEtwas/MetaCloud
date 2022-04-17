package io.metacloud.webservice;

import io.metacloud.Driver;
import io.metacloud.configuration.configs.group.GroupVersions;
import io.metacloud.console.logger.enums.MSGType;
import lombok.SneakyThrows;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadDriver {

    private String url = "";
    private String fileName = "";
    private String filePath = "";


    public DownloadDriver(String fileName, String filePath, String url){
        this.fileName = fileName;
        this.filePath = filePath;
        this.url = url;
        download();
    }
    public DownloadDriver(String fileName, String filePath, GroupVersions versions){
        this.fileName = fileName;
        this.filePath = filePath;
        switch (versions){
            case PAPERSPIGOT_1_8_8:
                this.url = "https://papermc.io/api/v2/projects/paper/versions/1.8.8/builds/443/downloads/paper-1.8.8-443.jar";
                break;
            case PAPERSPIGOT_1_9_4:
                this.url = "https://papermc.io/api/v2/projects/paper/versions/1.9.4/builds/775/downloads/paper-1.9.4-775.jar";
                break;
            case PAPERSPIGOT_1_10_2:
                this.url = "https://papermc.io/api/v2/projects/paper/versions/1.10.2/builds/918/downloads/paper-1.10.2-918.jar";
                break;
            case PAPERSPIGOT_1_11_2:
                this.url = "https://papermc.io/api/v2/projects/paper/versions/1.11.2/builds/1106/downloads/paper-1.11.2-1106.jar";
                break;
            case PAPERSPIGOT_1_12_2:
                this.url = "https://papermc.io/api/v2/projects/paper/versions/1.12.2/builds/1620/downloads/paper-1.12.2-1620.jar";
                break;
            case PAPERSPIGOT_1_13_2:
                this.url = "https://papermc.io/api/v2/projects/paper/versions/1.13.2/builds/657/downloads/paper-1.13.2-657.jar";
                break;
            case PAPERSPIGOT_1_14_4:
                this.url = "https://papermc.io/api/v2/projects/paper/versions/1.14.4/builds/245/downloads/paper-1.14.4-245.jar";
                break;
            case PAPERSPIGOT_1_15_2:
                this.url = "https://papermc.io/api/v2/projects/paper/versions/1.15.2/builds/393/downloads/paper-1.15.2-393.jar";
                break;
            case PAPERSPIGOT_1_16_5:
                this.url = "https://papermc.io/api/v2/projects/paper/versions/1.16.5/builds/794/downloads/paper-1.16.5-794.jar";
                break;
            case PAPERSPIGOT_1_17_1:
                this.url = "https://papermc.io/api/v2/projects/paper/versions/1.17.1/builds/404/downloads/paper-1.17.1-404.jar";
                break;
            case PAPERSPIGOT_1_18_1:
                this.url = "https://papermc.io/api/v2/projects/paper/versions/1.18.1/builds/112/downloads/paper-1.18.1-112.jar";
                break;
            case WATERFALL_LATEST:
                this.url = "https://papermc.io/api/v2/projects/waterfall/versions/1.18/builds/474/downloads/waterfall-1.18-474.jar";
                break;
            case BUNGEECORD_LATEST:
                this.url = "https://ci.md-5.net/job/BungeeCord/lastSuccessfulBuild/artifact/bootstrap/target/BungeeCord.jar";
                break;
        }
        download();

    }

    @SneakyThrows
    private void download() {
        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(filePath + "/" + fileName)) {
            byte dataBuffer[] = new byte[1024];
            Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_INFO, "Downloading file §b"+fileName+" §7....");

            int bytesRead;
        
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
            Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_INFO, "The file §b"+fileName+" §7is downloaded with §bsuccess");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
